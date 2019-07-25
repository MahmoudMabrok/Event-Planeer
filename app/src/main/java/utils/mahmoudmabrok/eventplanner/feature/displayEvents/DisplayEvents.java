package utils.mahmoudmabrok.eventplanner.feature.displayEvents;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import utils.mahmoudmabrok.eventplanner.R;

public class DisplayEvents extends AppCompatActivity {

    private static final String TAG = "DisplayEvents";

    @BindView(R.id.rvEvents)
    RecyclerView rvEvents;

    EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_events);
        ButterKnife.bind(this);

        initRV();
        loadData();

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

        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/{group-id}/events",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Log.d(TAG, "onCompleted: " + response.getRawResponse());
                    }

                }
        ).executeAsync();
    }

}
