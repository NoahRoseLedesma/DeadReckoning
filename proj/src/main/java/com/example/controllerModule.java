package com.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

import com.example.instruction;
import com.example.globalInfoPacket;

// For now all classes will be in this file. Im so sorry
public class controllerModule {
    static instructionSet<instruction> instructions;
    static BufferedWriter output;
    static File outFile = new File("Instructions.packet");
    static globalInfoPacket mainPacket;
    public static void main(String[] args) {
        System.out.println("Absolute Horseshit 6.0");
        mainPacket = new globalInfoPacket();
        instructions = new instructionSet<>(new AtomicReference(mainPacket));
        mainPacket.setEncoderCPR(12);
        mainPacket.setGearRatio(12);
        mainPacket.setWheelCurcumfrance(12);
        mainPacket.commitChanges();
        if(!mainPacket.exportPacket()){System.out.println("Could not export main packet");}
        instructions.add(new instruction(1, 3, 6, 8));
        instructions.add(new instruction(12, 13, 14, 15));
        instructions.exportInstructions();
    }

}
