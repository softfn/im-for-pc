package com.test.bean;

import com.only.xml.XmlBean;

public class Message implements XmlBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private int[] teamArray;
	private String[][][] color;
	private User[] userArray;

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

	public int[] getTeamArray() {
		return teamArray;
	}

	public void setTeamArray(int[] teamArray) {
		this.teamArray = teamArray;
	}

	public String[][][] getColor() {
		return color;
	}

	public void setColor(String[][][] color) {
		this.color = color;
	}

	public User[] getUserArray() {
		return userArray;
	}

	public void setUserArray(User[] userArray) {
		this.userArray = userArray;
	}

}
