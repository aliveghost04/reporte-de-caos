package com.example.erick.reportedecaos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Erick on 19/08/2017.
 */

public class MyReportRecyclerViewAdapter extends
        RecyclerView.Adapter<MyReportRecyclerViewAdapter.MyRecyclerItemViewHolder> {

    private Context context;
    private List<Report> reports;

    public MyReportRecyclerViewAdapter(Context context, List<Report> reports) {
        this.context = context;
        this.reports = reports;
    }

    @Override
    public MyRecyclerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.report_layout, parent, false);

        return new MyRecyclerItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyRecyclerItemViewHolder holder, int position) {
        Report report = reports.get(position);
        holder.description.setText(report.reason);
        holder.userFullname.setText(report.user.name);
        holder.dateTime.setText(
                new SimpleDateFormat().format(
                        new Date(report.timestamp).getTime()
                )
        );
        Glide.with(context)
                .load(report.imageUrl)
//                    .placeholder(R.drawable.ic_photo_black_24dp)
                .into(holder.reportImage);
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public class MyRecyclerItemViewHolder extends RecyclerView.ViewHolder {

        public TextView userFullname;
        public TextView dateTime;
        public TextView description;
        public ImageView userImage;
        public ImageView reportImage;
        public ImageButton voteConfirm;

        public MyRecyclerItemViewHolder(View itemView) {
            super(itemView);

            userFullname = (TextView) itemView.findViewById(R.id.userFullname);
            description = (TextView) itemView.findViewById(R.id.description);
            userImage = (ImageView) itemView.findViewById(R.id.userImage);
            reportImage = (ImageView) itemView.findViewById(R.id.reportImage);
//                voteConfirm = (ImageButton) itemView.findViewById(R.id.voteConfirm);
            dateTime = (TextView) itemView.findViewById(R.id.time);
        }
    }
}
