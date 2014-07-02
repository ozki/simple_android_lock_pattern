package com.asghonim.sample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.asghonim.salp.OnPasswordCompleteListener;
import com.asghonim.salp.PasswordGrid;


public class MainActivity extends ActionBarActivity implements OnPasswordCompleteListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((PasswordGrid)findViewById(R.id.password_grid)).setListener(this);
    }

    @Override
    public void onPasswordComplete(String s) {
        Toast.makeText(this, "Password: " + s, Toast.LENGTH_SHORT).show();
    }
}
