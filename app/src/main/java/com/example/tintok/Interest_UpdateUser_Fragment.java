package com.example.tintok;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tintok.Adapters_ViewHolder.InterestAdapter;
import com.example.tintok.CustomView.MyDialogFragment;
import com.example.tintok.DataLayer.DataRepository_Interest;
import com.google.android.material.button.MaterialButton;

public class Interest_UpdateUser_Fragment extends MyDialogFragment {

    private View view;
    private RecyclerView recyclerView;
    private MaterialButton backBtn, saveBtn;
    private MainPages_MyProfile_ViewModel mViewModel;
    private InterestAdapter interestAdapter;
    private TextView errorTV;

    public Interest_UpdateUser_Fragment(MainPages_MyProfile_ViewModel mViewModel) {
        this.mViewModel = mViewModel;
    }

    public static Interest_UpdateUser_Fragment newInstance(MainPages_MyProfile_ViewModel mViewModel) {
        Interest_UpdateUser_Fragment fragment = new Interest_UpdateUser_Fragment(mViewModel);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_interest, container, false);
        recyclerView = view.findViewById(R.id.interest_RV);
        backBtn = view.findViewById(R.id.interest_backBtn);
        saveBtn = view.findViewById(R.id.interest_saveBtn);
        errorTV = view.findViewById(R.id.interest_error);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(mViewModel == null)
            mViewModel = new ViewModelProvider(this).get(MainPages_MyProfile_ViewModel.class);

        interestAdapter = new InterestAdapter(getContext(), DataRepository_Interest.getInterestArrayList());

        interestAdapter.setOnInterestClickListener(position -> {
            interestAdapter.getItems().get(position).setSelected(!(interestAdapter.getItems().get(position).isSelected()));
        });

        recyclerView.setAdapter(interestAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        saveBtn.setOnClickListener(v -> {

         //   if(...) size() ==0 or isEmty()){
                errorTV.setText("please choose some interest");
                errorTV.setVisibility(View.VISIBLE);
                return;




         //   }
            //   mViewModel.updateUserInterests()






        });


    }
}