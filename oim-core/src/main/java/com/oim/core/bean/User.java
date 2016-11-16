package com.oim.core.bean;



/**
 * 
 * @author XiaHui
 * @date 2016年1月4日 下午9:18:55
 * @version 0.0.1
 */
public class User {

	private String id;// 用户Id
	private int number;// 数字帐号
	private String account;// 帐号
	private String email;// 电子信箱
	private String mobilePhone;// 手机
	private String password;// 密码
	private String createDateTime;// 建立时间
	private String cancelDateTime;// 注销时间
	private int mark;// 有效标志 1：启用 2：停用
	private String privateKey;// 私钥
	// /基本信息
	private String signature;
	private String head;// 照片
	private String headType;// 照片
	private String nickname;// 昵称
	private String name;// 姓名
	private String spell;
	private String simpleSpell;
	private String gender;// 性别

	private String telephone;// 联系电话
	private String QQ;// qq
	private String nation;// 民族
	private String birthdate;// 出生日期
	private Integer age;// 年龄
	private String identityCard;// 身份证号码
	private String politicsStatus;// 政治面貌
	private String maritalStatus;// 婚姻状况
	private String school;// 毕业学校
	private String education;// 学历
	private String graduationDate;// 毕业时间
	private String workDate;// 参加工作时间
	private String professional;// 专业
	private String nativePlace;// 籍贯
	private String zipCode;// 邮政编码
	private String homeAddress;// 家庭地址
	private String locationAddress;//所在地址
	private String remark;// 备注
	private String introduce;//介绍
	private String type;// 用户类型
	private String constellation;
	private String blood;

	//
	/** 有效标志：有效 **/
	public static final int mark_enable = 1;
	/** 有效标志：无效 **/
	public static final int mark_disable = 2;

	//
	public static final String status_online = "1";
	public static final String status_call_me = "2";
	public static final String status_away = "3";
	public static final String status_busy = "4";
	public static final String status_mute = "5";
	public static final String status_invisible = "6";
	public static final String status_offline = "7";

	//
	public static final String head_type_default = "1";
	public static final String head_type_custom = "2";
	
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
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCreateDateTime() {
		return createDateTime;
	}
	public void setCreateDateTime(String createDateTime) {
		this.createDateTime = createDateTime;
	}
	public String getCancelDateTime() {
		return cancelDateTime;
	}
	public void setCancelDateTime(String cancelDateTime) {
		this.cancelDateTime = cancelDateTime;
	}
	public int getMark() {
		return mark;
	}
	public void setMark(int mark) {
		this.mark = mark;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
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
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpell() {
		return spell;
	}
	public void setSpell(String spell) {
		this.spell = spell;
	}
	public String getSimpleSpell() {
		return simpleSpell;
	}
	public void setSimpleSpell(String simpleSpell) {
		this.simpleSpell = simpleSpell;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getQQ() {
		return QQ;
	}
	public void setQQ(String qQ) {
		QQ = qQ;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getIdentityCard() {
		return identityCard;
	}
	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}
	public String getPoliticsStatus() {
		return politicsStatus;
	}
	public void setPoliticsStatus(String politicsStatus) {
		this.politicsStatus = politicsStatus;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getGraduationDate() {
		return graduationDate;
	}
	public void setGraduationDate(String graduationDate) {
		this.graduationDate = graduationDate;
	}
	public String getWorkDate() {
		return workDate;
	}
	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}
	public String getProfessional() {
		return professional;
	}
	public void setProfessional(String professional) {
		this.professional = professional;
	}
	public String getNativePlace() {
		return nativePlace;
	}
	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	public String getLocationAddress() {
		return locationAddress;
	}
	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getConstellation() {
		return constellation;
	}
	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}
	public String getBlood() {
		return blood;
	}
	public void setBlood(String blood) {
		this.blood = blood;
	}

}
