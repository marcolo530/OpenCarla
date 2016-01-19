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

    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "192.168.0.106:1234/fyp/";

    public ServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("處理中");
        progressDialog.setMessage("請稍後");

    }

    public void storeUserDataInBackground(User user, GetUserCallback callBack) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, callBack).execute();
    }

    public void fetchUserDataInBackground(User user, GetUserCallback callBack) {
        progressDialog.show();
        new fetchUserDataAsyncTask(user, callBack).execute();

    }

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

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, String> {

        User user;
        GetUserCallback userCallback;

        public StoreUserDataAsyncTask(User user, GetUserCallback callBack) {
            this.user = user;
            this.userCallback = callBack;

        }

        @Override
        protected String doInBackground(Void... params) {
            if (user.carOwner == false) {
                String username = user.username;
                String password = user.password;
                String email = user.email;
                String sex = user.sex;
                String phone = user.phone;
                String car_owner = "" + user.carOwner;
                String paypal =user.paypal;


                String link = "http://192.168.0.102:1234/fyp/register.php";
                URL url;
                String response = "";
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("username", username);
                data.put("password", password);
                data.put("email", email);
                data.put("sex", sex);
                data.put("phone", phone);
                data.put("car_owner", car_owner);
                data.put("paypal", paypal);
                try {
                    url = new URL(link);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(data));

                    writer.flush();
                    writer.close();
                    os.close();
                    int responseCode = conn.getResponseCode();

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

                return response;

            }
            //user.carOwner==true
            else {
                String username = user.username;
                String password = user.password;
                String email = user.email;
                String sex = user.sex;
                String phone = user.phone;
                String car_owner = "" + user.carOwner;
                String paypal =user.paypal;
                String carNumber = user.carNumber;
                String carModel = user.carModel;
                String carSeatNumber = user.carSeatNumber;
                String carColor = user.carColor;

                String link = "http://192.168.0.102:1234/fyp/register.php";
                URL url;
                String response = "";
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("username", username);
                data.put("password", password);
                data.put("email", email);
                data.put("sex", sex);
                data.put("phone", phone);
                data.put("car_owner", car_owner);
                data.put("carNumber", carNumber);
                data.put("carModel", carModel);
                data.put("carSeatNumber", carSeatNumber);
                data.put("carColor", carColor);
                data.put("paypal", paypal);
                try {
                    url = new URL(link);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(data));

                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();

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

                return response;


            }

        }

        @Override
        protected void onPostExecute(String aVoid) {
            progressDialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }


    public class fetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {

        User user;
        GetUserCallback userCallback;

        public fetchUserDataAsyncTask(User user, GetUserCallback callBack) {
            this.user = user;
            this.userCallback = callBack;

        }


        @Override
        protected User doInBackground(Void... params) {
            String username = user.username;
            String password = user.password;
            String link = "http://192.168.0.102:1234/fyp/FetchUserData.php?username="+username+"&password="+password;


            User user = null;


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
                JSONObject result =new JSONObject(response.toString());
                in.close();
                System.out.println(result);
                user = new User (result.getString("username"),result.getString("password"),result.getInt("user_id"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println(user);
            return user;
        }

        protected void onPostExecute(User returnedUser) {
            progressDialog.dismiss();
            userCallback.done(returnedUser);
            super.onPostExecute(returnedUser);
        }
    }
}