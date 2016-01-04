package com.NoahR;

import java.util.ArrayList;
/**
 * Created by Noah Rose-Ledesma on 11/14/2015.
 * An array to hold instructions.
 */
public class instructionSet<E extends instruction> extends ArrayList<E>{
    public globalInfoPacket globalInfoPacket;

    public instructionSet(){

        super();
        globalInfoPacket = new globalInfoPacket();

    }
}
