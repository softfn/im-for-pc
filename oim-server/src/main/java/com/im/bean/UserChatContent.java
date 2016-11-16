package com.im.bean;

import java.sql.Timestamp;

import javax.persistence.Entity;

/**
 * @description:用户聊天记录
 * @author: Only
 * @date: 2016年8月15日 下午2:59:51
 */
@Entity(name = "im_user_chat_content")
public class UserChatContent extends BaseBean {

	private String messageId;// 消息id

	private String receiveUserId;
	private String receiveUserHead;// 用户头像
	private String receiveUserHeadType;
	private String receiveUserName;//
	private String receiveUserNickname;//

	private String sendUserId;// 发送消息用户id
	private String sendUserHead;// 用户头像
	private String sendUserHeadType;
	private String sendUserName;//
	private String sendUserNickname;//
	private String sendUserRemark;//

	private String isDeleted;//
	private String isSend;//
	private Timestamp time;//
	private long timestamp;//

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	public String getReceiveUserHead() {
		return receiveUserHead;
	}

	public void setReceiveUserHead(String receiveUserHead) {
		this.receiveUserHead = receiveUserHead;
	}

	public String getReceiveUserHeadType() {
		return receiveUserHeadType;
	}

	public void setReceiveUserHeadType(String receiveUserHeadType) {
		this.receiveUserHeadType = receiveUserHeadType;
	}

	public String getReceiveUserName() {
		return receiveUserName;
	}

	public void setReceiveUserName(String receiveUserName) {
		this.receiveUserName = receiveUserName;
	}

	public String getReceiveUserNickname() {
		return receiveUserNickname;
	}

	public void setReceiveUserNickname(String receiveUserNickname) {
		this.receiveUserNickname = receiveUserNickname;
	}

	public String getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	public String getSendUserHead() {
		return sendUserHead;
	}

	public void setSendUserHead(String sendUserHead) {
		this.sendUserHead = sendUserHead;
	}

	public String getSendUserHeadType() {
		return sendUserHeadType;
	}

	public void setSendUserHeadType(String sendUserHeadType) {
		this.sendUserHeadType = sendUserHeadType;
	}

	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}

	public String getSendUserNickname() {
		return sendUserNickname;
	}

	public void setSendUserNickname(String sendUserNickname) {
		this.sendUserNickname = sendUserNickname;
	}

	public String getSendUserRemark() {
		return sendUserRemark;
	}

	public void setSendUserRemark(String sendUserRemark) {
		this.sendUserRemark = sendUserRemark;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getIsSend() {
		return isSend;
	}

	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
