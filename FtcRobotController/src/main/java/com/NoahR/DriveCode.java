package com.NoahR;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
/**
 * Created by Noah Rose-Ledesma on 1/9/2016.
 * Handles drive code
 */
public class DriveCode {
    public int calculateRotations(int distance, int encoderCPR, float gearRatio, float wheelCircumference){
        if(encoderCPR == 0 || gearRatio == 0.0f || wheelCircumference == 0.0f){
            return 0;
        }
        return (int)(encoderCPR * (distance / wheelCircumference) * gearRatio);
    }
    public void setupMotors(DcMotor motorFR,DcMotor motorFL, DcMotor motorBR, DcMotor motorBL) {
        motorFR.setDirection(DcMotor.Direction.FORWARD);
        motorFL.setDirection(DcMotor.Direction.FORWARD);
        motorBL.setDirection(DcMotor.Direction.FORWARD);
        motorBR.setDirection(DcMotor.Direction.FORWARD);

        motorFR.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFL.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBR.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFL.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }
    public void resetEncoders(DcMotor motorFR, DcMotor motorFL, DcMotor motorBL, DcMotor motorBR)
    {
        motorFR.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorFL.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBR.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBL.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
    }
    public void setTargetPosition(DcMotor motorFR, DcMotor motorFL, DcMotor motorBL, DcMotor motorBR, int FRRotations, int FLRotations, int BRRotations, int BLRotations)
    {
        motorFR.setTargetPosition(FRRotations);
        motorFL.setTargetPosition(FLRotations);
        motorBR.setTargetPosition(BRRotations);
        motorBL.setTargetPosition(BLRotations);
    }
    public void runToPosition(DcMotor motorFR, DcMotor motorFL, DcMotor motorBL, DcMotor motorBR)
    {
        motorFR.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorFL.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorBR.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorBL.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }
    public void runUsingEncoders(DcMotor motorFR, DcMotor motorFL, DcMotor motorBL, DcMotor motorBR)
    {
        motorFR.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFL.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBR.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBL.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }
    public void setPower(DcMotor motorFR, DcMotor motorFL, DcMotor motorBL, DcMotor motorBR, float FRPower, float FLPower, float BRPower, float BLPower)
    {
        motorFR.setPower(FRPower);
        motorFL.setPower(FLPower);
        motorBR.setPower(BRPower);
        motorBL.setPower(BLPower);
    }

}
