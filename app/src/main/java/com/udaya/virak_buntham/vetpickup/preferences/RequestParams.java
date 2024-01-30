package com.udaya.virak_buntham.vetpickup.preferences;

import android.content.Context;

public class RequestParams {
    private static final String REQUEST_PARAMS = "request_params";
    private static final String TOKEN_REQUEST = "token";
    private static final String SIGNATURE_REQUEST = "signature";
    private static final String BRANCH_ID = "brandId";
    private static final String BRANCH_NAME = "branchName";

    public static void persistRequestParams(Context context, String token, String signature){
        context.getSharedPreferences(REQUEST_PARAMS, Context.MODE_PRIVATE).edit()
                .putString(TOKEN_REQUEST, token)
                .putString(SIGNATURE_REQUEST, signature)
                .apply();
    }

    public static String getTokenRequestParams(Context context){
        return context.getSharedPreferences(REQUEST_PARAMS, Context.MODE_PRIVATE)
                .getString(TOKEN_REQUEST, null);
    }

    public static String getSignatureRequestParams(Context context){
        return context.getSharedPreferences(REQUEST_PARAMS, Context.MODE_PRIVATE)
                .getString(SIGNATURE_REQUEST, null);
    }

    public static void persistBranch(Context context, String branchId, String branchName){
        context.getSharedPreferences(REQUEST_PARAMS, Context.MODE_PRIVATE).edit()
                .putString(BRANCH_ID, branchId)
                .putString(BRANCH_NAME, branchName)
                .apply();
    }

    public static String getBranchNameRequestParams(Context context){
        return context.getSharedPreferences(REQUEST_PARAMS, Context.MODE_PRIVATE)
                .getString(BRANCH_NAME, null);
    }
    public static String getBranchIdRequestParams(Context context){
        return context.getSharedPreferences(REQUEST_PARAMS, Context.MODE_PRIVATE)
                .getString(BRANCH_ID, null);
    }


}
