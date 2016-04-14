package com.xhc.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.gson.Gson;
import com.xhc.model.BaseMessage;

@Service
public class ChatService extends BaseService {
	@Transactional
	public Map<String, Object> getLastChatList(int start, int size,
			String search) {
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "SELECT cg.baseMessage ";
		String hql1 = "FROM ChatGroup AS cg WHERE ";
		hql1 += "(cg.baseMessage.fromUserName like '%" + search
				+ "%' OR cg.baseMessage.toUserName like '%" + search
				+ "%') AND ";
		hql1 += " cg.baseMessage.createTime in (SELECT MAX(cg1.baseMessage.createTime) FROM ChatGroup AS cg1 GROUP BY cg1.cgGroupId) GROUP BY cg.cgGroupId ORDER BY cg.baseMessage.createTime DESC";
		List<BaseMessage> baseMessages = weixinDao.queryByConditionWithPaging(
				BaseMessage.class, hql + hql1, null, start, size);
		System.out.println(baseMessages.size());
		int count = weixinDao.queryByCondition(BaseMessage.class, hql + hql1,
				null).size();
		map.put("rows", baseMessages);
		map.put("total", count);
		return map;
	}

	@Transactional
	public String countKefuJieruRenshu(String time) {
		long startTime = dealTime(time);
		String hql = "SELECT cg.baseMessage FROM ChatGroup AS cg WHERE cg.baseMessage.createTime>:startTime AND cg.baseMessage.createTime in (SELECT MAX(cg1.baseMessage.createTime) FROM ChatGroup AS cg1 GROUP BY cg1.cgGroupId) GROUP BY cg.cgGroupId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startTime", startTime);
		List<BaseMessage> baseMessages = weixinDao.queryByCondition(
				BaseMessage.class, hql, map);
		hql = "SELECT e.emNo FROM Employee AS e WHERE e.emStatus!=-1 AND e.emIdentity='user'";
		List<String> emNos = weixinDao
				.queryByCondition(String.class, hql, null);
		Map<String, Integer> jieruCount = new HashMap<String, Integer>();
		for (String emNo : emNos) {
			jieruCount.put(emNo, 0);
		}
		for (BaseMessage baseMessage : baseMessages) {
			if (jieruCount.containsKey(baseMessage.getFromUserName())) {
				jieruCount.put(baseMessage.getFromUserName(),
						jieruCount.get(baseMessage.getFromUserName()) + 1);
			} else if (jieruCount.containsKey(baseMessage.getToUserName())) {
				jieruCount.put(baseMessage.getToUserName(),
						jieruCount.get(baseMessage.getToUserName()) + 1);
			}
		}
		return toJsonMsg(jieruCount, time, emNos);
	}

	private String toJsonMsg(Map<String, Integer> map, String time,
			List<String> emNos) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, String> chart = new HashMap<String, String>(2);
		chart.put("type", "column");
		resultMap.put("chart", chart);

		Map<String, String> title = new HashMap<String, String>(2);
		if (time.equals("全部"))
			title.put("text", "客服人员接入客户人数统计");
		else
			title.put("text", "客服人员" + time + "接入客户人数统计");
		resultMap.put("title", title);

		Map<String, Object> xAxis = new HashMap<String, Object>(2);
		xAxis.put("categories", emNos);
		resultMap.put("xAxis", xAxis);

		Map<String, Object> yAxis = new HashMap<String, Object>(2);
		Map<String, String> title1 = new HashMap<String, String>(2);
		yAxis.put("min", 0);
		title1.put("text", "人数（个）");
		yAxis.put("title", title1);
		resultMap.put("yAxis", yAxis);

		Map<String, Object> unit = new HashMap<String, Object>();
		unit.put("name", "服务人数");
		List<Integer> data = new ArrayList<Integer>();
		for (String emNo : emNos) {
			data.add(map.get(emNo));
		}
		unit.put("data", data);
		List<Object> series = new ArrayList<Object>();
		series.add(unit);
		resultMap.put("series", series);

		Gson gson = new Gson();
		return gson.toJson(resultMap);
	}

	private long dealTime(String time) {
		Calendar c1 = new GregorianCalendar();
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		long startTime = 0;
		if (time.equals("本日")) {
			startTime = c1.getTime().getTime() / 1000;
		} else if (time.equals("本周")) {
			c1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			startTime = c1.getTime().getTime() / 1000;
		} else if (time.equals("本月")) {
			c1.set(Calendar.DAY_OF_MONTH, 1);
			startTime = c1.getTime().getTime() / 1000;
		} else if (time.equals("本年")) {
			c1.set(Calendar.MONTH, 0);
			c1.set(Calendar.DAY_OF_MONTH, 1);
			startTime = c1.getTime().getTime() / 1000;
		} else {
			startTime = 0;
		}
		return startTime;
	}
}
