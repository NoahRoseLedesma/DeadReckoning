package com.qualcomm.ftcrobotcontroller.opmodes;


import android.util.Log;

import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.NoahR.*;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

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

        mapHardware();

        setDriveMode(DcMotorController.RunMode.RUN_TO_POSITION);

        setDrivePower(0.0, 0.0, 0.0, 0.0);

        currentState = States.STATE_DO_MOVE;


    }

    @Override
    public void loop()
    {
        switch (currentState) {
            case STATE_DO_MOVE:
                doMove();
                break;

            case STATE_WAIT_FOR_ENCODER_RESET:

                waitForEncoderReset(true,currentInstruction,instructions.globalInfoPacket,1);
                break;

            case STATE_WAIT_FOR_MOVE_START:
                // Check to see if the robot has started moving
                waitForMoveStart();
                break;

            case STATE_MOVING:
                // Check to see when the robot has finished moving
                moving();
                break;

            case STATE_DONE:
                // Do Nothing
                break;
        }
        telemetry.addData("1 FR Current", motorFR.getCurrentPosition() + " vs " + motorFR.getTargetPosition());
        telemetry.addData("2 FL Current", motorFL.getCurrentPosition() + " vs " + motorFL.getTargetPosition());
        telemetry.addData("3 BR Current", motorBR.getCurrentPosition() + " vs " + motorBR.getTargetPosition());
        telemetry.addData("4 BL Current", motorBL.getCurrentPosition() + " vs " + motorBL.getTargetPosition());
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
        currentInstruction = it.next();
        // For now...
        //runInstruction();
    }


    void runInstruction(){

        instruction instruction = it.next();

        // instructionInProgress = true;

        instructionCount++;
        Log.e("STATE ERROR", currentState.toString());


        telemetry.addData("1 Left Current", motorFR.getCurrentPosition());
        telemetry.addData("2 Right Current", motorFL.getCurrentPosition());
        telemetry.addData("3 Left Current", motorBR.getCurrentPosition());
        telemetry.addData("4 Right Current", motorBL.getCurrentPosition());
        /*debugTelemetry("1", Integer.toString(instructionCount));
        // Reset encoders

        // Set target

        // Run to position

        // Run with encoders

        // Set motor power (.5 for test)
        */
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

    // Drive related code,
    // Courtesy of FTC 0409, with modifications from Noah Rose.
    // Code from thread: http://ftcforum.usfirst.org/showthread.php?4641-DC-Motor-Encoders-are-not-getting-Reset

    private enum States
    {
        STATE_DO_MOVE,
        STATE_WAIT_FOR_ENCODER_RESET,
        STATE_WAIT_FOR_MOVE_START,
        STATE_MOVING,
        STATE_DONE
    };

    private States currentState;

    public void doMove()
    {
        // In order to move, the motor encoders must get reset
        // After they are reset, then the move is started
        // Stay in the moving state until target encoder values
        // are reached. Then go on to the next script step

        // In order to move, the encoders have to reset to 0
        // which takes one cycle. Then once they are at 0,
        // then the mode has to set to run to position

        // Reset the encoders and set the state to STATE_RESET_ENCODER
        // once the current position are 0, then power will be applied
        // to the motors.
        setDriveMode(DcMotorController.RunMode.RESET_ENCODERS);
        currentState = States.STATE_WAIT_FOR_ENCODER_RESET;
    }

    /**
     * Returns true if the encoders are reset. Encoders are fully reset when the target position
     * is zero. The current position will reset to 0 fairly quick.
     * @return true if the encoders are reset
     */
    public boolean encodersAreReset() {
        boolean hasReset = false;
        int iterations = 0;
        for(DcMotor motor : hardwareMap.dcMotor)
        {
            if(iterations == 0)
            {
                hasReset = (motor.getTargetPosition() == 0);
            }
            else {
                hasReset = hasReset && (motor.getTargetPosition() == 0);
            }
            iterations += 1;
        }
        return hasReset;
    }

    /**
     * Returns if the encoders are zero. This is used to see if movement has started. It
     * assumes that the encoders were reset prior to making this call. If movement has started
     * and the isBusy flag works, then you know movement is done when isBusy is false.
     * @return true if current position of both motors are not zero
     */
    public boolean encodersAreZero() {
        boolean hasReset = false;
        int iterations = 0;
        for(DcMotor motor : hardwareMap.dcMotor)
        {
            if(iterations == 0)
            {
                hasReset = (motor.getCurrentPosition() == 0);
            }
            else {
                hasReset = hasReset && (motor.getCurrentPosition() == 0);
            }
            iterations += 1;
        }
        return hasReset;
    }

    /**
     * Check to see if the encoders are reset. Once they are reset, then the target positions
     * are set based on the desired movement. It then transitions to the START_MOVING state.
     */
    public void waitForEncoderReset(boolean straight,instruction currentInstruction, globalInfoPacket packet, double power) {
        if (encodersAreReset()) {
            // Encoders are reset so now power can be applied.
            int countsFR = 0;
            int countsFL = 0;
            int countsBR = 0;
            int countsBL = 0;
            int countsLeft = 0;
            int countsRight = 0;

            setDriveMode(DcMotorController.RunMode.RUN_TO_POSITION);
            //countsPerRotation / circumference * gearRatio;
            //counts = (int) Math.round(distanceToMove * countsPerInch);
            countsFR = (int)Math.round(currentInstruction.motorFRDest * (packet.encoderCPR / packet.wheelCircumference * packet.gearRatio));
            countsFL = (int)Math.round(currentInstruction.motorFLDest * (packet.encoderCPR / packet.wheelCircumference * packet.gearRatio));
            countsBR = (int)Math.round(currentInstruction.motorBRDest * (packet.encoderCPR / packet.wheelCircumference * packet.gearRatio));
            countsBL = (int)Math.round(currentInstruction.motorBLDest * (packet.encoderCPR / packet.wheelCircumference * packet.gearRatio));

            motorFR.setTargetPosition(countsFR);
            motorFL.setTargetPosition(countsFL);
            motorBR.setTargetPosition(countsBR);
            motorBL.setTargetPosition(countsBL);
            // Encoders control the direction of the motor so just send positive power
            power = Math.abs(power);
            setDrivePower(power, power, power, power);
            // Next step to is verify the robot is moving
            currentState = States.STATE_WAIT_FOR_MOVE_START;
            if(it.hasNext())
            {
                currentInstruction = it.next();
            }
        }
        // Encoders not reset, so do nothing - keep current state
    }

    /**
     * This is the third state for movement. This is to verify that movement has started by checking
     * that the encoders are not zero. If the isBusy works as I think it is suppose to work, the
     * isBusy flag will transition to false to true and return to false when the movement is done.
     * So don't check for isBusy false until it is seen to transition true. But since it does not
     * behave that way, this state is not really necessary.
     */
    public void waitForMoveStart() {
        // If the encoders are not 0 then moving has started
        if (!encodersAreZero()) {
            currentState = States.STATE_MOVING;
        }
    }

    /**
     * The fourth and final state of motion. This checks to see if the both motor encoders have
     * passed the target values. This assumes overshoot will happen. Ideally the isBusy could be
     * checked and wouldn't have to depend on overshoot.
     */
    public void moving() {
        boolean isMoving = false;
        int iterations = 0;
        for(DcMotor motor : hardwareMap.dcMotor)
        {
            if(iterations == 0)
            {
                isMoving = (motor.getCurrentPosition() >= motor.getTargetPosition());
            }
            else {
                isMoving = isMoving && (motor.getCurrentPosition() >= motor.getTargetPosition());
            }
            iterations += 1;
        }
        if(isMoving) {
                setDrivePower(0.0, 0.0, 0.0, 0.0);
                currentState = States.STATE_DONE;
        }
    }


    /**
     * Sets the drive mode for each motor.
     * The types of Run Modes are
     *   DcMotorController.RunMode.RESET_ENCODERS
     *      Resets the Encoder Values to 0
     *   DcMotorController.RunMode.RUN_TO_POSITION
     *      Runs until the encoders are equal to the target position
     *   DcMotorController.RunMode.RUN_USING_ENCODERS
     *      Attempts to keep the robot running straight utilizing
     *      the PID the reduces the maximum power by about 15%
     *   DcMotorController.RunMode.RUN_WITHOUT_ENCODERS
     *      Applies the power directly
     * @param mode
     */
    public void setDriveMode(DcMotorController.RunMode mode) {
        for(DcMotor motor : hardwareMap.dcMotor)
        {
            if(motor.getMode() != mode){
                motor.setMode(mode);
            }
        }
    }

    /**
     * Set the power to left and right motors, the values must range
     * between -1 and 1.
     */
    public void setDrivePower(double FR, double FL, double BR, double BL) {
        // This assumes power is given as -1 to 1
        // The range clip will make sure it is between -1 and 1
        // An incorrect value can cause the program to exception
        motorFR.setPower(Range.clip(FR, -1.0, 1.0));
        motorFL.setPower(Range.clip(FL, -1.0, 1.0));
        motorBR.setPower(Range.clip(BR, -1.0, 1.0));
        motorBL.setPower(Range.clip(BL, -1.0, 1.0));
    }
}
