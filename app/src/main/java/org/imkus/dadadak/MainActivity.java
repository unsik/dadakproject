package org.imkus.dadadak;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private AccessToken mToken = null;

    int way =0;
    int login = 0;
    int count = 0;
    String id="";
    String name="";
    String email="";
    String mode="";
    Context mContext;



    TextView textView1;
    TextView clock;
    TextView nameView;
    TextView maxscore;



    public static final int UI_UPDATE = 100;
    public static final String KEY = "KEY";
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UI_UPDATE: {
                    //실행시킬내용
                    Calendar now = Calendar.getInstance();
                    int year = now.get(Calendar.YEAR);
                    int month = now.get(Calendar.MONTH)+1;
                    int date = now.get(Calendar.DATE);
                    int hour = now.get(Calendar.HOUR);
                    int min = now.get(Calendar.MINUTE);
                    int sec = now.get(Calendar.SECOND);
                    clock = (TextView) findViewById(R.id.clock);
                    way=sec%3;
                    clock.setText(month + "월 "+ date + "일 " + hour + "시 " + min + "분 " + sec + "초 ");


                }break;
                default:
                    break;
            }
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        mToken = AccessToken.getCurrentAccessToken();
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        LoginManager.getInstance().logOut();







        handler.sendEmptyMessage(UI_UPDATE);


        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {//로그인이 성공되었을때 호출
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code
                                        try {

                                            id = (String) response.getJSONObject().get("id");//페이스북 아이디값
                                            name = (String) response.getJSONObject().get("name");//페이스북 이름
                                            email = (String) response.getJSONObject().get("email");//이메일
                                            mode="2";
                                            login=1;
                                            String time = clock.getText().toString();
                                            insertToDatabase(name, id, Integer.toString(count),time,mode);

                                            textView1 = (TextView) findViewById(R.id.textView9);
                                            nameView = (TextView) findViewById(R.id.nameView);
                                            nameView.setText("Welcome " + name);


                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }

                                        // new joinTask().execute(); //자신의 서버에서 로그인 처리를 해줍니다

                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(MainActivity.this, "로그인을 취소 하였습니다!", Toast.LENGTH_SHORT).show();
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(MainActivity.this, "에러가 발생하였습니다", Toast.LENGTH_SHORT).show();
                        // App code
                    }
                });








        FrameLayout layout1 = (FrameLayout) findViewById(R.id.frameLayout1);

        layout1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                handler.sendEmptyMessage(UI_UPDATE);

                if(login==1)
                {
                    if(way==0)
                    {
                        count++;
                    }
                    else
                    {

                        String time = clock.getText().toString();
                        mode="1";
                        insertToDatabase(name, id, Integer.toString(count),time,mode);

                    }
                    textView1.setText(String.valueOf(count));

                }
                else
                {
                    textView1 = (TextView) findViewById(R.id.textView9);
                    nameView = (TextView) findViewById(R.id.nameView);
                    nameView.setText("Please Login");
                }





                return false;
            }
        });

        FrameLayout layout2 = (FrameLayout) findViewById(R.id.frameLayout2);

        layout2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                handler.sendEmptyMessage(UI_UPDATE);
                if(login==1)
                {
                    if(way==1)
                    {
                        count++;
                    }
                    else
                    {

                        String time = clock.getText().toString();
                        mode="1";
                        insertToDatabase(name, id, Integer.toString(count),time,mode);
                    }
                    textView1.setText(String.valueOf(count));
                }
                else
                {
                    textView1 = (TextView) findViewById(R.id.textView9);
                    nameView = (TextView) findViewById(R.id.nameView);
                    nameView.setText("Please Login");
                }


                return false;
            }
        });

        FrameLayout layout3 = (FrameLayout) findViewById(R.id.frameLayout3);

        layout3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                handler.sendEmptyMessage(UI_UPDATE);
                if(login ==1)
                {
                    if(way==2)
                    {
                        count++;
                    }
                    else
                    {

                        String time = clock.getText().toString();
                        mode="1";
                        insertToDatabase(name, id, Integer.toString(count),time,mode);

                    }
                    textView1.setText(String.valueOf(count));
                }
                else
                {
                    textView1 = (TextView) findViewById(R.id.textView9);
                    nameView = (TextView) findViewById(R.id.nameView);
                    nameView.setText("Please Login");
                }



                return false;
            }
        });



    }

    private void insertToDatabase(String name, String id, String score,String time,String mode){

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;



            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                try{
                    String name = (String)params[0];
                    String id = (String)params[1];
                    String score = (String)params[2];
                    String time = (String)params[3];
                    String mode = (String)params[4];

                    String link="http://52.197.132.120/phptest/insert0.php";
                    String data  = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                    data += "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("score", "UTF-8") + "=" + URLEncoder.encode(score, "UTF-8");
                    data += "&" + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8");
                    data += "&" + URLEncoder.encode("mode", "UTF-8") + "=" + URLEncoder.encode(mode, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }

            }
        }

        InsertData task = new InsertData();
        task.execute(name,id,score,time,mode);


        count=0;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        Log.d("myLog"  ,"requestCode  "  + requestCode);
        Log.d("myLog"  ,"resultCode"  + resultCode);
        Log.d("myLog"  ,"data  "  + data.toString());



    }







}
