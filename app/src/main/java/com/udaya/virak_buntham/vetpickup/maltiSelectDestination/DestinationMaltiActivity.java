package com.udaya.virak_buntham.vetpickup.maltiSelectDestination;

import android.annotation.SuppressLint;
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

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.MoveItemToVanActivity;
import com.udaya.virak_buntham.vetpickup.activities.TransitActivity;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.maltiSelectDestination.chipView.ChipView;
import com.udaya.virak_buntham.vetpickup.maltiSelectDestination.chipView.SimpleChipAdapter;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DestinationMaltiActivity extends AppCompatActivity implements OnItemClickListener, OnInternetConnectionListener {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_selection_container)
    RecyclerView rvSelectionContainer;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.place_holder_no_record)
    TextView txtPlaceholderNoRecord;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvDone)
    TextView tvDone;

    private ProgressDialog progressDialog;
    private List<SelectionData> selectionList;

    private String branchId;
    private int requestSelectionCode;


    ChipView chipView;
    ArrayList arrayChipView = new ArrayList();
    ArrayList arrayIdChipView = new ArrayList();

    public String name = "auto";

    //searchItem

    public DestinationMaltiActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_malti);
        ButterKnife.bind(this);
        RegisterActionBar.registerSupportToolbar(this, mToolbar);


        progressDialog = new ProgressDialog(this);


        if (getIntent().getExtras() != null) {
            requestSelectionCode = getIntent().getExtras().getInt(Constants.REQUEST_SELECTION_KEY);
            String destinationFromId = getIntent().getExtras().getString("destination_from_id");
            branchId = getIntent().getExtras().getString("destination_to_by_branch_id");
            if (requestSelectionCode != 0) {
                if (requestSelectionCode == Constants.REQUEST_DESTINATION_BY_BRANCH_CODE)
                    mToolbarTitle.setText(R.string.destination_to);
                tvDone.setVisibility(View.VISIBLE);
                tvDone.setOnClickListener(v -> {
                    onBackPressed();
                    new MoveItemToVanActivity().destinationChoose = 1;
                  //  new TransitActivity().destinationChoose = 1;
                });
            }
        }
        if (requestSelectionCode == Constants.REQUEST_DESTINATION_BY_BRANCH_CODE) {
            requestDestinationToByBranch(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this)
                    , branchId, ""
            );
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            new MoveItemToVanActivity().destinationChoose = 1;
        //    new TransitActivity().destinationChoose = 1;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("nameGetBack==>", "" + name);
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
                                RequestParams.persistRequestParams(DestinationMaltiActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                                txtPlaceholderNoRecord.setVisibility(View.VISIBLE);
                            } else {
                                selectionList = Objects.requireNonNull(response.body()).getData();

                                //Replace request params
                                RequestParams.persistRequestParams(DestinationMaltiActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());


                                for (int i = 0; i < selectionList.size(); i++) {
                                    arrayChipView.add(selectionList.get(i).getName());
                                    arrayIdChipView.add(selectionList.get(i).getId());
                                }
                                setDatChipView();
                                setupAreaAdapter(selectionList);
                            }
                        } else {
                            AlertDialogUtil.alertMessageError(DestinationMaltiActivity.this, response.body().getInfo());
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

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setupAreaAdapter(List<SelectionData> selectionList) {
        SelectionMaltiAdapter selectionAdapter = new SelectionMaltiAdapter(selectionList);
        selectionAdapter.setOnItemClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvSelectionContainer.setLayoutManager(layoutManager);
        rvSelectionContainer.setAdapter(selectionAdapter);
    }

    @Override
    public void itemClick(int position) {
//        if (requestSelectionCode == Constants.REQUEST_DESTINATION_BY_BRANCH_CODE)
//            returnIntent(Constants.REQUEST_DESTINATION_TO_BY_BRANCH_KEY, selectionList.get(position));
    }

    private void returnIntent(String key, SelectionData result) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(key, result);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void setDatChipView() {
        chipView = findViewById(R.id.mChipView);
        SimpleChipAdapter adapter = new SimpleChipAdapter(arrayChipView, arrayIdChipView);
        chipView.setAdapter(adapter);
    }


}
