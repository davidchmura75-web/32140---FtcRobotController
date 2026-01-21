package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

/*
The MOST Sigma and cool programmer David C 😎 did not right this :(
But this is low key the code given on the website that I just copied
So I have no idea if it works :)
Hopefully it does ;)
Good luck Abyan and Abyan's Programmer Friend!!! 🥰🥰🥰

- Yes I know its 'write' not 'right'

- I also did a lil flywheel fix :)
 */
@TeleOp
public class Teleop extends LinearOpMode {

    private DcMotorEx flywheel;
    private DcMotor coreHex;
    private DcMotor leftDrive;
    private CRServo servo;
    private DcMotor rightDrive;

    // Setting our velocity targets. These values are in ticks per second!
    private static final int bankVelocity = 1300;
    private static final int farVelocity = 1900;
    private static final int maxVelocity = 2200;
    // FILL THESE IN ONCE THE TUNING HAS BEEN DONE ALLIGATOR PEOPLE (also uncomment the line, I have no idea how much programming you know)
    // private PIDFCoefficients pidfCoefficients = new PIDFCoefficients(0,0,0,0);

    @Override
    public void runOpMode() {
        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        coreHex = hardwareMap.get(DcMotor.class, "coreHex");
        leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
        servo = hardwareMap.get(CRServo.class, "servo");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");

        // Establishing the direction and mode for the motors
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel.setDirection(DcMotor.Direction.REVERSE);
        // THE MOST COOL PROGRAMMER EVER HAS BLESSED YOU WITH THIS LINE
        // CONGRATULATIONS!!!
        // flywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        coreHex.setDirection(DcMotor.Direction.REVERSE);
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        //Ensures the servo is active and ready
        servo.setPower(0);

        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                // Calling our methods while the OpMode is running
                splitStickArcadeDrive();
                setFlywheelVelocity();
                manualCoreHexAndServoControl();
                telemetry.addData("Flywheel Velocity", flywheel.getVelocity());
                telemetry.addData("Flywheel Power", flywheel.getPower());
                telemetry.update();
            }
        }
    }

    /**
     * Controls for the drivetrain. The robot uses a split stick stlye arcade drive.
     * Forward and back is on the left stick. Turning is on the right stick.
     */
    private void splitStickArcadeDrive() {
        float x;
        float y;

        x = gamepad1.right_stick_x;
        y = -gamepad1.left_stick_y;
        leftDrive.setPower(y - x);
        rightDrive.setPower(y + x);
    }

    /**
     * Manual control for the Core Hex powered feeder and the agitator servo in the hopper
     */
    private void manualCoreHexAndServoControl() {
        // Manual control for the Core Hex intake
        if (gamepad1.cross) {
            coreHex.setPower(0.5);
        } else if (gamepad1.triangle) {
            coreHex.setPower(-0.5);
        }
        // Manual control for the hopper's servo
        if (gamepad1.dpad_left) {
            servo.setPower(1);
        } else if (gamepad1.dpad_right) {
            servo.setPower(-1);
        }
    }

    /**
     * This if/else statement contains the controls for the flywheel, both manual and auto.
     * Circle and Square will spin up ONLY the flywheel to the target velocity set.
     * The bumpers will activate the flywheel, Core Hex feeder, and servo to cycle a series of balls.
     */
    private void setFlywheelVelocity() {
        if (gamepad1.options) {
            flywheel.setPower(-0.5);
        } else if (gamepad1.left_bumper) {
            farPowerAuto();
        } else if (gamepad1.right_bumper) {
            bankShotAuto();
        } else if (gamepad1.circle) {
            flywheel.setVelocity(bankVelocity);
        } else if (gamepad1.square) {
            flywheel.setVelocity(maxVelocity);
        } else {
            flywheel.setVelocity(0);
            coreHex.setPower(0);
            // The check below is in place to prevent stuttering with the servo. It checks if the servo is under manual control!
            if (!gamepad1.dpad_right && !gamepad1.dpad_left) {
                servo.setPower(0);
            }
        }
    }

    /**
     * The bank shot or near velocity is intended for launching balls touching or a few inches from the goal.
     * When running this function, the flywheel will spin up and the Core Hex will wait before balls can be fed.
     * The servo will spin until the bumper is released.
     */
    private void bankShotAuto() {
        /* I HAD TO TAKE OUT LIKE 10 OF THESE DC MOTOR EX CASTS BECAUSE THE PEOPLE AT REV ARE UGLY AND HAVE NEVER TALKED TO ANY WOMEN
        * I hate rev
        * Rev sucks
        * cheap ahh company
        * They disgust me 🥰
        * and now I find out they suck at coding
        * buy gobilda next year our team learned that lesson way too late
        * all the good teams use gobilda😎
        * Do that :) (if you can its somewhat expensive, so its for sure not mandatory especially for a team that meets twice a week)
        * btw I had to get rid of that DcMotorEx type thing down here like 50 times
        * JUST CALL THE FLYWHEEL MOTOR A DCMOTOR EX
        * ITS REALLY NOT THAT HARD
        * YOU STUPID STUPID IDIOTS REv
        * it was more like 10 times I cannot count
        * except I can lowkey hit to 25 in hindi 😎
        * chah sata, chah sata 😎😎
         ((DcMotorEx) flywheel).setVelocity(bankVelocity); */
        servo.setPower(-1);
        if (flywheel.getVelocity() >= bankVelocity - 50) {
            coreHex.setPower(1);
        } else {
            coreHex.setPower(0);
        }
    }

    /**
     * The far power velocity is intended for launching balls a few feet from the goal. It may require adjusting the deflector.
     * When running this function, the flywheel will spin up and the Core Hex will wait before balls can be fed.
     * The servo will spin until the bumper is released.
     */
    private void farPowerAuto() {
        flywheel.setVelocity(farVelocity);
        servo.setPower(-1);
        if (flywheel.getVelocity() >= farVelocity - 100) {
            coreHex.setPower(1);
        } else {
            coreHex.setPower(0);
        }
    }

}
