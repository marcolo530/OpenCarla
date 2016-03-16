package miraclegeneration.opencarla;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageButton;
public class login extends AppCompatActivity implements View.OnClickListener {
    //define activity_login layout variables
    ImageButton bLogin;
    EditText etUsername, etPassword;
    ImageButton registerLink;
    //define sharedpreferences
    UserLocal userLocalStore;

    //called when the activity created to check if the user logined jump to the MainActivity
    public void onStart(){
        super.onStart();
        //check the sharedpreferences
        if (authenticate()==true){
            //if user logined go to the MainActivity
            startActivity(new Intent(login.this, MainActivity.class));
        }
    }
    //method create the view of the login and initialize the variable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //initialize sharedpreferences
        userLocalStore =new UserLocal(this);
        //initialize variables
        bLogin=(ImageButton)findViewById(R.id.bLogin);
        registerLink=(ImageButton)findViewById(R.id.registerLink);
        etUsername= (EditText) findViewById(R.id.etUsername);
        etPassword= (EditText) findViewById(R.id.etPassword);
        //apply listener to the buttons
        bLogin.setOnClickListener(this);
        registerLink.setOnClickListener(this);
    }
    //when detected a click run this method
    @Override
    public void onClick(View v) {
        //identify which button is clicked
        switch(v.getId()){
            //when login button click
            case R.id.bLogin:
                //store the input in the login interface
                User user=new User(etUsername.getText().toString(),etPassword.getText().toString());
                //Check database the user whether exist and start the MainActivity if logined
                check(user);
                break;
            //when register button click
            case R.id.registerLink:
                //go to the ActivityRegister
                startActivity(new Intent(login.this , register.class));
                break;
        }
    }
    //method go the login
    private void check(User user) {
        //make connection to db and check the user exist or not
        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.fetchUserDataInBackground(user, new GetUserCallback() {
            //call back function run when the background method  finish
            @Override
            public void done(User returnedUser) {
                //no user found in db
                if (returnedUser == null) {
                    showErrorMessage();
                }
                //user found in the database
                else {
                    //write in the sharedpreferences
                    userLocalStore.storeUserData(returnedUser);
                    userLocalStore.setUserLoggedIn(true);
                    //go to the MainActivity
                    startActivity(new Intent(login.this, MainActivity.class));
                }
            }
        });
    }
    //method return the sharedpreference which show the user logined or not
    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }
    //method to show error message during login
    public void showErrorMessage(){
        AlertDialog.Builder dialogBuilder =new AlertDialog.Builder(login.this);
        dialogBuilder.setMessage("帳戶或密碼錯誤");
        dialogBuilder.setPositiveButton("ok", null);
        dialogBuilder.show();
    }
}

