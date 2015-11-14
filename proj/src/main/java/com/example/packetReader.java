package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Noah Rose-Ledesma on 11/14/2015.
 */
public class packetReader {
    File instructionPacket;
    File globalPacket;
    BufferedReader input;
    instructionSet<instruction> lastInstructions;
    globalInfoPacket lastGlobalPacket;
    int globalHash;
    int instructionHash;

    packetReader(File instructionPacket, File globalPacket){
        this.instructionPacket = instructionPacket;
        this.globalPacket = globalPacket;
    }
    void setGlobalPacket(File globalPacket){
        this.globalPacket = globalPacket;
    }
    void setInstructionPacket(File instructionPacket){
        this.instructionPacket = instructionPacket;
    }

    instructionSet<instruction> getInstructionPacket(){
        if(lastGlobalPacket == null){return null;}
        instructionSet<instruction> readingInstructionSet = new instructionSet<instruction>(new AtomicReference<>(lastGlobalPacket));
        if(input != null){input = null;}
        try {
            input = new BufferedReader(new FileReader(instructionPacket.getAbsoluteFile()));
            String readLine;
            instructionHash = Integer.parseInt(input.readLine());
            while ((readLine = input.readLine()) != null) {
                if(readLine.isEmpty()){break;}
                boolean motorFR = Boolean.parseBoolean(readLine);
                int motorFRDest = Integer.parseInt(input.readLine());
                boolean motorFL = Boolean.parseBoolean(input.readLine());
                int motorFLDest = Integer.parseInt(input.readLine());
                boolean motorBR = Boolean.parseBoolean(input.readLine());
                int motorBRDest = Integer.parseInt(input.readLine());
                boolean motorBL = Boolean.parseBoolean(input.readLine());
                int motorBLDest = Integer.parseInt(input.readLine());
                readingInstructionSet.add(new instruction(motorFRDest, motorFLDest, motorBRDest, motorBLDest));
            }
            input.close();
        } catch (IOException io){return null;}
        input = null;
        return readingInstructionSet;
    }

    globalInfoPacket getGlobalPacket(){
        if(input != null){ input = null;}
        globalInfoPacket returnPacket = new globalInfoPacket();
        try{
            input = new BufferedReader(new FileReader(globalPacket.getAbsoluteFile()));
            String readLine;
            // We will set the globalInfoPacket values manually. Don't do this at home kids
            // Read the hash
            readLine = input.readLine();
            if(readLine == null){return null;}
            globalHash = Integer.parseInt(readLine);
            returnPacket.sessionHash = globalHash;
            //Read the encoder CPR
            readLine = input.readLine();
            if(readLine == null){return null;}
            returnPacket.encoderCPR = Integer.parseInt(readLine);
            //Read the gear ratio
            readLine = input.readLine();
            if(readLine == null){return null;}
            returnPacket.gearRatio = Float.parseFloat(readLine);
            //Read the wheel circumference
            readLine = input.readLine();
            if(readLine == null){return null;}
            returnPacket.wheelCircumference = Float.parseFloat(readLine);
            //Set global packet misc values
            returnPacket.uncommittedChanges = false;
            input.close();
            input = null;
        } catch (IOException io){
            //Handle Exception later
        }
        lastGlobalPacket = returnPacket;
        return returnPacket;
    }
}
