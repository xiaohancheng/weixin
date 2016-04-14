package com.xhc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhc.entity.UserBaseMessage;
import com.xhc.model.Employee;
import com.xhc.util.MD5Util;
import com.xhc.util.UUIDGen;

@Service
public class AccountService extends BaseService {
	@Transactional
	public Employee login(String username, String password, String identity) {
		String hql = "FROM Employee  e WHERE e.emNo=:username AND e.emPassword=:password AND e.emIdentity=:identity AND e.emStatus=1";
		Map<String, Object> map = new HashMap<String, Object>(4);
		map.put("username", username);
		map.put("password", password);
		map.put("identity", identity);
		List<Employee> employees = this.weixinDao.queryByCondition(
				Employee.class, hql, map);
		if (employees == null || employees.size() == 0)
			return null;
		return employees.get(0);
	}

	public boolean logout(String username) {
		List<UserBaseMessage> list = KeFuDispatcherService.kf_user
				.get(username);
		if (list != null && list.size() > 0)
			return false;
		return true;
	}

	@Transactional
	public Map<String, Object> getUserList(int start, int size, String search) {
		Map<String, Object> map = new HashMap<String, Object>(4);
		map.put("emNo", "admin");
		map.put("emStatus", -1);
		StringBuilder hql = new StringBuilder(
				"FROM Employee AS e  WHERE e.emNo!=:emNo AND  e.emStatus!=:emStatus AND (e.emNo like '%"
						+ search + "%'  OR e.emName like '%" + search + "%' ");
		hql.append(" OR e.emEmail like '%" + search + "%' OR e.emPhone like '%"
				+ search + "%' ");
		if (search.equals("男")) {
			hql.append("OR e.emSex=1 ");
		} else if (search.equals("女")) {
			hql.append("OR e.emSex=0 ");
		}
		if ("启用".contains(search)) {
			hql.append("OR e.emStatus=1 ");
		} else if ("禁用".contains(search)) {
			hql.append("OR e.emStatus=0 ");
		}
		if ("客服人员".contains(search)) {
			hql.append(" OR e.emIdentity='user' ");
		} else if ("管理员".contains(search)) {
			hql.append(" OR e.emIdentity='admin' ");
		}
		hql.append(")");
		List<Employee> list = weixinDao.queryByConditionWithPaging(
				Employee.class, hql.toString(), map, start, size);
		hql = hql.insert(0, "SELECT COUNT(*) ");
		int count = weixinDao.count(hql.toString(), map);
		map.clear();
		map.put("rows", list);
		map.put("total", count);
		return map;
	}

	@Transactional
	public void deleteUser(String id) {
		String hql = "UPDATE Employee AS e SET e.emStatus=:status WHERE e.emId=:id";
		Map<String, Object> values = new HashMap<String, Object>(2);
		values.put("status", -1);
		Map<String, Object> conditions = new HashMap<String, Object>(2);
		conditions.put("id", id);
		weixinDao.update(hql, values, conditions);
	}

	@Transactional
	public void disableUser(String id) {
		String hql = "UPDATE Employee AS e SET e.emStatus=:status WHERE e.emId=:id";
		Map<String, Object> values = new HashMap<String, Object>(2);
		values.put("status", 0);
		Map<String, Object> conditions = new HashMap<String, Object>(2);
		conditions.put("id", id);
		weixinDao.update(hql, values, conditions);
	}

	@Transactional
	public void enableUser(String id) {
		String hql = "UPDATE Employee AS e SET e.emStatus=:status WHERE e.emId=:id";
		Map<String, Object> values = new HashMap<String, Object>(2);
		values.put("status", 1);
		Map<String, Object> conditions = new HashMap<String, Object>(2);
		conditions.put("id", id);
		weixinDao.update(hql, values, conditions);
	}

	@Transactional
	public void updateUser(String str) {
		JSONObject object = JSONObject.fromObject(str);
		String emNo = (String) object.get("emNo");
		String emName = (String) object.get("emName");
		String emSex = (String) object.get("emSex");
		String emEmail = (String) object.get("emEmail");
		String emPhone = (String) object.get("emPhone");
		String hql = "UPDATE Employee AS e SET e.emNo=:emNo,e.emName=:emName,e.emSex=:emSex,e.emEmail=:emEmail,e.emPhone=:emPhone WHERE e.emNo=:emNo";
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("emName", emName);
		values.put("emSex", emSex);
		values.put("emEmail", emEmail);
		values.put("emPhone", emPhone);
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("emNo", emNo);
		weixinDao.update(hql, values, conditions);
	}

	@Transactional
	public String changePassword(String str) {
		JSONObject object = JSONObject.fromObject(str);
		String emNo = object.getString("emNo");
		String newPassword = (String) object.get("newPassword");
		String oldPassword = (String) object.get("oldPassword");
		String ensurePassword = (String) object.get("ensurePassword");
		if (!newPassword.equals(ensurePassword))
			return "error1";
		String hql = "FROM Employee AS e WHERE e.emNo=:emNo";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("emNo", emNo);
		Employee employee = weixinDao
				.queryByCondition(Employee.class, hql, map).get(0);
		if (!employee.getEmPassword().equals(oldPassword))
			return "error2";
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("password", newPassword);
		hql = "UPDATE Employee AS e SET e.emPassword=:password WHERE e.emNo=:emNo";
		weixinDao.update(hql, values, map);
		return "success";
	}

	@Transactional
	public void addUser(Employee employee) {
		employee.setEmId(UUIDGen.generate());
		employee.setEmStatus(1);
		employee.setEmPassword(MD5Util.string2MD5("123456"));
		this.add(employee);
	}
}
