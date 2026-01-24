package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
/*
The MOST Sigma and cool programmer David C did write this :) {insert sunglasses emoji} 😎😎😎😎😎😎
You're welcome guys!!!!! 😎😎😎😎😎
(I didn't steal this from a youtube video wdym) 😎
ok fine its the (flywheel going 30 times faster) video you caught me 😎😎😎
- My flywheel now spins at 120,000 rpm after watching :)) 😎😎😎😎😎😎😎😎😎😎😎😎
You can get an in depth guide into how the tuning works there

BUT

If you want to read my cool and awesome guide instead here goes!!!

Ok so you increase F until you get to the right speed about for both the low and the high
 (Y) swaps between the two targets
THEN
- you increase P so that the error is reduced and it goes between them fast
- if it goes over and under over and over again
    DECREASE YOUR P

LAST STEP
do NOT stop the code, write down the numbers first!!!
You then put those in the Teleop and you are done :)))))

HUZZAH!
If you guys can't find where to put the PIDFCOEFFICIENTS then you people in Florida may have mental damage :)
the capitalized word is a hint btw
 */
@TeleOp
public class PIDF_tuner extends OpMode
{
    //Might have to change this to just be flywheel since that's what its called for our bot.
    public DcMotorEx FlywheelMotor;
    public double highVelocity = 1500;
    public double lowVelocity = 900;

    double currTargetVelocity = lowVelocity;

    double F = 0;
    double P = 0;

    double[] stepSizes = {10.0, 1.0, 0.1, 0.01, 0.001, 0.0001};

    int stepIndex = 1;

    @Override
    public void init()
    {
        //Or it might be this one that i have to change i am unsure
        FlywheelMotor = hardwareMap.get(DcMotorEx.class, "flywheel");
        FlywheelMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        //FlywheelMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0,  0, F);
        FlywheelMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        telemetry.addLine("Init Complete");
    }

    @Override
    public void loop()
    {
        if (gamepad1.yWasPressed()) {
            if (currTargetVelocity == highVelocity) {
                currTargetVelocity = lowVelocity;
            } else {
                currTargetVelocity = highVelocity;
            }
        }
        if (gamepad1.bWasPressed()) {
            stepIndex = (stepIndex + 1) % stepSizes.length;
        }
        if (gamepad1.dpadLeftWasPressed()) {
            F -= stepSizes[stepIndex];
        }
        if (gamepad1.dpadRightWasPressed()) {
            F += stepSizes[stepIndex];
        }

        if (gamepad1.dpadUpWasPressed()) {
            P += stepSizes[stepIndex];
        }
        if (gamepad1.dpadDownWasPressed()) {
            P -= stepSizes[stepIndex];
        }

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        FlywheelMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        FlywheelMotor.setVelocity(currTargetVelocity);

        double curVelocity = FlywheelMotor.getVelocity();
        double error = currTargetVelocity - curVelocity;

        telemetry.addData("TargetVelocity", currTargetVelocity);
        telemetry.addData("Current Velocity", "%.2f", curVelocity);
        telemetry.addData("Error", "%.2f", error);
        telemetry.addLine("-------------------------------------");
        telemetry.addData("Tuning P", "%.4f (D-Pad U/D)", P);
        telemetry.addData("Tuning F", "%.4f (D-Pad L/R)", F);
        telemetry.addData("Step Size", "%.4f (B Button)", stepSizes[stepIndex]);



    }
}
