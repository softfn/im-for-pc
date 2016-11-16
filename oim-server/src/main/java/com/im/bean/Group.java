package com.im.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

/**
 * 描述：群信息实体
 * 
 * @author XiaHui
 * @date 2014年6月15日 下午2:30:04
 * @version 0.0.1
 */
@Entity(name = "im_group")
public class Group {

	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", unique = true, nullable = false, length = 40)
	private String id;
	@Column(unique = true, nullable = false)
	private int number;// 帐号

	private String name = "";
	private String head;// 照片
	private String headType;// 照片
	private String remarks = "";
	private String classification = "";//分类
	private String publicNotice = "";//公告
	private String introduce = "";//介绍
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
