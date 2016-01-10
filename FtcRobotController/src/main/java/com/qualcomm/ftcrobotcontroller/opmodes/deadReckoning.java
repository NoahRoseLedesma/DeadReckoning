package com.qualcomm.ftcrobotcontroller.opmodes;

import android.util.Log;

import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.NoahR.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Iterator;

/**
 * Created by Noah Rose-Ledesma on 11/17/2015.
 * Opmode to be run to create deadReckoning opmodes. That is an awkward sentence, but you know what I mean.
 */
public class deadReckoning extends OpMode{
    boolean debugBuild = true; // Modify this to enable or disable debug printing
    boolean instructionInProgress;
    int instructionCount;
    DcMotorController motorController, motorController2;
    DcMotor motorFR, motorFL, motorBR, motorBL;
    instruction currentInstruction;
    instructionSet<instruction> instructions;
    Iterator<instruction> it;

    public static AtomicReference<deadReckoning> deadReckoningObjectAtomicReference;
    public deadReckoning(){
        // Create public references to object being created.
        deadReckoningObjectAtomicReference = new AtomicReference<deadReckoning>(this);
    }
    // ActivityTransmission
    public void ActivityTransmission(instructionSet newInstructionSet)
    {
        this.instructions = objectToInstructionSet(newInstructionSet);
        debugTelemetry("1", Integer.toString(instructions.globalInfoPacket.encoderCPR));
        debugTelemetry("2", Integer.toString(instructions.size()));
    }

    @Override
    public void init(){
        FtcRobotControllerActivity.notifyUseOfDeadReckoning();
        if(!FtcRobotControllerActivity.testDeadReckoningConnection()){
            //Cant establish a connection to the service. Abort
            //TODO: Find a safe way to abort opmode.
            telemetry.addData("0", "Could not connect to activity!");
        }
        //mapHardware();
    }

    @Override
    public void loop()
    {
        /* For now ...
        if(hasReachedTarget()){
            instructionInProgress = false;
        }
        if(!instructionInProgress && it.hasNext())
        {
            runInstruction();
        }
        */
    }

    @Override
    public void start()
    {


        //globalInfoPacket = deserializeGlobalInfoPacket();

        //instructions = deserializeInstructionSet();



        if(instructions == null || instructions.globalInfoPacket == null) {
            stop(); // Does this even work?
            return;
        }

        it = instructions.iterator();
        if(!it.hasNext()){
            stop();
            return;
        }
        // For now...
        //runInstruction();
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
        if(instructions.globalInfoPacket.encoderCPR == 0 || instructions.globalInfoPacket.gearRatio == 0.0f || instructions.globalInfoPacket.wheelCircumference == 0.0f){
            //Something has broke. Handle the error
            return 0;
        }
        return (int)(instructions.globalInfoPacket.encoderCPR * (distance / instructions.globalInfoPacket.wheelCircumference) * instructions.globalInfoPacket.gearRatio);
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

    instructionSet<instruction> objectToInstructionSet(Object obj)
    {
        globalInfoPacket extractedGlobalInfo = null;
        instructionSet<instruction> returnSet = new instructionSet<instruction>();
        if (obj instanceof instructionSet<?>) {
            // Get the List.
            instructionSet<?> al = (instructionSet<?>) obj;
            if (al.size() > 0) {
                // Iterate.
                for (int i = 0; i < al.size(); i++) {
                    // Still not enough for a type.
                    Object o = al.get(0);
                    if (o instanceof instruction) {
                        // Here we go!
                        instruction v = (instruction) o;
                        // use v.
                        returnSet.add(v);
                    }
                }
            }
             extractedGlobalInfo = ((instructionSet) obj).globalInfoPacket;
        }
        if(extractedGlobalInfo != null) {
            returnSet.globalInfoPacket = extractedGlobalInfo;
        }
        return returnSet;
    }
}
