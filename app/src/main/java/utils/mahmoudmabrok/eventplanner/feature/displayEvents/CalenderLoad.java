package utils.mahmoudmabrok.eventplanner.feature.displayEvents;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
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
import com.uk.tastytoasty.TastyToasty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.mahmoudmabrok.eventplanner.R;
import utils.mahmoudmabrok.eventplanner.dataLayer.local.SharedPref;
import utils.mahmoudmabrok.eventplanner.dataLayer.remote.Remote;
import utils.mahmoudmabrok.eventplanner.dataLayer.remote.model.WeatherResponce;
import utils.mahmoudmabrok.eventplanner.service.UpdateData;

import static utils.mahmoudmabrok.eventplanner.feature.login.LogIn.PREF_ACCOUNT_NAME;

public class CalenderLoad extends AppCompatActivity {

    private static final int REQUEST_CODE = 5001;
    @BindView(R.id.rvEvents)
    RecyclerView rvEvents;


    private static final int REQUEST_ACCOUNT_PICKER = 10;
    private static final String TAG = "CalenderLoad";
    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    private GoogleAccountCredential credential;
    private Calendar client;
    private Remote remote;
    private EventAdapter adapter;

    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_load);
        ButterKnife.bind(this);

        credential = GoogleAccountCredential.usingOAuth2(this,
                Collections.singleton(CalendarScopes.CALENDAR));
        remote = new Remote();
        name = getIntent().getStringExtra(PREF_ACCOUNT_NAME);
        TastyToasty.violet(this, name, null).show();
        Log.d(TAG, "onCreate: name " + name);
        configureAndLoad();

        initRV();
        loadData();

        // call weather api
        loadWeather();
        scheduleUpdateTask();
    }

    private void scheduleUpdateTask() {
        // intent for service
        Intent intent = new Intent(this, UpdateData.class);
        // intent that will start service
        PendingIntent pendingIntent = PendingIntent.getService(this,
                REQUEST_CODE, intent, 0);

        // add 3 seconds after first register.
        long timeToStart = SystemClock.elapsedRealtime() + 4 * 1000;

        long time = 30 * 1000;

        // Schedule the alarm.
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

       /* am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeToStart,
                time, pendingIntent);*/
        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeToStart,
                time, pendingIntent);

    }


    /**
     * Load data (now is a fake data)
     */
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

    /**
     * call api and retrieve weather then update data in recyclerview
     */
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

    /**
     * call Calender API and retrieve events.
     */
    private void configureAndLoad() {
        Log.d(TAG, "configureAndLoad:  name " + name);
        if (name == null) {
            return;
        }
        credential.setSelectedAccountName(name);
        // Calendar client
        client = new Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Google Calendar API")
                .build();

        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        try {
            // create thread to not block UI.
            new Thread(() -> {
                try {
                    // "primary" for current user or base calender.
                    // is snippet from google repo.
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
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        TastyToasty.violet(this, "There is error with Calender API", null).show();
                    });
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
