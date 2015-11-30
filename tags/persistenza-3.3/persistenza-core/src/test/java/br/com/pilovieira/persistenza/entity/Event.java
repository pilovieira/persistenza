package br.com.pilovieira.persistenza.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Event {
	
	public static final String ATR_START = "start";
	
	@Id
	int id;
	private String name;
	@Column(name = ATR_START)
	private Date start;
	
	public Event() {}
	
	public Event(int id, String name, Date start) {
		this.id = id;
		this.name = name;
		this.start = start;
	}
	
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
	
	public Date getStart() {
		return start;
	}
	
}
