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
    UserLocal userLocalStore;
    EditText etUsername, etPassword;
    TextView registerLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        userLocalStore =new UserLocal(this);

        bLogin=(Button)findViewById(R.id.bLogin);
        registerLink=(TextView)findViewById(R.id.bLogin);
        etUsername= (EditText) findViewById(R.id.etUsername);
        etPassword= (EditText) findViewById(R.id.etPassword);
        bLogin.setOnClickListener(this);
        registerLink.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.bLogin:
                
                User user=new User(etUsername.getText().toString(),etPassword.getText().toString());
                //Check database the user whether exist
                check(user);









                break;

            case R.id.registerLink:
                startActivity(new Intent(login.this , register.class));

                break;

        }
    }

    private void check(User user) {
        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.fetchUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {
                    showErrorMessage();

                } else {
                    userLocalStore.storeUserData(returnedUser);
                    userLocalStore.setUserLoggedIn(true);



                    startActivity(new Intent(login.this, MainActivity.class));

                }

            }
        });

    }


    public void onStart(){
        super.onStart();
        //If login already
        if (authenticate()==true){
            startActivity(new Intent(login.this, MainActivity.class));

        }


    }
    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();

    }


    public void showErrorMessage(){
        AlertDialog.Builder dialogBuilder =new AlertDialog.Builder(login.this);
        dialogBuilder.setMessage("帳戶或密碼錯誤");
        dialogBuilder.setPositiveButton("ok", null);
        dialogBuilder.show();

    }
}

