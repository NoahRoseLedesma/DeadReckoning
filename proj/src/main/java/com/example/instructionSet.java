package com.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Noah Rose-Ledesma on 11/14/2015.
 */
public class instructionSet<E extends instruction> extends ArrayList<E>{
    BufferedWriter output;
    File outFile = new File("Instructions.packet");
    AtomicReference<globalInfoPacket> mainPacket;

    instructionSet(AtomicReference<globalInfoPacket> mainPacketAtomicReference){

        super();
        this.mainPacket = mainPacketAtomicReference;
    }
    void exportInstructions()
    {
        Iterator<E> it = this.iterator();
        try{
            //Delete the old instruction packet. If you cant delete it, throw an IOException. I know its cheap, but its still technically an IO Error
            if(outFile.exists()){
                if(!outFile.delete()) {
                    throw new IOException();
                }
            }
            output = new BufferedWriter(new FileWriter(outFile.getAbsoluteFile(), true));
            output.write(Integer.toString(mainPacket.get().sessionHash)); output.newLine();
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
