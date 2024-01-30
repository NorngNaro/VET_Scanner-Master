package com.udaya.virak_buntham.vetpickup.utils;

import android.Manifest;

public class Constants {
    public static final String REQUEST_AREA_KEY = "request_area_key";
    public static final String RETURN_PROVINCE_KEY = "province";
    public static final String RETURN_DISTRICT_KEY = "district";
    public static final String RETURN_COMMUNE_KEY = "commune";
    public static final int REQUEST_PROVINCE = 1;
    public static final int REQUEST_DISTRICT = 2;
    public static final int REQUEST_COMMUNE = 3;

    //Selection
    public static final String REQUEST_SELECTION_KEY = "request_selection_key";

    public static final String REQUEST_DESTINATION_FROM_KEY = "destination_from";
    public static final String REQUEST_DESTINATION_TO_KEY = "destination_to";
    public static final String REQUEST_CURRENCY_KEY = "currency";
    public static final String REQUEST_ITEM_TYPE_KEY = "item_type";
    public static final String REQUEST_UOM_KEY = "uom";
    public static final String REQUEST_AREA_DELIVER_KEY = "area_deliver";
    public static final String REQUEST_VAN_KEY = "van";
    public static final String REQUEST_BRANCH_KEY = "brand";
    public static final String REQUEST_LOCATION_KEY = "location";
    public static final String REQUEST_DESTINATION_TO_BY_BRANCH_KEY = "destination_to_by_branch";
    public static final String REQUEST_CUSTOMER_KEY = "customer";
    public static final String REQUEST_MEMBER_SHIP_KEY = "memberShip";
    public static final String REQUEST_REASON_KEY = "reason";
    public static final String REQUEST_CHANGE_DESTINATION_KEY = "change_destination";
    public static final int REQUEST_DESTINATION_FROM_CODE = 1;
    public static final int REQUEST_DESTINATION_TO_CODE = 2;
    public static final int REQUEST_CURRENCY_CODE = 3;
    public static final int REQUEST_ITEM_TYPE_CODE = 4;
    public static final int REQUEST_UOM_CODE = 5;
    public static final int REQUEST_AREA_DELIVER_CODE = 6;
    public static final int REQUEST_VAN_CODE = 7;
    public static final int REQUEST_BRANCH_CODE = 8;
    public static final int REQUEST_DESTINATION_BY_BRANCH_CODE = 9;
    public static final int REQUEST_LOCATION_CODE = 10;
    public static final int REQUEST_USER_NAME_CODE = 10;
    public static final int REQUEST_CUSTOMER_CODE = 11;
    public static final int REQUEST_MEMBER_SHIP_CODE = 12;
    public static final int REQUEST_REASON_CODE = 13;
    public static final int REQUEST_CHANGE_DESTINATION_CODE = 14;


    //User login
    public static final String USERNAME = "086821188";
    public static final String PASSWORD = "1";

    //Language PREF
    public static final String LANGUAGE_PREF_KEY = "language";
    public static final String LANGUAGE_PREF_NAME = "language_pref_name";
    public static final String KHMER_LANGUAGE = "km";
    public static final String ENGLISH_LANGUAGE = "en";
    public static final String THAI_LANGUAGE = "km";

    //User session
    public static final String PREF_USER_SESSION = "pref_user_session";
    public static final String USER_SESSION = "user_session";

    //Fix params request
    public static final String FIX_PARAMS_REQUEST = "params_request";
    public static final String TOKEN_REQUEST = "token";
    public static final String SIGNATURE_REQUEST = "signature";

    //Request Status
    public static final String STATUS_SUCCESS = "1";
    public static final String STATUS_NO_RECORD = "2";

    // Check text in button
    public static final String NO_DATA_ADDED = "Please Select";

    //Permission
    public static final String[] PERMISSION_ARRAY = {Manifest.permission.CALL_PHONE
            , Manifest.permission.ACCESS_FINE_LOCATION
            , Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int REQUEST_PERMISSION_CODE = 105;

    //Membership activity for result
    public static final int MEMBERSHIP_REQUEST_CODE = 401;
    public static final String MEMBERSHIP_KEY = "membership_key";

    //Symbol for set in goods transfer
    public static final String DEFAULT_SYMBOL = "$";
    public static final String DEFAULT_SYMBOL_ID = "1";

    public static final String DEFAULT_VALUE = "0";


}
