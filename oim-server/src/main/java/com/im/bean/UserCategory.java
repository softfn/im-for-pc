package com.im.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

/**
 * 描述：好友分组信息
 * 
 * @author XiaHui
 * @date 2014年6月15日 下午2:32:18
 * @version 0.0.1
 */
@Entity(name = "im_user_category")
public class UserCategory {

	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", unique = true, nullable = false, length = 40)
	private String id;
	@Column(unique = true, nullable = false)
	private String userId;//拥有分组用户id
	private int rank;
	private int sort;
	private String name;

	public static final int sort_default = 1;
	public static final int sort_custom = 2;

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
