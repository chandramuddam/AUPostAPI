package com.aupost.util;

/**
 * To define Australia post api constants
 * @author Chandra
 *
 */

public class Constants {
	
	public static final String BASE_URI = "https://digitalapi.auspost.com.au";
	public static final String POSTAGE_COST_RESOURCE_URI = "/postage/letter/domestic/calculate.json";
	public static final String DOMESTIC_SERVICE_RESOURCE_URI = "/postage/letter/domestic/service.json";
	public static final String SUBURB_RESOURCE_URI = "/postcode/search.json";
	public static final String AUTH_KEY = "auth-key";
	public static final String AUTH_KEY_VALUE = "63e019f7-d867-4cc6-84ec-8862d890ff38";
	public static final String FROM_POST_CODE = "from_postcode";
	public static final String SUBURB_KEY = "q";
	public static final String TO_POST_CODE = "to_postcode";
	public static final String STATE_KEY = "state";
	public static final String LENGTH = "length";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String THICKNESS = "thickness";
	public static final String WEIGHT = "weight";
	public static final String SERVICE_CODE = "service_code";
	public static final String SERVICE_CODE_VALUE = "AUS_PARCEL_REGULAR";
	public static final String COUNTRY_CODE = "country_code";
	
	public static final String DOMESTIC_LETTER_THICKNESS_RESOURCE_URI = "/postage/letter/domestic/thickness.json";
	public static final String INTERNATIONAL_PARCEL_SERVICES_RESOURCE_URI = "/postage/parcel/international/service.json";
	
}
