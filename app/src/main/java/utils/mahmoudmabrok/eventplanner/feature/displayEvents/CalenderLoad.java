package utils.mahmoudmabrok.eventplanner.feature.displayEvents;

import androidx.appcompat.app.AppCompatActivity;
import utils.mahmoudmabrok.eventplanner.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.calendar.CalendarScopes;

import java.util.Collections;

public class CalenderLoad extends AppCompatActivity {

    private HttpRequestInitializer credential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_load);


        credential =
                GoogleAccountCredential.usingOAuth2(this, Collections.singleton(CalendarScopes.CALENDAR));
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        credential.setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));
        // Calendar client
        client = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential).setApplicationName("Google-CalendarAndroidSample/1.0")
                .build();
    }
}
