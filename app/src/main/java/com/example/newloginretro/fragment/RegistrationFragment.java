package com.example.newloginretro.fragment;


import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.newloginretro.R;
import com.example.newloginretro.activity.MainActivity;
import com.example.newloginretro.model.UserTenent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationFragment extends Fragment {

    private EditText nameInput, emailInput, phoneInput, passwordInput;
    Button regBtn;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_registration, container, false);
        nameInput = view.findViewById(R.id.tenenteditName);
        emailInput = view.findViewById(R.id.tenenteditEmail);
        phoneInput = view.findViewById(R.id.tenenteditMobile);
        passwordInput = view.findViewById(R.id.tenenteditpassword);
        regBtn = view.findViewById(R.id.tenentSubmit);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
                Log.e("reg button", "clicked");
            }
        });
        return view;
    }

    public void registerUser() {
        String name = nameInput.getText().toString();
        String email = emailInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (TextUtils.isEmpty(name)){
            MainActivity.appPreference.showToast("Your name is required.");
        } else if (TextUtils.isEmpty(email)){
            MainActivity.appPreference.showToast("Your email is required.");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            MainActivity.appPreference.showToast("Invalid email");
        } else if (TextUtils.isEmpty(password)){
            MainActivity.appPreference.showToast("Password required");
        } else if (password.length() < 6){
            MainActivity.appPreference.showToast("Create a password at least 6 characters long.");
        }
        else {
            Call<UserTenent> userTenentCall = MainActivity.serviceApi.doRegistration(name, email, phone, password);
            userTenentCall.enqueue(new Callback<UserTenent>() {
                @Override
                public void onResponse(Call<UserTenent> call, Response<UserTenent> response) {
                    if (response.body().getResponse().equals("inserted")){
                        Log.e("response", response.body().getResponse());
                        nameInput.setText("");
                        emailInput.setText("");
                        phoneInput.setText("");
                        passwordInput.setText("");
                        MainActivity.appPreference.showToast("Registered Successfully");
                    } else if (response.body().getResponse().equals("exists")){
                        MainActivity.appPreference.showToast("This email already exists");
                    } else if (response.body().getResponse().equals("error")){
                        MainActivity.appPreference.showToast("Oops! something went wrong.");
                    }
                }

                @Override
                public void onFailure(Call<UserTenent> call, Throwable t) {
                }
            });
        }

    }

}
