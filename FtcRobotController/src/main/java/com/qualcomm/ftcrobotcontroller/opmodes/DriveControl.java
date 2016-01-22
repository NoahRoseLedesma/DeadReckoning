package com.qualcomm.ftcrobotcontroller.opmodes;

import com.NoahR.instruction;
import com.NoahR.instructionSet;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;

import java.util.Iterator;

/**
 * Created by Noah Rose-Ledesma on 1/22/2016.
 */
public class DriveControl extends OpMode{

        // These must be adjusted based on the robot built
        private static double countsPerRotation = 1440.0;
        private static double wheelDiameter = 4.0;
        private static double gearRatio = 1.0;
        private static double circumference = wheelDiameter * Math.PI;
        private static double countsPerInch = countsPerRotation / circumference * gearRatio;
        private static double countsPerDegree = 29000.0 / 5.0 / 360.0;

        private static double distanceToMove = 24.0;  // inches
        private static final double powerToUse = 1.0;       // Positive forward, negative backwards.

        // To go straight, Left Counts increases, Right Counts decreases
        // To go backward, Left Counts decreases, Right Counts increases
        private static final boolean leftReversed = true;
        private static final boolean rightReversed = true;

        instructionSet<instruction> instructions;
        private Iterator<instruction> iterator;

        private enum States
        {
            STATE_DO_MOVE,
            STATE_WAIT_FOR_ENCODER_RESET,
            STATE_WAIT_FOR_MOVE_START,
            STATE_MOVING,
            STATE_DONE
        };

        private States currentState;

        public DriveControl() {

        }

        @Override
        public void init() {

            // The strings must match names given in Settings->Configure Robot
            deadReckoning.deadReckoningObjectAtomicReference.get().mapHardware();
            // Set the drive mode to run to a specific encoder value
            setDriveMode(DcMotorController.RunMode.RUN_TO_POSITION);

            setDrivePower(0.0, 0.0);

            currentState = States.STATE_DO_MOVE;


        }

        @Override
        public void start() {
            iterator = instructions.iterator();
            distanceToMove = iterator.next().motorFRDest;
            countsPerRotation = instructions.globalInfoPacket.encoderCPR;
            gearRatio = instructions.globalInfoPacket.gearRatio;
            circumference = instructions.globalInfoPacket.wheelCircumference;
            countsPerInch = countsPerRotation / circumference * gearRatio;
        }
        // This is generally the "forever" loop in RobotC. But don't do a forever loop
        // The FTC App will constantly call this loop code.
        @Override
        public void loop() {

            // Do operation based on the current state.
            switch (currentState) {
                case STATE_DO_MOVE:
                    doMove();
                    break;

                case STATE_WAIT_FOR_ENCODER_RESET:
                    waitForEncoderReset(true,distanceToMove,powerToUse);
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
                    // Next movement
                    if(iterator.hasNext()) {
                        distanceToMove = iterator.next().motorFRDest;
                        currentState = States.STATE_DO_MOVE;
                    }
                    break;
            }

        }


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
            return (deadReckoning.deadReckoningObjectAtomicReference.get().motorFR.getTargetPosition() == 0 && deadReckoning.deadReckoningObjectAtomicReference.get().motorFL.getTargetPosition() == 0);
        }

        /**
         * Returns if the encoders are zero. This is used to see if movement has started. It
         * assumes that the encoders were reset prior to making this call. If movement has started
         * and the isBusy flag works, then you know movement is done when isBusy is false.
         * @return true if current position of both motors are not zero
         */
        public boolean encodersAreZero() {
            return (deadReckoning.deadReckoningObjectAtomicReference.get().motorFR.getCurrentPosition() == 0 && deadReckoning.deadReckoningObjectAtomicReference.get().motorFL.getCurrentPosition() == 0);
        }

        /**
         * Check to see if the encoders are reset. Once they are reset, then the target positions
         * are set based on the desired movement. It then transitions to the START_MOVING state.
         */
        public void waitForEncoderReset(boolean straight,double distance,double power) {
            if (encodersAreReset()) {
                // Encoders are reset so now power can be applied.
                int counts = 0;
                int countsLeft = 0;
                int countsRight = 0;

                setDriveMode(DcMotorController.RunMode.RUN_TO_POSITION);
                counts = (int) Math.round(distanceToMove * countsPerInch);
                if (straight == true)
                {
                    // Determine the number of counts for the distance to go
                    counts = (int) Math.round(distance * countsPerInch);
                    // If power is less than 0, then go backwards
                    // If power is greater than 0, then go forward
                    if (power < 0)
                    {
                        counts = -counts;
                    }

                    // A local variable is used to know whether
                    // the motor is "reversed" versus settin a motor direction.
                    // Therefore the target counts will be positive for one side and negative for the
                    // other.
                    if (leftReversed)
                    {
                        countsLeft = -counts;
                    }
                    else
                    {
                        countsLeft = counts;
                    }
                    if (rightReversed)
                    {
                        countsRight = -counts;
                    }
                    else
                    {
                        countsRight = counts;
                    }
                }
                else
                {
                    // Determine the number of counts for the degrees to turn
                    counts = (int) Math.round(distance * countsPerDegree);

                    // If power is less than 0, then go left
                    // If power is greater than 0, then go right
                    if (power < 0)
                    {
                        counts = -counts;
                    }

                    // To go left and left side NOT reversed, then left counts should be negative
                    // which if power is less than 0 then they already be negative
                    if (leftReversed)
                    {
                        countsLeft = -counts;
                    }
                    else
                    {
                        countsLeft = counts;
                    }

                    // To go right and right IS reversed, then right counts should be negative
                    // but for going right the counts are positive so take the opposite sign if
                    // reversed
                    if (rightReversed)
                    {
                        countsRight = counts;
                    }
                    else
                    {
                        countsRight = -counts;
                    }
                }
                deadReckoning.deadReckoningObjectAtomicReference.get().motorFL.setTargetPosition(countsLeft);
                deadReckoning.deadReckoningObjectAtomicReference.get().motorFR.setTargetPosition(countsRight);
                // Encoders control the direction of the motor so just send positive power
                setDrivePower(Math.abs(power),Math.abs(power));
                // Next step to is verify the robot is moving
                currentState = States.STATE_WAIT_FOR_MOVE_START;
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
            if ( (Math.abs(deadReckoning.deadReckoningObjectAtomicReference.get().motorFL.getCurrentPosition()) > Math.abs(deadReckoning.deadReckoningObjectAtomicReference.get().motorFL.getTargetPosition())) &&
                    (Math.abs(deadReckoning.deadReckoningObjectAtomicReference.get().motorFR.getCurrentPosition()) > Math.abs(deadReckoning.deadReckoningObjectAtomicReference.get().motorFR.getTargetPosition()))) {
                // Robot is done moving - go to the next step in the script
                setDrivePower(0.0, 0.0);
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

            if (deadReckoning.deadReckoningObjectAtomicReference.get().motorFL.getChannelMode() != mode) {
                deadReckoning.deadReckoningObjectAtomicReference.get().motorFL.setChannelMode(mode);
            }

            if (deadReckoning.deadReckoningObjectAtomicReference.get().motorFR.getChannelMode() != mode) {
                deadReckoning.deadReckoningObjectAtomicReference.get().motorFR.setChannelMode(mode);
            }
        }

        /**
         * Set the power to left and right motors, the values must range
         * between -1 and 1.
         * @param left
         * @param right
         */
        public void setDrivePower(double left, double right) {
            // This assumes power is given as -1 to 1
            // The range clip will make sure it is between -1 and 1
            // An incorrect value can cause the program to exception
            deadReckoning.deadReckoningObjectAtomicReference.get().motorFL.setPower(Range.clip(left, -1.0, 1.0));
            deadReckoning.deadReckoningObjectAtomicReference.get().motorFR.setPower(Range.clip(right, -1.0, 1.0));
        }
}
