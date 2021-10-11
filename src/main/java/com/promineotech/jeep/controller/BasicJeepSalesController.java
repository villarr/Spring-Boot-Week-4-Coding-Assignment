package com.promineotech.jeep.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.entity.Jeeps;
import com.promineotech.jeep.service.JeepSalesService;

import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
public class BasicJeepSalesController implements JeepSalesController{
	
	@Autowired
	private JeepSalesService jeepSalesService;
	@Override
	public List<Jeeps> fetchJeepList(JeepModel model, String trim) {
		log.debug("test");
		return jeepSalesService.fetchJeeps(model, trim);
	}

}
