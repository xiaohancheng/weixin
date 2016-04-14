package com.xhc.service;

import java.io.Serializable;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.xhc.dao.WeixinDao;

@Component
public class BaseService {
	protected WeixinDao weixinDao;

	@Autowired
	public void setVolunteerDao(WeixinDao weixinDao) {
		this.weixinDao = weixinDao;
	}

	@Transactional
	public void add(Object entity) {
		this.weixinDao.save(entity);
	}

	public void update(Object entity) {
		this.weixinDao.update(entity);
	}

	@Transactional
	public void update(Class<?> clazz, Map<String, Object> values,
			Map<String, Object> conditions) {
		String className = clazz.getName();
		StringBuffer hql = new StringBuffer("UPDATE " + className + " cn SET ");
		if (values != null && !values.isEmpty()) {
			for (String key : values.keySet()) {
				hql.append(" cn." + key + "=:" + key + ",");
			}
			hql.deleteCharAt(hql.length() - 1);
		}
		if (conditions != null && !conditions.isEmpty()) {
			hql.append(" WHERE");
			for (String key : conditions.keySet()) {
				hql.append(" cn." + key + "=:" + key + " AND");
			}
			hql.delete(hql.length() - 4, hql.length());
		}
		weixinDao.update(hql.toString(), values, conditions);
	}

	@Transactional
	public void delete(Class<?> clazz, Serializable id) {
		weixinDao.deleteByPriKey(clazz, id);
	}

	public int getCount(Class<?> clazz) {
		String className = clazz.getName();
		String hql = "SELECT COUNT(*) FROM " + className;
		return weixinDao.count(hql);
	}

	public int getCount(Class<?> clazz, Map<String, Object> map) {
		String className = clazz.getName();
		StringBuffer hql = new StringBuffer("SELECT COUNT(*) FROM " + className
				+ " cn ");
		if (map != null && !map.isEmpty()) {
			hql.append(" WHERE");
			for (String key : map.keySet()) {
				hql.append(" cn." + key + "=:" + key + " AND");
			}
			hql.delete(hql.length() - 4, hql.length());
		}
		return weixinDao.count(hql.toString(), map);
	}
}
