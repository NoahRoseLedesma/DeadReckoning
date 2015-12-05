package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.NoahR.*;
import com.qualcomm.robotcore.robocol.Telemetry;
import java.io.File;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Iterator;

/**
 * Created by Noah Rose-Ledesma on 11/17/2015.
 * why is this yellow? Gosh dangit android studio.
 */
public class deadReckoning extends OpMode{
    boolean loadedPacket; //Use these?
    boolean loadedGlobalPacket; // ^
    DcMotorController motorController, motorController2;
    DcMotor motorFR, motorFL, motorBR, motorBL;
    // Backend stuff
    instructionSet<instruction> instructions;
    globalInfoPacket globalInfoPacket;
    packetReader packetRead;
    Iterator<instruction> it;
    instruction currentInstruction;
    boolean instructionInProgress;
    int instructionCount;


    @Override
    public void init(){
        /*
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
        */
        //This demonstrates writing instructions to the filesystem. And than reading them into the instructions object.

        //Hardware config.
        motorController = hardwareMap.dcMotorController.get("Motor Controller 1");
        motorController2 = hardwareMap.dcMotorController.get("Motor Controller 2");
        motorFR = hardwareMap.dcMotor.get("Motor FR");
        motorFL = hardwareMap.dcMotor.get("Motor FL");
        motorBR = hardwareMap.dcMotor.get("Motor BR");
        motorBL = hardwareMap.dcMotor.get("Motor BL");

    }

    @Override
    public void loop()
    {
        if(motorFR.getCurrentPosition() >= motorFR.getTargetPosition() && motorFL.getCurrentPosition() >= motorFL.getTargetPosition() && motorBR.getCurrentPosition() >= motorBR.getTargetPosition() && motorBL.getCurrentPosition() >= motorFL.getTargetPosition()){
            instructionInProgress = false;
        }
        if(!instructionInProgress && it.hasNext())
        {
            currentInstruction = it.next();
            instructionInProgress = true;
            instructionCount++;
            telemetry.addData("1", instructionCount);
            runInstruction(currentInstruction);
        }
    }

    @Override
    public void start(){
        // Load our packets
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
        // Emulating the activity side.
        globalInfoPacket = packetRead.getGlobalPacket();
        if(instructions == null || globalInfoPacket == null) {

            // Something is broken, handle it.
        }
        // Create our it
        it = instructions.iterator();
        if(!it.hasNext()){
            // No instructions! Handle this error.
        }
        currentInstruction = it.next();
        // Run the first instruction
        instructionInProgress = true;
        instructionCount++;
        telemetry.addData("1", instructionCount);
        runInstruction(currentInstruction);
    }
    void runInstruction(instruction instruction){
        // Reset encoders
        motorFR.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorFL.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBR.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBL.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        // Set target
        motorFR.setTargetPosition(calculateRotations(instruction.motorFRDest));
        motorFL.setTargetPosition(calculateRotations(instruction.motorFLDest));
        motorBR.setTargetPosition(calculateRotations(instruction.motorBRDest));
        motorBL.setTargetPosition(calculateRotations(instruction.motorBLDest));
        // Run to position
        motorFR.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorFL.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorBR.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorBL.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        // Run with encoders
        motorFR.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFL.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBR.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBL.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        // Set motor power (.5 for test)
        motorFR.setPower(0.5);
        motorFL.setPower(0.5);
        motorBR.setPower(0.5);
        motorBL.setPower(0.5);
    }
    int calculateRotations(int distance){
        if(globalInfoPacket.encoderCPR == 0 || globalInfoPacket.gearRatio == 0.0f || globalInfoPacket.wheelCircumference == 0.0f){
            //Something has broke. Handle the error
            return 0;
        }
        return (int)(globalInfoPacket.encoderCPR * (distance / globalInfoPacket.wheelCircumference) * globalInfoPacket.gearRatio);
    }

}
