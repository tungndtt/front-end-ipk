package com.example.tintok.CustomView;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.tintok.R;

public class MyDialogFragment extends DialogFragment {
    protected void setupFullscreen() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.FILL);
        window.setWindowAnimations(R.style.MyAnimation_Window);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
    }

    @Override
    public void onStart() {
        super.onStart();
        setupFullscreen();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
}
