package com.example.tintok;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tintok.Adapters_ViewHolder.InterestAdapter;
import com.example.tintok.DataLayer.DataRepository_Interest;
import com.example.tintok.Model.Interest;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Sign_up_Fragment extends Fragment implements Login_SignUp_ViewModel.requestListener {


    private Button registerButton, backBtn;
    private ProgressBar loadingBar;
    private TextView status;
    private EditText name, email,password, retypepassword, day,month,year;
    private Login_SignUp_ViewModel viewModel;
    private RadioGroup mGenderGroup;
    private TextView mInterestTV;
    private InterestAdapter interestAdapter;
    private String selectedInterest;
    private DialogFragment interestFragment;

    public static Sign_up_Fragment newInstance(Login_SignUp_ViewModel viewModel) {
        return new Sign_up_Fragment(viewModel);
    }

    public Sign_up_Fragment(Login_SignUp_ViewModel viewModel){
        this.viewModel = viewModel;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sign_up_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(viewModel == null){
            viewModel = new ViewModelProvider(this).get(Login_SignUp_ViewModel.class);
        }
        if(DataRepository_Interest.getInterestArrayList().size() == 0)
            DataRepository_Interest.initInterestArrayList();
        if(viewModel.getSelectedInterests().getValue() == null){

            //HashMap<Integer, Interest> selectedItems = new HashMap<>();
            boolean[] selectedItems = new boolean[(DataRepository_Interest.getInterestArrayList().size())];
            viewModel.setSelectedInterests(selectedItems);
            selectedInterest = "click here to choose your interests";
        }

        // TODO: Use the ViewModel
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onStart() {
        super.onStart();
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void init(){
        registerButton = getView().findViewById(R.id.sign_upButton);
        backBtn = getView().findViewById(R.id.register_backBtn);
        status = getView().findViewById(R.id.status);
        loadingBar = getView().findViewById(R.id.progressBar);
        name = getView().findViewById(R.id.nameInput);
        email = getView().findViewById(R.id.emailInput);
        password = getView().findViewById(R.id.passInput);
        retypepassword = getView().findViewById(R.id.passInputConfirm);
        day = getView().findViewById(R.id.dayofbirth_date);
        month = getView().findViewById(R.id.dayofbirth_month);
        year = getView().findViewById(R.id.dayofbirth_year);
        mGenderGroup = getView().findViewById(R.id.register_gender_group);
        mInterestTV = getView().findViewById(R.id.register_interests_inputTV);

        mInterestTV.setText(selectedInterest);

        viewModel.getSelectedInterests().observe(getViewLifecycleOwner(), booleans -> {
            Log.e("getSelec", "isSelected");
            selectedInterest= "";
            Log.e("length", String.valueOf(booleans.length));
            for(int i=0; i < booleans.length; i++){
                if(booleans[i]){
                    selectedInterest += DataRepository_Interest.interests[i].toLowerCase() + " ";
                }
            }
            if(selectedInterest.isEmpty())
                selectedInterest = "click here to choose your interests";
            mInterestTV.setText(selectedInterest);
        });


        interestAdapter = new InterestAdapter(getActivity(), DataRepository_Interest.getInterestArrayList());

        /*
        interestAdapter.setOnCheckboxListner(position -> {
            Log.e("sign_up", String.valueOf(interestAdapter.getItems().get(position).isSelected()));
            Interest tmp = interestAdapter.getItems().get(position);
            tmp.setSelected(!(tmp.isSelected()));
            HashMap<Integer, Interest> tmpItems = viewModel.getSelectedInterests().getValue();
            if(viewModel.getSelectedInterests().getValue().containsKey(tmp.getId())){
               tmpItems.remove(tmp.getId());
            }else tmpItems.put(tmp.getId(), tmp);
            viewModel.setSelectedInterests(tmpItems);
            Log.e("sign_up", String.valueOf(interestAdapter.getItems().get(position).isSelected()));
        });


         */
        registerButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
               HandleSignUp();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        mInterestTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                interestFragment = Interests_SignUp_Fragment.newInstance(viewModel);
                interestFragment.show(getChildFragmentManager(), "interests_signUp");
                /*
                View view = LayoutInflater.from(getContext()).inflate(R.layout.interest_recyclerview, null);

                RecyclerView interestList = view.findViewById(R.id.interest_list);

                interestList.setAdapter(interestAdapter);
                interestList.setLayoutManager(new LinearLayoutManager(getActivity()));
                MaterialAlertDialogBuilder alertDialogBuilder =  new MaterialAlertDialogBuilder(getActivity());
                alertDialogBuilder.setTitle("Interests")
                        .setMessage("Choose your interests")
                        .setView(view)
                        .setCancelable(false)
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() { //delete
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for(Interest interest: interestAdapter.getItems()){
                                    interest.setSelected(false);
                                }
                                interestAdapter.clearSelectedItems();
                                viewModel.setSelectedInterests(interestAdapter.getSelectedItems());
                                selectedInterest = "click here to choose your interests";
                                mInterestTV.setText(selectedInterest);
                            }
                        })
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("save", "...");
                                selectedInterest = "";
                                for(Interest interest :  interestAdapter.getSelectedItems().values())
                                    selectedInterest += interest.getInterest().toLowerCase() + " ";
                                viewModel.setSelectedInterests(interestAdapter.getSelectedItems());
                                mInterestTV.setText(selectedInterest);
                            }})
                        .create()
                        .show();

                 */
            }
        });


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void HandleSignUp(){
        Log.e("handle", "again");
        email.onEditorAction(EditorInfo.IME_ACTION_DONE);
        if(name.getText().toString().isEmpty()){
           // name.setError("Name cant be blank");
            setErrorStatus(R.string.error_username_empty);

            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()){
            setErrorStatus(R.string.error_email_invalid);
            return;
        }
        if(password.getText().toString().length() < 6){
            setErrorStatus(R.string.error_password_length);

            return;
        }

        int dayInt, monthInt, yearInt;
        if(day.getText().toString().isEmpty() || month.getText().toString().isEmpty()||year.getText().toString().isEmpty()){
            setErrorStatus(R.string.error_birthday_invalid);
            return;
        }
        try {
            dayInt = Integer.parseInt(day.getText().toString());
            monthInt = Integer.parseInt(month.getText().toString());
            yearInt = Integer.parseInt(year.getText().toString());
        }catch (Exception e){
            setErrorStatus(R.string.error_birthday_invalid);
            return;
        }
        if(year.getText().toString().length()!=4 || (dayInt <=0 || dayInt >31)
            || (monthInt<=0 || monthInt > 12)){
            setErrorStatus(R.string.error_birthday_invalid);
            return;
        }

        if(retypepassword.getText().toString().compareTo(password.getText().toString()) != 0){
           setErrorStatus(R.string.error_password_notMatching);
            return;
        }

        if(mGenderGroup.getCheckedRadioButtonId() == -1){
            setErrorStatus(R.string.error_gender_empty);
            return;
        }
        if(selectedInterest.equals("click here to choose your interests")){
            status.setVisibility(View.VISIBLE);
            status.setText("No interests selected");
            return;
        }

        status.setVisibility(View.VISIBLE);
        status.setText(R.string.registration_signUp);
        status.setTextColor(Color.BLACK);
        loadingBar.setVisibility(View.VISIBLE);
        /*
        JsonObject data = new JsonObject();
        data.addProperty("email", email.getText().toString());
        data.addProperty("password", password.getText().toString());
        data.addProperty("day_ofBirth", dayInt);
        data.addProperty("month_ofBirth", monthInt);
        data.addProperty("year_ofBirth", yearInt);
        data.addProperty("name", name.getText().toString());

         */
        String birthday = dayInt+"/"+monthInt+"/"+yearInt;
        int gender = mGenderGroup.indexOfChild(getView().findViewById(mGenderGroup.getCheckedRadioButtonId())) + 1;

        boolean[] interests = viewModel.getSelectedInterests().getValue();
        ArrayList<Integer> chosenInterests = new ArrayList<>();

        for(int i=0; i < interests.length; i++){
            if(interests[i])
                chosenInterests.add(i);
        }
        /*
        HashMap<Integer, Interest> result = viewModel.getSelectedInterests().getValue();
        int i = 0;
        for(Interest interest :  result.values()){
            Log.e("selected", String.valueOf(interest.getId()));
            interests[i] = interest.getId();
            i++;
        }

         */


        this.viewModel.signUpRequest(name.getText().toString(), email.getText().toString(), birthday, password.getText().toString(), gender, chosenInterests, this);
        /*
        communication.LoginRequest(data, new RestAPI_Entity.RestApiListener(){

            @Override
            public void onSuccess(RestAPI_Entity.AbstractResponseEntity response) {
                RestAPI_Entity.StatusResponseEntity res = (RestAPI_Entity.StatusResponseEntity)response;
                if(((RestAPI_Entity.StatusResponseEntity) res).status ){

                }
                else{

                }
            }

            @Override
            public void onFailure() {
                status.setVisibility(View.VISIBLE);
                status.setText("Signing up failed");
            }
        });
         */
    }

    private void setErrorStatus(int error){
        status.setVisibility(View.VISIBLE);
        status.setText(error);
        status.setTextColor(Color.RED);
    }


    @Override
    public void requestSuccess() {
        loadingBar.setVisibility(View.INVISIBLE);
        Snackbar.make(getView(), R.string.registration_email_sent, Snackbar.LENGTH_LONG);
        //TODO:
        getActivity().getSupportFragmentManager().popBackStack();//.beginTransaction().replace(R.id.fragment, )

    }

    @Override
    public void requestFail(String reason) {
       // setErrorStatus(R.string.error_registration_failed);
        status.setText(reason);
        loadingBar.setVisibility(View.INVISIBLE);
        //TODO: reset email textfield?
    }

    @Override
    public void connectionFail() {
        status.setVisibility(View.VISIBLE);
        status.setText(R.string.error_connection_failed);
        loadingBar.setVisibility(View.INVISIBLE);
    }
}