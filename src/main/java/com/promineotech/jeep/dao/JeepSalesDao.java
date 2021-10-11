package com.promineotech.jeep.dao;

import java.util.List;

import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.entity.Jeeps;

public interface JeepSalesDao {

	List<Jeeps> fetchJeeps(JeepModel model, String trim);

}
