package com.example.erick.reportedecaos;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.security.Timestamp;
import java.util.Date;

public class ReportActivity extends AppCompatActivity {

    private DatabaseReference reportsReference;
    private StorageReference reportsImagesReference;
    private static final int RC_PHOTO_PICKER = 2;
    private Uri imageUri;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageUri = new Uri.Builder().build();
        setContentView(R.layout.activity_report);

        imageView = ((ImageView) findViewById(R.id.imageView));
        reportsReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(MainActivity.REPORTS_CHILD);

        reportsImagesReference = FirebaseStorage.getInstance()
                .getReference()
                .child(MainActivity.REPORTS_CHILD);

        findViewById(R.id.sendReport).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Report report = new Report();
                report.timestamp = new Date().getTime();
                report.user = Report.castFirebaseUser(FirebaseAuth.getInstance().getCurrentUser());
                report.reason = ((EditText) findViewById(R.id.description)).getText().toString();
                report.imageUrl = imageUri.toString();
                reportsReference.push().setValue(report);
                finish();
            }
        });

        findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete with"), RC_PHOTO_PICKER);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            reportsImagesReference.child(
                    String.valueOf(new Date().getTime())
            ).putFile(uri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageUri = taskSnapshot.getDownloadUrl();
                    Glide.with(ReportActivity.this).load(imageUri).into(imageView);
                }
            });
        }
    }
}
