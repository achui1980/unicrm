/**
 * @(#) BatchSoUCallback.java 2007-7-18
 * 
 * Copyright 2006 ptnetwork
 */
package com.sail.cot.common.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.sail.cot.common.dao.SaveEvent;

/**
 * <p>Title: 预算支撑系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: 厦门纵横集团科技股份有限公司</p>
 * @author jasonzhang
 *
 */
public class BatchSoUCallback implements HibernateCallback {

	private List records;

	public BatchSoUCallback(List records) {
		this.records = records;
	}
	public Object doInHibernate(Session session) throws HibernateException, SQLException {
		session.clear();
		for (int i=0; i<records.size(); i++) {
			Object record = records.get(i);
			SaveEvent.doInjectProperties(record);
			session.saveOrUpdate(record);
		}
		session.flush();
		session.clear();
		return new Integer(records.size());
	}
}
