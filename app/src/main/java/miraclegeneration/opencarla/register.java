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
import android.widget.ImageButton;

import java.io.Serializable;

public class register extends AppCompatActivity implements View.OnClickListener {
    //define activity_login layout variables
    EditText etUsername, etPassword, etEmail;
    String sex;
    EditText etPhone,etCredit_card;
    boolean carOwner = false;
    //carOwner variables
    EditText etCarNumber;
    String carSeatNumber;
    String carModel;
    String carColor;
    //radio button
    private RadioGroup radioSexGroup;
    private RadioGroup radioIsDriver;
    //drop down menu
    Spinner spinner;
    Spinner spinner2;
    Spinner spinner3;
    //hiden layout only  enable if the user select is car owner
    GridLayout carDetailLayout;
    ImageButton bRegister;
    //called when the activity created to check if the user logined jump to the MainActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Set items to spinner spinner_carSeat
        String[] carSeatNumberitems = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, carSeatNumberitems);
        spinner = (Spinner) findViewById(R.id.spinner_carSeat);
        spinner.setAdapter(adapter);
        //setting of the on select listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedTextView = (TextView) view;
                carSeatNumber = selectedTextView.getText().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //Set items to spinner spinner_carModel
        String[] carModelItems = new String[]{"no", "BMW", "Benz", "Ferrari", "Porsche", "Ford", "Honda", "Mitsubishi", "Toyota", "Lexus", "Bentley"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, carModelItems);
        spinner2 = (Spinner) findViewById(R.id.spinner_carModel);
        spinner2.setAdapter(adapter2);
        //setting of the on select listener
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedTextView = (TextView) view;
                carModel = selectedTextView.getText().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //Set items to spinner spinner_carColor
        String[] carColorItems = new String[]{"", "Black", "Blue", "Red", "Yellow", "Silver", "Brown", "Orange ", "Peach", "Golden", "Green"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, carColorItems);
        spinner3 = (Spinner) findViewById(R.id.spinner_carColor);
        spinner3.setAdapter(adapter3);
        //setting of the on select listener
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedTextView = (TextView) view;
                carColor = selectedTextView.getText().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        //initialize variables
        carDetailLayout = (GridLayout) findViewById(R.id.carDetailLayout);
        //normal user variables
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPhone = (EditText) findViewById(R.id.etPhone);
        //car owner user variable
        etCarNumber = (EditText) findViewById(R.id.carNumber);
        etCredit_card= (EditText) findViewById(R.id.credit_card);
        bRegister = (ImageButton) findViewById(R.id.bRegister);
        //set on click listener
        bRegister.setOnClickListener(this);
        //Sex radio button,save result in sex
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_male:
                        sex = "m";
                        break;
                    case R.id.radio_female:
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
                        //hide the hidden layout
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
                //set the string inside edit text to string format
                String name = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String email = etEmail.getText().toString();
                String phone = etPhone.getText().toString();
                String credit_card =etCredit_card.getText().toString();
                String carNumber = etCarNumber.getText().toString();

                //check the field is blank or not
                if (name.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty() || credit_card.isEmpty()) {
                    //empty field found
                    toastMessage("基本資料未填");
                } else {
                    if(carOwner == true)
                    {
                        if(carNumber.isEmpty() || carModel.isEmpty() || carSeatNumber.isEmpty() || carColor.isEmpty()){
                            toastMessage("車輛資料未填");
                        }
                        else{
                            //create the new use and car
                            User user = new User(name, password, email, sex, phone, carOwner, null, credit_card);
                            Car car = new Car(carNumber, carModel, carColor, carSeatNumber);
                            registerUserCar(user, car);
                        }
                    }
                    else{   //not a car owner
                        //create the user
                        User user = new User(name, password, email, sex, phone, carOwner, null, credit_card);
                        registerUser(user);
                    }
                }
                break;
        }
    }

    public void toastMessage(String message) {
        Toast success = Toast.makeText(this, message, Toast.LENGTH_LONG);
        success.show();
    }
    //method to register a normal user
    private void registerUser(User user) {
        toastMessage("你將會收到一個SMS,請把驗證碼輸入");
        Intent myIntent = new Intent(register.this, verification.class);
        myIntent.putExtra("userObject", (Serializable) user);
        myIntent.putExtra("phoneNumber", (Serializable)etPhone.getText().toString());
        startActivity(myIntent);
    }
    //method to register a car owner
    private void registerUserCar(User user, Car car) {
        toastMessage("你將會收到一個SMS,請把驗證碼輸入");
        Intent myIntent = new Intent(register.this, verification.class);
        myIntent.putExtra("userObject", (Serializable) user);
        myIntent.putExtra("carObject", (Serializable) car);
        myIntent.putExtra("phoneNumber", (Serializable)etPhone.getText().toString());
        startActivity(myIntent);
    }
}
