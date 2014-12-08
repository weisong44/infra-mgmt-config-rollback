package daos;

import models.VmsCluster;
import models.ZkNodeOp;

public class VmsClusterDao {
	
	public static void createOrUpdate(VmsCluster o) {
		ZkNodeOp op = o.toZkNodeOp();
		if(ZkNodeOpDao.findByTypeAndPath(VmsCluster.class, o.getPath()).isEmpty()) {
			op.setType(ZkNodeOp.Type.C);
		}
		else {
			op.setType(ZkNodeOp.Type.U);
		}
		
		op.setTag1(o.getName());
		op.setTag2(o.getAddress());
		
		ZkNodeOpDao.create(op);
	}

	public static void delete(String name) {
		VmsCluster cluster = new VmsCluster();
		cluster.setName(name);
		cluster.setAddress("D");
		ZkNodeOp op = cluster.toZkNodeOp();
		op.setType(ZkNodeOp.Type.D);
		ZkNodeOpDao.create(op);
	}
}
