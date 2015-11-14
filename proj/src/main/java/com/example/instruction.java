package com.example;

import java.io.File;
import java.io.PrintWriter;

/**
 * Created by Noah Rose-Ledesma on 11/12/2015.
 */
public class instruction {
    protected
    boolean motorFR = true, motorBR = true, motorFL = true, motorBL= true, hasSet = false; // Use this motor? and Have the values been init
    int motorFRDest, motorBRDest, motorFLDest, motorBLDest; // Distance to travel in mm
    static
    PrintWriter output;
    public File outFile = new File("Instruction.packet");
    instruction(int motorFRDest, int motorFLDest, int motorBRDest, int motorBLDest){
        setValues(motorFRDest, motorFLDest, motorBRDest, motorBLDest);
    }
    instruction(){
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
