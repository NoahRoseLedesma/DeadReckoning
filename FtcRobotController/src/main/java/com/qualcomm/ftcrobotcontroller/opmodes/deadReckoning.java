package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.NoahR.*;
import com.qualcomm.robotcore.robocol.Telemetry;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Iterator;

/**
 * Created by Noah Rose-Ledesma on 11/17/2015.
 * why is this yellow? Gosh dangit android studio.
 * Global TODO: unity of purpose
 */
public class deadReckoning extends OpMode{
    private boolean debugBuild = true; // Modify this to enable or disable debug printing
    boolean instructionInProgress;
    DcMotorController motorController, motorController2;
    DcMotor motorFR, motorFL, motorBR, motorBL;
    globalInfoPacket globalInfoPacket;
    instruction currentInstruction;
    instructionSet<instruction> instructions;
    int instructionCount;
    Iterator<instruction> it;
    packetReader packetRead;


    @Override
    public void init(){
        mapHardware();
    }

    @Override
    public void loop()
    {
        if(hasReachedTarget()){
            instructionInProgress = false;
        }
        if(!instructionInProgress && it.hasNext())
        {
            runInstruction();
        }
    }

    @Override
    public void start()
    {
        packetRead = new packetReader(new File(hardwareMap.appContext.getFilesDir().getAbsolutePath(), "instruction.packet"), new File(hardwareMap.appContext.getFilesDir().getAbsolutePath(), "instruction.packet"), new AtomicReference<Telemetry>(telemetry));

        globalInfoPacket = packetRead.getGlobalPacket();

        instructions = packetRead.getInstructionPacket();

        if(instructions == null || globalInfoPacket == null) {
            stop(); // Does this even work?
            return;
        }

        it = instructions.iterator();
        if(!it.hasNext()){
            stop();
            return;
        }
        runInstruction();
    }


    void runInstruction(){
        instruction instruction = it.next();
        instructionInProgress = true;
        instructionCount++;
        debugTelemetry("1", Integer.toString(instructionCount));
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
    private void mapHardware()
    {
        motorController = hardwareMap.dcMotorController.get("Motor Controller 1");
        motorController2 = hardwareMap.dcMotorController.get("Motor Controller 2");
        motorFR = hardwareMap.dcMotor.get("Motor FR");
        motorFL = hardwareMap.dcMotor.get("Motor FL");
        motorBR = hardwareMap.dcMotor.get("Motor BR");
        motorBL = hardwareMap.dcMotor.get("Motor BL");
    }
    private boolean hasReachedTarget(){
        if(motorFR.getCurrentPosition() >= motorFR.getTargetPosition())
        {
            if(motorFL.getCurrentPosition() >= motorFL.getTargetPosition())
            {
                if(motorBR.getCurrentPosition() >= motorBR.getTargetPosition())
                {
                    if(motorBL.getCurrentPosition() >= motorFL.getTargetPosition())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    void debugTelemetry(String id, String text)
    {
        if((id == null || text == null) && !debugBuild)
        {
            return;
        }
        telemetry.addData(id, text);
    }
    void emulateActivitySide()
    {
        globalInfoPacket = new globalInfoPacket();
        globalInfoPacket.sessionHash = 123;
        instructions = new instructionSet<instruction>(new AtomicReference<com.NoahR.globalInfoPacket>(globalInfoPacket));
        instructions.add(new instruction(1, 2, 3, 4));
        instructions.add(new instruction(4,3,2,1));
        instructions.outFile = new File(hardwareMap.appContext.getFilesDir().getAbsoluteFile(), "instruction.packet");
        instructions.exportInstructions();
        instructions = null;
        globalInfoPacket = null;
    }
}
