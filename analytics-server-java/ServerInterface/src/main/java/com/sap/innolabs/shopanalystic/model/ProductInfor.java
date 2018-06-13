package com.sap.innolabs.shopanalystic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"ProductInfor\"")
public class ProductInfor {
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "\"ID\"")
	private int id;
	
	@Column(name = "\"Name\"")
	private String name;
	
	@Column(name = "\"Price\"")
	private float price;
	
	@Column(name = "\"Location\"")
	private String location;
	
	@Column(name = "\"Notes\"")
	private String notes;
	
	@Column(name = "\"Pic\"")
	private String pic;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
	
	
	
}
