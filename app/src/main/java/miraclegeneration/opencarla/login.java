package miraclegeneration.opencarla;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
public class login extends AppCompatActivity implements View.OnClickListener {
    Button bLogin;
    EditText etUsername, etPassword;
    TextView registerLink;
    //sharedpreferences
    UserLocal userLocalStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //sharedpreferences
        userLocalStore =new UserLocal(this);
        //assgin button
        bLogin=(Button)findViewById(R.id.bLogin);
        registerLink=(TextView)findViewById(R.id.bLogin);
        etUsername= (EditText) findViewById(R.id.etUsername);
        etPassword= (EditText) findViewById(R.id.etPassword);
        //listen to click
        bLogin.setOnClickListener(this);
        registerLink.setOnClickListener(this);
    }
    //method handle click
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //when login button click
            case R.id.bLogin:
                User user=new User(etUsername.getText().toString(),etPassword.getText().toString());//store input
                //Check database the user whether exist
                check(user);
                break;
            //when register button click
            case R.id.registerLink:
                startActivity(new Intent(login.this , register.class)); //go to register page
                break;
        }
    }
    //method check the input for login
    private void check(User user) {
        //make connection
        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.fetchUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                //no user found in db
                if (returnedUser == null) {
                    showErrorMessage();
                } else {
                    //write in the sharedpreferences
                    userLocalStore.storeUserData(returnedUser);
                    userLocalStore.setUserLoggedIn(true);
                    //go to the main page
                    startActivity(new Intent(login.this, MainActivity.class));
                }
            }
        });
    }
    //first check user login already or not
    public void onStart(){
        super.onStart();
        //If login already
        if (authenticate()==true){
            startActivity(new Intent(login.this, MainActivity.class));
        }
    }
    //method check user login or not
   private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }
    //method to show error message druing login
    public void showErrorMessage(){
        AlertDialog.Builder dialogBuilder =new AlertDialog.Builder(login.this);
        dialogBuilder.setMessage("帳戶或密碼錯誤");
        dialogBuilder.setPositiveButton("ok", null);
        dialogBuilder.show();
    }
}

