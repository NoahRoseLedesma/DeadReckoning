package com.NoahR;


/**
 * Created by Noah Rose-Ledesma on 11/10/2015.
 * Class to hold info about the motors and wheels.
 */
public class globalInfoPacket{
    public int encoderCPR;
    public float gearRatio, wheelCircumference;


    public void setEncoderCPR(int cpr){encoderCPR = cpr;}
    public void setWheelCircumference(float cir){wheelCircumference = cir;}
    public void setGearRatio(float gr){gearRatio = gr;}

}
