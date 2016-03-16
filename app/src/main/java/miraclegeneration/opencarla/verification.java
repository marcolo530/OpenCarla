package miraclegeneration.opencarla;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class verification extends AppCompatActivity  implements View.OnClickListener {
    TextView codeField;
    int code;
    public static final String SERVER_ADDRESS = "http://fypcarpool.netne.net/"; ///your localhost server address
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        ImageButton confirm = (ImageButton) findViewById(R.id.confirm);
        ImageButton resend = (ImageButton) findViewById(R.id.resend);
        confirm.setOnClickListener(this);
        resend.setOnClickListener(this);
        //random a four digit verification code
        code = (int)(Math.random()*9000)+1000;
        codeField = (TextView) findViewById(R.id.editText);
    }

    public void toastMessage(String message) {
        Toast success = Toast.makeText(this, message, Toast.LENGTH_LONG);
        success.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                Intent i = getIntent();
                final User user = (User) i.getSerializableExtra("userObject");
                final Car car = (Car) i.getSerializableExtra("carObject");

                final ServerRequests serverRequests = new ServerRequests(this);
                if ((codeField.getText().toString()).equals(String.valueOf(code))){
                    //enter correct code
                    //create the user account
                    serverRequests.storeUserDataInBackground(user, new GetUserCallback() {
                    @Override
                    public void done(User returnedUser) {
                        //done with the user
                    }
                });
                    //if the user is car owner
                    if(user.getCar_owner()){
                        //set  the user_id
                        serverRequests.fetchUserDataInBackground(user, new GetUserCallback() {
                            @Override
                            public void done(User returnedUser) {
                                user.setUser_id(returnedUser.getUser_id()) ;
                            }
                        });
                        //create the car account
                        serverRequests.storeCarDataInBackground(user, car, new GetUserCallback() {
                            @Override
                            public void done(User returnedUser) {
                                //done with the car table
                            }
                        });
                    }
                    startActivity(new Intent(verification.this, login.class));
                    toastMessage("成功註冊");
                }
                else {
                    toastMessage("驗證碼錯誤");
                }
                break;

            case R.id.resend:
                code = (int) (Math.random() * 9999);
                new sendSMS().execute();
                break;
        }
    }

    public void onStart() {
        super.onStart();
        new sendSMS().execute();
    }

    public class sendSMS extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Intent i = getIntent();
            String phoneNumber = (String) i.getSerializableExtra("phoneNumber");

            String link = SERVER_ADDRESS+"nexmo/test.php?message="+code +"&phone=" +phoneNumber;

            HttpURLConnection conn = null;
            try {
                URL obj = new URL(link);
                conn = (HttpURLConnection) obj.openConnection();

                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}