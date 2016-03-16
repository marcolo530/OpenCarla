package miraclegeneration.opencarla;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by marco on 15/1/2016.
 */
public class ServerRequests {
    //create the progress bar
    ProgressDialog progressDialog;
    //define the connection setting
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://fypcarpool.netne.net/";///your localhost address
    //define sharedpreferences
    UserLocal userLocalStore;
    //constructor set up the progress bar
    public ServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("處理中");
        progressDialog.setMessage("請稍後");
    }

    //method store data  to db in background
    public void storeCarDataInBackground(User user, Car car, GetUserCallback callBack) {
        progressDialog.show();
        new StoreCarDataAsyncTask(user, car, callBack).execute();
    }
    //method check data from db in background
    public void fetchCarDataInBackground(User user, GetCarCallback callBack) {
        progressDialog.show();
        new fetchCarDataInBackground(user, callBack).execute();
    }
    //method update data to db in background
    public void updateCarDataInBackground(User user, Car car, GetCarCallback callBack) {
        progressDialog.show();
        new UpdateCarDataAsyncTask(user, car, callBack).execute();
    }
    //method store data  to db in background
    public void storeUserDataInBackground(User user, GetUserCallback callBack) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, callBack).execute();
    }
    //method check data from db in background
    public void fetchUserDataInBackground(User user, GetUserCallback callBack) {
        progressDialog.show();
        new fetchUserDataAsyncTask(user, callBack).execute();
    }
    //method update data to db in background
    public void updateUserDataInBackground(User user, GetUserCallback callBack) {
        progressDialog.show();
        new UpdateUserDataAsyncTask(user, callBack).execute();
    }
    //unknow
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    //background class to store data
    public class StoreCarDataAsyncTask extends AsyncTask<Void, Void, Car> {
        //varibles needed
        User user;
        Car car;
        GetUserCallback userCallback;
        //constructor
        public StoreCarDataAsyncTask(User user, Car car, GetUserCallback callBack) {
            this.user = user;
            this.car = car;
            this.userCallback = callBack;
        }

        @Override
        protected Car doInBackground(Void... params) {
            //get the value inside the user class
            int user_id = user.getUser_id();
            String carNumber = car.carNumber;
            String carModel = car.carModel;
            String carColor = car.carColor;
            String carSeatNumber = car.carSeatNumber;
            //set the path of the php code run
            String link =SERVER_ADDRESS+ "registerCar.php";
            URL url;
            String response = "";
            //pass the data in to the arrray named data.
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("user_id", ""+user_id);
            data.put("car_number", carNumber);
            data.put("model", carModel);
            data.put("seat_number", carSeatNumber);
            data.put("color", carColor);
            //run the php code
            try {
                url = new URL(link);
                //connection settings
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //unknow
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(data));
                //unknow
                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();
                //unknow
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                }
                else {
                    response = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return car;
        }
        //method called after the server  request
        @Override
        protected void onPostExecute(Car returnedCar) {
            progressDialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(returnedCar);
        }
    }

    //background class to check data from db
    public class fetchCarDataInBackground extends AsyncTask<Void, Void, Car> {
        //variable
        User user;
        GetCarCallback carCallback;
        //constructor
        public fetchCarDataInBackground(User user, GetCarCallback callBack) {
            this.user = user;
            this.carCallback = callBack;
        }
        @Override
        protected Car doInBackground(Void... params) {
            //get the value inside the user class
            int user_id = user.getUser_id();
            //set the path of the php code run
            String link =SERVER_ADDRESS+ "fetchCarData.php?user_id="+user_id;
            Car car = null;

            HttpURLConnection conn = null;
            //run the php code
            try {
                URL obj = new URL(link);
                //connection settings
                conn = (HttpURLConnection) obj.openConnection();
                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();
                //unknow
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                JSONObject result =new JSONObject(response.toString());
                Log.d("fetch car", result.toString());
                in.close();
                //create a car  and  store the fetch infor
                String car_number = result.getString("car_number");
                String model = result.getString("model");
                String seat_number =result.getString("seat_number");
                String color = result.getString("color");
                //create the user with full data
                car = new Car(car_number, model,color,seat_number);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return car;
        }
        //method called after server request
        protected void onPostExecute(Car car) {
            progressDialog.dismiss();
            carCallback.done(car);
            super.onPostExecute(car);
        }
    }

    //background class to update data
    public class UpdateCarDataAsyncTask extends AsyncTask<Void, Void, Car> {
        //varibles needed
        User user;
        Car car;
        GetCarCallback carCallback;
        //constructor
        public UpdateCarDataAsyncTask(User user, Car car, GetCarCallback callBack) {
            this.user = user;
            this.car = car;
            this.carCallback = callBack;
        }
        //method do in background to do the php things
        @Override
        protected Car doInBackground(Void... params) {
            //get the value inside the user class
            int user_id = user.getUser_id();
            String car_number = car.getCarNumber();
            String model = car.getCarModel();
            String seat_number = car.getCarSeatNumber();
            String color = car.getCarColor();

            //set the path of the php code run
            String link =SERVER_ADDRESS+ "updateCar.php";
            URL url;
            String response = "";
            //pass the data in to the arrray named data.
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("user_id", ""+user_id);
            data.put("car_number", car_number);
            data.put("model", model);
            data.put("seat_number", seat_number);
            data.put("color", color);

            //run the php code
            try {
                url = new URL(link);
                //connection settings
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //unknow
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(data));
                //unknow
                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();
                //unknow
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } else {
                    response = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return car;
        }
        //method called after the server  request
        @Override
        protected void onPostExecute(Car car) {
            progressDialog.dismiss();
            carCallback.done(car);
            super.onPostExecute(car);
        }
    }

    //background class to store data
    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        //varibles needed
        User user;
        GetUserCallback userCallback;
        //constructor
        public StoreUserDataAsyncTask(User user, GetUserCallback callBack) {
            this.user = user;
            this.userCallback = callBack;
        }

        @Override
        protected User doInBackground(Void... params) {
            //get the value inside the user class
            String username = user.username;
            String password = user.password;
            String email = user.email;
            String sex = user.sex;
            String phone = user.phone;
            String car_owner = "" + user.car_owner;
            String icon = user.icon;
            String credit_card =user.credit_card;
            double balance = user.balance;
            //set the path of the php code run
            String link =SERVER_ADDRESS+ "register.php";
            URL url;
            String response = "";
            //pass the data in to the arrray named data.
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("username", username);
            data.put("password", password);
            data.put("email", email);
            data.put("sex", sex);
            data.put("phone", phone);
            data.put("car_owner", car_owner);
            data.put("credit_card", credit_card);
            //run the php code
            try {
                url = new URL(link);
                //connection settings
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //unknow
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(data));
                //unknow
                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();
                //unknow
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                }
                else {
                    response = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
                }
            return user;
        }
        //method called after the server  request
        @Override
        protected void onPostExecute(User returnedUser) {
            progressDialog.dismiss();
            userCallback.done(returnedUser);
            super.onPostExecute(returnedUser);
        }
    }

    //background class to check data from db
    public class fetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        //variable
        User user;
        GetUserCallback userCallback;
        //constructor
        public fetchUserDataAsyncTask(User user, GetUserCallback callBack) {
            this.user = user;
            this.userCallback = callBack;
        }
        @Override
        protected User doInBackground(Void... params) {
            //get the value inside the user class
            String username = user.username;
            String password = user.password;
            //set the path of the php code run
            String link =SERVER_ADDRESS+ "fetchUserData.php?username="+username+"&password="+password;
            User user = null; //clear the user

            HttpURLConnection conn = null;
            //run the php code
            try {
                URL obj = new URL(link);
                //connection settings
                conn = (HttpURLConnection) obj.openConnection();
                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();
                //unknow
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                JSONObject result =new JSONObject(response.toString());
                in.close();
                //create a user and  store the fetch infor
                int temp_user_id = result.getInt("user_id");
                String temp_name = result.getString("username");
                String temp_password = result.getString("password");
                String temp_email = result.getString("email");
                String temp_sex = result.getString("sex");
                String temp_phone = result.getString("phone");
                boolean temp_car_owner;
                if(result.getInt("car_owner") == 0){
                    temp_car_owner = false;
                }
                else{
                    temp_car_owner = true;
                }
                String temp_icon = result.getString("icon");
                String temp_credit_card = result.getString("credit_card");
                double temp_balance = Double.valueOf(result.getString("balance"));

                //create the user with full data
                user = new User(temp_user_id,temp_name,temp_password,temp_email,temp_sex,temp_phone,temp_car_owner,temp_icon,temp_credit_card,temp_balance);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return user;
        }
        //method called after server request
        protected void onPostExecute(User user) {
            progressDialog.dismiss();
            userCallback.done(user);
            super.onPostExecute(user);
        }
    }

    //background class to update data
    public class UpdateUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        //varibles needed
        User user;
        GetUserCallback userCallback;
        //constructor
        public UpdateUserDataAsyncTask(User user, GetUserCallback callBack) {
            this.user = user;
            this.userCallback = callBack;
        }
        //method do in background to do the php things
        @Override
        protected User doInBackground(Void... params) {
            //get the value inside the user class
            int user_id = user.getUser_id();
            String username = user.username;
            String password = user.password;
            String email = user.email;
            String sex = user.sex;
            String phone = user.phone;
            String car_owner = "" + user.car_owner;
            String icon = user.icon;
            String credit_card =user.credit_card;
            double balance = user.balance;
            //set the path of the php code run
            String link =SERVER_ADDRESS+ "update.php";
            URL url;
            String response = "";
            //pass the data in to the arrray named data.
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("user_id", ""+user_id);
            data.put("username", username);
            data.put("password", password);
            data.put("email", email);
            data.put("sex", sex);
            data.put("phone", phone);
            data.put("car_owner", car_owner);
            data.put("credit_card", credit_card);
            //run the php code
            try {
                url = new URL(link);
                //connection settings
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //unknow
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(data));
                //unknow
                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();
                //unknow
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } else {
                    response = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return user;
        }
        //method called after the server  request
        @Override
        protected void onPostExecute(User user) {
            progressDialog.dismiss();
            userCallback.done(user);
            super.onPostExecute(user);
        }
    }
}