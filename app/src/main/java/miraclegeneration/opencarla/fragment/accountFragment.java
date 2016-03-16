package miraclegeneration.opencarla.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ImageButton;

import miraclegeneration.opencarla.Car;
import miraclegeneration.opencarla.GetCarCallback;
import miraclegeneration.opencarla.GetUserCallback;
import miraclegeneration.opencarla.R;
import miraclegeneration.opencarla.ServerRequests;
import miraclegeneration.opencarla.User;
import miraclegeneration.opencarla.UserLocal;
import miraclegeneration.opencarla.login;

/**
 * Created by kenex on 10/1/2016.
 */
public class accountFragment extends Fragment implements View.OnClickListener {
    //define activity_login layout variables
    EditText username, password, email;
    RadioGroup sex;
    RadioButton radio_male;
    RadioButton radio_female;
    EditText phone;
    RadioGroup driver;
    RadioButton radio_isDriver;
    RadioButton radio_isNotDriver;
    EditText icon, credit_card, balance;
    ImageButton btnChange;
    //carOwner variables
    EditText etCarNumber;
    String carSeatNumber;
    String carModel;
    String carColor;
    //drop down menu
    Spinner spinner;
    Spinner spinner2;
    Spinner spinner3;
    //hiden layout only  enable if the user select is car owner
    GridLayout carDetailLayout;
    //define sharedpreferences
    UserLocal userLocalStore;
    User user;

    //method inflate the view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);//first parameter resource id, second is view group, third is attach to root or not

        //Set items to spinner spinner_carSeat
        String[] carSeatNumberitems = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, carSeatNumberitems);
        spinner = (Spinner) rootView.findViewById(R.id.spinner_carSeat);
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
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, carModelItems);
        spinner2 = (Spinner) rootView.findViewById(R.id.spinner_carModel);
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
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, carColorItems);
        spinner3 = (Spinner) rootView.findViewById(R.id.spinner_carColor);
        spinner3.setAdapter(adapter3);
        //setting of the on select listener
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

        //initialize variables
        carDetailLayout = (GridLayout) rootView.findViewById(R.id.carDetailLayout);
        //set the textview
        username = (EditText) rootView.findViewById(R.id.etUsername);
        password = (EditText) rootView.findViewById(R.id.etPassword);
        email = (EditText) rootView.findViewById(R.id.etEmail);
        sex = (RadioGroup) rootView.findViewById(R.id.radioSex);
        radio_male = (RadioButton) rootView.findViewById(R.id.radio_male);
        radio_female = (RadioButton) rootView.findViewById(R.id.radio_female);
        phone = (EditText) rootView.findViewById(R.id.etPhone);
        driver = (RadioGroup) rootView.findViewById(R.id.radioIsDriver);
        radio_isDriver = (RadioButton) rootView.findViewById(R.id.radio_isDriver);
        radio_isNotDriver = (RadioButton) rootView.findViewById(R.id.radio_isNotDriver);
        credit_card = (EditText) rootView.findViewById(R.id.paypal);
        etCarNumber = (EditText) rootView.findViewById(R.id.carNumber);
        //set listener
        btnChange = (ImageButton) rootView.findViewById(R.id.btnChange);
        btnChange.setOnClickListener(this);

        //get the preferenced user infor ( id+name+pw)
        userLocalStore =new UserLocal(getActivity());
        user = userLocalStore.getLoggedUser();
        //connect to the db and get the user information
        final ServerRequests serverRequests=new ServerRequests(getActivity());
        serverRequests.fetchUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                //no user found in db
                if (returnedUser == null) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    dialogBuilder.setMessage("資料錯誤,請重新登入");
                    dialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            userLocalStore.clearUserData();
                            userLocalStore.setUserLoggedIn(false);
                            startActivity(new Intent(getActivity(), login.class));
                        }
                    });
                } else {//found the user in the database
                    username.setText(returnedUser.getUsername());
                    password.setText(returnedUser.getPassword());
                    email.setText(returnedUser.getEmail());
                    if (returnedUser.getSex().contains("m")) {
                        sex.check(radio_male.getId());
                    } else {
                        sex.check(radio_female.getId());
                    }
                    phone.setText(returnedUser.getPhone());
                    if (returnedUser.getCar_owner()) {
                        driver.check(radio_isDriver.getId());
                    } else {
                        driver.check(radio_isNotDriver.getId());
                    }
                    credit_card.setText(returnedUser.getCredit_card());
                }
                if(radio_isDriver.isChecked()) {
                    //fetch car info
                    serverRequests.fetchCarDataInBackground(user, new GetCarCallback() {
                        @Override
                        public void done(Car returnCar) {
                            etCarNumber.setText(returnCar.getCarNumber());
                            spinner.setSelection(getIndex(spinner, returnCar.getCarSeatNumber()));
                            spinner2.setSelection(getIndex(spinner2, returnCar.getCarModel()));
                            spinner3.setSelection(getIndex(spinner3, returnCar.getCarColor()));
                        }
                    });
                }

                if (radio_isDriver.isChecked()) {
                    carDetailLayout.setAlpha(0f);
                    carDetailLayout.setVisibility(View.VISIBLE);
                    // Animate the content view to 100% opacity, and clear any animation
                    // listener set on the view.
                    carDetailLayout.animate()
                            .alpha(1f)
                            .setDuration(5)
                            .setListener(null);
                } else {
                    //hide the hidden layout
                    carDetailLayout.setVisibility(View.GONE);
                    etCarNumber.setText("");
                    spinner.setSelection(0);
                    spinner2.setSelection(0);
                    spinner3.setSelection(0);
                }
                //change car owner option if click
                driver.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.radio_isDriver:
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
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {
        //get the input
        Car car = new Car(null, null, null,null);
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());
        user.setEmail(email.getText().toString());
        if(radio_male.isChecked()){
            user.setSex("m");
        }else{
            user.setSex("f");
        }
        user.setPhone(phone.getText().toString());
        if(radio_isDriver.isChecked()){
            user.setCar_owner(true);
            car.setCarNumber(etCarNumber.getText().toString());
            car.setCarSeatNumber(carSeatNumber);
            car.setCarModel(carModel);
            car.setCarColor(carColor);
        }else{
            user.setCar_owner(false);
        }
        user.setCredit_card(credit_card.getText().toString());


        //connect to db
        ServerRequests serverRequests = new ServerRequests(getActivity());
        if(car.getCarNumber() != null){
            serverRequests.updateCarDataInBackground(user, car, new GetCarCallback() {
                @Override
                public void done(Car returnCar) {

                }
            });
        }
        //update user info
        serverRequests.updateUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(final User returnedUser) {
                userLocalStore.storeUserData(returnedUser);
                AlertDialog.Builder dialogBuilder =new AlertDialog.Builder(getActivity());
                dialogBuilder.setMessage("資料已更改");
                dialogBuilder.setPositiveButton("ok", null);
                dialogBuilder.show();
            }
        });
    }
    //private method of your class
    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }
}
