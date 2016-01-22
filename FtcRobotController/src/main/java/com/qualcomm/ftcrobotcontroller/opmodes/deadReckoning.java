package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.NoahR.*;
import com.qualcomm.ftcrobotcontroller.opmodes.DriveControl;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Noah Rose-Ledesma on 11/17/2015.
 * Opmode to be run to create deadReckoning opmodes. That is an awkward sentence, but you know what I mean.
 */
public class deadReckoning extends OpMode{
    boolean debugBuild = true; // Modify this to enable or disable debug printing
    instructionSet<instruction> instructions;
    DriveControl driveController;

    DcMotorController motorController;
    DcMotorController motorController2;
    DcMotor motorFR, motorFL, motorBR, motorBL;
    public static AtomicReference<deadReckoning> deadReckoningObjectAtomicReference;

    public deadReckoning(){
        // Create public references to object being created.
        deadReckoningObjectAtomicReference = new AtomicReference<deadReckoning>(this);
        driveController = new DriveControl();
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

        driveController.init();


    }

    @Override
    public void loop()
    {
        driveController.loop();
        telemetry.addData("1 Left Current", motorFL.getCurrentPosition());
        telemetry.addData("2 Right Current", motorFR.getCurrentPosition());
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
        driveController.instructions = instructions;
        driveController.start();
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
    public void mapHardware()
    {
        motorController = hardwareMap.dcMotorController.get("Controller 1");
        motorController2 = hardwareMap.dcMotorController.get("Controller 2");
        motorFR = hardwareMap.dcMotor.get("Motor FR");
        motorFL = hardwareMap.dcMotor.get("Motor FL");
        motorBR = hardwareMap.dcMotor.get("Motor BR");
        motorBL = hardwareMap.dcMotor.get("Motor BL");
    }
}
