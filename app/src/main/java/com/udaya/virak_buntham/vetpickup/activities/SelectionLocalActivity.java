package com.udaya.virak_buntham.vetpickup.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.adapters.SelectionAdapter;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.selectionlist.DestinationFromResponse;
import com.udaya.virak_buntham.vetpickup.models.selectionlist.SelectionData;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.AlertDialogUtil;
import com.udaya.virak_buntham.vetpickup.utils.Constants;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectionLocalActivity extends AppCompatActivity implements OnItemClickListener, OnInternetConnectionListener {

    @BindView(R.id.rv_selection_container)
    RecyclerView rvSelectionContainer;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.place_holder_no_record)
    TextView txtPlaceholderNoRecord;
    @BindView(R.id.search_btn_search)
    ImageButton searchItem;

    private List<SelectionData> selectionList;

    private int requestSelectionCode;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        ButterKnife.bind(this);

        RegisterActionBar.registerSupportToolbar(this, mToolbar);

        progressDialog = new ProgressDialog(this);

        if (getIntent().getExtras() != null) {
            requestSelectionCode = getIntent().getExtras().getInt(Constants.REQUEST_SELECTION_KEY);
            String destinationFromId = getIntent().getExtras().getString("destination_from_id");
            if (requestSelectionCode != 0) {
                if (requestSelectionCode == Constants.REQUEST_DESTINATION_FROM_CODE)
                    mToolbarTitle.setHint(R.string.destination_from);
                if (requestSelectionCode == Constants.REQUEST_DESTINATION_TO_CODE)
                    mToolbarTitle.setHint(R.string.destination_to);
                if (requestSelectionCode == Constants.REQUEST_CURRENCY_CODE)
                    mToolbarTitle.setHint(R.string.select_currency);
                if (requestSelectionCode == Constants.REQUEST_ITEM_TYPE_CODE)
                    mToolbarTitle.setHint(R.string.item_type);
                if (requestSelectionCode == Constants.REQUEST_UOM_CODE)
                    mToolbarTitle.setHint(R.string.uom);
                if (requestSelectionCode == Constants.REQUEST_AREA_DELIVER_CODE)
                    mToolbarTitle.setHint(R.string.delivery_area);
                if (requestSelectionCode == Constants.REQUEST_CUSTOMER_CODE)
                    mToolbarTitle.setHint("Customer");
                if (requestSelectionCode == Constants.REQUEST_MEMBER_SHIP_CODE)
                    mToolbarTitle.setHint("Member Ship");
            }
        }

        if (requestSelectionCode == Constants.REQUEST_DESTINATION_FROM_CODE) {
            requestDestinationFrom(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this), ""
            );
            searchItem.setOnClickListener(view -> requestDestinationFrom(DeviceID.getDeviceId(SelectionLocalActivity.this)
                    , RequestParams.getTokenRequestParams(SelectionLocalActivity.this)
                    , RequestParams.getSignatureRequestParams(SelectionLocalActivity.this)
                    , UserSession.getUserSession(SelectionLocalActivity.this), "" + mToolbarTitle.getText().toString().trim()
            ));
        } else if
        (requestSelectionCode == Constants.REQUEST_DESTINATION_TO_CODE) {
            requestSelection(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this)
                    , Constants.REQUEST_AREA_DELIVER_CODE, ""
            );
            searchItem.setOnClickListener(view -> requestSelection(DeviceID.getDeviceId(SelectionLocalActivity.this)
                    , RequestParams.getTokenRequestParams(SelectionLocalActivity.this)
                    , RequestParams.getSignatureRequestParams(SelectionLocalActivity.this)
                    , UserSession.getUserSession(SelectionLocalActivity.this)
                    , Constants.REQUEST_AREA_DELIVER_CODE, "" + mToolbarTitle.getText().toString().trim()
            ));
        } else if (requestSelectionCode == Constants.REQUEST_CURRENCY_CODE) {
            requestSelection(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this)
                    , Constants.REQUEST_CURRENCY_CODE, ""
            );
        } else if (requestSelectionCode == Constants.REQUEST_ITEM_TYPE_CODE) {
            requestSelection(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this)
                    , Constants.REQUEST_ITEM_TYPE_CODE, ""
            );
            searchItem.setOnClickListener(view -> requestSelection(DeviceID.getDeviceId(SelectionLocalActivity.this)
                    , RequestParams.getTokenRequestParams(SelectionLocalActivity.this)
                    , RequestParams.getSignatureRequestParams(SelectionLocalActivity.this)
                    , UserSession.getUserSession(SelectionLocalActivity.this)
                    , Constants.REQUEST_ITEM_TYPE_CODE, "" + mToolbarTitle.getText().toString().trim()
            ));
        } else if (requestSelectionCode == Constants.REQUEST_UOM_CODE) {
            requestSelection(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this)
                    , Constants.REQUEST_UOM_CODE, ""
            );
            searchItem.setOnClickListener(view -> requestSelection(DeviceID.getDeviceId(SelectionLocalActivity.this)
                    , RequestParams.getTokenRequestParams(SelectionLocalActivity.this)
                    , RequestParams.getSignatureRequestParams(SelectionLocalActivity.this)
                    , UserSession.getUserSession(SelectionLocalActivity.this)
                    , Constants.REQUEST_UOM_CODE, "" + mToolbarTitle.getText().toString().trim()
            ));
        } else if (requestSelectionCode == Constants.REQUEST_AREA_DELIVER_CODE) {
            requestSelection(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this)
                    , Constants.REQUEST_AREA_DELIVER_CODE, ""
            );
            searchItem.setOnClickListener(view -> requestSelection(DeviceID.getDeviceId(SelectionLocalActivity.this)
                    , RequestParams.getTokenRequestParams(SelectionLocalActivity.this)
                    , RequestParams.getSignatureRequestParams(SelectionLocalActivity.this)
                    , UserSession.getUserSession(SelectionLocalActivity.this)
                    , Constants.REQUEST_AREA_DELIVER_CODE, "" + mToolbarTitle.getText().toString().trim()
            ));
        } else if (requestSelectionCode == Constants.REQUEST_CUSTOMER_CODE) {
            requestCustomer(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this), ""
            );
            searchItem.setOnClickListener(view -> requestCustomer(DeviceID.getDeviceId(SelectionLocalActivity.this)
                    , RequestParams.getTokenRequestParams(SelectionLocalActivity.this)
                    , RequestParams.getSignatureRequestParams(SelectionLocalActivity.this)
                    , UserSession.getUserSession(SelectionLocalActivity.this), "" + mToolbarTitle.getText().toString().trim()
            ));
        } else if (requestSelectionCode == Constants.REQUEST_MEMBER_SHIP_CODE) {
            requestMember(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this), ""
            );
            searchItem.setOnClickListener(view -> requestMember(DeviceID.getDeviceId(SelectionLocalActivity.this)
                    , RequestParams.getTokenRequestParams(SelectionLocalActivity.this)
                    , RequestParams.getSignatureRequestParams(SelectionLocalActivity.this)
                    , UserSession.getUserSession(SelectionLocalActivity.this), "" + mToolbarTitle.getText().toString().trim()
            ));
        }
    }

    private void setupAreaAdapter(List<SelectionData> selectionList) {
        SelectionAdapter selectionAdapter = new SelectionAdapter(selectionList);
        selectionAdapter.setOnItemClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvSelectionContainer.setLayoutManager(layoutManager);
//        rvSelectionContainer.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
//        rvSelectionContainer.setItemAnimator(new DefaultItemAnimator());
        rvSelectionContainer.setAdapter(selectionAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClick(int position) {

        if (requestSelectionCode == Constants.REQUEST_DESTINATION_FROM_CODE) {
            returnIntent(Constants.REQUEST_DESTINATION_FROM_KEY, selectionList.get(position));
        }

        if (requestSelectionCode == Constants.REQUEST_DESTINATION_TO_CODE)
            returnIntent(Constants.REQUEST_DESTINATION_TO_KEY, selectionList.get(position));

        if (requestSelectionCode == Constants.REQUEST_CURRENCY_CODE)
            returnIntent(Constants.REQUEST_CURRENCY_KEY, selectionList.get(position));

        if (requestSelectionCode == Constants.REQUEST_ITEM_TYPE_CODE)
            returnIntent(Constants.REQUEST_ITEM_TYPE_KEY, selectionList.get(position));

        if (requestSelectionCode == Constants.REQUEST_UOM_CODE)
            returnIntent(Constants.REQUEST_UOM_KEY, selectionList.get(position));

        if (requestSelectionCode == Constants.REQUEST_AREA_DELIVER_CODE)
            returnIntent(Constants.REQUEST_AREA_DELIVER_KEY, selectionList.get(position));

        if (requestSelectionCode == Constants.REQUEST_CUSTOMER_CODE)
            returnIntent(Constants.REQUEST_CUSTOMER_KEY, selectionList.get(position));

        if (requestSelectionCode == Constants.REQUEST_MEMBER_SHIP_CODE)
            returnIntent(Constants.REQUEST_MEMBER_SHIP_KEY, selectionList.get(position));

        closeKeyboard();
    }

    private void returnIntent(String key, SelectionData result) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(key, result);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
        closeKeyboard();
    }

    private void requestSelection(String device, String token, String signature, String session,
                                  int requestSelectionStatus, String itemSearch) {
        closeKeyboard();
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.show();

        Call<DestinationFromResponse> call = getApiService(device, token, signature, session, requestSelectionStatus, itemSearch);
        call.enqueue(new Callback<DestinationFromResponse>() {
            @Override
            public void onResponse(@NonNull Call<DestinationFromResponse> call, @NonNull Response<DestinationFromResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (Objects.requireNonNull(response.body()).getStatus().equals(Constants.STATUS_SUCCESS)) {
                            if (response.body().getData().size() == 0) {
                                //Replace request params
                                RequestParams.persistRequestParams(SelectionLocalActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                                txtPlaceholderNoRecord.setVisibility(View.VISIBLE);
                            } else {
                                selectionList = Objects.requireNonNull(response.body()).getData();
                                //Replace request params
                                RequestParams.persistRequestParams(SelectionLocalActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                                setupAreaAdapter(selectionList);
                            }
                        } else {
//                            AlertDialogUtil.alertMessageError(SelectionLocalActivity.this, response.body().getInfo());
                            RequestParams.persistRequestParams(SelectionLocalActivity.this
                                    , Objects.requireNonNull(response.body()).getToken()
                                    , Objects.requireNonNull(response.body()).getSignature());
                            Toast.makeText(SelectionLocalActivity.this, "token null", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@NonNull Call<DestinationFromResponse> call, @NonNull Throwable t) {
                Log.i("requestInfo", Objects.requireNonNull(t.getMessage()));
                progressDialog.hide();
            }
        });
    }

    private void requestDestinationFrom(String device, String token, String signature, String
            session, String searchText) {
        closeKeyboard();
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.show();

        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<DestinationFromResponse> call = apiService.getDestinationFrom(device, token, signature, session, searchText);
        call.enqueue(new Callback<DestinationFromResponse>() {
            @Override
            public void onResponse(@NonNull Call<DestinationFromResponse> call, @NonNull Response<DestinationFromResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (Objects.requireNonNull(response.body()).getStatus().equals(Constants.STATUS_SUCCESS)) {
                            if (response.body().getData().size() == 0) {
                                //Replace request params
                                RequestParams.persistRequestParams(SelectionLocalActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                                txtPlaceholderNoRecord.setVisibility(View.VISIBLE);
                            } else {
                                selectionList = Objects.requireNonNull(response.body()).getData();

                                //Replace request params
                                RequestParams.persistRequestParams(SelectionLocalActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());

                                setupAreaAdapter(selectionList);
                            }
                        } else {
                            AlertDialogUtil.alertMessageError(SelectionLocalActivity.this, response.body().getInfo());
                        }
                    }
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@NonNull Call<DestinationFromResponse> call, @NonNull Throwable t) {
                Log.i("requestInfo", Objects.requireNonNull(t.getMessage()));
                progressDialog.hide();
            }
        });
    }

    private void requestDestinationTo(String device, String token, String signature, String session, String destinationFromId, String searchText) {
        closeKeyboard();
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<DestinationFromResponse> call = apiService.getDestinationTo(device, token, signature, session, destinationFromId, searchText);
        call.enqueue(new Callback<DestinationFromResponse>() {
            @Override
            public void onResponse(@NonNull Call<DestinationFromResponse> call, @NonNull Response<DestinationFromResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (Objects.requireNonNull(response.body()).getStatus().equals(Constants.STATUS_SUCCESS)) {
                            if (response.body().getData().size() == 0) {
                                //Replace request params
                                RequestParams.persistRequestParams(SelectionLocalActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                                txtPlaceholderNoRecord.setVisibility(View.VISIBLE);
                            } else {
                                selectionList = Objects.requireNonNull(response.body()).getData();

                                //Replace request params
                                RequestParams.persistRequestParams(SelectionLocalActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());

                                setupAreaAdapter(selectionList);
                            }
                        } else {
                            AlertDialogUtil.alertMessageError(SelectionLocalActivity.this, response.body().getInfo());
                        }
                    }
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@NonNull Call<DestinationFromResponse> call, @NonNull Throwable t) {
                Log.i("requestInfo", Objects.requireNonNull(t.getMessage()));
                progressDialog.hide();
            }
        });
    }

    private Call<DestinationFromResponse> getApiService(String device, String token, String
            signature, String session, int requestSelectionStatus, String itemSearch) {
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        if (requestSelectionStatus == Constants.REQUEST_CURRENCY_CODE) {
            return apiService.getCurrency(device, token, signature, session);

        } else if (requestSelectionStatus == Constants.REQUEST_ITEM_TYPE_CODE) {
            return apiService.getGoodsType(device, token, signature, session, itemSearch);

        } else if (requestSelectionStatus == Constants.REQUEST_UOM_CODE) {
            return apiService.getUOM(device, token, signature, session, itemSearch);

        } else {
            return apiService.getDeliveryArea(device, token, signature, session, itemSearch);
        }
    }

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {
        AlertDialogUtil.alertMessageInternetConnection(this);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void requestCustomer(String device, String token, String signature, String
            session, String searchText) {
        closeKeyboard();
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        } else {
            progressDialog.show();
        }

        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<DestinationFromResponse> call = apiService.getCustomer(device, token, signature, session, searchText);
        call.enqueue(new Callback<DestinationFromResponse>() {
            @Override
            public void onResponse(@NonNull Call<DestinationFromResponse> call, @NonNull Response<DestinationFromResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (Objects.requireNonNull(response.body()).getStatus().equals(Constants.STATUS_SUCCESS)) {
                            if (response.body().getData().size() == 0) {
                                //Replace request params
                                RequestParams.persistRequestParams(SelectionLocalActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                                txtPlaceholderNoRecord.setVisibility(View.VISIBLE);
                            } else {
                                selectionList = Objects.requireNonNull(response.body()).getData();

                                //Replace request params
                                RequestParams.persistRequestParams(SelectionLocalActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());

                                setupAreaAdapter(selectionList);
                            }
                        } else {
                            AlertDialogUtil.alertMessageError(SelectionLocalActivity.this, response.body().getInfo());
                        }
                    }
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@NonNull Call<DestinationFromResponse> call, @NonNull Throwable t) {
                Log.i("requestInfo", Objects.requireNonNull(t.getMessage()));
                progressDialog.hide();
            }
        });
    }

    private void requestMember(String device, String token, String signature, String
            session, String searchText) {
        closeKeyboard();
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        } else {
            progressDialog.show();
        }

        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<DestinationFromResponse> call = apiService.getmembership(device, token, signature, session, searchText);
        call.enqueue(new Callback<DestinationFromResponse>() {
            @Override
            public void onResponse(@NonNull Call<DestinationFromResponse> call, @NonNull Response<DestinationFromResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (Objects.requireNonNull(response.body()).getStatus().equals(Constants.STATUS_SUCCESS)) {
                            if (response.body().getData().size() == 0) {
                                //Replace request params
                                RequestParams.persistRequestParams(SelectionLocalActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                                txtPlaceholderNoRecord.setVisibility(View.VISIBLE);
                            } else {
                                selectionList = Objects.requireNonNull(response.body()).getData();

                                //Replace request params
                                RequestParams.persistRequestParams(SelectionLocalActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());

                                setupAreaAdapter(selectionList);
                            }
                        } else {
                            AlertDialogUtil.alertMessageError(SelectionLocalActivity.this, response.body().getInfo());
                        }
                    }
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@NonNull Call<DestinationFromResponse> call, @NonNull Throwable t) {
                Log.i("requestInfo", Objects.requireNonNull(t.getMessage()));
                progressDialog.hide();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }
}
