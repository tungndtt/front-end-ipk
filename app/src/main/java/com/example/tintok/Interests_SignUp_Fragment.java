package com.example.tintok;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tintok.Adapters_ViewHolder.InterestAdapter;
import com.example.tintok.DataLayer.DataRepository_Interest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class Interests_SignUp_Fragment extends DialogFragment {


    private View view;
    private RecyclerView recyclerView;
    private MaterialButton backBtn, saveBtn;
    private Login_SignUp_ViewModel mViewModel;
    private InterestAdapter interestAdapter;
    private TextView errorTV;

    public Interests_SignUp_Fragment(Login_SignUp_ViewModel viewModel) {
        this.mViewModel = viewModel;
        // Required empty public constructor
    }

    public static Interests_SignUp_Fragment newInstance(Login_SignUp_ViewModel viewModel) {
        Interests_SignUp_Fragment fragment = new Interests_SignUp_Fragment(viewModel);
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

        if (mViewModel == null)
            mViewModel = new ViewModelProvider(this).get(Login_SignUp_ViewModel.class);
        mViewModel.setSelectedInterests(new boolean[DataRepository_Interest.getInterestArrayList().size()]);

        interestAdapter = new InterestAdapter(this.getContext(), DataRepository_Interest.getDefaultArrayList());
        interestAdapter.setOnInterestClickListener(pos -> {
            interestAdapter.getItems().get(pos).setSelected(!interestAdapter.getItems().get(pos).isSelected());
            boolean[] tmp = mViewModel.getSelectedInterests().getValue();
            tmp[interestAdapter.getItems().get(pos).getId()] = !tmp[interestAdapter.getItems().get(pos).getId()];
            mViewModel.setSelectedInterests(tmp);
        });

        recyclerView.setAdapter(interestAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);

        backBtn.setOnClickListener(v -> {
            Log.e("back", "clicked");
            MaterialAlertDialogBuilder alertDialog =  new MaterialAlertDialogBuilder(getActivity());
            alertDialog.setTitle("Warning")
                        .setMessage("Your current changes will be lost. Do you want to save?")
                        .setPositiveButton("Save", (dialog, which) -> {
                            getDialog().dismiss();
                        }).setNegativeButton("Don't save", (dialog, which) -> {
                            mViewModel.setSelectedInterests(new boolean[DataRepository_Interest.getInterestArrayList().size()]);
                            getDialog().dismiss();
                        })
                    .show();
        });
        saveBtn.setOnClickListener(v -> {
            getDialog().dismiss();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.FILL);
        window.setWindowAnimations(R.style.MyAnimation_Window);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
    }

}
