package com.example.tintok;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tintok.Adapters_ViewHolder.ImagePage2Adapter;
import com.example.tintok.Model.Post;

import java.util.ArrayList;

public class Image_Profile_Fragment extends Fragment {

    private ImagePage2Adapter adapter;
    private ViewPager2 vp2;
    ArrayList<Post> list;

    public Image_Profile_Fragment(ArrayList<Post> list) {
        this.list = list;
    }

    public static Image_Profile_Fragment getInstance(ArrayList<Post> list) {
        Image_Profile_Fragment fragment = new Image_Profile_Fragment(list);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("INFO", "Creating new profile image...");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("INFO", "Creating view for displaying photo profile ...");
        View view = inflater.inflate(R.layout.profile_image_fragment, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.onStart();
        setRetainInstance(true);
        this.adapter = new ImagePage2Adapter(this.getContext() ,list);
        this.vp2 = getView().findViewById(R.id.profile_image_list_page);
        vp2.setAdapter(adapter);
        final int offScreenPageLimit = 3;
        vp2.setOffscreenPageLimit(offScreenPageLimit);
        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(10));
        transformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                /*Log.e("transform: at" + page.getId() , String.valueOf(position));
                float x = 1-Math.abs(position);
                page.setScaleY( 0.8f+x*0.2f);*/
                int pageWidth = page.getWidth();
                page.setElevation(-Math.abs(position));

                float DEFAULT_TRANSLATION_X = .0f;
                float DEFAULT_TRANSLATION_FACTOR = 1.2f;

                float SCALE_FACTOR = .14f;
                float DEFAULT_SCALE = 1f;

                float ALPHA_FACTOR = .3f;
                float DEFAULT_ALPHA = 1f;

                float scaleFactor = -SCALE_FACTOR * position + DEFAULT_SCALE;
                float alphaFactor = -ALPHA_FACTOR * position + DEFAULT_ALPHA;
                if(position<=0){
                    page.setTranslationX(DEFAULT_TRANSLATION_X);
                    page.setScaleX(DEFAULT_SCALE);
                    page.setScaleY(DEFAULT_SCALE);
                    page.setAlpha(DEFAULT_ALPHA+position);
                }
                else if(position <= offScreenPageLimit ){
                    page.setScaleX(scaleFactor);
                    page.setScaleY(scaleFactor);
                    page.setTranslationX(-pageWidth/DEFAULT_TRANSLATION_FACTOR*position);
                    page.setAlpha(alphaFactor);
                }
                else{
                    page.setTranslationX(DEFAULT_TRANSLATION_X);
                    page.setScaleX(DEFAULT_SCALE);
                    page.setScaleY(DEFAULT_SCALE);
                    page.setAlpha(DEFAULT_ALPHA);
                }
            }
        });
        vp2.setPageTransformer(transformer);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("INFO", "Destroying view of displaying photo profile ...");
    }

}