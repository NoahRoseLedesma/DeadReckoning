package com.NoahR;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Noah Rose-Ledesma on 11/14/2015.
 * An array to hold instructions.
 */
public class instructionSet<E extends instruction> extends ArrayList<E> implements Serializable{
    transient BufferedWriter output;
    globalInfoPacket globalInfoPacket;
    transient AtomicReference<globalInfoPacket> mainPacket;

    public instructionSet(AtomicReference<globalInfoPacket> mainPacketAtomicReference){

        super();
        this.mainPacket = mainPacketAtomicReference;

    }
    private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
        globalInfoPacket = mainPacket.get();
    }
}
