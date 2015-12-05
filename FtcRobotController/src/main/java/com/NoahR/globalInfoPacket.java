package com.NoahR;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Created by Noah Rose-Ledesma on 11/10/2015.
 */
public class globalInfoPacket {
    public boolean uncommittedChanges;
    public int sessionHash, encoderCPR;
    public float gearRatio, wheelCircumference;
    Random ran;
    PrintWriter output;
    File outFile = new File("Global.packet");
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

    public boolean exportPacket(){
        if(uncommittedChanges) {return false;}
        if(encoderCPR == 0 && gearRatio == 0.0f && wheelCircumference == 0.0f){return false;}
        if(outFile.exists()) {
            if(!outFile.delete()) {
                return false;
            }
        }
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
        output.println(encoderCPR); output.println(gearRatio); output.println(wheelCircumference);
        output.close();
        output = null;
        return true;
    }

    public void setEncoderCPR(int cpr){encoderCPR = cpr; uncommittedChanges = true;}
    public void setWheelCircumference(int cir){wheelCircumference = cir; uncommittedChanges = true;}
    public void setGearRatio(int gr){gearRatio = gr; uncommittedChanges = true;}

}
