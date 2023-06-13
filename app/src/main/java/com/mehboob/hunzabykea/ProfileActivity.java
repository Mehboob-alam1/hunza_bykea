package com.mehboob.hunzabykea;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mehboob.hunzabykea.databinding.ActivityProfileBinding;
import com.mehboob.hunzabykea.ui.DashboardActivity;
import com.mehboob.hunzabykea.ui.models.UserProfileInfo;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    String phonenumber;

    UserProfileInfo profileInfo;
    ProgressDialog dialog;
    DatabaseReference userInfo;
    String currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dialog = new ProgressDialog(this);

        userInfo = FirebaseDatabase.getInstance().getReference("HunzaBykea");
        currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        dialog.setTitle("Uploading");
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);

        phonenumber = getIntent().getStringExtra("pronumber");


        if (phonenumber != null) {
            binding.submitBtn.setOnClickListener(v -> {
                dialog.show();
                String name = binding.edittxtPersonName.getText().toString();
                String email = binding.editEmail.getText().toString();
                if (name.isEmpty()) {
                    binding.edittxtPersonName.setError("Field Can't be Empty");
                } else if (email.isEmpty()) {
                    binding.editEmail.setError("Field Can't be Empty");
                } else {
                    profileInfo = new UserProfileInfo(name, email, phonenumber);
                    userInfo.child("UserInfo")
                            .child(currentUser)
                            .setValue(profileInfo)
                            .addOnCompleteListener(task -> {
                                if (task.isComplete()) {
                                    dialog.dismiss();
                                    startActivity(new Intent(ProfileActivity.this, DashboardActivity.class));
                                    finishAffinity();
                                    Toast.makeText(this, "Data submitted Successfully ", Toast.LENGTH_SHORT).show();
                                }
                            });

                }


            });
        }


    }
}