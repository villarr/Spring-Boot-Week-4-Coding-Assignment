package com.promineotech.jeep.service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.promineotech.jeep.dao.JeepSalesDao;
import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.entity.Jeeps;


@Service
public class DefaultJeepSalesService implements JeepSalesService {
	Logger logged = LoggerFactory.getLogger(DefaultJeepSalesService.class);
	
	@Autowired
	private JeepSalesDao jeepSalesDao;
	
	@Transactional(readOnly = true)
	@Override
	public List<Jeeps> fetchJeeps(JeepModel model, String trim) {
		logged.info("The fetchJeeps method was called with model = {} "
				+ "and trim = {}", model, trim);
		
		List<Jeeps> jeeps = jeepSalesDao.fetchJeeps(model, trim);
		if (jeeps.isEmpty()) {
			String msg = String.format("No jeeps found with model=%s and trim=%s", model, trim); 
			throw new NoSuchElementException(msg);
		}
		Collections.sort(jeeps);
		return jeepSalesDao.fetchJeeps(model, trim);
	}

}
