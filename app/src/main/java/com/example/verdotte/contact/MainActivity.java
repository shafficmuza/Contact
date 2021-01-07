package com.example.verdotte.contact;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btn,send;
    EditText name,number,message;
    private final int REQUEST_CODE=99;
    String numero = "";
    String nom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (EditText) findViewById(R.id.name);
        number = (EditText)findViewById(R.id.number);
        message =(EditText)findViewById(R.id.message);
        btn = (Button)findViewById(R.id.btn);
        send = (Button)findViewById(R.id.send);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = name.getText().toString().trim();
                String b = number.getText().toString().trim();
                String msg = message.getText().toString().trim();

                sendSms(b,msg);

            }
        });
        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case(REQUEST_CODE):
                if (resultCode == Activity.RESULT_OK){
                    Uri contactData = data.getData();
                    Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
                    if (cursor.moveToFirst()){
                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (Integer.valueOf(hasNumber) == 1){
                            Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                            while (numbers.moveToNext()) {
                                numero = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                nom = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                name.setText(nom);
                                number.setText(numero);
                            }

                        }

                    }
                    break;
                }


        }
    }

    public void sendSms(String strNumber, String strMeassage) {
        try {
            // Construct data
            String apiKey = "apikey=" + "jUbtvfVtzLI-FoQ4ORrOSKD1Kwf9QbGmr3HnqD5Iob";
            String message = "&message=" + strMeassage;
            String sender = "&sender=" + "Verdotte Aututu";
            String numbers = "&numbers=" + strNumber;

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.txtlocal.com/send/?").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
               // stringBuffer.append(line);
                Toast.makeText(getApplicationContext(), "message is" +line, Toast.LENGTH_LONG).show();
            }
            rd.close();

          //  return stringBuffer.toString();
        } catch (Exception e) {
            System.out.println("Error SMS "+e);
           // return "Error "+e;
            Toast.makeText(getApplicationContext(), "Error" +e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }




}
