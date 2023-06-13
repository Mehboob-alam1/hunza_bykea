package com.mehboob.hunzabykea.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehboob.hunzabykea.ProfileActivity;
import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.databinding.FragmentAccountBinding;
import com.mehboob.hunzabykea.ui.DashboardActivity;
import com.mehboob.hunzabykea.ui.models.UserProfileInfo;

import java.util.Objects;


public class AccountFragment extends Fragment {

    FragmentAccountBinding binding;
    private DatabaseReference userInfoRef;
    private String currentUser;
    ProgressDialog dialog;
    UserProfileInfo profileInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);

        dialog = new ProgressDialog(getContext());



        dialog.setTitle("Updating");
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);

        userInfoRef = FirebaseDatabase.getInstance().getReference("HunzaBykea");
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();


        binding.userDetailsEditBtn.setOnClickListener(v -> {
            binding.userDetailsDoneBtn.setVisibility(View.VISIBLE);
            binding.userDetailsEditBtn.setVisibility(View.GONE);
            enableEditText(binding.userNameEditTxt);
            enableEditText(binding.userEmailEditTxt);
//            enableEditText(binding.userMobileNoEditTxt);
        });
        binding.userDetailsDoneBtn.setOnClickListener(v -> {

                dialog.show();
                String name = binding.userNameEditTxt.getText().toString();
                String email = binding.userEmailEditTxt.getText().toString();
                if (name.isEmpty()) {
                    binding.userNameEditTxt.setError("Field Can't be Empty");
                } else if (email.isEmpty()) {
                    binding.userEmailEditTxt.setError("Field Can't be Empty");
                } else {
                    profileInfo = new UserProfileInfo(name, email, binding.userMobileNoEditTxt.getText().toString());
                    userInfoRef.child("UserInfo")
                            .child(currentUser)
                            .setValue(profileInfo)
                            .addOnCompleteListener(task -> {
                                if (task.isComplete()) {
                                    binding.userDetailsEditBtn.setVisibility(View.VISIBLE);
                                    binding.userDetailsDoneBtn.setVisibility(View.GONE);
                                    disableEditText(binding.userNameEditTxt);
                                    disableEditText(binding.userEmailEditTxt);
                                    disableEditText(binding.userMobileNoEditTxt);
                                    dialog.dismiss();
//                                    startActivity(new Intent(ProfileActivity.this, DashboardActivity.class));
//                                    finishAffinity();
                                    Toast.makeText(getContext(), "Data Updated Successfully ", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
        });

        binding.userMobileNoEditTxt.setOnClickListener(v -> {
            if (!binding.userMobileNoEditTxt.isEnabled()){
                Toast.makeText(getContext(), "Mobile Can't be change", Toast.LENGTH_SHORT).show();
            }
        });


        userInfoRef.child("UserInfo").child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    UserProfileInfo userInfo = snapshot.getValue(UserProfileInfo.class);
                    assert userInfo != null;
                    binding.userNameEditTxt.setText(userInfo.getName());
                    binding.userEmailEditTxt.setText(userInfo.getEmail());
                    binding.userMobileNoEditTxt.setText(userInfo.getPhone());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage()+" ", Toast.LENGTH_SHORT).show();
            }
        });



        return binding.getRoot();
    }

    private void disableEditText(EditText editText) {
//        editText.setFocusable(false);
        editText.setEnabled(false);
//        editText.setCursorVisible(false);
//        editText.setKeyListener(null);
        editText.setGravity(Gravity.END | Gravity.CENTER);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }
    private void enableEditText(EditText editText) {
//        editText.setFocusable(true);
        editText.setEnabled(true);
//        editText.setCursorVisible(true);
        editText.setGravity(Gravity.START | Gravity.CENTER );
//        editText.setKeyListener(variable);
        editText.setBackgroundResource(R.drawable.accountedittxt_background);
    }

    @Override
    public void onStart() {
        super.onStart();
        disableEditText(binding.userEmailEditTxt);
        disableEditText(binding.userNameEditTxt);
        disableEditText(binding.userMobileNoEditTxt);
//        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).hide();
    }
}