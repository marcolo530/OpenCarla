package miraclegeneration.opencarla;

import java.io.Serializable;

/**
 * Created by marco on 15/1/2016.
 */
public class User implements Serializable {

    String username,password,email,phone,carNumber,carModel,carColor,carSeatNumber,paypal;
    String sex;
    boolean carOwner;
    int user_id;

    public User(String username,String password,String email,String sex,String phone,boolean carOwner,String paypal){
        this.username=username;
        this.password=password;
        this.email=email;
        this.phone=phone;
        this.carOwner= carOwner;
        this.sex= sex;
        this.paypal= paypal;

    }

    public User(String username,String password,String email,String sex,String phone,boolean carOwner,String paypal,String carNumber,String carModel,String carSeatNumber,String carColor){
        this.username=username;
        this.password=password;
        this.email=email;
        this.phone=phone;
        this.carOwner= carOwner;
        this.carNumber=carNumber;
        this.carModel=carModel;
        this.carSeatNumber=carSeatNumber;
        this.carColor=carColor;
        this.sex= sex;
        this.paypal= paypal;

    }

    public User(String username,String password,int user_id){
        this.username=username;
        this.password=password;
        this.user_id =user_id;

    }

    public User(String username,String password){
        this.username=username;
        this.password=password;
        this.user_id =user_id;

    }
}
