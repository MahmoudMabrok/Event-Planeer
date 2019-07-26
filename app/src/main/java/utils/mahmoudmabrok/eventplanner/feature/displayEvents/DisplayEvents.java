package utils.mahmoudmabrok.eventplanner.feature.displayEvents;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Events;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;
import utils.mahmoudmabrok.eventplanner.R;

public class DisplayEvents extends AppCompatActivity {

    private static final String TAG = "DisplayEvents";
    private static final int RC_STORAGE = 10;

    @BindView(R.id.rvEvents)
    RecyclerView rvEvents;

    EventAdapter adapter;

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Directory to store user credentials for this application.
     */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".credentials/DisCal");


    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "credentials.json";

    private DataStoreFactory DATA_STORE_FACTORY;


    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.

        InputStream in = DisplayEvents.this.getAssets().open(CREDENTIALS_FILE_PATH);

        GoogleClientSecrets clientSecrets = null;
        try {
            clientSecrets = GoogleClientSecrets
                    .load(JSON_FACTORY, new InputStreamReader(in));
        } catch (IOException e) {
            e.printStackTrace();
            in = getResources().openRawResource(R.raw.credentials);
            clientSecrets = GoogleClientSecrets
                    .load(JSON_FACTORY, new InputStreamReader(in));

        }

        //  Log.d(TAG, "getCredentials: " + String.valueOf(in.available()));

        File a = new File(TOKENS_DIRECTORY_PATH);

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(DATA_STORE_DIR))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public Credential authoize(final NetHttpTransport HTTP_TRANSPORT) throws IOException {

        InputStream in = getResources().openRawResource(R.raw.credentials);

        GoogleClientSecrets clientSecrets = null;
        clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));


        DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
                JSON_FACTORY,
                clientSecrets,
                SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();

        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");

        //Try to close input stream since I don't think it was ever closed?
        // in.close();


        return credential;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_events);
        ButterKnife.bind(this);

        initRV();
        // loadData();

        acquirePermission();

        try {
            load();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load() throws GeneralSecurityException, IOException {
        Log.d(TAG, "load: start");
        final NetHttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT,
                JSON_FACTORY,
                authoize(HTTP_TRANSPORT))
                .setApplicationName("event reminder")
                .build();

        Log.d(TAG, "load: service created");

        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        List<com.google.api.services.calendar.model.Event> items = events.getItems();
        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (com.google.api.services.calendar.model.Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start);
            }
        }
    }


    private void initRV() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvEvents.setLayoutManager(manager);
        adapter = new EventAdapter();
        rvEvents.setAdapter(adapter);
    }

    private void loadData() {
        List<Event> eventList = new ArrayList<>();
        eventList.add(new Event("Android Meetup #1", "22-8-2019"));
        eventList.add(new Event("Android Meetup #2", "28-8-2019"));
        adapter.setEventList(eventList);

    }

    private void acquirePermission() {
        String[] perms = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        EasyPermissions.requestPermissions(new PermissionRequest.Builder(this, RC_STORAGE, perms).build());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            load();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
