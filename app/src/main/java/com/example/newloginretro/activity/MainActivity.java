package com.example.newloginretro.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.newloginretro.R;
import com.example.newloginretro.constants.Constant;
import com.example.newloginretro.extras.AppPreference;
import com.example.newloginretro.fragment.LoginFragment;
import com.example.newloginretro.fragment.ProfileFragment;
import com.example.newloginretro.fragment.RegistrationFragment;
import com.example.newloginretro.services.MyInterface;
import com.example.newloginretro.services.RetrofitClient;
import com.example.newloginretro.services.ServiceApi;
import com.example.newloginretro.services.ServiceApiT;

public class MainActivity extends AppCompatActivity implements MyInterface {
    public static AppPreference appPreference;
    public static String c_date;
    FrameLayout container_layout;
    public static ServiceApi serviceApi;
    public static ServiceApi serviceApiT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container_layout = findViewById(R.id.fragment_container);
        appPreference = new AppPreference(this);

        serviceApi = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL).create(ServiceApi.class);
        serviceApiT = (ServiceApi) RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL).create(ServiceApiT.class);


        if (container_layout != null) {
            if (savedInstanceState != null) {
                return;
            }
            //check login status from sharedPreference
            if (appPreference.getLoginStatus()) {
                //when true
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, new ProfileFragment())
                        .commit();
            } else {
                // when get false
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, new LoginFragment())
                        .commit();
            }
        }
    }

    @Override
    public void register() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new RegistrationFragment())
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void login(String name, String email, String created_at) {
        appPreference.setDisplayName(name);
        appPreference.setDisplayEmail(email);
        appPreference.setCreDate(created_at);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new ProfileFragment())
                .commit();

    }

    @Override
    public void logout() {
        appPreference.setLoginStatus(false);
        appPreference.setDisplayName("Name");
        appPreference.setDisplayEmail("Email");
        appPreference.setCreDate("DATE");

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .commit();

    }
}
