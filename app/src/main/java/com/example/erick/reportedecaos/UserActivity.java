package com.example.erick.reportedecaos;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UserActivity extends AppCompatActivity {

    private boolean textChanged;
    private boolean imageChanged;
    private Uri imageUri;
    private boolean emailChanged;
    private boolean passwordChanged;

    private StorageReference storageReference;
    private FirebaseUser firebaseUser;

    private static final int RC_FILE_CHOOSER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        storageReference = FirebaseStorage.getInstance().getReference().child(MainActivity.USERS_CHILD);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        ImageView ivUser = (ImageView) findViewById(R.id.ivUser);
        EditText etName = (EditText) findViewById(R.id.etName);
        EditText etEmail = (EditText) findViewById(R.id.etEmail);
        EditText etPassword = (EditText) findViewById(R.id.etPassword);

        etName.setText(firebaseUser.getDisplayName());
        etEmail.setText(firebaseUser.getEmail());
        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(ivUser);

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserProfileChangeRequest.Builder userProfileChangeRequest =
                        new UserProfileChangeRequest.Builder();

                if (imageChanged) {
                    userProfileChangeRequest.setPhotoUri(imageUri);
                }

                if (textChanged) {
                    userProfileChangeRequest
                            .setDisplayName(
                                    ((EditText) findViewById(R.id.etName)).getText().toString()
                            );
                }

                UserProfileChangeRequest request = userProfileChangeRequest.build();
                firebaseUser.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (passwordChanged) {
                            firebaseUser.updatePassword(
                                    ((EditText) findViewById(R.id.etPassword)).getText().toString()
                            );
                        }

                        if (emailChanged) {
                            firebaseUser.updateEmail(
                                    ((EditText) findViewById(R.id.etEmail)).getText().toString()
                            );
                        }

                        finish();
                        Toast.makeText(UserActivity.this, "Perfil actualizado correctamente!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        findViewById(R.id.btnChangeImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, null), RC_FILE_CHOOSER);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_FILE_CHOOSER && resultCode == RESULT_OK) {
            storageReference.child(firebaseUser.getUid()).putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageChanged = true;
                    imageUri = taskSnapshot.getDownloadUrl();
                }
            });
        }
    }
}
