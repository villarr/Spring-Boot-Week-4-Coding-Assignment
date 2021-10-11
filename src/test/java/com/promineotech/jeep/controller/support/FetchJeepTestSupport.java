package com.promineotech.jeep.controller.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.entity.Jeeps;

public class FetchJeepTestSupport extends BaseTest  {

  public static List<Jeeps> buildExpected() {
			List<Jeeps> list = new LinkedList<>();
			//// @formatter:off
			list.add(Jeeps.builder().modelId(JeepModel.WRANGLER)
				.trimLevel("Sport")
				.numDoors(2)
				.wheelSize(17)
				.basePrice(new BigDecimal("28475.00"))
				.build());
					
			list.add(Jeeps.builder().modelId(JeepModel.WRANGLER)
				.trimLevel("Sport")
				.numDoors(4)
				.wheelSize(17)
				.basePrice(new BigDecimal("31975.00"))
				.build());
			// @formatter:on
			Collections.sort(list);
			return list;
		}
  protected void assertErrorMessageValue(Map<String, Object> error, HttpStatus status) {
		assertThat(error).containsKey("message").containsEntry("status code", 
					status.value())
					.containsEntry("uri","/jeeps")
					.containsKey("timestamp")
					.containsEntry("reason",status.getReasonPhrase());
	}

  }
