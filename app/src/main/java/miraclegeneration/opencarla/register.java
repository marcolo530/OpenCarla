package miraclegeneration.opencarla;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class register extends AppCompatActivity implements View.OnClickListener {

    Button bRegister;
    private RadioGroup radioSexGroup;
    private RadioGroup radioIsDriver;

    EditText etUsername, etPassword, etEmail, etPhone, etCarNumber,etcredit_card;
    String carSeatNumber = "";
    String carModel = "";
    String carColor = "";
    boolean carOwner = false;
    String sex = "";
    Spinner spinner2;
    Spinner spinner;
    Spinner spinner3;

    GridLayout carDetailLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Set items to spinner spinner_carSeat
        String[] carSeatNumberitems = new String[]{"", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, carSeatNumberitems);
        spinner = (Spinner) findViewById(R.id.spinner_carSeat);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedTextView = (TextView) view;
                carSeatNumber = selectedTextView.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Set items to spinner spinner_carModel
        String[] carModelItems = new String[]{"", "BMW", "Benz", "Ferrari", "Porsche", "Ford", "Honda", "Mitsubishi", "Toyota", "Lexus", "Bentley"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, carModelItems);
        spinner2 = (Spinner) findViewById(R.id.spinner_carModel);
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedTextView = (TextView) view;
                carModel = selectedTextView.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Set items to spinner spinner_carColor
        String[] carColorItems = new String[]{"", "Black", "Blue", "Red", "Yellow", "Silver", "Brown", "Orange ", "Peach", "Golden", "Green"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, carColorItems);
        spinner3 = (Spinner) findViewById(R.id.spinner_carColor);
        spinner3.setAdapter(adapter3);

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedTextView = (TextView) view;
                carColor = selectedTextView.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        carDetailLayout = (GridLayout) findViewById(R.id.carDetailLayout);
        bRegister = (Button) findViewById(R.id.bRegister);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etCarNumber = (EditText) findViewById(R.id.carNumber);
        etcredit_card= (EditText) findViewById(R.id.credit_card);
        bRegister.setOnClickListener(this);
        //Sex radio button,save result in sex
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_male:
                        // do operations specific to this selection
                        sex = "m";

                        break;

                    case R.id.radio_female:
                        // do operations specific to this selection
                        sex = "f";

                        break;

                }
            }
        });
        //carOwner radio button ,save result in carOwner
        radioIsDriver = (RadioGroup) findViewById(R.id.radioIsDriver);
        radioIsDriver.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_isDriver:
                        // do operations specific to this selection
                        carOwner = true;
                        carDetailLayout.setAlpha(0f);
                        carDetailLayout.setVisibility(View.VISIBLE);

                        // Animate the content view to 100% opacity, and clear any animation
                        // listener set on the view.
                        carDetailLayout.animate()
                                .alpha(1f)
                                .setDuration(5)
                                .setListener(null);
                        break;

                    case R.id.radio_isNotDriver:
                        // do operations specific to this selection
                        carOwner = false;

                        carDetailLayout.setVisibility(View.GONE);
                        etCarNumber.setText("");
                        spinner.setSelection(0);
                        spinner2.setSelection(0);
                        spinner3.setSelection(0);
                        break;

                }
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bRegister:

                /* AlertDialog.Builder builer =new AlertDialog.Builder(this);
                 builer.setTitle("Test");
                 String temp ="username"+etUsername.getText().toString()+"password"+etPassword.getText().toString()+"email"+etEmail.getText()+"sex"+sex+"phone"+etPhone.getText().toString()+"is carOwner" +carOwner
                 +"carnumber"+ etCarNumber.getText().toString()+" carmodel"+carModel+"carColor"+carColor+"carSeat"+carSeatNumber;
                 builer.setMessage(temp);
                 AlertDialog alertDialog=builer.create();
                 alertDialog.show();*/
                String name = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String email = etEmail.getText().toString();
                String phone = etPhone.getText().toString();
                String carNumber = etCarNumber.getText().toString();
                String credit_card =etcredit_card.getText().toString();

                //if car Owner is false
                if (!name.equals("") && !password.equals("") && !email.equals("") && !phone.equals("") && carOwner == false && !credit_card.equals("")&& !sex.equals("")) {
                    User user = new User(name, password, email, sex, phone, carOwner,credit_card);

                    registerUser(user);

                } else if (!name.equals("") && !password.equals("") && !email.equals("") && !phone.equals("") && carOwner == true && !credit_card.equals("")&& !sex.equals("")) {
                    if (carNumber != "" && !carModel.equals("") && !carSeatNumber.equals("") && !carColor.equals("")) {


                        User user = new User(name, password, email, sex, phone, carOwner,credit_card, carNumber, carModel, carSeatNumber, carColor);

                        registerUser(user);

                    } else {

                        toastMessage("私家車資料未填");

                    }

                } else {

                    toastMessage("基本資料未填");
                }
                // startActivity(new Intent(register.this, login.class));
                break;

        }
    }

    public void toastMessage(String message) {
        Toast success = Toast.makeText(this, message, Toast.LENGTH_LONG);
        success.show();

    }

    private void registerUser(User user) {
        toastMessage("你將會收到一個SMS,請把驗證碼輸入");
        Intent myIntent = new Intent(register.this, verification.class);
        myIntent.putExtra("userObject", (Serializable) user);
        myIntent.putExtra("phoneNumber", (Serializable)etPhone.getText().toString());
        startActivity(myIntent);

    }


}
