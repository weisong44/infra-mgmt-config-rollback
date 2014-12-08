package models;

import util.JsonUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;

import daos.ChangeSetDao;


abstract public class DomainObject {
	
	@JsonIgnore
	public ZkNodeOp op;
	
	@JsonIgnore
	public ChangeSet cs;
	
	@JsonIgnore
	abstract public String getPath();
	
	public ZkNodeOp toZkNodeOp() {
		ZkNodeOp op = new ZkNodeOp();
		op.setChangeSetId(ChangeSetDao.getOrCreateCurrent().getId());
		op.setPath(getPath());
		op.setValue(JsonUtil.toJsonString(this).replace("\n", ""));
		op.setTag0(getClass().getSimpleName());
		return op;
	}

	public ZkNodeOp getOp() {
		return op;
	}
	public void setOp(ZkNodeOp op) {
		this.op = op;
	}
	public ChangeSet getCs() {
		return cs;
	}
	public void setCs(ChangeSet cs) {
		this.cs = cs;
	}
}
