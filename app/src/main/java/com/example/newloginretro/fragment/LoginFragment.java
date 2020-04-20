package com.example.newloginretro.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.newloginretro.R;
import com.example.newloginretro.activity.MainActivity;
import com.example.newloginretro.model.UserTenent;
import com.example.newloginretro.services.MyInterface;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private MyInterface loginFromActivityListener;
    private TextView registerTV;

    private EditText emailInput, passwordInput;
    private Button loginBtn;

    public LoginFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        // for login
        emailInput = view.findViewById(R.id.editText2);
        passwordInput = view.findViewById(R.id.editText3);
        loginBtn = view.findViewById(R.id.button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        registerTV = view.findViewById(R.id.textView9);
        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFromActivityListener.register();
            }
        });
        return view;
    } //ending onCreateView

    private void loginUser() {
        String Email = emailInput.getText().toString();
        String Password = passwordInput.getText().toString();

        if (TextUtils.isEmpty(Email)){
            MainActivity.appPreference.showToast("Your email is required.");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            MainActivity.appPreference.showToast("Invalid email");
        } else if (TextUtils.isEmpty(Password)){
            MainActivity.appPreference.showToast("Password required");
        } else if (Password.length() < 6){
            MainActivity.appPreference.showToast("Password  may be at least 6 characters long.");
        } else {
            Call<UserTenent> userCall = MainActivity.serviceApi.doLogin(Email, Password);
            userCall.enqueue(new Callback<UserTenent>() {
                @Override
                public void onResponse(Call<UserTenent> call, Response<UserTenent> response) {
                    if (response.body().getResponse().equals("data")){
                        MainActivity.appPreference.setLoginStatus(true); // set login status in sharedPreference
                        loginFromActivityListener.login(
                                response.body().getName(),
                                response.body().getEmail(),
                                response.body().getCreatedAt());
                    } else if (response.body().getResponse().equals("login_failed")){
                        MainActivity.appPreference.showToast("Error. Login Failed");
                        emailInput.setText("");
                        passwordInput.setText("");
                    }
                }
                @Override
                public void onFailure(Call<UserTenent> call, Throwable t) {
                }
            });
        }
    } //ending loginUser()

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        loginFromActivityListener = (MyInterface) activity;
    }

}
