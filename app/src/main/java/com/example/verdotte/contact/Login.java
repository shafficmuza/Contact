package com.example.verdotte.contact;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    Button login;
    EditText username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.log);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = username.getText().toString().trim();
                String passw = password.getText().toString().trim();

                if (user.equals("ignoto") && passw.equals("papa123")){
                    Intent myIntent = new Intent(Login.this, MainActivity.class);
                    startActivity(myIntent);

                }else{
                    Toast.makeText(getApplicationContext(), "wrong details", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
