package com.mehboob.hunzabykea;

import static com.mehboob.hunzabykea.utils.HideKeyboard.hideKeyboard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mehboob.hunzabykea.databinding.ActivityProfileBinding;
import com.mehboob.hunzabykea.ui.DashboardActivity;
import com.mehboob.hunzabykea.ui.models.UserProfileInfo;

import java.util.Objects;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    String phonenumber;

    UserProfileInfo profileInfo;
    ProgressDialog dialog;
    DatabaseReference userInfo;
    String currentUser;
    private static final int IMAGE_REQUEST = 13;
    private Uri uri=null;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dialog = new ProgressDialog(this);

        userInfo = FirebaseDatabase.getInstance().getReference(Constants.HUNZA_BYKEA);
        currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        storageReference= FirebaseStorage.getInstance().getReference("Users");

        dialog.setTitle("Uploading");
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);


        binding.imgUserProfile.setOnClickListener(v -> {
            pickImage();
        });

            binding.submitBtn.setOnClickListener(v -> {


                if (binding.edittxtPersonName.getText().toString().isEmpty()) {
                    binding.edittxtPersonName.setError("Field Can't be Empty");
                } else if (binding.editEmail.getText().toString().isEmpty()) {
                    binding.editEmail.setError("Field Can't be Empty");
                } else if (uri ==null) {
                    Toast.makeText(this, "Select an image", Toast.LENGTH_SHORT).show();
                }else if(binding.editPhone.getText().toString().isEmpty()){
                    binding.editPhone.setError("Field Can't be Empty");
                }else{
                    String name = binding.edittxtPersonName.getText().toString();
                    String email = binding.editEmail.getText().toString();
                    String phone = binding.editPhone.getText().toString();
                    hideKeyboard(this);
                    profileInfo = new UserProfileInfo(name, email, phone,"");
                    uploadImage(uri,profileInfo);



                }


            });



    }

    private void uploadImage(Uri uri, UserProfileInfo profileInfo) {

        String imageName = UUID.randomUUID().toString() + ".jpg";
        StorageReference imageReference=        storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("UserImage");

        dialog.show();
        // Upload image to Firebase Storage
        UploadTask uploadTask = imageReference.putFile(uri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Get the download URL of the image from Firebase Storage
            imageReference.getDownloadUrl().addOnSuccessListener(downloadUrl -> {

                uploadData(new UserProfileInfo(profileInfo.getName(),profileInfo.getEmail(),profileInfo.getPhone(),downloadUrl.toString()));

                Toast.makeText(getApplicationContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e -> {
            // Display an error message to the user
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void uploadData(UserProfileInfo profileInfo) {

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


    private void pickImage() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            binding.imgUserProfile.setImageURI(uri);
        }
    }
}