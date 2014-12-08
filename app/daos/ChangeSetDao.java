package daos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.ChangeSet;
import play.db.ebean.Model.Finder;
import util.ContextHolder;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;

public class ChangeSetDao {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Finder<Long, ChangeSet> finder = new Finder(Long.class, ChangeSet.class);
	
	public static List<ChangeSet> all() {
		return finder.all();
	}

	public static ChangeSet getById(Long id) {
		return Ebean.find(ChangeSet.class, id);
	}
	
	public static int getMaxSeq() {
		int max = 1;
		for(ChangeSet cs : all()) {
			if(cs.getPublishSequence() != null && cs.getPublishSequence() > max) {
				max = cs.getPublishSequence();
			}
		}
		return max;
	}

	public static List<ChangeSet> findPending(boolean latestFirst) {
		List<ChangeSet.State> stateList = new ArrayList<>();
		stateList.add(ChangeSet.State.editing);
		stateList.add(ChangeSet.State.approved);
		stateList.add(ChangeSet.State.approving);
		ExpressionList<ChangeSet> exprList = Ebean.find(ChangeSet.class).where()
			.eq("owner", ContextHolder.get().curUser)
			.in("state", stateList);
		if(latestFirst) {
			return exprList.order("updatedAt desc").findList();
		}
		else {
			return exprList.order("updatedAt").findList();
		}
	}
	
	public static ChangeSet getOrCreateCurrent() {
		ChangeSet set = getCurrent();
		if(set != null) {
			return set;
		}
		
		set = new ChangeSet();
		set.setCreatedBy(ContextHolder.get().curUser);
		set.setCreatedAt(new Date());
		set.setUpdatedBy(set.getCreatedBy());
		set.setUpdatedAt(set.getCreatedAt());
		set.setState(ChangeSet.State.editing);
		createOrUpdate(set);
		return set;
		
	}

	public static ChangeSet getCurrent() {
		return getCurrent(ContextHolder.get().curUser);
	}

	public static ChangeSet getCurrent(String user) {
		List<ChangeSet> sets = Ebean.find(ChangeSet.class).where()
			.eq("createdBy", user)
			.eq("state", ChangeSet.State.editing)
			.findList();
		if(sets.isEmpty() == false) {
			return sets.get(0);
		}
		return null;
		
	}

	public static void createOrUpdate(ChangeSet cs) {
		cs.setUpdatedAt(new Date());
		cs.setUpdatedBy(ContextHolder.get().curUser);
		cs.save();
	}

	public static void delete(Long id) {
		finder.ref(id).delete();
	}

}
