package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

import com.promineotech.jeep.JeepSales;
import com.promineotech.jeep.controller.support.CreateOrderTestSupport;
import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.entity.Order;

@SpringBootTest(classes = JeepSales.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = {"classpath:migrations/V1.0__Jeep_Schema.sql"}, 
    config = @SqlConfig(encoding = "utf-8"))

class CreateOrderTest extends CreateOrderTestSupport {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Test
	void testCreateOrderReturnsSuccess201() {
		
	//Given; an order as JSON 
	String body = createOrderBody();
	String uri = getBaseUriForOrders();
	
	int numRowsOrder = JdbcTestUtils.countRowsInTable(jdbcTemplate, "orders");
	int numRowsOptions = JdbcTestUtils.countRowsInTable(jdbcTemplate, "order_options");
	
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	HttpEntity<String> bodyEntity = new HttpEntity<>(body, headers);
	
	//When: the order is sent
	ResponseEntity<Order> response = getRestTemplate().exchange(uri, HttpMethod.POST, bodyEntity ,
			Order.class);
	
	//Then: a 201 status is returned
	assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	
	//And: the returned order is correct
	assertThat(response.getBody()).isNotNull();
	Order order = response.getBody();
	assertThat(order.getCustomer().getCustomerId()).isEqualTo("KAPPEL_TERZO");
	assertThat(order.getModel().getModelId()).isEqualTo(JeepModel.RENEGADE);
	assertThat(order.getModel().getTrimLevel()).isEqualTo("Islander");
	assertThat(order.getModel().getNumDoors()).isEqualTo(4);
	assertThat(order.getColor().getColorId()).isEqualTo("EXT_SANGRIA");
	assertThat(order.getEngine().getEngineId()).isEqualTo("1_3_TURBO");
	assertThat(order.getTire().getTireId()).isEqualTo("37_YOKOHAMA");
	assertThat(order.getOptions()).hasSize(6);
	assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "orders")).isEqualTo(numRowsOrder + 1);
	assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "order_options")).isEqualTo(numRowsOptions + 6);
	}

	
}
	
