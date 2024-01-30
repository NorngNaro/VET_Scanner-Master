package com.udaya.virak_buntham.vetpickup.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.adapters.SelectAreaAdapter;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.utils.Constants;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectAreaActivity extends AppCompatActivity implements OnItemClickListener {

    @BindView(R.id.rv_area_container)
    RecyclerView rvAreaContainer;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    private List<String> provinceList;
    private List<String> districtList;
    private List<String> communeList;
    private int requestAreaCode;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_area);
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null) {
            requestAreaCode = getIntent().getExtras().getInt(Constants.REQUEST_AREA_KEY);
            if (requestAreaCode != 0) {
                if (requestAreaCode == Constants.REQUEST_PROVINCE)
                    mToolbarTitle.setText("Select Province");
                if (requestAreaCode == Constants.REQUEST_DISTRICT)
                    mToolbarTitle.setText("Select District");
                if (requestAreaCode == Constants.REQUEST_COMMUNE)
                    mToolbarTitle.setText("Select Commune");
            }
        }

        RegisterActionBar.registerSupportToolbar(this, mToolbar);

        provinceList = new ArrayList<>();
        for (int index = 0; index < 24; index++) {
            provinceList.add("Phnom Penh");
        }

        districtList = new ArrayList<>();
        districtList.add("Chamkar Mon");
        districtList.add("Doun Penh");
        districtList.add("Prampir Meakkakra");
        districtList.add("Tuol Kouk");
        districtList.add("Dangkao");
        districtList.add("Mean Chey");
        districtList.add("Ruessei Kaev");
        districtList.add("Sen Sok");
        districtList.add("Pou Senchey");
        districtList.add("Chrouy Changva");
        districtList.add("Preaek Pnov");
        districtList.add("Chbar Ampov");

        communeList = new ArrayList<>();
        for (int index = 0; index < 25; index++) {
            communeList.add("Commune");
        }
        if (requestAreaCode == Constants.REQUEST_PROVINCE)
            setupAreaAdapter(provinceList);
        if (requestAreaCode == Constants.REQUEST_DISTRICT)
            setupAreaAdapter(districtList);
        if (requestAreaCode == Constants.REQUEST_COMMUNE)
            setupAreaAdapter(communeList);
    }

    private void setupAreaAdapter(List<String> areaList) {
        SelectAreaAdapter areaAdapter = new SelectAreaAdapter(areaList);
        areaAdapter.setOnItemClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvAreaContainer.setLayoutManager(layoutManager);
        rvAreaContainer.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rvAreaContainer.setItemAnimator(new DefaultItemAnimator());
        rvAreaContainer.setAdapter(areaAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClick(int position) {
        if (requestAreaCode == Constants.REQUEST_PROVINCE)
            returnIntent(Constants.RETURN_PROVINCE_KEY, provinceList.get(position));
        if (requestAreaCode == Constants.REQUEST_DISTRICT)
            returnIntent(Constants.RETURN_DISTRICT_KEY, districtList.get(position));
        if (requestAreaCode == Constants.REQUEST_COMMUNE)
            returnIntent(Constants.RETURN_COMMUNE_KEY, communeList.get(position));
    }

    private void returnIntent(String key, String result) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(key, result);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
