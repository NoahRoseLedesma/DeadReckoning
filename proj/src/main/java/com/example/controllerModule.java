package com.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import com.example.instruction;
import com.example.globalInfoPacket;

// For now all classes will be in this file. Im so sorry
public class controllerModule {
    static ArrayList<instruction> instructionSet= new ArrayList();
    static BufferedWriter output;
    static File outFile = new File("Instructions.packet");
    static globalInfoPacket mainPacket;
    public static void main(String[] args) {
        System.out.println("Absolute Horseshit 5.0");
        mainPacket = new globalInfoPacket();
        mainPacket.setEncoderCPR(12);
        mainPacket.setGearRatio(12);
        mainPacket.setWheelCurcumfrance(12);
        mainPacket.commitChanges();
        if(!mainPacket.exportPacket()){System.out.println("Could not export main packet");}
        instructionSet.add(new instruction(1, 3, 6, 8));
        instructionSet.add(new instruction(12, 13, 14, 15));

        exportInstructions();
    }
    // TODO: Move exportInstruction and similar necessities to it's own class
    static void exportInstructions()
    {
        Iterator<instruction> it = instructionSet.iterator();
        try{
            //Delete the old instruction packet. If you cant delete it, throw an IOException. I know its cheap, but its still technically an IO Error
            if(outFile.exists()){
                if(!outFile.delete()) {
                    throw new IOException();
                }
            }
            output = new BufferedWriter(new FileWriter(outFile.getAbsoluteFile(), true));
            output.write(Integer.toString(mainPacket.sessionHash)); output.newLine();
        } catch (IOException io){
            System.out.println("Could not delete file");
            return;
        }
        while (it.hasNext())
        {
            instruction currentInstruction = it.next();
            if(!currentInstruction.hasSet){return;}
            try{
                //Primitives cant be converted to string correctly. Converted to objects first
                //FR, FL, BR, BL
                output.write(Boolean.toString(currentInstruction.motorFR)); output.newLine(); output.write(Integer.toString(currentInstruction.motorFRDest)); output.newLine(); output.write(Boolean.toString(currentInstruction.motorFL)); output.newLine(); output.write(Integer.toString(currentInstruction.motorFLDest)); output.newLine(); output.write(Boolean.toString(currentInstruction.motorBR)); output.newLine(); output.write(Integer.toString(currentInstruction.motorBRDest)); output.newLine(); output.write(Boolean.toString(currentInstruction.motorBL)); output.newLine(); output.write(Integer.toString(currentInstruction.motorBLDest)); output.newLine();
                //There is no way to tell if the itt is refering to the last item of the arraylist.
                //Therefore a new line is written to the end of the file. The other end will have to handle this quirk
            } catch (IOException io){
                System.out.println("Could not write to the filesystem");
            }
        }
        try{output.close();}catch (IOException io){System.out.println("Could not close filewriter");}
    }
}
