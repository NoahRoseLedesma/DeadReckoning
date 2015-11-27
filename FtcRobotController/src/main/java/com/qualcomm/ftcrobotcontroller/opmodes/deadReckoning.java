package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.NoahR.*;
import com.qualcomm.robotcore.robocol.Telemetry;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Noah Rose-Ledesma on 11/17/2015.
 * why is this yellow? Gosh dangit android studio.
 */
public class deadReckoning extends OpMode{
    boolean loadedPacket;
    boolean loadedGlobalPacket;
    int encoderCPR;
    float gearRatio, wheelCircumference, drivePower;
    DcMotorController motorController;
    DcMotor motorFR, motorFL, motorBR, motorBL;
    // Backend stuff
    instructionSet<instruction> instructions;
    globalInfoPacket globalInfoPacket;
    packetReader packetRead;
    @Override
    public void init(){
        globalInfoPacket = new globalInfoPacket();
        globalInfoPacket.sessionHash = 123;
        instructions = new instructionSet<instruction>(new AtomicReference<com.NoahR.globalInfoPacket>(globalInfoPacket));
        instructions.add(new instruction(1, 2, 3, 4));
        instructions.add(new instruction(4,3,2,1));
        instructions.outFile = new File(hardwareMap.appContext.getFilesDir().getAbsoluteFile(), "instruction.packet");
        instructions.exportInstructions();
        instructions = null;
        telemetry.addData("1", hardwareMap.appContext.getFilesDir().getAbsolutePath());
        packetRead = new packetReader(new File(hardwareMap.appContext.getFilesDir().getAbsolutePath(), "instruction.packet"), new File(hardwareMap.appContext.getFilesDir().getAbsolutePath(), "instruction.packet"), new AtomicReference<Telemetry>(telemetry));
        globalInfoPacket = packetRead.getGlobalPacket();
        instructions = packetRead.getInstructionPacket();
        telemetry.addData("2", instructions.get(0).motorFRDest + " " + instructions.get(1).motorFRDest);
    }

    @Override
    public void loop()
    {

    }

    @Override
    public void start(){

    }
}
