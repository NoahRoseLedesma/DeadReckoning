package com.NoahR;

/**
 * Created by Noah Rose-Ledesma on 11/12/2015.
 */
public class instruction {
    public
    boolean motorFR = true, motorBR = true, motorFL = true, motorBL= true, hasSet = false; // Use this motor? and Have the values been init
    public int motorFRDest, motorBRDest, motorFLDest, motorBLDest; // Distance to travel in mm
    public instruction(int motorFRDest, int motorFLDest, int motorBRDest, int motorBLDest){
        setValues(motorFRDest, motorFLDest, motorBRDest, motorBLDest);
    }
    public instruction(){
        // Empty constructor to allow class to be instantiated without setting values
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
