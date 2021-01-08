package com.example.tintok.CustomView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tintok.DataLayer.DataRepositoryController;
import com.example.tintok.Model.MediaEntity;
import com.example.tintok.Model.Post;
import com.example.tintok.R;

import java.util.ArrayList;

public class PostUploadFragment extends DialogFragment {

    private EditTextSupportIME status;
    private ImageView image;

    MediaEntity chosenImage;

    // Code for get image from gallery
    private static final int IMAGE_PICK_CODE = 224;
    private static final int REQUEST_IMAGE = 1998;

    private static final int CAMERA_PICK_CODE = 225;
    private static final int REQUEST_CAMERA = 225;

    public PostUploadFragment(onNewPostListener mListner){
        this.mListner = mListner;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.post_upload_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = (Toolbar) getView().findViewById(R.id.new_post_activity_toolbar);

        String[] colors = {"Gallery", "Camera"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Picking image from");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0)
                    askForPermission(REQUEST_IMAGE);
                else
                    askForPermission(REQUEST_CAMERA);
            }
        });

        this.status = getView().findViewById(R.id.new_post_status);
        this.image = getView().findViewById(R.id.new_post_image);

        this.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
            }
        });
        Button postBtn = getView().findViewById(R.id.new_post_submit);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(chosenImage != null){
                   Post mPost = new Post("", status.getText().toString(),
                           DataRepositoryController.getInstance().getUser().getUserID(),
                           DataRepositoryController.getInstance().getUser().getUserName(),
                           chosenImage );
                   mListner.onNewPost(mPost);
                   getDialog().dismiss();
               } else {
                   Toast.makeText(getContext(), "No image chosen", Toast.LENGTH_LONG).show();
               }
            }
        });

        status.setKeyBoardInputCallbackListener((inputContentInfo, flags, opts) -> {
            Uri imgUri = inputContentInfo.getContentUri();
            Glide.with(this.getContext()).load(imgUri).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(image);
            chosenImage = new MediaEntity(imgUri, "");
        });
        this.getDialog().setCanceledOnTouchOutside(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.MyAnimation_Window);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    private void askForPermission(int requestCode){
        String permission = requestCode == REQUEST_IMAGE ? Manifest.permission.READ_EXTERNAL_STORAGE : Manifest.permission.CAMERA;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String[] permissions = {permission};
                requestPermissions(permissions,requestCode);
            } else {
                if(requestCode == REQUEST_IMAGE)
                    pickImageFromGallery();
                else pickImageFromCamera();
            }
        } else {
            if(requestCode == REQUEST_IMAGE)
                pickImageFromGallery();
            else pickImageFromCamera();
        }
    }

    private void pickImageFromCamera(){
        Intent imgIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (imgIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(imgIntent, CAMERA_PICK_CODE);
        }
    }

    private void pickImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            if(requestCode == REQUEST_IMAGE)
                pickImageFromGallery();
            else pickImageFromCamera();
        else
            Toast.makeText(this.getContext(),"Permission denied...", Toast.LENGTH_LONG);
    }

    @Override
   public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null && resultCode == getActivity().RESULT_OK) {
            if (requestCode == IMAGE_PICK_CODE) {
                Uri imgUri= data.getData();
                Glide.with(this.getContext()).load(imgUri).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(image);
                chosenImage = new MediaEntity(imgUri, "");
            } else if(requestCode == CAMERA_PICK_CODE){
                if(data.getExtras() != null){
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    Glide.with(this.getContext()).load(bitmap).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(image);
                    chosenImage = new MediaEntity(bitmap);
                }
            } else{
                Log.i("Info", "Some things wrong happened");
            }
        }
    }

    onNewPostListener mListner;
    public interface onNewPostListener {
       public void onNewPost(Post newPost);

    }
}
