package com.NoahR;

import java.io.Serializable;

/**
 * Created by Noah Rose-Ledesma on 11/12/2015.
 * Holds a single instruction for four motors.
 */
public class instruction implements Serializable{
    boolean motorFR = true, motorBR = true, motorFL = true, motorBL= true;
    transient boolean hasSet = false;
    public int motorFRDest, motorBRDest, motorFLDest, motorBLDest; // Distance to travel in mm

    public instruction(){
        // Empty so that method may be overloaded with initializer values
    }
    public instruction(int motorFRDest, int motorFLDest, int motorBRDest, int motorBLDest){
        setValues(motorFRDest, motorFLDest, motorBRDest, motorBLDest);
    }
    public void setValues(int motorFRDest, int motorFLDest, int motorBRDest, int motorBLDest)
    {
        this.motorBLDest = motorBLDest; if(motorBLDest <= 0){this.motorBL = false;}
        this.motorBRDest = motorBRDest; if(motorBRDest <= 0){this.motorBR = false;}
        this.motorFLDest = motorFLDest; if(motorFLDest <= 0){this.motorFL = false;}
        this.motorFRDest = motorFRDest; if(motorFRDest <= 0){this.motorFR = false;}
        this.hasSet = true;
    }
}
