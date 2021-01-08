package com.example.tintok.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.tintok.Model.MediaEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;

public class FileUtil {
    public static MultipartBody.Part prepareImageFileBody(Context context, String name, MediaEntity media){
        RequestBody body = null;
        File mFile = null;

        if(media.uri != null){
            MediaType extension = MediaType.parse(context.getContentResolver().getType(media.uri));
            mFile = new File(context.getCacheDir(),"temp."+ extension.subtype());
            InputStream in = null;
            try {
                in = context.getContentResolver().openInputStream(media.uri);
                OutputStream out = new FileOutputStream(mFile);
                byte[] buffer = new byte[1024];
                int length;
                while((length = in.read(buffer)) >0){
                    out.write(buffer, 0, length);
                }
                out.close();
                in.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            body =  RequestBody.create(extension,
                    mFile);
        }
        else if(media.bitmap!= null) {
            mFile =  getFile(context, media.bitmap);
            body = RequestBody.create(
                    MediaType.parse("image/*"),
                    mFile);
        }
        if(mFile != null){
            mFile.deleteOnExit();
            return MultipartBody.Part.createFormData(name, mFile.getName() , body);
        }
        else return null;
    }

    public static File getFile(Context context, Bitmap bitmap){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] image = bos.toByteArray();
        File file = null;
        try {
            file = new File(context.getCacheDir(),"unknown.jpg");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(image);
            fos.close();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
