package com.example.tintok;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

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
}
