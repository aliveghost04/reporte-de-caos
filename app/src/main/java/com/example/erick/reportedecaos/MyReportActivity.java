package com.example.erick.reportedecaos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.firebase.ui.database.FirebaseArray;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyReportActivity extends AppCompatActivity {
    private List<Report> reports;
    private MyReportRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_report);
        reports = new ArrayList();
        adapter = new MyReportRecyclerViewAdapter(MyReportActivity.this, reports);

        FirebaseDatabase.getInstance()
                .getReference()
                .child(MainActivity.REPORTS_CHILD)
                .equalTo(
                        FirebaseAuth.getInstance().getCurrentUser().getUid()
                ).addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot report : dataSnapshot.getChildren()) {
                    reports.add(report.getValue(Report.class));
                    Log.d("Main", dataSnapshot.getValue(Report.class).toString());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        RecyclerView listReports = (RecyclerView) findViewById(R.id.listReports);
        listReports.setAdapter(adapter);
        listReports.setLayoutManager(new LinearLayoutManager(MyReportActivity.this));
    }
}
