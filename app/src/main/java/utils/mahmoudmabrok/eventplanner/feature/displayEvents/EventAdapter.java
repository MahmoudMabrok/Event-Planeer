package utils.mahmoudmabrok.eventplanner.feature.displayEvents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import utils.mahmoudmabrok.eventplanner.R;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.Holder> {


    private List<Event> list;

    public EventAdapter() {
        list = new ArrayList<>();
    }

    public void addEvent(Event item) {
        list.add(item);
        notifyItemInserted(list.size() - 1);
        notifyItemRangeChanged(list.size() - 1, list.size());
    }


    public void setEventList(List<Event> newList) {
        list = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public List<Event> getList() {
        return list;
    }

    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_item, viewGroup, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        Event item = list.get(i);
        holder.tvEventName.setText(item.getName());
        holder.tvEventDate.setText(item.getDate());
        holder.textView4.setText(item.getWeather());
        holder.tvEventDetailse.setText(item.getDetails());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateWithWeather(double temp, int humidity) {
        for (Event event : list) {
            event.setTemperature(temp);
            event.setHumidity(humidity);
        }
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvEventName)
        TextView tvEventName;
        @BindView(R.id.tvEventDate)
        TextView tvEventDate;
        @BindView(R.id.tvEventDetailse)
        TextView tvEventDetailse;
        @BindView(R.id.textView4)
        TextView textView4;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}