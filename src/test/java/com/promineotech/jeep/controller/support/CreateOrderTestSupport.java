package com.promineotech.jeep.controller.support;

public class CreateOrderTestSupport extends BaseTest{
	protected String createOrderBody() {
		// @formatter:off
		return "{\n"
			+ "  \"customer\":\"KAPPEL_TERZO\",\n"
			+ "  \"model\":\"RENEGADE\",\n"
			+ "  \"trim\":\"Islander\",\n"
			+ "  \"doors\":4,\n"
			+ "  \"color\":\"EXT_SANGRIA\",\n"
			+ "  \"engine\":\"1_3_TURBO\",\n"
			+ "  \"tire\":\"37_YOKOHAMA\",\n"
			+ "  \"options\":[\n"
			+ "    \"DOOR_SMITTY_FRONT\",\n"
			+ "    \"EXT_DUAL_UPPER_PREMIUM\",\n"
			+ "    \"EXT_FISHBONE_REAR\",\n"
			+ "    \"EXT_WARN_BUMPER_FRONT\",\n"
			+ "    \"EXT_TACTIK_FRONT_WINCH\",\n"
			+ "    \"EXT_CROWN_SOFT\"\n"
			+ "  ]\n"
			+ "}";
	// @formatter:on	
	}
}
