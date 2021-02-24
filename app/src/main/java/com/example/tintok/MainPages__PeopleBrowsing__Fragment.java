package com.example.tintok;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.DefaultItemAnimator;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.tintok.Adapters_ViewHolder.PeopleBrowsingAdapter;
import com.example.tintok.CustomView.AfterRefreshCallBack;
import com.example.tintok.CustomView.FilterDialogFragment;
import com.example.tintok.CustomView.MyDialogFragment;
import com.example.tintok.CustomView.Refreshable;
import com.example.tintok.Model.UserSimple;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;

public class MainPages__PeopleBrowsing__Fragment extends MyDialogFragment {

    private MainPages_PeopleBrowsing_ViewModel  mViewModel;
    MaterialButton filterBtn;
    MaterialToolbar toolbar;
    private int currentItem = 0;
    private int offScreenPageLimit = 2;

    CardStackView cardStackView;
    PeopleBrowsingAdapter adapter;
    CardStackLayoutManager  manager;

    DialogFragment filterFragment = null;
    FilterDialogFragment.FilterState currentState ;
    ShapeableImageView backBtn;

    ImageButton likeBtn, dislikeBtn, profileBtn;

    public MainPages__PeopleBrowsing__Fragment() {
        currentState = new FilterDialogFragment.FilterState();
        Log.e("Main_People_Frag", "Constructor");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.mainpages__peoplebrowsing__fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(MainPages_PeopleBrowsing_ViewModel.class);
        initFragment(view);
        return view;
    }



    public void initFragment(View view){

        this.setRetainInstance(true);
        // TODO: Use the ViewModel
        mViewModel.getMatchingPeople().observe(this.getViewLifecycleOwner(), userSimples -> {
            adapter.setItems(userSimples);
        });
        toolbar = view.findViewById(R.id.people_matching_toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            getDialog().dismiss();
        });
        filterBtn = toolbar.findViewById(R.id.filterBtn);
        filterBtn.setOnClickListener(v -> {
            handleFilterBtnClicked();
        });
        /*
        backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {

        });

         */
        initCardView(view);
        initButtonGroup(view);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateFilterBtn();
    }

    public void handleFilterBtnClicked(){
        if(filterFragment != null)
            return;
        filterFragment = new FilterDialogFragment(currentState, state -> {
            filterFragment = null;
            if(state != null){
                currentState = state;
                mViewModel.submitFilter(currentState);
            }
            updateFilterBtn();
        });
        filterFragment.show(getChildFragmentManager(), "Filtering");
    }

    void updateFilterBtn(){
        if(currentState.equals(new FilterDialogFragment.FilterState()))
            filterBtn.setText("ADD NEW FILTER");
        else
            filterBtn.setText("MODIFY CURRENT FILTER");
    }


    

    void initCardView(View view){
        cardStackView = view.findViewById(R.id.card_stack_view);
        ArrayList<UserSimple> models = new ArrayList<UserSimple>();
        UserSimple user_1 = new UserSimple();
        user_1.setUserName("1");
        user_1.setDescription("im 1");
        UserSimple user_2 = new UserSimple();
        user_2.setUserName("2");
        user_2.setDescription("im 2");
       // models.add(user_1);
        //models.add(user_2);

        final String TAG ="Test";
        manager = new CardStackLayoutManager(getContext(), new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                if(ratio < 0.4)
                    return;
                ImageView likeImg = manager.getTopView().findViewById(R.id.likeImg),
                        dislikeImg = manager.getTopView().findViewById(R.id.dislikeImg);

                if (direction == Direction.Right){
                    likeImg.setVisibility(View.VISIBLE);
                    dislikeImg.setVisibility(View.INVISIBLE);
                }
                if (direction == Direction.Top){

                }
                if (direction == Direction.Left){
                    dislikeImg.setVisibility(View.VISIBLE);
                    likeImg.setVisibility(View.INVISIBLE);
                }
                if (direction == Direction.Bottom){
                }
            }

            @Override
            public void onCardSwiped(Direction direction) {
                boolean isLiked = false;
                if (direction == Direction.Right)
                    isLiked = true;
                
                else if (direction == Direction.Left)
                    isLiked = false;

                mViewModel.submitPeopleReaction(adapter.getItems().get(manager.getTopPosition()-1), isLiked);
            }

            @Override
            public void onCardRewound() {
                Log.e(TAG, "onCardRewound: " + manager.getTopPosition());

            }

            @Override
            public void onCardCanceled() {
                ImageView likeImg = manager.getTopView().findViewById(R.id.likeImg),
                        dislikeImg = manager.getTopView().findViewById(R.id.dislikeImg);
                dislikeImg.setVisibility(View.INVISIBLE);
                likeImg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCardAppeared(View view, int position) {
                //TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " +adapter.getItems());
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                Log.d(TAG, "onCardDisAppeared: " + position + ", nama: " +adapter.getItems() );
            }
        });
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.6f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(true);
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        adapter = new PeopleBrowsingAdapter(this.getActivity(), models);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.setItemAnimator(new DefaultItemAnimator());
        cardStackView.swipe();
    }

    private void swipeCard(Direction direction){
        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                .setDirection(direction)
                .setDuration(Duration.Slow.duration)
                .build();
        manager.setSwipeAnimationSetting(setting);
        cardStackView.swipe();
    }


    private void initButtonGroup(View view) {
        likeBtn = view.findViewById(R.id.likeBtn);
        dislikeBtn = view.findViewById(R.id.dislikeBtn);
        profileBtn = view.findViewById(R.id.profileBtn);
        likeBtn.setOnClickListener(v -> {
            onReactionClick(true);
        });
        dislikeBtn.setOnClickListener(v -> {
            onReactionClick(false);
        });
        profileBtn.setOnClickListener(v -> {
            onProfileBtnClick();
        });
    }

    public void onReactionClick(boolean isLiked) {
        if(adapter.getItems().isEmpty())
            return;
        Direction dir = isLiked?Direction.Right : Direction.Left;
        ImageView likeImg = manager.getTopView().findViewById(R.id.likeImg),
                dislikeImg = manager.getTopView().findViewById(R.id.dislikeImg);

        if (dir == Direction.Right){
            likeImg.setVisibility(View.VISIBLE);
            dislikeImg.setVisibility(View.INVISIBLE);
        }
        else if (dir == Direction.Left){
            dislikeImg.setVisibility(View.VISIBLE);
            likeImg.setVisibility(View.INVISIBLE);
        }
        this.swipeCard(dir);
    }

    public void onProfileBtnClick() {
        if(adapter.getItems().isEmpty())
            return;
        UserSimple userSimple = adapter.getItems().get(manager.getTopPosition());
        String userID = userSimple.getUserID();
        App.startActivityViewProfile(this.requireContext(), userID);
    }


}