package com.NoahR;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Random;

/**
 * Created by Noah Rose-Ledesma on 11/10/2015.
 * Class to hold info about the motors and wheels.
 */
public class globalInfoPacket implements Serializable{
    public boolean uncommittedChanges;
    public int sessionHash, encoderCPR;
    public float gearRatio, wheelCircumference;
    transient Random ran;
    transient PrintWriter output;
    public globalInfoPacket()
    {
        //Init out values
        ran = new Random();


    }

    public void commitChanges()
    {
        sessionHash = ran.nextInt();
        uncommittedChanges = false;
    }

    public void setEncoderCPR(int cpr){encoderCPR = cpr; uncommittedChanges = true;}
    public void setWheelCircumference(int cir){wheelCircumference = cir; uncommittedChanges = true;}
    public void setGearRatio(int gr){gearRatio = gr; uncommittedChanges = true;}

}
