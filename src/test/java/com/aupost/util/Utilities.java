package com.aupost.util;

import java.util.ArrayList;
import java.util.List;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
/**
 * To define all the common methods
 * @author Chandra
 *
 */
public class Utilities {
	/**
	 * Convert Response object to JsonPath object
	 * @param resp
	 * @return JsonPath object
	 */
	public static JsonPath rawToJson(Response resp) {
		String respString = resp.asString();
		JsonPath js = new JsonPath(respString);
		return js;
	}
	
	/**
	 * This method returns the list of parcel services for the country New Zealand
	 * @return List<String>
	 */
	public static List<String> getParcelServicesNZ(){
		List<String> lstExpectedInternationalParcelServicesNZ = new ArrayList<String>();
		lstExpectedInternationalParcelServicesNZ.add("INT_PARCEL_COR_OWN_PACKAGING");
		lstExpectedInternationalParcelServicesNZ.add("INT_PARCEL_EXP_OWN_PACKAGING");
		lstExpectedInternationalParcelServicesNZ.add("INT_PARCEL_STD_OWN_PACKAGING");
		lstExpectedInternationalParcelServicesNZ.add("INT_PARCEL_AIR_OWN_PACKAGING");
		return lstExpectedInternationalParcelServicesNZ;
	}
}
