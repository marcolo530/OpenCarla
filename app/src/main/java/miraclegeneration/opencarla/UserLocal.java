package miraclegeneration.opencarla;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by marco on 15/1/2016.
 */
public class UserLocal {
    //sharedpreference name
    public static final String SP_NAME="userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocal(Context context){
        userLocalDatabase=context.getSharedPreferences(SP_NAME,0);
    }
    //write sharedprefrences user logined
    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putInt("user_id", user.user_id);
        spEditor.putString("name",user.username);
        spEditor.putString("password",user.password);
        spEditor.commit();
    }
    //read sharedpreference user login before
    public User getLoggedUser() {
        String name = userLocalDatabase.getString("name", "");
        String password = userLocalDatabase.getString("password", "");
        int user_id = userLocalDatabase.getInt("user_id", 0);
        User storeUser =new User(user_id,name,password);
        return storeUser;
    }
    //set to user logined
    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor=userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn",loggedIn);
        spEditor.commit();

    }
    //check user login or not
    public boolean getUserLoggedIn(){
        if (userLocalDatabase.getBoolean("loggedIn",false)==true){
            return true;
        }
        else{
            return false;
        }
    }
    //clean all the preferences (logout)
    public void clearUserData(){
        SharedPreferences.Editor spEditor =userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
