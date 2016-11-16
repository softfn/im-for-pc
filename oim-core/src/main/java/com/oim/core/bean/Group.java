package com.oim.core.bean;



/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2014年6月15日 下午2:30:04
 * @version 0.0.1
 */
public class Group {

	private String id;
	private int number;// 帐号
	private String name = "";
	private String head;// 照片
	private String headType;// 照片
	private String remarks = "";
	private String classification = "";
	private String publicNotice = "";
	private String introduce = "";
	private String position = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getHeadType() {
		return headType;
	}

	public void setHeadType(String headType) {
		this.headType = headType;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getPublicNotice() {
		return publicNotice;
	}

	public void setPublicNotice(String publicNotice) {
		this.publicNotice = publicNotice;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}
