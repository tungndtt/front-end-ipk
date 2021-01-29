package com.example.tintok;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import android.widget.ImageView;
import android.widget.Toast;


import com.example.tintok.Adapters_ViewHolder.PeopleBrowsingAdapter;
import com.example.tintok.CustomView.AfterRefreshCallBack;
import com.example.tintok.CustomView.FilterDialogFragment;
import com.example.tintok.CustomView.Refreshable;
import com.example.tintok.Model.UserSimple;
import com.google.android.material.button.MaterialButton;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;

public class MainPages__PeopleBrowsing__Fragment extends Fragment implements Refreshable {

    private MainPages_PeopleBrowsing_ViewModel  mViewModel;
    MaterialButton filterBtn;
    CardStackView cardStackView;

    private int currentItem = 0;
    private int offScreenPageLimit = 2;

    PeopleBrowsingAdapter adapter;
    CardStackLayoutManager  manager;

    DialogFragment filterFragment = null;
    FilterDialogFragment.FilterState currentState ;

    public static MainPages__PeopleBrowsing__Fragment newInstance() {
        return new MainPages__PeopleBrowsing__Fragment();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mainpages__peoplebrowsing__fragment, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainPages_PeopleBrowsing_ViewModel.class);
        initFragment();
        this.setRetainInstance(true);
        // TODO: Use the ViewModel
        mViewModel.getMatchingPeople().observe(this.getViewLifecycleOwner(), userSimples -> {
            int currentPos = manager.getTopPosition();
            adapter.setItems(userSimples);
            cardStackView.smoothScrollToPosition(currentPos);
        });
        currentState = new FilterDialogFragment.FilterState();
        filterBtn.setOnClickListener(v -> {
            if(filterFragment != null)
                return;
            filterFragment = new FilterDialogFragment(currentState, state -> {
                filterFragment = null;
                if(state != null){
                    currentState = state;
                    mViewModel.submitFilter(currentState);
                }
                Log.e("Main_People_Frag", "ListenerCalled");
            });
            filterFragment.show(getChildFragmentManager(), "Filtering");
        });
    }

    /*int currentPos = 0;
    @Override
    public void onResume() {
        super.onResume();
        Log.e("People_Frag", "at :"+ this.currentPos);
        manager.setTopPosition(this.currentPos);
    }

    @Override
    public void onPause() {
        super.onPause();
        currentPos = manager.getTopPosition();
    }*/

    boolean isLiked;

    void initFragment(){
        filterBtn = getView().findViewById(R.id.filterBtn);
        cardStackView = getView().findViewById(R.id.card_stack_view);
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
                    isLiked = true;
                }
                if (direction == Direction.Top){

                }
                if (direction == Direction.Left){
                    dislikeImg.setVisibility(View.VISIBLE);
                    likeImg.setVisibility(View.INVISIBLE);
                    isLiked = false;
                }
                if (direction == Direction.Bottom){
                }
            }

            @Override
            public void onCardSwiped(Direction direction) {

            }

            @Override
            public void onCardRewound() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
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
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " );
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                mViewModel.submitPeopleReaction(adapter.getItems().get(position), isLiked);
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
        manager.setCanScrollVertical(false);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        adapter = new PeopleBrowsingAdapter(this.getActivity(), models);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.setItemAnimator(new DefaultItemAnimator());

    }


    @Override
    public void onRefresh(AfterRefreshCallBack e) {
        mViewModel.refreshData(e);
    }
}