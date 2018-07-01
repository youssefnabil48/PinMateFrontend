package com.example.saraelsheemi.pinmate.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.saraelsheemi.pinmate.R;

/**
 * Created by Sara ElSheemi on 5/18/2018.
 */

public class AccountSettingsFragment extends Fragment implements View.OnClickListener{
    Button changePW, deactivateAcc, linkedAcc, blockedUsers;
    Fragment fragment = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_account_settings,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        getActivity().setTitle("Account Settings");
    }

    private void init(View view) {
        changePW =  view.findViewById(R.id.btn_change_pw);
        changePW.setOnClickListener(this);
        deactivateAcc =  view.findViewById(R.id.btn_deactivate);
        deactivateAcc.setOnClickListener(this);
        linkedAcc =  view.findViewById(R.id.btn_linked_acc);
        linkedAcc.setOnClickListener(this);
        blockedUsers =  view.findViewById(R.id.btn_blocked_users);
        blockedUsers.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_change_pw : {
                fragment = new ChangePWFragment();
            }
                break;
            case R.id.btn_deactivate :
                break;
            case R.id.btn_linked_acc :
                break;
            case R.id.btn_blocked_users :
                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}
