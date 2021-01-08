package com.example.tintok;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.tintok.Adapters_ViewHolder.BaseAdapter;
import com.example.tintok.Adapters_ViewHolder.EmojiAdapter;
import com.example.tintok.Adapters_ViewHolder.EmojiViewHolder;
import com.example.tintok.Adapters_ViewHolder.MessageViewHolder;
import com.example.tintok.Adapters_ViewHolder.MessagesAdapter;
import com.example.tintok.CustomView.EditTextSupportIME;

import com.example.tintok.Model.EmojiModel;
import com.example.tintok.Model.MessageEntity;
import com.example.tintok.Utils.AppNotificationChannelManager;
import com.example.tintok.Utils.EmoticonHandler;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;

import android.view.View;

import android.widget.ImageButton;

import java.util.ArrayList;


public class Activity_ChatRoom extends AppCompatActivity {
    //const
    private String roomID="";
    Activity_Chatroom_ViewModel mViewModel;

    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int PICK_FROM_GALLERY = 101;
    private final static int PICK_FROM_CAMERA = 102;
    //GUI
    RecyclerView emoji;
    RecyclerView messages;
    EditTextSupportIME nextMsg;
    ImageButton emojiButton, galleryImgButton, profileImg, sendBtn, cameraBtn;
    String newRawMsg;
    EmoticonHandler mEmoHandler;
    //Data
    ArrayList<EmojiModel> emojis;
    BaseAdapter<MessageEntity, MessageViewHolder> msgAdapter;

    private ArrayList<String> permissionsToRequest = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        if (getIntent() != null) {
            this.roomID = getIntent().getStringExtra("roomID");
        }
        this.mViewModel = new ViewModelProvider(this).get(Activity_Chatroom_ViewModel.class);

        initComponents();
        initEmoji();
        initMessages();
        askpermission();
        final Intent intent = new Intent(this, Activity_AppMainPages.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);

        mViewModel.getMessEntity(roomID).observe(this, messageEntities -> {
            msgAdapter.setItems(messageEntities);
            messages.smoothScrollToPosition(msgAdapter.getItemCount());
            AppNotificationChannelManager.getInstance().pushNotificationBasic("Message", "Message" , "new Message Received", intent );
        });

        //Testing only
        AppNotificationChannelManager.getInstance().createNotificationChannel("Message");
    }


    void initComponents() {
        emoji = findViewById(R.id.emojiView);
        messages = findViewById(R.id.messageView);
        nextMsg = findViewById(R.id.newMsg);
        emojiButton = findViewById(R.id.openEmojibtn);
        cameraBtn = findViewById(R.id.cameraBtn);
        galleryImgButton = findViewById(R.id.sendImgbtn);
        profileImg = findViewById(R.id.profileImg);
        sendBtn = findViewById(R.id.sendButton);
        emojis = new ArrayList<>();

        nextMsg.requestFocus();

        profileImg.setOnClickListener(v -> {
            //goToProfilePage
        });

        emojiButton.setOnClickListener(v -> {
            if (emoji.getVisibility() == View.VISIBLE)
                emoji.setVisibility(View.INVISIBLE);
            else
                emoji.setVisibility(View.VISIBLE);
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImgfromCamera();
            }
        });

        galleryImgButton.setOnClickListener(v -> handleImgSfromGallery());

        sendBtn.setOnClickListener(v -> handleSendMessage());

        mEmoHandler = mViewModel.getEmoticonHandler(this, nextMsg);
        nextMsg.addTextChangedListener(mEmoHandler);
        nextMsg.setKeyBoardInputCallbackListener((inputContentInfo, flags, opts) -> {

            ArrayList<Uri> imgs = new ArrayList<>();
            Uri imgUri = inputContentInfo.getContentUri();
            imgs.add(imgUri);
            handleSendImg(imgs);
        });

    }


    void initMessages() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        messages.setLayoutManager(layoutManager);
        ArrayList<MessageEntity> msgs = mViewModel.getMessEntity(this.roomID).getValue();
        msgAdapter = new MessagesAdapter(this, msgs);
        messages.setAdapter(msgAdapter);
        messages.addItemDecoration(new DividerItemDecoration( this, LinearLayoutManager.VERTICAL ));
    }

    void initEmoji() {


        //SampleData
        String dataname = "sample";
        int emojiID;
        int i = 1;
        do {
            String imgName = dataname + i;
            emojiID = this.getResources().getIdentifier(imgName, "drawable", this.getPackageName());
            if (emojiID == 0)
                break;
            emojis.add(new EmojiModel(imgName, emojiID));
            i++;
        } while (true);
        //endSampleData

        BaseAdapter<EmojiModel, EmojiViewHolder> emojidapter = new EmojiAdapter(this, emojis, position -> mEmoHandler.insertEmoji(emojis.get(position).getResourceImgName()));
        emoji.setAdapter(emojidapter);

        GridLayoutManager gridView = new GridLayoutManager(this, 5);
        emoji.setLayoutManager(gridView);

    }

    void askpermission() {
        if (permissions.isEmpty()) {
            permissions.add(Manifest.permission.CAMERA);
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        for (String perm : permissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(perm) == PackageManager.PERMISSION_DENIED) ;
                permissionsToRequest.add(perm);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ALL_PERMISSIONS_RESULT) {
            permissionsToRequest.clear();
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED)
                    permissionsToRequest.add(permissions[i]);
            }

            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }

    void handleImgSfromGallery() {
        Intent imgIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imgIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(imgIntent, "Select Gallery"), PICK_FROM_GALLERY);
    }

    void handleImgfromCamera() {
        Intent imgIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (imgIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(imgIntent, PICK_FROM_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<Uri> imgs = new ArrayList<>();
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            ClipData imgData = data.getClipData();

            if (imgData != null) {
                int n = imgData.getItemCount();
                for (int i = 0; i < n; i++) {
                    Uri imageUri = imgData.getItemAt(i).getUri();
                    /*try {
                        InputStream is = getContentResolver().openInputStream(imageUri);
                        imgs.add(BitmapFactory.decodeStream(is));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    imgs.add(imageUri);
                }
            } else {
                Uri imageUri = data.getData();
                imgs.add(imageUri);
            }
            handleSendImg(imgs);
        } else if (requestCode == PICK_FROM_CAMERA) {
            if (data == null)
                return;
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            handleSendImgFromCamera(bitmap);
        }
    }


    void handleSendImg(ArrayList<Uri> imgs) {
        mViewModel.handleSendImg(this.roomID, imgs);
    }

    void handleSendImgFromCamera(Bitmap m) {
        mViewModel.handleSendImgFromCamera(this.roomID, m);
    }

    void handleSendMessage() {
        if (nextMsg.getText().toString().compareTo("") == 0)
            return;
        emoji.setVisibility(View.GONE);
        mViewModel.handleSendMessage(roomID);
        nextMsg.setText("");
    }



}