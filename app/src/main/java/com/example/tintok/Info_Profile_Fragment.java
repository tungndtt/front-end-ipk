package com.example.tintok;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tintok.DataLayer.DataRepository_Interest;
import com.example.tintok.DataLayer.ResponseEvent;
import com.example.tintok.Model.UserProfile;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class Info_Profile_Fragment extends Fragment {

    public static final String INTEREST_FRAGMENT = "interest_fragment";
    private MainPages_MyProfile_ViewModel mViewModel;
    private TextView mEmailTextView, mAgeTextView, mBirthdayTextView, mInterestsTV;
    private Spinner mGenderSpinner;
    private EditText mDescriptionEditText;
    View view;
    private UserProfile user;
    private ProgressBar mProgressBar;
    private MaterialButton saveBtn, cancelBtn, interestBtn;
    private DatePickerDialog.OnDateSetListener  mOnDataSetListener;
    private DateTimeFormatter formatter;
    private int day, year, month;
    private String interests;
    private DialogFragment interestFragment;




    public Info_Profile_Fragment() {
        // Required empty public constructor
    }

    public static Info_Profile_Fragment getInstance() {
        Info_Profile_Fragment fragment = new Info_Profile_Fragment();
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
        Log.i("INFO", "Creating view for info profile ...");
        view = inflater.inflate(R.layout.profile_info_fragment, container, false);
        initViews();
        return view;
    }

    private void initViews() {
        mEmailTextView = view.findViewById(R.id.profile_email);
        mAgeTextView = view.findViewById(R.id.profile_age);
        mGenderSpinner = view.findViewById(R.id.profile_gender);
        mDescriptionEditText = view.findViewById(R.id.profile_description);
        mProgressBar = view.findViewById(R.id.profile_progressBar);
        saveBtn = view.findViewById(R.id.profile_edit_profile_button);
        cancelBtn = view.findViewById(R.id.profile_cancel_profile_button);
        mBirthdayTextView = view.findViewById(R.id.profile_birthday);
        mInterestsTV = view.findViewById(R.id.profile_interest);
        interestBtn = view.findViewById(R.id.profile_interests_btn);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mViewModel == null){
            mViewModel = new ViewModelProvider(getParentFragment()).get(MainPages_MyProfile_ViewModel.class);
        }

        formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        Log.e("viewmodel info", mViewModel.toString() );

        mViewModel.getUserProfile().observe(getViewLifecycleOwner(), userProfile -> {
            Log.e("OnChange", "Info");
            if(userProfile.getBirthday().toString().isEmpty()){
                mAgeTextView.setText("0");
                mBirthdayTextView.setHint("pick your birthday");
                mBirthdayTextView.setText("");
            }else{
                mAgeTextView.setText(Integer.toString(userProfile.getAge()));
                mBirthdayTextView.setText(formatter.format(userProfile.getBirthday()));
            }
            if(userProfile.getDescription() == null || userProfile.getDescription().isEmpty())
                mDescriptionEditText.setHint(R.string.inspirational_quote);
            else mDescriptionEditText.setText(userProfile.getDescription());
        });
        mViewModel.getIsUserUpdating().observe(getViewLifecycleOwner(), aBoolean ->  {
            if(aBoolean)
                mProgressBar.setVisibility(View.VISIBLE);
            else{mProgressBar.setVisibility(View.INVISIBLE);}
        });

        mViewModel.getNetworkResponse().observe(getViewLifecycleOwner(), responseEvent -> {
            if(responseEvent.getType() == ResponseEvent.Type.USER_UPDATE){
                String response = responseEvent.getContentIfNotHandled();
                if(response != null && response.equals("Created"))
                    Snackbar.make(view, "Updated", Snackbar.LENGTH_LONG).show();
                if(response != null && response.equals("Forbidden"))
                    Snackbar.make(view, "Your changes could not be saved ", Snackbar.LENGTH_LONG).show(); // error textview
            }
        });

        mViewModel.getUserProfile().getValue().getUserInterests().observe(getViewLifecycleOwner(), integers -> {
            interests="";
            for(int i=0; i<integers.size();i++)
                interests += DataRepository_Interest.interests[integers.get(i)] + " ";
            mInterestsTV.setText(interests);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        user = mViewModel.getUserProfile().getValue();
        mEmailTextView.setText(user.getEmail());
        mAgeTextView.setText(Integer.toString(user.getAge()));
        mBirthdayTextView.setText(formatter.format(user.getBirthday()));
        interestFragment = Interest_UpdateUser_Fragment.newInstance(mViewModel);


        mBirthdayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault()));//"UTC"
                if(mBirthdayTextView.getText().toString().isEmpty()){
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    day = calendar.get(Calendar.DAY_OF_MONTH);
                }else {
                    year = user.getBirthday().getYear();
                    month = user.getBirthday().getMonthValue();
                    day = user.getBirthday().getDayOfMonth();
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mOnDataSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(1904);
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTime().getTime());
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        mOnDataSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String day = String.valueOf(dayOfMonth);
                String newMonth = String.valueOf(month +1);
                if(dayOfMonth < 10)
                    day = "0"+day;
                if(month+1 < 10)
                    newMonth = "0"+newMonth;
                Log.e("date", year + " " + day + " " +newMonth);
                mBirthdayTextView.setText(day + "." + newMonth  + "." + year);
                mViewModel.setInfoIsEdited(true);
            }
        };

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGenderSpinner.setAdapter(adapter);
        setCurrentGenderSpinner(user);
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = position+1;
                if(mViewModel.getUserProfile().getValue().getGender() != null && mViewModel.getUserProfile().getValue().getGender().getI() != pos)
                    mViewModel.setInfoIsEdited(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(user.getDescription() == null ||user.getDescription().isEmpty())
            mDescriptionEditText.setHint(R.string.inspirational_quote);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleInput();
            }
        });
        cancelBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfile currentUser = mViewModel.getUserProfile().getValue();
                mViewModel.getUserProfile().setValue(currentUser);
                mViewModel.setUsername(currentUser.getUserName());
                mViewModel.setLocation(currentUser.getLocation());
                mViewModel.setInfoIsEdited(false);
            }
        }));

        interestBtn.setOnClickListener(v -> {
            interestFragment.show(getActivity().getSupportFragmentManager(), INTEREST_FRAGMENT);
        });


        /* save configuration if user wants to swap
        if(mViewModel.getInfoIsEdited().getValue()) {

            new MaterialAlertDialogBuilder(this.getContext())
                    .setTitle("Save")
                    .setMessage("Your current changes will be lost")
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.e("cancel", "what now?");
                            return;
                        }
                    })
                    .setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.e("continuer", "asdf");
                            UserProfile currentUser = mViewModel.getUserProfile().getValue();
                            mViewModel.getUserProfile().setValue(currentUser);
                            mViewModel.setUsername(currentUser.getUserName());
                            mViewModel.setLocation(currentUser.getLocation());
                            mViewModel.setInfoIsEdited(false);
                        }})
                    .setPositiveButton("save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.e("save", "...");
                        }})
                    .show();
        }


         */

    }

    private void handleInput() {

        if(mViewModel.getUsername().getValue().isEmpty()){
            Snackbar.make(view, "Username cannot be empty",Snackbar.LENGTH_SHORT).show();
            return;
        }

        int newGender = mGenderSpinner.getSelectedItemPosition() + 1;
        String newLocation= mViewModel.getLocation().getValue();
        String newUsername = mViewModel.getUsername().getValue();
        String newDescription = mDescriptionEditText.getText().toString();
        LocalDate newDate = LocalDate.parse(mBirthdayTextView.getText().toString(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        UserProfile updatedUser = new UserProfile();
        updatedUser.setUserID(mViewModel.getUserProfile().getValue().getUserID());
        updatedUser.setUserName(newUsername);
        updatedUser.setGender(newGender);
        updatedUser.setLocation(newLocation);
        updatedUser.setBirthday(newDate);
        updatedUser.setDescription(newDescription);

        mViewModel.updateUserInfo(updatedUser);
        mViewModel.setInfoIsEdited(false);
    }

    private void setCurrentGenderSpinner(UserProfile user){
        if(user.getGender() != null){
            switch (user.getGender()){
                case MALE: mGenderSpinner.setSelection(0);
                    break;
                case FEMALE: mGenderSpinner.setSelection(1);
                    break;
                case DIVERS: mGenderSpinner.setSelection(2);
                    break;

            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("INFO", "Destroying view for info profile ...");
    }
}
