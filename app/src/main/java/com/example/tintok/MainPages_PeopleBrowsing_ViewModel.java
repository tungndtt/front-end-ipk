package com.example.tintok;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.tintok.CustomView.AfterRefreshCallBack;
import com.example.tintok.CustomView.FilterDialogFragment;
import com.example.tintok.DataLayer.DataRepositoryController;
import com.example.tintok.Model.UserSimple;

import java.util.ArrayList;

public class MainPages_PeopleBrowsing_ViewModel extends AndroidViewModel {
    public MainPages_PeopleBrowsing_ViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ArrayList<UserSimple>> getMatchingPeople(){
        return DataRepositoryController.getInstance().getMatchingPeople();
    }

    public void refreshData(AfterRefreshCallBack e) {
    }

    public void submitPeopleReaction(UserSimple userSimple, boolean isLiked) {
        Log.e("PeopelBrVM", "new Reaction "+isLiked );
    }

    public void submitFilter(FilterDialogFragment.FilterState currentState) {
        String toPrint = "";
        for(int i  = 0; i<currentState.getInterestBitmap().length;i++)
            if(currentState.getInterestBitmap()[i]) toPrint+="1";
            else toPrint+="0";
        Log.e("PeopelBrVM", "new State: " + toPrint);
        DataRepositoryController.getInstance().findNewMatching(currentState);
    }
}
