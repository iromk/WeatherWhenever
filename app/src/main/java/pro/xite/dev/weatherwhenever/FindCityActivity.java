package pro.xite.dev.weatherwhenever;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import pro.xite.dev.weatherwhenever.data.WebJsonDataProvider;
import pro.xite.dev.weatherwhenever.data.ods.OdsCityProvider;
import pro.xite.dev.weatherwhenever.data.ods.OdsResponse;
import pro.xite.dev.weatherwhenever.manage.DataProviderListener;

public class FindCityActivity extends AppCompatActivity
        implements DataProviderListener {

    private static final String TAG = "FindCityActivity/TRACER";
    static final int REQUEST_CODE = 22;
    private ArrayAdapter<String> listAdapter;

    private OdsCityProvider odsService;
    private boolean bound = false;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            OdsCityProvider.DataProviderBinder odsServiceBinder =
                    (OdsCityProvider.DataProviderBinder) service;
            odsService = odsServiceBinder.getDataProviderService();
            odsService.setListener(FindCityActivity.this);
            bound = true;
            Log.d(TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_city);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText editTextCityname = findViewById(R.id.edittext_cityname);
        editTextCityname.setVisibility(View.VISIBLE);

        editTextCityname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                suggestCities(editTextCityname.getText());
                Log.d(TAG, String.format("onTextChanged: %s", editTextCityname.getText()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Intent intent = new Intent(this, OdsCityProvider.class);
        bindService(intent, connection, BIND_AUTO_CREATE);

        listAdapter = new ArrayAdapter<>( this,
                android.R.layout.simple_list_item_1, citi.cities);
        ListView listCities = findViewById(R.id.list_view);
        listCities.setAdapter(listAdapter);
    }

    private void suggestCities(Editable text) {
        odsService.suggest(text.toString()); // request service
//        OdsCityProvider.request(text.toString(), this); // request in a thread

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bound) {
            unbindService(connection);
            bound = false;
        }
    }

    @Override
    public void onSerializedDataReceived(Serializable object) {
        citi.clear();
        OdsResponse odsResponse = (OdsResponse) object;
        for (OdsResponse.OdsCity city: odsResponse.getCities()) {
            citi.add(city.getName());
        }
        listAdapter.notifyDataSetChanged();
    }

    City citi = new City();
    private class City {
        ArrayList<String> cities = new ArrayList<>(Arrays.asList("dasd", "aqweqe", "poopor", "ngvs"));
        public void add(String s) {
            cities.add(s);
        }
        public void clear() {
            cities.clear();
        }

    }
}
