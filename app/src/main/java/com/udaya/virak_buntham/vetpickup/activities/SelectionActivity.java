package com.udaya.virak_buntham.vetpickup.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.callToCustomer.CallToCustomer;
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

public class SelectionActivity extends AppCompatActivity implements OnItemClickListener, OnInternetConnectionListener {

    @BindView(R.id.rv_selection_container)
    RecyclerView rvSelectionContainer;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    EditText mToolbarTitle;
    @BindView(R.id.place_holder_no_record)
    TextView txtPlaceholderNoRecord;

    private List<SelectionData> selectionList;

    private int requestSelectionCode;
    private String destinationFromId;
    private String branchId;
    private String branchIdLocation;
    private String branchIdLocate;
    @BindView(R.id.search_btn_search)
    ImageButton searchItem;
    private ProgressDialog progressDialog;

    //changeDestinationTo
    private  int destinationToId;
    SelectionAdapter selectionAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        ButterKnife.bind(this);

        RegisterActionBar.registerSupportToolbar(this, mToolbar);

        progressDialog = new ProgressDialog(this);

        if (getIntent().getExtras() != null) {
            requestSelectionCode = getIntent().getExtras().getInt(Constants.REQUEST_SELECTION_KEY);
            destinationFromId = getIntent().getExtras().getString("destination_from_id");
            branchId = getIntent().getExtras().getString("destination_to_by_branch_id");
            branchIdLocation = getIntent().getExtras().getString("branch_id_location");
            branchIdLocate = getIntent().getExtras().getString("branchIdLocate");
            destinationToId = getIntent().getExtras().getInt("destination_id",0);
            if (requestSelectionCode != 0) {
                if (requestSelectionCode == Constants.REQUEST_DESTINATION_FROM_CODE)
                    mToolbarTitle.setHint(R.string.destination_from);
                else if (requestSelectionCode == Constants.REQUEST_DESTINATION_TO_CODE)
                    mToolbarTitle.setHint(R.string.destination_to);
                else if (requestSelectionCode == Constants.REQUEST_CURRENCY_CODE)
                    mToolbarTitle.setHint(R.string.select_currency);
                else if (requestSelectionCode == Constants.REQUEST_ITEM_TYPE_CODE)
                    mToolbarTitle.setHint(R.string.item_type);
                else if (requestSelectionCode == Constants.REQUEST_UOM_CODE)
                    mToolbarTitle.setHint(R.string.uom);
                else if (requestSelectionCode == Constants.REQUEST_AREA_DELIVER_CODE)
                    mToolbarTitle.setHint(R.string.delivery_area);
                else if (requestSelectionCode == Constants.REQUEST_VAN_CODE)
                    mToolbarTitle.setHint(R.string.van);
                else if (requestSelectionCode == Constants.REQUEST_BRANCH_CODE)
                    mToolbarTitle.setHint(R.string.branch);
                else if (requestSelectionCode == Constants.REQUEST_DESTINATION_BY_BRANCH_CODE)
                    mToolbarTitle.setHint(R.string.destination_to);
                else if (requestSelectionCode == Constants.REQUEST_LOCATION_CODE)
                    mToolbarTitle.setHint("Location");
                else if (requestSelectionCode == Constants.REQUEST_REASON_CODE)
                    mToolbarTitle.setHint("reason");
                else if (requestSelectionCode == Constants.REQUEST_CHANGE_DESTINATION_CODE)
                    mToolbarTitle.setHint("Destination To");

            }
        }

        if (requestSelectionCode == Constants.REQUEST_DESTINATION_FROM_CODE) {
            requestDestinationFrom(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this), ""
            );
            searchItem.setOnClickListener(view -> requestDestinationFrom(DeviceID.getDeviceId(SelectionActivity.this)
                    , RequestParams.getTokenRequestParams(SelectionActivity.this)
                    , RequestParams.getSignatureRequestParams(SelectionActivity.this)
                    , UserSession.getUserSession(SelectionActivity.this), "" + mToolbarTitle.getText().toString().trim()
            ));
        } else if (requestSelectionCode == Constants.REQUEST_DESTINATION_TO_CODE) {
            requestDestinationTo(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this)
                    , destinationFromId, ""
            );
            searchItem.setOnClickListener(view -> requestDestinationTo(DeviceID.getDeviceId(SelectionActivity.this)
                    , RequestParams.getTokenRequestParams(SelectionActivity.this)
                    , RequestParams.getSignatureRequestParams(SelectionActivity.this)
                    , UserSession.getUserSession(SelectionActivity.this)
                    , destinationFromId, "" + mToolbarTitle.getText().toString().trim()
            ));

            mToolbarTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    selectionAdapter.getFilter().filter(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else if (requestSelectionCode == Constants.REQUEST_CURRENCY_CODE) {
            requestSelection(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this)
                    , Constants.REQUEST_CURRENCY_CODE, ""
            );
            searchItem.setOnClickListener(view -> requestSelection(DeviceID.getDeviceId(SelectionActivity.this)
                    , RequestParams.getTokenRequestParams(SelectionActivity.this)
                    , RequestParams.getSignatureRequestParams(SelectionActivity.this)
                    , UserSession.getUserSession(SelectionActivity.this)
                    , Constants.REQUEST_CURRENCY_CODE, "" + mToolbarTitle.getText().toString().trim()
            ));
            mToolbarTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    selectionAdapter.getFilter().filter(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else if (requestSelectionCode == Constants.REQUEST_ITEM_TYPE_CODE) {
            requestSelection(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this)
                    , Constants.REQUEST_ITEM_TYPE_CODE, ""
            );
            searchItem.setOnClickListener(view -> requestSelection(DeviceID.getDeviceId(SelectionActivity.this)
                    , RequestParams.getTokenRequestParams(SelectionActivity.this)
                    , RequestParams.getSignatureRequestParams(SelectionActivity.this)
                    , UserSession.getUserSession(SelectionActivity.this)
                    , Constants.REQUEST_ITEM_TYPE_CODE, "" + mToolbarTitle.getText().toString().trim()
            ));
            mToolbarTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    selectionAdapter.getFilter().filter(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else if (requestSelectionCode == Constants.REQUEST_UOM_CODE) {
            requestSelection(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this)
                    , Constants.REQUEST_UOM_CODE, ""
            );
            searchItem.setOnClickListener(view -> requestSelection(DeviceID.getDeviceId(SelectionActivity.this)
                    , RequestParams.getTokenRequestParams(SelectionActivity.this)
                    , RequestParams.getSignatureRequestParams(SelectionActivity.this)
                    , UserSession.getUserSession(SelectionActivity.this)
                    , Constants.REQUEST_UOM_CODE, "" + mToolbarTitle.getText().toString().trim()
            ));
            mToolbarTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    selectionAdapter.getFilter().filter(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else if (requestSelectionCode == Constants.REQUEST_AREA_DELIVER_CODE) {
            requestSelection(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this)
                    , Constants.REQUEST_AREA_DELIVER_CODE, ""
            );
            searchItem.setOnClickListener(view -> {
                requestSelection(DeviceID.getDeviceId(this)
                        , RequestParams.getTokenRequestParams(this)
                        , RequestParams.getSignatureRequestParams(this)
                        , UserSession.getUserSession(this)
                        , Constants.REQUEST_AREA_DELIVER_CODE, "" + mToolbarTitle.getText().toString().trim()
                );
            });
            mToolbarTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    selectionAdapter.getFilter().filter(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else if (requestSelectionCode == Constants.REQUEST_VAN_CODE) {
            requestVan(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this), ""
            );
            searchItem.setOnClickListener(view -> requestVan(DeviceID.getDeviceId(SelectionActivity.this)
                    , RequestParams.getTokenRequestParams(SelectionActivity.this)
                    , RequestParams.getSignatureRequestParams(SelectionActivity.this)
                    , UserSession.getUserSession(SelectionActivity.this), "" + mToolbarTitle.getText().toString().trim()
            ));
            mToolbarTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    selectionAdapter.getFilter().filter(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else if (requestSelectionCode == Constants.REQUEST_BRANCH_CODE) {
            requestBranch(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this), ""
            );
            searchItem.setOnClickListener(view -> requestBranch(DeviceID.getDeviceId(SelectionActivity.this)
                    , RequestParams.getTokenRequestParams(SelectionActivity.this)
                    , RequestParams.getSignatureRequestParams(SelectionActivity.this)
                    , UserSession.getUserSession(SelectionActivity.this), "" + mToolbarTitle.getText().toString().trim()
            ));
            mToolbarTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    selectionAdapter.getFilter().filter(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else if (requestSelectionCode == Constants.REQUEST_DESTINATION_BY_BRANCH_CODE) {
            requestDestinationToByBranch(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this)
                    , branchId, ""
            );
            searchItem.setOnClickListener(view -> requestDestinationToByBranch(DeviceID.getDeviceId(SelectionActivity.this)
                    , RequestParams.getTokenRequestParams(SelectionActivity.this)
                    , RequestParams.getSignatureRequestParams(SelectionActivity.this)
                    , UserSession.getUserSession(SelectionActivity.this)
                    , branchId, "" + mToolbarTitle.getText().toString().trim()
            ));
            mToolbarTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    selectionAdapter.getFilter().filter(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else if (requestSelectionCode == Constants.REQUEST_LOCATION_CODE) {
            requestLocation(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this), ""
            );
            searchItem.setOnClickListener(view -> requestLocation(DeviceID.getDeviceId(SelectionActivity.this)
                    , RequestParams.getTokenRequestParams(SelectionActivity.this)
                    , RequestParams.getSignatureRequestParams(SelectionActivity.this)
                    , UserSession.getUserSession(SelectionActivity.this), "" + mToolbarTitle.getText().toString().trim()
            ));
        } else if (requestSelectionCode == Constants.REQUEST_REASON_CODE) {
            requestReason(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this), ""
            );
            searchItem.setOnClickListener(view -> requestReason(DeviceID.getDeviceId(SelectionActivity.this)
                    , RequestParams.getTokenRequestParams(SelectionActivity.this)
                    , RequestParams.getSignatureRequestParams(SelectionActivity.this)
                    , UserSession.getUserSession(SelectionActivity.this), "" + mToolbarTitle.getText().toString().trim()
            ));

            mToolbarTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    selectionAdapter.getFilter().filter(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        else if (requestSelectionCode == Constants.REQUEST_CHANGE_DESTINATION_CODE) {
            requestChangeDestinationTo(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this),""
            );
            searchItem.setOnClickListener(view ->
                    requestChangeDestinationTo(DeviceID.getDeviceId(this)
                            , RequestParams.getTokenRequestParams(this)
                            , RequestParams.getSignatureRequestParams(this)
                            , UserSession.getUserSession(this),"" + mToolbarTitle.getText().toString().trim()
            ));
            mToolbarTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    selectionAdapter.getFilter().filter(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void setupAreaAdapter(List<SelectionData> selectionList) {
        selectionAdapter = new SelectionAdapter(selectionList);
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
            new MoveItemToVanActivity().destinationChoose = 0;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClick(int position) {
        progressDialog.dismiss();
        if (requestSelectionCode == Constants.REQUEST_DESTINATION_FROM_CODE) {
            returnIntent(Constants.REQUEST_DESTINATION_FROM_KEY, selectionList.get(position));
        } else if (requestSelectionCode == Constants.REQUEST_DESTINATION_TO_CODE)
            returnIntent(Constants.REQUEST_DESTINATION_TO_KEY, selectionList.get(position));
        else if (requestSelectionCode == Constants.REQUEST_CURRENCY_CODE) {
            returnIntent(Constants.REQUEST_CURRENCY_KEY, selectionList.get(position));
        } else if (requestSelectionCode == Constants.REQUEST_ITEM_TYPE_CODE) {
            returnIntent(Constants.REQUEST_ITEM_TYPE_KEY, selectionList.get(position));
        } else if (requestSelectionCode == Constants.REQUEST_UOM_CODE) {
            returnIntent(Constants.REQUEST_UOM_KEY, selectionList.get(position));
        } else if (requestSelectionCode == Constants.REQUEST_AREA_DELIVER_CODE) {
            returnIntent(Constants.REQUEST_AREA_DELIVER_KEY, selectionList.get(position));
        } else if (requestSelectionCode == Constants.REQUEST_VAN_CODE) {
            returnIntent(Constants.REQUEST_VAN_KEY, selectionList.get(position));
        } else if (requestSelectionCode == Constants.REQUEST_BRANCH_CODE) {
            CallToCustomer.checkResume = 1;
            returnIntent(Constants.REQUEST_BRANCH_KEY, selectionList.get(position));
        } else if (requestSelectionCode == Constants.REQUEST_DESTINATION_BY_BRANCH_CODE) {
            returnIntent(Constants.REQUEST_DESTINATION_TO_BY_BRANCH_KEY, selectionList.get(position));
        } else if (requestSelectionCode == Constants.REQUEST_LOCATION_CODE) {
            returnIntent(Constants.REQUEST_LOCATION_KEY, selectionList.get(position));
        } else if (requestSelectionCode == Constants.REQUEST_REASON_CODE) {
            returnIntent(Constants.REQUEST_REASON_KEY, selectionList.get(position));
        } else if (requestSelectionCode == Constants.REQUEST_CHANGE_DESTINATION_CODE) {
            returnIntent(Constants.REQUEST_CHANGE_DESTINATION_KEY, selectionList.get(position));
        }
        new MoveItemToVanActivity().destinationChoose = 0;
        closeKeyboard();
    }

    private void returnIntent(String key, SelectionData result) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(key, result);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void requestSelection(String device, String token, String signature, String session, int requestSelectionStatus, String itemSearch) {
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
                                RequestParams.persistRequestParams(SelectionActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                                txtPlaceholderNoRecord.setVisibility(View.VISIBLE);
                            } else {
                                selectionList = Objects.requireNonNull(response.body()).getData();
                                //Replace request params
                                RequestParams.persistRequestParams(SelectionActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());

                                setupAreaAdapter(selectionList);
                            }
                        } else {
                            AlertDialogUtil.alertMessageError(SelectionActivity.this, response.body().getInfo());
                        }
                    }
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@NonNull Call<DestinationFromResponse> call, @NonNull Throwable t) {
                Log.i("requestInfo", Objects.requireNonNull(Objects.requireNonNull(t.getMessage())));
                progressDialog.hide();
            }
        });
    }

    private void requestDestinationFrom(String device, String token, String signature, String session, String searchText) {
        closeKeyboard();
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.show();
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
                                RequestParams.persistRequestParams(SelectionActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                                txtPlaceholderNoRecord.setVisibility(View.VISIBLE);
                            } else {
                                selectionList = Objects.requireNonNull(response.body()).getData();

                                //Replace request params
                                RequestParams.persistRequestParams(SelectionActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());

                                setupAreaAdapter(selectionList);
                            }
                        } else {
                            AlertDialogUtil.alertMessageError(SelectionActivity.this, response.body().getInfo());
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
        progressDialog.setMessage(getResources().getString(R.string.loadings));
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
                                RequestParams.persistRequestParams(SelectionActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                                txtPlaceholderNoRecord.setVisibility(View.VISIBLE);
                            } else {
                                selectionList = Objects.requireNonNull(response.body()).getData();

                                //Replace request params
                                RequestParams.persistRequestParams(SelectionActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());

                                setupAreaAdapter(selectionList);
                            }
                        } else {
                            AlertDialogUtil.alertMessageError(SelectionActivity.this, response.body().getInfo());
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

    private Call<DestinationFromResponse> getApiService(String device, String token, String signature, String session, int requestSelectionStatus, String itemSearch) {
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

    private void requestVan(String device, String token, String signature, String session, String searchText) {
        closeKeyboard();
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<DestinationFromResponse> call = apiService.getVan(device, token, signature, session, searchText);
        call.enqueue(new Callback<DestinationFromResponse>() {
            @Override
            public void onResponse(@NonNull Call<DestinationFromResponse> call, @NonNull Response<DestinationFromResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (Objects.requireNonNull(response.body()).getStatus().equals(Constants.STATUS_SUCCESS)) {
                            if (response.body().getData().size() == 0) {
                                //Replace request params
                                RequestParams.persistRequestParams(SelectionActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                                txtPlaceholderNoRecord.setVisibility(View.VISIBLE);
                            } else {
                                selectionList = Objects.requireNonNull(response.body()).getData();

                                //Replace request params
                                RequestParams.persistRequestParams(SelectionActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());

                                setupAreaAdapter(selectionList);
                            }
                        } else {
                            AlertDialogUtil.alertMessageError(SelectionActivity.this, response.body().getInfo());
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

    private void requestBranch(String device, String token, String signature, String session, String searchText) {
        closeKeyboard();
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        try {
            progressDialog.show();
        } catch (Exception e) {

        }
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<DestinationFromResponse> call = apiService.getBrand(device, token, signature, session, searchText);
        call.enqueue(new Callback<DestinationFromResponse>() {
            @Override
            public void onResponse(@NonNull Call<DestinationFromResponse> call, @NonNull Response<DestinationFromResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (Objects.requireNonNull(response.body()).getStatus().equals(Constants.STATUS_SUCCESS)) {
                            if (response.body().getData().size() == 0) {
                                //Replace request params
                                RequestParams.persistRequestParams(SelectionActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                                txtPlaceholderNoRecord.setVisibility(View.VISIBLE);
                            } else {
                                selectionList = Objects.requireNonNull(response.body()).getData();

                                //Replace request params
                                RequestParams.persistRequestParams(SelectionActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());

                                setupAreaAdapter(selectionList);
                            }
                        } else {
                            AlertDialogUtil.alertMessageError(SelectionActivity.this, response.body().getInfo());
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


    private void requestDestinationToByBranch(String device, String token, String signature, String session, String brandId, String searchText) {
        closeKeyboard();
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<DestinationFromResponse> call = apiService.getDestinationToByBranch(device, token, signature, session, brandId, searchText);
        call.enqueue(new Callback<DestinationFromResponse>() {
            @Override
            public void onResponse(@NonNull Call<DestinationFromResponse> call, @NonNull Response<DestinationFromResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (Objects.requireNonNull(response.body()).getStatus().equals(Constants.STATUS_SUCCESS)) {
                            if (response.body().getData().size() == 0) {
                                //Replace request params
                                RequestParams.persistRequestParams(SelectionActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                                txtPlaceholderNoRecord.setVisibility(View.VISIBLE);
                            } else {
                                selectionList = Objects.requireNonNull(response.body()).getData();

                                //Replace request params
                                RequestParams.persistRequestParams(SelectionActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());

                                setupAreaAdapter(selectionList);
                            }
                        } else {
                            AlertDialogUtil.alertMessageError(SelectionActivity.this, response.body().getInfo());
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

    private void requestLocation(String device, String token, String signature, String session, String searchText) {
        closeKeyboard();
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<DestinationFromResponse> call = apiService.getLocation(device, token, signature, session,branchIdLocate, searchText);
        call.enqueue(new Callback<DestinationFromResponse>() {
            @Override
            public void onResponse(@NonNull Call<DestinationFromResponse> call, @NonNull Response<DestinationFromResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (Objects.requireNonNull(response.body()).getStatus().equals(Constants.STATUS_SUCCESS)) {
                            if (response.body().getData().size() == 0) {
                                //Replace request params
                                RequestParams.persistRequestParams(SelectionActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                                txtPlaceholderNoRecord.setVisibility(View.VISIBLE);
                            } else {
                                selectionList = Objects.requireNonNull(response.body()).getData();

                                //Replace request params
                                RequestParams.persistRequestParams(SelectionActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                                setupAreaAdapter(selectionList);
                            }
                        } else {
                            AlertDialogUtil.alertMessageError(SelectionActivity.this, response.body().getInfo());
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

    private void requestReason(String device, String token, String signature, String session, String searchText) {
        closeKeyboard();
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<DestinationFromResponse> call = apiService.getreason(device, token, signature, session, searchText);
        call.enqueue(new Callback<DestinationFromResponse>() {
            @Override
            public void onResponse(@NonNull Call<DestinationFromResponse> call, @NonNull Response<DestinationFromResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (Objects.requireNonNull(response.body()).getStatus().equals(Constants.STATUS_SUCCESS)) {
                            if (response.body().getData().size() == 0) {
                                //Replace request params
                                RequestParams.persistRequestParams(SelectionActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                                txtPlaceholderNoRecord.setVisibility(View.VISIBLE);
                            } else {
                                selectionList = Objects.requireNonNull(response.body()).getData();

                                //Replace request params
                                RequestParams.persistRequestParams(SelectionActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                                setupAreaAdapter(selectionList);
                            }
                        } else {
                            AlertDialogUtil.alertMessageError(SelectionActivity.this, response.body().getInfo());
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
    private void requestChangeDestinationTo(String device, String token, String signature, String session,String search) {
        closeKeyboard();
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<DestinationFromResponse> call = apiService.getchangedestinationto(device, token, signature, session,branchIdLocation,search);
        call.enqueue(new Callback<DestinationFromResponse>() {
            @Override
            public void onResponse(@NonNull Call<DestinationFromResponse> call, @NonNull Response<DestinationFromResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (Objects.requireNonNull(response.body()).getStatus().equals(Constants.STATUS_SUCCESS)) {
                            if (response.body().getData().size() == 0) {
                                //Replace request params
                                RequestParams.persistRequestParams(SelectionActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                                txtPlaceholderNoRecord.setVisibility(View.VISIBLE);
                            } else {
                                selectionList = Objects.requireNonNull(response.body()).getData();

                                //Replace request params
                                RequestParams.persistRequestParams(SelectionActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                                setupAreaAdapter(selectionList);
                            }
                        } else {
                            AlertDialogUtil.alertMessageError(SelectionActivity.this, response.body().getInfo());
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

}
