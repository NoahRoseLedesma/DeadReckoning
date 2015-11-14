package com.example;

import java.io.File;

/**
 * Created by Noah Rose-Ledesma on 11/14/2015.
 */
public class opmodeModule {
    static packetReader packetRead = new packetReader(new File("Instructions.packet"), new File("Global.packet"));
    static globalInfoPacket globalPacket;
    static instructionSet<instruction> instructions;
    public static void main(String[] args){
        globalPacket = packetRead.getGlobalPacket();
        System.out.println(globalPacket.sessionHash);
        System.out.println(globalPacket.encoderCPR);
        System.out.println(globalPacket.gearRatio);
        System.out.println(globalPacket.wheelCircumference);
        instructions = packetRead.getInstructionPacket();

    }
}
