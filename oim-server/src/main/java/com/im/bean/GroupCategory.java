package com.im.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

/**
 * 描述：群分组信息
 * 
 * @author 夏辉
 * @date 2014年6月15日 下午2:32:18
 * @version 0.0.1
 */
@Entity(name = "im_group_category")
public class GroupCategory {

	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", unique = true, nullable = false, length = 40)
	private String id;// 
	private String userId;// 所属用户id
	private int rank;//排序
	private int sort;//类型：1、系统默认生成的 2、用户自己新增的
	private String name;//组名字

	public static final int sort_default = 1;//系统默认生成的
	public static final int sort_custom = 2;//用户自己新增的

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
