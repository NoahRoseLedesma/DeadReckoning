package com.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Created by Noah Rose-Ledesma on 11/10/2015.
 */
public class globalInfoPacket {
    protected
    boolean uncommitedChanges;
    int sessionHash, encoderCPR;
    float gearRatio, wheelCurcumfrance;
    Random ran;
    PrintWriter output;
    public File outFile = new File("Global.packet");
    globalInfoPacket()
    {
        //Init out values
        ran = new Random();


    }

    public void commitChanges()
    {
        sessionHash = ran.nextInt();
        uncommitedChanges = false;
    }

    public boolean exportPacket(){
        if(uncommitedChanges) {return false;}
        if(encoderCPR == 0 && gearRatio == 0.0f && wheelCurcumfrance == 0.0f){return false;}
        if(output == null)
        {
            //Attempt to assume File permissions
            try {
                output = new PrintWriter(outFile.getAbsoluteFile(), "UTF-8");
            } catch (IOException io){
                //Handle errors
            }
        }
        output.println(sessionHash);
        output.println(encoderCPR); output.println(gearRatio); output.println(wheelCurcumfrance);
        output.close();
        return true;
    }

    public void setEncoderCPR(int cpr){encoderCPR = cpr; uncommitedChanges = true;}
    public void setWheelCurcumfrance(int cir){wheelCurcumfrance = cir; uncommitedChanges = true;}
    public void setGearRatio(int gr){gearRatio = gr; uncommitedChanges = true;}

}
