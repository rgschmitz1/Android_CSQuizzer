package edu.tacoma.uw.csquizzer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.tacoma.uw.csquizzer.model.Topic;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.MyViewHolder> {
    private List<Topic> lTopics = new ArrayList<>();
    private List<Topic> queryTopics = new ArrayList<>();
    private Context mContext;
    public static final int TYPE1 = 0;
    public static final int TYPE2 = 1;

    public TopicAdapter(Context mContext, List<Topic> lTopics) {
        this.mContext = mContext;
        this.lTopics = lTopics;
        this.queryTopics.addAll(lTopics);
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0)
            return TYPE2;
        else
            return TYPE1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = null;
        switch (viewType)
        {
            case TYPE1:
                view = inflater.inflate(R.layout.item_topics_white, parent, false);
                break;
            case TYPE2:
                view = inflater.inflate(R.layout.item_topics_purple, parent, false);
                break;
        }

        return new TopicAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Topic topic = lTopics.get(position);
        holder.tvTopicId.setText(topic.getTopicId() + ".");
        holder.tvTopicTitle.setText(topic.getTopicDescription());
    }

    @Override
    public int getItemCount() {
        return lTopics.size();
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        lTopics.clear();
        if (charText.length() == 0) {
            lTopics.addAll(queryTopics);
        } else {
            for (Topic topic : queryTopics) {
                if (topic.getTopicDescription().toLowerCase(Locale.getDefault()).contains(charText)) {
                    lTopics.add(topic);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTopicId;
        private TextView tvTopicTitle;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvTopicId = itemView.findViewById(R.id.tv_TopicId);
            this.tvTopicTitle = itemView.findViewById(R.id.tv_TopicDescription);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    EditTopicFragment editTopicFragment = new EditTopicFragment(mContext, tvTopicId.getText().toString(), tvTopicTitle.getText().toString());
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, editTopicFragment)
                            .commit();
                }
            });
        }
    }
}
