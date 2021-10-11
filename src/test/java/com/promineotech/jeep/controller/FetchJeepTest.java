package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import com.promineotech.jeep.Constants;
import com.promineotech.jeep.JeepSales;
import com.promineotech.jeep.controller.support.FetchJeepTestSupport;
import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.entity.Jeeps;
import com.promineotech.jeep.service.JeepSalesService;
import static org.junit.jupiter.params.provider.Arguments.arguments;


class FetchJeepTest extends FetchJeepTestSupport {
	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	@SpringBootTest(classes = JeepSales.class, webEnvironment = WebEnvironment.RANDOM_PORT)
	@ActiveProfiles("test")
	@Sql(scripts = {"classpath:migrations/V1.0__Jeep_Schema.sql"}, 
	    config = @SqlConfig(encoding = "utf-8"))
	
	class TestsThatDoNotPolluteApplicationContext extends FetchJeepTestSupport{
		@Test
		
		void testThatJeepsAreReturnedWhichUseValidJeepModelAndTrim() {
			
    //given a valid model trim and URI
    JeepModel model = JeepModel.WRANGLER;
    String trim = "Sport";
      String uri = String.format("%s?model=%s&trim=%s",getBaseUriForJeeps(),model,trim);
      
    //when a connection is made to the URI
      //RestTemplate restTemplate = new RestTemplate();
      ResponseEntity<List<Jeeps>> response = 
    		  getRestTemplate().exchange(uri, HttpMethod.GET, null, 
            		  new ParameterizedTypeReference<List<Jeeps>>() {});
    
    //then a list of jeeps are returned - a status code
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    
    // and the actual list returned is the same as the expected list
    List<Jeeps> actual = response.getBody();
    List<Jeeps> expected = buildExpected();
    //actual.forEach(jeep -> jeep.setModelPK(null));
    
    assertThat(actual).isEqualTo(expected);
    
	}
	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	@Test
	void testThatAnErrorMessageIsReturnedWhenAnUnknownTrimIsSupplied() {
		
		//given a valid model trim and URI
		JeepModel model = JeepModel.WRANGLER;
		String trim = "Unknown Value";
		  String uri = String.format("%s?model=%s&trim=%s",getBaseUriForJeeps(),model,trim);
		  
		//when a connection is made to the URI
		  //RestTemplate restTemplate = new RestTemplate();
		  ResponseEntity<Map<String,Object>> response = 
				  getRestTemplate().exchange(uri, HttpMethod.GET, null, 
		        		  new ParameterizedTypeReference<Map<String,Object>>() {});
		
		//then a not found (404) status code is returned
	 assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		
		// and an error message is returned
   Map<String, Object> error = response.getBody();
   assertErrorMessageValue(error, HttpStatus.NOT_FOUND);
		
	}

	@ParameterizedTest
	@MethodSource("com.promineotech.jeep.controller.FetchJeepTest$TestsThatDoNotPolluteApplicationContext#parametersForInvalidInput")
	
	void testThatAnErrorMessageIsReturnedWhenAnInvalidIsSupplied(
			String model, String trim, String reason) {
		
		//given a valid model trim and URI
		String uri = String.format("%s?model=%s&trim=%s",getBaseUriForJeeps(),model,trim);
		  
		//when a connection is made to the URI
		  ResponseEntity<Map<String,Object>> response = 
				  getRestTemplate().exchange(uri, HttpMethod.GET, null, 
		        		  new ParameterizedTypeReference<Map<String,Object>>() {});
		
		//then an invalid parameter (400) status code is returned
		  assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		
		// and an error message is returned
   Map<String, Object> error = response.getBody();
   assertErrorMessageValue(error, HttpStatus.BAD_REQUEST);
   
   
   }
	public Stream<Arguments> parametersForInvalidInput(){
		/// @formatter:off
		return Stream.of(
				arguments("WRANGLER","@#$*","Trim contains non-alpha-numeric characters"),
				arguments("CHEROKEE","C".repeat(Constants.TRIM_MAX_LENGTH + 1),
						"Trim length is too long"),
				arguments("INVALID","Sport","Invalid model was given."));
		//@formatter:on	
	}
	/*
	 * 
	 * 
	 * 
	 * 
	 */
	
	@Nested
	@SpringBootTest(classes = JeepSales.class, webEnvironment = WebEnvironment.RANDOM_PORT)
	@ActiveProfiles("test")
	@Sql(scripts = {"classpath:migrations/V1.0__Jeep_Schema.sql"}, 
	    config = @SqlConfig(encoding = "utf-8"))
	class TestsThatPolluteApplicationContext extends FetchJeepTestSupport{
		@MockBean
		private JeepSalesService jeepSalesService;
		
		@Test
		void testThatAnUnplannedErrorResultsIn500Status() {
			
			//given a valid model trim and URI
			JeepModel model = JeepModel.WRANGLER;
			String trim = "Invalid";
			  String uri = String.format("%s?model=%s&trim=%s",getBaseUriForJeeps(),model,trim);
			 
			  doThrow(new RuntimeException("Gotcha!")).when(jeepSalesService)
			.fetchJeeps(model, trim);
			  
			  //when a connection is made to the URI
			  //RestTemplate restTemplate = new RestTemplate();
			  ResponseEntity<Map<String,Object>> response = 
					  getRestTemplate().exchange(uri, HttpMethod.GET, null, 
			        		  new ParameterizedTypeReference<Map<String,Object>>() {});
			
			//then a not found (500) status code is returned
		 Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
			
			// and an error message is returned
	   Map<String, Object> error = response.getBody();
	   assertErrorMessageValue(error, HttpStatus.INTERNAL_SERVER_ERROR);
			
	}
	}
	}
}