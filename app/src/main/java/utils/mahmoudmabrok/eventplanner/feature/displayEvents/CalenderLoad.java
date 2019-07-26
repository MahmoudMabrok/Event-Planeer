package utils.mahmoudmabrok.eventplanner.feature.displayEvents;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.mahmoudmabrok.eventplanner.R;
import utils.mahmoudmabrok.eventplanner.dataLayer.remote.Remote;
import utils.mahmoudmabrok.eventplanner.dataLayer.remote.model.WeatherResponce;

public class CalenderLoad extends AppCompatActivity {

    private static final int REQUEST_ACCOUNT_PICKER = 10;
    private static final String PREF_ACCOUNT_NAME = "account ";
    private static final String TAG = "CalenderLoad";
    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    @BindView(R.id.rvEvents)
    RecyclerView rvEvents;
    private GoogleAccountCredential credential;
    private Calendar client;
    private Remote remote;
    private EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_load);
        ButterKnife.bind(this);
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        Log.d(TAG, "onCreate: ");
        credential = GoogleAccountCredential.usingOAuth2(this,
                Collections.singleton(CalendarScopes.CALENDAR));

        if (settings.getString(PREF_ACCOUNT_NAME, null) == null) {
            chooseAccount();
            Log.d(TAG, "chooseAccount: ");
        } else {
            configureAndLoad();
            Log.d(TAG, "configureAndLoad: ");
        }

        initRV();
        loadData();
        remote = new Remote();
        // call weather api
        loadWeather();
    }

    private void loadData() {
        List<utils.mahmoudmabrok.eventplanner.feature.displayEvents.Event> eventList = new ArrayList<>();
        eventList.add(new utils.mahmoudmabrok.eventplanner.feature.displayEvents.Event("Android Meetup #1", "22-8-2019"));
        eventList.add(new utils.mahmoudmabrok.eventplanner.feature.displayEvents.Event("Android Meetup #2", "28-8-2019"));
        adapter.setEventList(eventList);
    }

    private void initRV() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvEvents.setLayoutManager(manager);
        adapter = new EventAdapter();
        rvEvents.setAdapter(adapter);
    }

    private void loadWeather() {
        remote.getWeather().enqueue(new Callback<WeatherResponce>() {
            @Override
            public void onResponse(Call<WeatherResponce> call, Response<WeatherResponce> response) {
                Log.d(TAG, "onResponse: " + response.message());
                WeatherResponce weatherResponce = response.body();
                if (weatherResponce != null) {
                    String temp;
                    temp = weatherResponce.getName() + " " + weatherResponce.getMain().getTemp();
                    Log.d(TAG, "onResponse: temp " + temp);
                    // update with weather data
                    adapter.updateWithWeather(weatherResponce.getMain().getTemp(),
                            weatherResponce.getMain().getHumidity());
                }

            }

            @Override
            public void onFailure(Call<WeatherResponce> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void configureAndLoad() {
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        credential.setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, ""));
        // Calendar client
        client = new Calendar.Builder(
                transport, jsonFactory, credential).setApplicationName("Google-CalendarAndroidSample/1.0")
                .build();

        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        try {
            new Thread(() -> {
                try {
                    Events events = client.events().list("primary")
                            .setMaxResults(10)
                            .setTimeMin(now)
                            .setOrderBy("startTime")
                            .setSingleEvents(true)
                            .execute();

                    List<Event> items = events.getItems();
                    if (items.isEmpty()) {
                        System.out.println("No upcoming events found.");
                    } else {
                        System.out.println("Upcoming events");
                        for (Event event : items) {
                            DateTime start = event.getStart().getDateTime();
                            if (start == null) {
                                start = event.getStart().getDate();
                            }
                            System.out.printf("%s (%s)\n", event.getSummary(), start);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Starts an activity in Google Play Services so the user can pick an
     * account.
     */
    private void chooseAccount() {
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ACCOUNT_PICKER:
                checkPicler(data, resultCode);
        }
    }

    private void checkPicler(Intent data, int resultCode) {
        if (resultCode == RESULT_OK) {
            if (data != null) {
                String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
                if (accountName != null) {
                    //  credential.setSelectedAccountName(accountName);
                    SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(PREF_ACCOUNT_NAME, accountName);
                    editor.commit();

                    configureAndLoad();
                    // // TODO: 7/26/2019  load
                }
            }
        }
    }
}
