package miraclegeneration.opencarla;

import java.io.Serializable;

/**
 * Created by kenex on 1/3/2016.
 */
public class Car implements Serializable {
    String carNumber;
    String carModel;
    String carColor;
    String carSeatNumber;

    //constructor
    public Car(String carNumber,String carModel,String carColor,String carSeatNumber){
        this.carNumber = carNumber;
        this.carModel = carModel;
        this.carColor = carColor;
        this.carSeatNumber = carSeatNumber;
    }
    //setter
    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public void setCarSeatNumber(String carSeatNumber) {
        this.carSeatNumber = carSeatNumber;
    }

    //getter
    public String getCarNumber() {
        return carNumber;
    }

    public String getCarModel() {
        return carModel;
    }

    public String getCarColor() {
        return carColor;
    }

    public String getCarSeatNumber() {
        return carSeatNumber;
    }
}
