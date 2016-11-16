package com.im.business.common.service;

import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.im.bean.GroupCategory;
import com.im.bean.User;
import com.im.bean.UserCategory;
import com.im.bean.UserNumber;
import com.im.business.common.dao.GroupCategoryDAO;
import com.im.business.common.dao.NumberDAO;
import com.im.business.common.dao.UserCategoryDAO;
import com.im.business.common.dao.UserDAO;
import com.im.business.common.service.api.UserBaseService;
import com.im.business.server.message.Head;
import com.im.business.server.message.Message;
import com.im.business.server.message.data.LoginData;
import com.im.business.server.thread.SessionServerHandler;
import com.only.common.util.OnlyDateUtil;
import com.only.net.session.SocketSession;

/**
 * 描述： 个人业务处理service
 * 
 * @author XiaHui
 * @date 2016年1月7日 下午7:48:52
 * @version 0.0.1
 */
@Service
@Transactional
public class PersonalService {

	@Resource
	private UserDAO userDAO;
	@Resource
	private NumberDAO numberDAO;
	@Resource
	private UserCategoryDAO userCategoryDAO;
	@Resource
	private GroupCategoryDAO groupCategoryDAO;
	@Resource
	private UserBaseService userBaseService;
	@Resource
	SessionServerHandler sessionServerHandler;
	
	public Message verify(Head head, String address, String account) {
		Message m = new Message();
		m.setInfoCode(Message.code_success);
		return m;
	}

	boolean isNeedVerify(String address, String account) {
		return false;
	}

	/**
	 * 登陆
	 * 
	 * @param userMessage
	 * @return
	 */
	public Message login(SocketSession socketSession,LoginData loginData) {
		Message m = new Message();
		if (null != loginData) {
			String account = loginData.getAccount();
			if (StringUtils.isNotBlank(account)) {
				account = account.replace(" ", "").replace("'", "");
				User user = userDAO.getByAccount(account);
				if (null != user) {
					if (StringUtils.isNotBlank(loginData.getPassword())) {
						if (loginData.getPassword().equals(user.getPassword())) {
							m.put("user", user);
							m.setInfoCode(Message.code_success);
							m.setInfoValue("登录成功。");
							socketSession.setAuth(true);
							socketSession.setKey(user.getId());
							sessionServerHandler.put(user.getId(), socketSession);
							userBaseService.putUserStatus(user.getId(), loginData.getStatus());
						} else {
							m.setInfoCode(Message.code_fail);
							m.setInfoValue("密码错误！");
						}
					} else {
						m.setInfoCode(Message.code_fail);
						m.setInfoValue("密码不能为空！");
					}
				} else {
					m.setInfoCode(Message.code_fail);
					m.setInfoValue("帐号不存在！");
				}
			} else {
				m.setInfoCode(Message.code_fail);
				m.setInfoValue("帐号不能为空！");
			}
		} else {
			m.setInfoCode(Message.code_fail);
			m.setInfoValue("帐号和密码不能为空！");
		}
		return m;
	}

	/**
	 * 注册
	 * 
	 * @param user
	 * @return
	 */
	public User register(User user) {
		int i = new Random().nextInt(20);
		i = i + 1;
		UserNumber userNumber = new UserNumber();// 生成数子账号
		userNumber.setDateTime(OnlyDateUtil.getCurrentDateTime());

		numberDAO.save(userNumber);

		user.setAccount(userNumber.getId() + "");
		user.setNumber(userNumber.getId());
		user.setHead(i + "");
		user.setHeadType(User.head_type_default);
		user.setCreateDateTime(OnlyDateUtil.getCurrentDateTime());
		userDAO.save(user);

		UserCategory userCategory = new UserCategory();// 生成默认分组信息
		userCategory.setUserId(user.getId());
		userCategory.setName("我的好友");
		userCategory.setSort(UserCategory.sort_default);

		userCategoryDAO.save(userCategory);

		GroupCategory groupCategory = new GroupCategory();// 生成默认分组信息
		groupCategory.setUserId(user.getId());
		groupCategory.setName("我的聊天群");
		groupCategory.setSort(GroupCategory.sort_default);

		groupCategoryDAO.save(groupCategory);

		return user;
	}

	/**
	 * 修改用户信息
	 * 
	 * @param userMessage
	 * @return
	 */
	public Message update(User user) {
		Message message = new Message();
		int mark = userDAO.updateSelective(user);
		if (mark > 0) {
			message.setInfoCode(Message.code_success);
			message.setInfoValue("修改成功。");
		} else {
			message.setInfoCode(Message.code_fail);
			message.setInfoValue("修改失败！");
		}
		return message;
	}
	
	public Message updatePassword(String userId,String password) {
		Message message = new Message();
		User user=new User();
		user.setId(userId);
		user.setPassword(password);
		int mark = userDAO.updateSelective(user);
		if (mark > 0) {
			message.setInfoCode(Message.code_success);
			message.setInfoValue("修改成功。");
		} else {
			message.setInfoCode(Message.code_fail);
			message.setInfoValue("修改失败！");
		}
		return message;
	}
}
