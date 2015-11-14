package com.example;

import java.io.BufferedWriter;
import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public class controllerModule {
    static instructionSet<instruction> instructions;
    static BufferedWriter output;
    static File outFile = new File("Instructions.packet");
    static globalInfoPacket mainPacket;
    public static void main(String[] args) {
        mainPacket = new globalInfoPacket();
        instructions = new instructionSet<>(new AtomicReference<>(mainPacket));
        mainPacket.setEncoderCPR(12);
        mainPacket.setGearRatio(12);
        mainPacket.setWheelCircumference(12);
        mainPacket.commitChanges();
        if(!mainPacket.exportPacket()){System.out.println("Could not export main packet"); return;}
        instructions.add(new instruction(1, 3, 6, 8));
        instructions.add(new instruction(12, 13, 14, 15));
        instructions.add(new instruction(20, 23, 0, 1));
        instructions.exportInstructions();
    }

}
