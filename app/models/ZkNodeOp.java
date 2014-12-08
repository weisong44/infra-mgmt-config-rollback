package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
@Table(name = "c")
public class ZkNodeOp extends Model {

	public static final long serialVersionUID = 1L;

	public enum Type {
		C, U, D
	}

	protected ZkNodeOp() {}
	
	public ZkNodeOp(Long changeSetId, Type type, String path) {
		this.changeSetId = changeSetId;
		this.type = type;
		this.path = path;
		this.createdAt = new Date();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Long id;

	@Required
	@Column(name = "TYPE", length = 1)
	@Enumerated(EnumType.STRING)
	public Type type;

	@Required
	@Column(name = "PATH", length = 128)
	public String path;

	@Required
	@Column(name = "VALUE", length = 256)
	public String value;

	@Required
	@Column(name = "CHANGE_SET_ID", nullable=false)
	public Long changeSetId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_AT")
	public Date createdAt = new Date();

	public String tag0;
	public String tag1;
	public String tag2;
	public String tag3;
	public String tag4;
	public String tag5;
	public String tag6;
	public String tag7;
	public String tag8;
	public String tag9;

	
	public Long getChangeSetId() {
		return changeSetId;
	}

	public void setChangeSetId(Long changeSetId) {
		this.changeSetId = changeSetId;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public String getTag0() {
		return tag0;
	}

	public void setTag0(String tag0) {
		this.tag0 = tag0;
	}

	public String getTag1() {
		return tag1;
	}

	public void setTag1(String tag1) {
		this.tag1 = tag1;
	}

	public String getTag2() {
		return tag2;
	}

	public void setTag2(String tag2) {
		this.tag2 = tag2;
	}

	public String getTag3() {
		return tag3;
	}

	public void setTag3(String tag3) {
		this.tag3 = tag3;
	}

	public String getTag4() {
		return tag4;
	}

	public void setTag4(String tag4) {
		this.tag4 = tag4;
	}

	public String getTag5() {
		return tag5;
	}

	public void setTag5(String tag5) {
		this.tag5 = tag5;
	}

	public String getTag6() {
		return tag6;
	}

	public void setTag6(String tag6) {
		this.tag6 = tag6;
	}

	public String getTag7() {
		return tag7;
	}

	public void setTag7(String tag7) {
		this.tag7 = tag7;
	}

	public String getTag8() {
		return tag8;
	}

	public void setTag8(String tag8) {
		this.tag8 = tag8;
	}

	public String getTag9() {
		return tag9;
	}

	public void setTag9(String tag9) {
		this.tag9 = tag9;
	}

	public Long getId() {
		return id;
	}
	
}
