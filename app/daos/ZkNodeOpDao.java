package daos;

import java.util.ArrayList;
import java.util.List;

import models.ChangeSet;
import models.DomainObject;
import models.ZkNodeOp;
import play.db.ebean.Model.Finder;
import util.JsonUtil;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.google.common.collect.Lists;

public class ZkNodeOpDao {
	
	public enum ChangeSetStrategy {
		publishedOnly, includeActive, includePending 
	}

	final static String querySql = "select c.id, c.type, c.path, c.value, c.change_set_id, c.tag0 from (" +
		"	  select * from (" +
		"	    select c.id, c.path, c.change_set_id, t3.seq, t3.seq2, t3.owner from c" +
		"	      join (" +
		"	        select *," +
		"	          case    " +
		"	            when seq > 0 then seq" + 
		"	            ${whenPart}" +
		"	            else null " +
		"	          end as seq2 " +
		"	        from cs" +
		"	        where seq > 0 ${orIdInPart}" +
		"	      ) t3 " +
		"	      on c.change_set_id=t3.id" +
		"	      where t3.seq2 is not null " +
		"	      order by path, seq2 desc, id desc" +
		"	  ) t2" +
		"	  group by path" +
		"	) t1 " +
		"	join c on t1.id=c.id";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Finder<Long, ZkNodeOp> finder = new Finder(Long.class, ZkNodeOp.class);
	
	public static List<ZkNodeOp> all() {
		return finder.all();
	}

	public static List<ZkNodeOp> findByChangeSet(Long csId) {
		List<ZkNodeOp> opList = Ebean.find(ZkNodeOp.class)
				.where()
					.eq("changeSetId", csId)
				.findList();
			return opList;
	}
	
	private static String replaceSql(ChangeSet ... sets) {
		int n = 20000;
		String when = "";
		String in = "";
		for(ChangeSet cs : sets) {
			when += String.format(" when id=%d then %d \n", cs.getId(), n++);
			if(in.length() > 0) {
				in += ",";
			}
			in += cs.getId();
		}
		
		if(in.length() > 0) {
			in = String.format(" or id in (%s)", in);
		}
		
		String sql = querySql
				.replace("${whenPart}", when)
		        .replace("${orIdInPart}", in);
		
		return sql;
	}
	
	public static <T extends DomainObject> List<T> findByType(Class<T> type, List<ChangeSet> csList) {
		String sql = replaceSql(csList.toArray(new ChangeSet[csList.size()]));
		String whereClause = " where c.type != 'D' and c.tag0='" + type.getSimpleName() + "'";
		sql += whereClause;
		
		//System.out.println(sql);
		
		RawSql rawSql = RawSqlBuilder
				.parse(sql)
				.columnMapping("c.id", "id")
				.create();
		
		List<ZkNodeOp> opList = Ebean.find(ZkNodeOp.class)
				.setRawSql(rawSql)
				.findList();
		List<T> list = new ArrayList<>();
		for(ZkNodeOp op : opList) {
			T o = JsonUtil.toObject(op.getValue(), type);
			o.setOp(op);
			o.setCs(ChangeSetDao.getById(op.getChangeSetId()));
			list.add(o);
		}
		return list;
	}
	
	public static <T extends DomainObject> List<T> findByType(Class<T> type, ChangeSetStrategy strategy) {
		
		List<ChangeSet> csList = null;
		switch(strategy) {
		case publishedOnly:
			csList = new ArrayList<>();
			break;
		case includeActive:
			ChangeSet cs = ChangeSetDao.getCurrent();
			csList = cs == null ? 
				new ArrayList<ChangeSet>()
			  : Lists.asList(ChangeSetDao.getCurrent(), new ChangeSet[0]);
			break;
		case includePending:
			csList = ChangeSetDao.findPending(false);
			break;
		default:
			break;
		}
		
		return findByType(type, csList);
	}

	public static <T extends DomainObject> List<T> findByTypeAndPath(Class<T> type, String path) {
		List<ZkNodeOp> opList = Ebean.find(ZkNodeOp.class)
			.where()
				.eq("tag0", type.getSimpleName())
				.eq("path", path)
			.findList();
		List<T> list = new ArrayList<>();
		for(ZkNodeOp op : opList) {
			T o = JsonUtil.toObject(op.getValue(), type);
			o.setOp(op);
			list.add(o);
		}
		return list;
	}

	public static void create(ZkNodeOp op) {
		op.save();
	}

	public static <T extends DomainObject> void createOrUpdate(T o) {
		o.toZkNodeOp().save();
	}

	public static void delete(Long id) {
		finder.ref(id).delete();
	}


}
