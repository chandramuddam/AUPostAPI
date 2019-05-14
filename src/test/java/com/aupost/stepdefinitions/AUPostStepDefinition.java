package com.aupost.stepdefinitions;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.*;

import com.aupost.util.Constants;
import com.aupost.util.Utilities;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

/**
 * This class is used to glue the steps mentioned in the feature file
 * 
 * @author Chandra
 *
 */

public class AUPostStepDefinition {
	private static Logger log = LogManager.getLogger(AUPostStepDefinition.class.getName());
	private int fromPostCode;
	private int toPostCode;
	private float weight;
	private String serviceCode;
	private String postageCost;
	private String countryCode;
	private List<String> lstServiceCode;
	private List<Integer> lstPostCode;
	private List<String> lstThickness;
	private List<String> internationalParcelServices;

	@And("^I have \"(.*)\" as country code$")
	public void countryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Given("^I find the first from post code for the suburb \"(.*)\" and state \"(.*)\"$")
	public void getFromPostCode(String suburb, String state) {
		this.fromPostCode = getPostCode(suburb, state);
	}

	@Given("^I find the first to post code for the suburb \"(.*)\" and state \"(.*)\"$")
	public void getToPostCode(String suburb, String state) {
		this.toPostCode = getPostCode(suburb, state);
	}

	@And("^I have \"(.*)\" as weight$")
	public void countryCode(float weight) {
		this.weight = weight;
	}

	/**
	 * This method send the api call to retrieve the list of service codes then get
	 * the first service code as sometimes it returns multiple service codes
	 * 
	 * @param length
	 * @param width
	 * @param thickness
	 * @param weight
	 */

	@And("^I find the first service code for the item length \"(.*)\" width \"(.*)\" thickness \"(.*)\" weight \"(.*)\"$")
	public void getServiceCode(String length, String width, String thickness, String weight) {
		this.lstServiceCode = new ArrayList<String>();
		RestAssured.baseURI = Constants.BASE_URI;
		log.info("Before calling the API for the first service code ... getServiceCode()");
		Response resp = given().header(Constants.AUTH_KEY, Constants.AUTH_KEY_VALUE)
				.queryParam(Constants.LENGTH, length).queryParam(Constants.WIDTH, width)
				.queryParam(Constants.THICKNESS, thickness).queryParam(Constants.WEIGHT, weight).when()
				.get(Constants.DOMESTIC_SERVICE_RESOURCE_URI).then().extract().response();
		log.info("Response : " + resp.asString());
		JsonPath js = Utilities.rawToJson(resp);
		if (null != js && null != js.get("services.service.code")) {
			this.lstServiceCode = js.get("services.service.code");
			if (js.get("services.service.code") instanceof String) {
				this.serviceCode = js.get("services.service.code");
			} else if (js.get("services.service.code") instanceof List) {
				this.lstServiceCode = js.get("services.service.code");
				Collections.sort(lstServiceCode);
				if (lstServiceCode.size() > 0) {
					this.serviceCode = lstServiceCode.get(0);
				}
			}
		} else {
			log.info("No response from the api. Please check");
		}
	}

	/**
	 * This method send the api call to calculate the postage cost for the given
	 * service code, from and to post codes, length, width height and weight
	 * 
	 * @param length
	 * @param width
	 * @param height
	 * @param weight
	 */

	@When("^I build the API request to calculate the postage cost for service code and length \"(.*)\" width \"(.*)\" height \"(.*)\" weight \"(.*)\"$")
	public void buildPostageCostAPIRequestByServiceCode(String length, String width, String height, String weight) {

		RestAssured.baseURI = Constants.BASE_URI;
		log.info("Before calling the API to calculate the postage cost ... buildPostageCostAPIRequestByServiceCode()");
		Response resp = given().header(Constants.AUTH_KEY, Constants.AUTH_KEY_VALUE)
				.queryParam(Constants.FROM_POST_CODE, this.fromPostCode)
				.queryParam(Constants.TO_POST_CODE, this.toPostCode).queryParam(Constants.LENGTH, length)
				.queryParam(Constants.WIDTH, width).queryParam(Constants.HEIGHT, height)
				.queryParam(Constants.WEIGHT, weight).queryParam(Constants.SERVICE_CODE, this.serviceCode).when()
				.get(Constants.POSTAGE_COST_RESOURCE_URI).then().extract().response();
		log.info("Response....  : " + resp.asString());
		JsonPath js = Utilities.rawToJson(resp);
		if (null != js && null != js.get("postage_result.costs.cost.cost")) {
			this.postageCost = js.get("postage_result.costs.cost.cost");
		} else {
			log.info("No response from the api. Please check");
		}
	}

	@Then("^I validate the postage cost as \"(.*)\"$")
	public void postageCost(String postageCost) {
		Assert.assertEquals(this.postageCost, postageCost);
	}

	/**
	 * This method send the api call to retrieve the list of domestic letter
	 * thickness values
	 */

	@Given("^I build the API request for a list of domestic letter thickness values$")
	public void buildLetterThicknessValuesAPIRequest() {
		RestAssured.baseURI = Constants.BASE_URI;
		log.info(
				"Before calling the API to retrieve the list of domestic letter thickness values ... buildLetterThicknessValuesAPIRequest()");
		Response resp = given().header(Constants.AUTH_KEY, Constants.AUTH_KEY_VALUE).when()
				.get(Constants.DOMESTIC_LETTER_THICKNESS_RESOURCE_URI).then().extract().response();
		log.info("Response : " + resp.asString());
		JsonPath js = Utilities.rawToJson(resp);
		if (null != js && null != js.get("thicknesses.thickness.value")) {
			this.lstThickness = js.get("thicknesses.thickness.value");
		} else {
			log.info("No response from the api. Please check");
		}
	}

	/**
	 * This method validates the list of letter thickness values with the expected
	 * values
	 * 
	 * @param fiveMM
	 * @param twentyMM
	 */
	@Then("^I validate the letter thickness values \"(.*)\" and \"(.*)\"$")
	public void letterThicknessValues(int fiveMM, int twentyMM) {
		if (this.lstThickness.size() >= 2) {
			Assert.assertEquals(this.lstThickness.get(0), fiveMM);
			Assert.assertEquals(this.lstThickness.get(1), twentyMM);
		} else {
			Assert.assertTrue(false);
		}
	}

	/**
	 * This method send the api call to retrieve the international parcel services
	 * for the country New Zealand
	 */

	@When("^I build the API request for international parcel services for New Zealand$")
	public void buildInternationalParcelServicesNZAPIRequest() {
		RestAssured.baseURI = Constants.BASE_URI;
		log.info(
				"Before calling the API to retrieve the international parcel services for New Zealand ... buildInternationalParcelServicesNZAPIRequest()");
		Response resp = given().header(Constants.AUTH_KEY, Constants.AUTH_KEY_VALUE)
				.queryParam(Constants.COUNTRY_CODE, this.countryCode).queryParam(Constants.WEIGHT, this.weight).when()
				.get(Constants.INTERNATIONAL_PARCEL_SERVICES_RESOURCE_URI).then().extract().response();
		log.info("Response : " + resp.asString());
		JsonPath js = Utilities.rawToJson(resp);
		if (null != js && null != js.get("services.service.code")) {
			this.internationalParcelServices = js.get("services.service.code");
		} else {
			log.info("No response from the api. Please check");
		}

	}

	@Then("^I validate the international parcel services for New Zealand$")
	public void internationalParcelServicesNZ() {

		List<String> lstExpectedInternationalParcelServicesNZ = Utilities.getParcelServicesNZ();
		Collections.sort(this.internationalParcelServices);
		Collections.sort(lstExpectedInternationalParcelServicesNZ);

		boolean isEqual = this.internationalParcelServices.equals(lstExpectedInternationalParcelServicesNZ);
		Assert.assertTrue(isEqual);
	}

	/**
	 * This api call returns the first post code as sometimes it returns multiple
	 * post codes for a given suburb and state
	 * 
	 * @param suburb
	 * @param state
	 */

	private Integer getPostCode(String suburb, String state) {

		RestAssured.baseURI = Constants.BASE_URI;
		log.info("Before calling the API to retrieve the first post code ... getPostCode()");
		Response resp = given().header(Constants.AUTH_KEY, Constants.AUTH_KEY_VALUE)
				.queryParam(Constants.SUBURB_KEY, suburb).queryParam(Constants.STATE_KEY, state).when()
				.get(Constants.SUBURB_RESOURCE_URI).then().extract().response();
		log.info("Response : " + resp.asString());
		JsonPath js = Utilities.rawToJson(resp);
		if (null != js && null != js.get("localities.locality.postcode")) {
			if (js.get("localities.locality.postcode") instanceof Integer) {
				return js.get("localities.locality.postcode");
			} else if (js.get("localities.locality.postcode") instanceof List) {
				this.lstPostCode = js.get("localities.locality.postcode");
				if (null != this.lstPostCode && this.lstPostCode.size() > 0) {
					Collections.sort(this.lstPostCode);
					return this.lstPostCode.get(0);
				}
			}
		} else {
			log.info("No response from the api. Please check");
		}

		return null;
	}
}
