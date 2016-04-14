package com.xhc.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.xhc.entity.PicModel;
import com.xhc.model.Employee;
import com.xhc.model.PicLibrary;
import com.xhc.model.TmLibrary;
import com.xhc.util.UUIDGen;

@Service
public class SucaiService extends BaseService {
	@Transactional
	public void addTextMsg(TmLibrary tmLibrary) {
		tmLibrary.setTlStatus(1);
		String emNo = tmLibrary.getEmployee().getEmNo();
		String hql = "FROM Employee AS e WHERE e.emNo=:emNo";
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("emNo", emNo);
		Employee employee = weixinDao
				.queryByCondition(Employee.class, hql, map).get(0);
		tmLibrary.setEmployee(employee);
		this.add(tmLibrary);
	}

	@Transactional
	public Map<String, Object> getTextMsgList(int start, int size, String search) {
		Map<String, Object> map = new HashMap<String, Object>(4);
		StringBuilder hql = new StringBuilder(
				"FROM TmLibrary AS tl  WHERE  (tl.tlTitle like '%" + search
						+ "%'  OR tl.tlContent like '%" + search
						+ "%' OR tl.employee.emNo like '%" + search + "%' ");
		if ("启用".contains(search)) {
			hql.append(" OR tl.tlStatus=1 ");
		} else if ("禁用".contains(search)) {
			hql.append(" OR tl.tlStatus=0 ");
		}
		hql.append(")");
		List<TmLibrary> list = weixinDao.queryByConditionWithPaging(
				TmLibrary.class, hql.toString(), map, start, size);
		hql = hql.insert(0, "SELECT COUNT(*) ");
		int count = weixinDao.count(hql.toString(), map);
		map.clear();
		map.put("rows", mapTextMsg(list));
		map.put("total", count);
		return map;
	}

	private List<Map<String, Object>> mapTextMsg(List<TmLibrary> list) {
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		for (TmLibrary tmLibrary : list) {
			map = new HashMap<String, Object>();
			map.put("tlId", tmLibrary.getTlId());
			map.put("emNo", tmLibrary.getEmployee().getEmNo());
			map.put("tlContent", tmLibrary.getTlContent());
			map.put("tlTitle", tmLibrary.getTlTitle());
			map.put("tlStatus", tmLibrary.getTlStatus());
			list1.add(map);
		}
		return list1;
	}

	@Transactional
	public void enableTextMsg(String id) {
		String hql = "UPDATE TmLibrary AS tl SET tl.tlStatus=:status WHERE tl.tlId=:id";
		Map<String, Object> values = new HashMap<String, Object>(2);
		values.put("status", 1);
		Map<String, Object> conditions = new HashMap<String, Object>(2);
		conditions.put("id", Integer.parseInt(id));
		weixinDao.update(hql, values, conditions);
	}

	@Transactional
	public void disableTextMsg(String id) {
		String hql = "UPDATE TmLibrary AS tl SET tl.tlStatus=:status WHERE tl.tlId=:id";
		Map<String, Object> values = new HashMap<String, Object>(2);
		values.put("status", 0);
		Map<String, Object> conditions = new HashMap<String, Object>(2);
		conditions.put("id", Integer.parseInt(id));
		weixinDao.update(hql, values, conditions);
	}

	@Transactional
	public void deleteTextMsg(String id) {
		delete(TmLibrary.class, Integer.parseInt(id));
	}

	@Transactional
	public void editTextMsg(TmLibrary tmLibrary) {
		System.out.println(tmLibrary.getTlContent());
		System.out.println(tmLibrary.getTlTitle());
		System.out.println(tmLibrary.getTlId());
		System.out.println(tmLibrary.getEmployee().getEmNo());
		System.out.println(tmLibrary.getTlStatus());
		String emNo = tmLibrary.getEmployee().getEmNo();
		String hql = "FROM Employee AS e WHERE e.emNo=:emNo";
		Map<String, Object> conditions = new HashMap<String, Object>(2);
		conditions.put("emNo", emNo);
		Employee employee = weixinDao.queryByCondition(Employee.class, hql,
				conditions).get(0);
		hql = "UPDATE TmLibrary AS tl SET tl.employee=:employee,tl.tlTitle=:tlTitle,tl.tlContent=:tlContent WHERE tl.tlId=:tlId";
		conditions.clear();
		conditions.put("tlId", tmLibrary.getTlId());
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("employee", employee);
		values.put("tlTitle", tmLibrary.getTlTitle());
		values.put("tlContent", tmLibrary.getTlContent());
		weixinDao.update(hql, values, conditions);
	}

	@Transactional
	public void addPicMsg(PicModel picModel, String emNo) {
		MultipartFile multipartFile = picModel.getImages().get(0);
		String fileName = multipartFile.getOriginalFilename();
		int index = fileName.lastIndexOf(".");
		fileName = UUIDGen.generate() + fileName.substring(index);
		try {
			FileOutputStream out = new FileOutputStream(
					"D:/Program Files (x86)/Tomcat 7.0/webapps/weixin/picture/"
							+ fileName);
			// 写入文件
			out.write(multipartFile.getBytes());
			out.flush();
			out.close();

			PicLibrary picLibrary = new PicLibrary();
			picLibrary.setPlStatus(1);
			String hql = "FROM Employee AS e WHERE e.emNo=:emNo";
			Map<String, Object> map = new HashMap<String, Object>(2);
			map.put("emNo", emNo);
			Employee employee = weixinDao.queryByCondition(Employee.class, hql,
					map).get(0);
			picLibrary.setEmployee(employee);
			picLibrary.setPlTitle(picModel.getPicTitle());
			picLibrary.setPlUrl("./picture/" + fileName);
			this.add(picLibrary);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Transactional
	public Map<String, Object> getpicMsgList(int start, int size, String search) {
		Map<String, Object> map = new HashMap<String, Object>(4);
		StringBuilder hql = new StringBuilder(
				"FROM PicLibrary AS pl  WHERE  (pl.plTitle like '%" + search
						+ "%'  OR pl.employee.emNo like '%" + search + "%' ");
		if ("启用".contains(search)) {
			hql.append(" OR pl.plStatus=1 ");
		} else if ("禁用".contains(search)) {
			hql.append(" OR pl.plStatus=0 ");
		}
		hql.append(")");
		List<PicLibrary> list = weixinDao.queryByConditionWithPaging(
				PicLibrary.class, hql.toString(), map, start, size);
		hql = hql.insert(0, "SELECT COUNT(*) ");
		int count = weixinDao.count(hql.toString(), map);
		map.clear();
		map.put("rows", mapPicMsg(list));
		map.put("total", count);
		return map;
	}

	private List<Map<String, Object>> mapPicMsg(List<PicLibrary> list) {
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		for (PicLibrary picLibrary : list) {
			map = new HashMap<String, Object>();
			map.put("plId", picLibrary.getPlId());
			map.put("emNo", picLibrary.getEmployee().getEmNo());
			map.put("plUrl", picLibrary.getPlUrl());
			map.put("plTitle", picLibrary.getPlTitle());
			map.put("plStatus", picLibrary.getPlStatus());
			list1.add(map);
		}
		return list1;
	}

	@Transactional
	public void deletePicMsg(String id) {
		String hql = "From PicLibrary AS pl WHERE  pl.plId=:id";
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("id", Integer.parseInt(id));
		String url = weixinDao.queryByCondition(PicLibrary.class, hql, map)
				.get(0).getPlUrl();
		deletePicture(url.substring(url.lastIndexOf("/") + 1));
		delete(PicLibrary.class, Integer.parseInt(id));
	}

	private void deletePicture(String fileName) {
		String path = "D:/Program Files (x86)/Tomcat 7.0/webapps/weixin/picture/"
				+ fileName;
		File file = new File(path);
		file.delete();
	}

	@Transactional
	public void disablePicMsg(String id) {
		String hql = "UPDATE PicLibrary AS pl SET pl.plStatus=:status WHERE pl.plId=:id";
		Map<String, Object> values = new HashMap<String, Object>(2);
		values.put("status", 0);
		Map<String, Object> conditions = new HashMap<String, Object>(2);
		conditions.put("id", Integer.parseInt(id));
		weixinDao.update(hql, values, conditions);
	}

	@Transactional
	public void enablePicMsg(String id) {
		String hql = "UPDATE PicLibrary AS pl SET pl.plStatus=:status WHERE pl.plId=:id";
		Map<String, Object> values = new HashMap<String, Object>(2);
		values.put("status", 1);
		Map<String, Object> conditions = new HashMap<String, Object>(2);
		conditions.put("id", Integer.parseInt(id));
		weixinDao.update(hql, values, conditions);
	}

	@Transactional
	public void editPicMsg(PicModel picModel, String emNo) {
		MultipartFile multipartFile = picModel.getImages().get(0);
		String fileName = null;
		Map<String, Object> map = new HashMap<String, Object>(8);
		fileName = multipartFile.getOriginalFilename();
		if (fileName != null && fileName.length() > 0) {
			int index = fileName.lastIndexOf(".");
			fileName = UUIDGen.generate() + fileName.substring(index);
			try {
				FileOutputStream out = new FileOutputStream(
						"D:/Program Files (x86)/Tomcat 7.0/webapps/weixin/picture/"
								+ fileName);
				// 写入文件
				out.write(multipartFile.getBytes());
				out.flush();
				out.close();
				String hql = "From PicLibrary AS pl WHERE  pl.plId=:id";
				map.put("id", Integer.parseInt(picModel.getPicId()));
				String url = weixinDao
						.queryByCondition(PicLibrary.class, hql, map).get(0)
						.getPlUrl();
				deletePicture(url.substring(url.lastIndexOf("/") + 1));
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		map.clear();
		String hql = "FROM Employee AS e WHERE e.emNo=:emNo";
		map.put("emNo", emNo);
		Employee employee = weixinDao
				.queryByCondition(Employee.class, hql, map).get(0);
		map.clear();
		map.put("employee", employee);
		map.put("plTitle", picModel.getPicTitle());
		hql = "UPDATE PicLibrary AS pl SET pl.plTitle=:plTitle,pl.employee=:employee";
		if (fileName != null && fileName.length() > 0) {
			hql += ",pl.plUrl=:plUrl";
			map.put("plUrl", "./picture/" + fileName);
		}
		hql += " WHERE pl.plId=:plId";
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("plId", Integer.parseInt(picModel.getPicId()));
		weixinDao.update(hql, map, conditions);
	}

	@Transactional
	public Map<String, Object> getTextMsgList(String search) {
		Map<String, Object> map = new HashMap<String, Object>(4);
		StringBuilder hql = new StringBuilder(
				"FROM TmLibrary AS tl  WHERE  tl.tlStatus=1 AND (tl.tlTitle like '%"
						+ search + "%'  OR tl.tlContent like '%" + search
						+ "%' OR tl.employee.emNo like '%" + search + "%' )");
		List<TmLibrary> list = weixinDao.queryByCondition(TmLibrary.class,
				hql.toString(), map);
		hql = hql.insert(0, "SELECT COUNT(*) ");
		int count = weixinDao.count(hql.toString(), map);
		map.clear();
		map.put("rows", mapTextMsg(list));
		map.put("total", count);
		return map;
	}
}
