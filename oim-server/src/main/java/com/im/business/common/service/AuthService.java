package com.im.business.common.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.im.common.util.CacheKeyUtil;
import com.im.common.web.ResultData;
import com.im.business.server.message.data.Auth;
import com.im.business.common.service.api.CacheBaseService;

/**
 * @author Only
 * @date 2016年5月20日 下午2:00:02
 */
@Service
public class AuthService {
	protected final Logger logger = LogManager.getLogger(this.getClass());
	@Resource
	CacheBaseService cacheBox;

	Map<String, String> map = new HashMap<String, String>();

	public AuthService() {
		map.put("own-000001", "kkkyyyttt");
	}

	public boolean isAuth(Auth auth) {
		boolean isAuth = false;
		String authId = auth.getId();
		String authKey = auth.getKey();
		if (map.containsKey(authId)) {
			String key = map.get(authId);
			isAuth = (authKey.equals(key));
		}
		return isAuth;
	}

	public ResultData getToken(Auth auth, String userId) {
		ResultData result = new ResultData();
		String code = ResultData.code_success;
		String authId = auth.getId();
		String authKey = auth.getKey();
		if (map.containsKey(authId)) {
			String key = map.get(authId);
			if (authKey.equals(key)) {
				String text = authKey + userId;
				String token = getToken(text);
				result.put("token", token);
				cacheBox.putDefault(CacheKeyUtil.getTokenCacheKey(userId), token);// 保存5分钟令牌
			} else {
				code = ResultData.code_fail;
				result.setMessage("authKey无效");
			}
		} else {
			code = ResultData.code_fail;
			result.setMessage("authId无效");
		}
		result.setCode(code);
		return result;
	}

	protected String getToken(String text) {
		try {
			byte id[] = text.getBytes();
			byte now[] = new Long(System.currentTimeMillis()).toString().getBytes();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(id);
			md.update(now);
			BigInteger a = new BigInteger(md.digest());
			return a.toString(16);
		} catch (IllegalStateException e) {
			return (text);
		} catch (NoSuchAlgorithmException e) {
			return (text);
		}
	}
}
