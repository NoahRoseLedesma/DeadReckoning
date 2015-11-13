package com.example;
import com.example.globalInfoPacket;
// For now all classes will be in this file. Im so sorry
public class controllerModule {
    public static void main(String[] args)
    {
        System.out.println("Absolute Horseshit");
        globalInfoPacket mainPacket = new globalInfoPacket();
        mainPacket.setEncoderCPR(12);
        mainPacket.setGearRatio(12);
        mainPacket.setWheelCurcumfrance(12);
        mainPacket.commitChanges();
        if(mainPacket.exportPacket()){System.out.println("Hi");}else{System.out.println("YO");}
    }
}
