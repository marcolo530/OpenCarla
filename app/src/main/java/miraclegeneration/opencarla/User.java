package miraclegeneration.opencarla;
import java.io.Serializable;
/**
 * Created by marco on 15/1/2016.
 */
//class store user information, Serializable is user for transfer data through the network
public class User implements Serializable {
    //create variable store user information according to the database user_infor
    int user_id;
    String username,password,email,sex,phone;
    boolean car_owner;
    String icon;
    String credit_card;
    double balance;
    //constructor of full data
    public User(int user_id,String username,String password,String email,String sex,String phone,boolean carOwner,String icon,String credit_card,double balance){
        this.user_id=user_id;
        this.username=username;
        this.password=password;
        this.email=email;
        this.sex= sex;
        this.phone=phone;
        this.car_owner= carOwner;
        this.icon=icon;
        this.credit_card= credit_card;
        this.balance=balance;
    }
    //constructor as normal user
    public User(String username,String password,String email,String sex,String phone,boolean carOwner,String icon,String credit_card){
        this.username=username;
        this.password=password;
        this.email=email;
        this.sex= sex;
        this.phone=phone;
        this.car_owner= carOwner;
        this.icon=icon;
        this.credit_card= credit_card;
        this.balance=0;
    }
    //constructor for login
    public User(String username,String password){
        this.username=username;
        this.password=password;
    }
    //constructor for preference
    public User(int user_id,String username,String password){
        this.user_id=user_id;
        this.username=username;
        this.password=password;
    }
    //Accessors
    public int getUser_id(){return user_id;}
    public String getUsername(){return username;}
    public String getPassword(){return password;}
    public String getEmail() {return email;}
    public String getSex() {return sex;}
    public String getPhone() {return phone;}
    public boolean getCar_owner() {return car_owner;}
    public String getIcon() {return icon;}
    public String getCredit_card() {return credit_card;}
    public double getBalance() {return balance;}
    //Mutator
    public void setUser_id(int user_id){ this.user_id=user_id;}
    public void setUsername(String username){this.username=username;}
    public void setPassword(String password){ this.password=password;}
    public void setEmail(String email){ this.email=email;}
    public void setSex(String sex){ this.sex=sex;}
    public void setPhone(String phone){ this.phone=phone;}
    public void setCar_owner(boolean car_owner){ this.car_owner=car_owner;}
    public void setIcon(String icon){ this.icon=icon;}
    public void setCredit_card(String credit_card){ this.credit_card=credit_card;}
    public void setBalance(double balance){ this.balance=balance;}
}
