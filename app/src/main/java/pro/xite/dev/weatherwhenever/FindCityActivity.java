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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

import pro.xite.dev.weatherwhenever.data.IDataProvider;
import pro.xite.dev.weatherwhenever.data.WebJsonProvider;
import pro.xite.dev.weatherwhenever.data.ods.OdsCityProvider;
import pro.xite.dev.weatherwhenever.data.ods.OdsResponse;
import pro.xite.dev.weatherwhenever.manage.IDataProviderListener;

public class FindCityActivity extends AppCompatActivity
        implements IDataProviderListener, AdapterView.OnItemClickListener {

    private static final String TAG = "FindCityActivity/TRACER";
    static final int REQUEST_CODE = 22;
    static final String RESULT_CITY = "CITYENTITY";
    private ArrayAdapter<OdsResponse.OdsCity> listAdapter;

    private IDataProvider odsService;
    private boolean bound = false;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WebJsonProvider.DataProviderServiceBinder odsServiceBinder =
                    (WebJsonProvider.DataProviderServiceBinder) service;
            odsService = odsServiceBinder.getDataProviderService();
            odsService.setListener(FindCityActivity.this);
            odsService.setDelayedRequestTimeout(1200);
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
                String userInput = editTextCityname.getText().toString();
                odsService.delayedRequest(userInput); // overriding service request
//                odsService.queuedRequest(userInput); // queued service request
//                OdsCityProvider.asyncRequest(FindCityActivity.this, userInput); // request in a thread
                Log.d(TAG, String.format("onTextChanged: %s", userInput));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Intent intent = new Intent(this, OdsCityProvider.class);
        bindService(intent, connection, BIND_AUTO_CREATE);

        listAdapter = new ArrayAdapter<>( this,
                android.R.layout.simple_list_item_1, odsCities);
        ListView listCities = findViewById(R.id.list_view);
        listCities.setAdapter(listAdapter);
        listCities.setOnItemClickListener(this);
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
        OdsResponse odsResponse = (OdsResponse) object;
        odsCities.clear();
        odsCities.addAll(odsResponse.getCities());
        listAdapter.notifyDataSetChanged();
    }

    final private ArrayList<OdsResponse.OdsCity> odsCities = new ArrayList<>();

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent selection = new Intent();
        selection.putExtra(RESULT_CITY, odsCities.get(position));
        setResult(RESULT_OK, selection);
        finish();
    }
}
