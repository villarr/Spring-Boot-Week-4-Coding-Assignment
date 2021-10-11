package com.promineotech.jeep.service;

import java.util.List;

import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.entity.Jeeps;

public interface JeepSalesService {
	/**
	 * 
	 * @param model
	 * @param trim
	 * @return
	 */
	List<Jeeps> fetchJeeps(JeepModel model, String trim);
	
}