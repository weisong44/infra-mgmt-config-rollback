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

import play.db.ebean.Model;

@Entity
@Table(name = "cs")
public class ChangeSet extends Model {

	public static final long serialVersionUID = 1L;

	public enum State {
		editing, staged, approving, approved, rejected, published
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Long id;

	@Column(name = "STATE")
	@Enumerated(EnumType.STRING)
	public State state = State.editing;

	@Column(name = "SEQ")
	public Integer publishSequence;

	@Column(name = "OWNER")
	public String createdBy;

	@Column(name = "UPDATED_BY")
	public String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_AT")
	public Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_AT")
	public Date updatedAt;

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Integer getPublishSequence() {
		return publishSequence;
	}

	public void setPublishSequence(Integer publishSequence) {
		this.publishSequence = publishSequence;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getId() {
		return id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	
	
}
