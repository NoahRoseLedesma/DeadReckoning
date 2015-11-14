package com.example;

import java.util.concurrent.atomic.AtomicReference;

public class controllerModule {

    static instructionSet<instruction> instructions;
    static globalInfoPacket mainPacket;

    public static void main(String[] args) {
        mainPacket = new globalInfoPacket();
        instructions = new instructionSet<>(new AtomicReference<>(mainPacket));

    }

}
