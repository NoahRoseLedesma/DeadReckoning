package com.example;
import com.example.globalInfoPacket;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import com.example.instruction;
import com.sun.org.apache.bcel.internal.generic.Instruction;

// For now all classes will be in this file. Im so sorry
public class controllerModule {
    static ArrayList<instruction> instructionSet= new ArrayList();
    static BufferedWriter output;
    static File outFile = new File("Instructions.packet");
    public static void main(String[] args)
    {
        System.out.println("Absolute Horseshit 2.0");
        globalInfoPacket mainPacket = new globalInfoPacket();
        mainPacket.setEncoderCPR(12);
        mainPacket.setGearRatio(12);
        mainPacket.setWheelCurcumfrance(12);
        mainPacket.commitChanges();
        if(mainPacket.exportPacket()){System.out.println("Hi");}else{System.out.println("YO");}
        instructionSet.add(new instruction(1, 3, 6, 8));
        instructionSet.add(new instruction(12, 13, 14, 15));
        exportInstructions();
    }
    static void exportInstructions()
    {
        Iterator<instruction> it = instructionSet.iterator();
        try{
            //Delete the old instruction packet. If you cant delete it, throw an IOException. I know its cheap, but its still technically an IO Error
            if(!outFile.delete()){throw new IOException();}
            output = new BufferedWriter(new FileWriter(outFile.getAbsoluteFile(), true));
        } catch (IOException io){
            return;
        }
        while (it.hasNext())
        {
            System.out.println("YESBABY");
            instruction currentInstruction = it.next();
            if(!currentInstruction.hasSet){return;}
            if(output == null){
                //Attempt to write to the filesystem
                try {
                    //Primitive booleans dont support toString, because... well... thats primitives!
                    //Outputs seperated with BufferedWriter.newLine() which will append the system's new line char
                    //Outputs FR, FL BR BL
                    output.write(Boolean.toString(currentInstruction.motorFR));  output.newLine(); output.write(currentInstruction.motorFRDest); output.newLine(); output.write(Boolean.toString(currentInstruction.motorFL)); output.newLine(); output.write(currentInstruction.motorFLDest); output.newLine(); output.write(Boolean.toString(currentInstruction.motorBR)); output.newLine(); output.write(currentInstruction.motorBRDest); output.newLine(); output.write(Boolean.toString(currentInstruction.motorBL)); output.newLine(); output.write(currentInstruction.motorBLDest);
                } catch (IOException io){
                    //Handle errors
                }
            }
            output = null;
        }
    }
}
