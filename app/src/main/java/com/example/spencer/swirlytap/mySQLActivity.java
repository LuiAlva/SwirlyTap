
package com.example.spencer.swirlytap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class mySQLActivity extends Activity
{
    EditText editName,Password;
    Button bSubmit;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_my_sql);
        editName = (EditText)findViewById(R.id.editName);
        Password = (EditText)findViewById(R.id.Password);
        bSubmit = (Button)findViewById(R.id. Submit);
        bSubmit.setOnClickListener(new View.OnClickListener(){
            InputStream is = null;

            @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
            @Override
            public void onClick(View arg0)
            {
                String name = editName.getText().toString();
                String password = Password.getText().toString();
                //String score = --+Score.getText().toString();

                List<NameValuePair> nameValuePairs;
                nameValuePairs = new ArrayList<>(1);
                nameValuePairs.add(new BasicNameValuePair("Name", name));
                nameValuePairs.add(new BasicNameValuePair("Password",password));

                //}
                try

                {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost("http://10.0.2.2/tutorial.php");


                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();
                    String msg = "Data entered successfully";
                    Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();

                }
                catch(ClientProtocolException e)

                {
                    Log.e("ClientProtocol", "Log_tag");
                    e.printStackTrace();
                }
                catch(IOException e)

                {
                    Log.e("Log_tag", "IOException");
                    e.printStackTrace();
                }






            }

        });


    }

}


