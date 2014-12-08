package models;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class VmsCluster extends DomainObject {

	public String name;
	public String address;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Override @JsonIgnore
	public String getPath() {
		return "/vms/cluster/" + name;
	}
	
}
