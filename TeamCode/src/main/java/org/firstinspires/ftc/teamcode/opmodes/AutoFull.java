
package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.robotutil.DriveTrain;
import org.firstinspires.ftc.teamcode.robotutil.Functions;
import org.firstinspires.ftc.teamcode.robotutil.IMU;
import org.firstinspires.ftc.teamcode.robotutil.MRColorSensor;
import org.firstinspires.ftc.teamcode.robotutil.Team;
import org.firstinspires.ftc.teamcode.robotutil.VuMark;
import org.lasarobotics.vision.util.color.Color;

@Autonomous(name = "AutoFull")
public class AutoFull extends LinearOpMode {
    static double jewelArmInitPosition = 0, jewelArmDownPos = 0.8, jewelArmUpPos = 0.3;
    static DcMotor rF, rB, lF, lB, flywheel1, flywheel2, sweeperLow;
    static GyroSensor gyro;
    static Servo jewelArm;
    boolean red = false;
    DriveTrain driveTrain;
    ColorSensor jewelColor;
    ColorSensor cryptoColor;
    MRColorSensor colorSensor;
    int startingPos = 0; //0 IS CORNER POSITION, 1 IS SANDWITCH POSITION
    int state;
    private BNO055IMU adaImu;
    private IMU imu;
    public int crypHeading = 0;
    VuMark vm;

    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        options();
        waitForStart();
        state = 0;// Todo:
        if (opModeIsActive()) {

            jewelColor.enableLed(true);
            jewelArm.setPosition(jewelArmDownPos);
            Functions.waitFor(2000);
            telemetry.addData("Red", colorSensor.getRed());
            telemetry.addData("Blue", colorSensor.getBlue());
            telemetry.addData("Correct color: " , colorSensor.correctColor());
            telemetry.update();
            hitJewel(0.2,2);
            //hitJewelRotate(20,0.2,10);
            Functions.waitFor(1000);
            jewelArm.setPosition(jewelArmUpPos);
            Functions.waitFor(1000);
            //RelicRecoveryVuMark vumark = vm.detectColumn(5);
            if (startingPos == 0){
                if(red) {
                    driveTrain.encoderDriveIMU(0.4, 36, DriveTrain.Direction.FORWARD, 10);
                } else{
                    driveTrain.encoderDriveIMU(0.4, 36, DriveTrain.Direction.BACKWARD, 10);
                }
                Functions.waitFor(1000);
                driveTrain.rotateIMURamp(-90,.4, 10, telemetry);
                Functions.waitFor(1000);
                dumpBlock(1,1,500,3000);
                Functions.waitFor(1000);
                driveTrain.encoderDriveIMU(0.5,1000, DriveTrain.Direction.BACKWARD,4);

            }
            else{
                driveTrain.rotateIMURamp(30,.4, 10, telemetry);
                Functions.waitFor(1000);

                driveTrain.encoderDriveIMU(0.4, 36, DriveTrain.Direction.FORWARD, 10);
                Functions.waitFor(1000);

                if(red) {
                    driveTrain.rotateIMURamp(180,.4, 10, telemetry);
                    Functions.waitFor(1000);

                }
                dumpBlock(1,1,500,3000);
                Functions.waitFor(1000);
                driveTrain.encoderDriveIMU(0.5,1000, DriveTrain.Direction.BACKWARD,4);

            }

        }
    }
    public void dumpBlock(double slidePower, double rollerPower, int slideTime, int rollerTime){
        driveTrain.moveSlidesTime(slidePower,slideTime);
        driveTrain.moveRollersTime(rollerPower,slideTime);
        driveTrain.moveSlidesTime(-slidePower,slideTime);
    }
    public void hitJewel(double speed, int dist){
        if(colorSensor.wrongColor()){
            driveTrain.encoderDriveIMU(speed,dist, DriveTrain.Direction.BACKWARD,3);
            driveTrain.encoderDriveIMU(speed,dist, DriveTrain.Direction.FORWARD,3);
        }else{
            driveTrain.encoderDriveIMU(speed,dist, DriveTrain.Direction.FORWARD,3);
            driveTrain.encoderDriveIMU(speed,dist, DriveTrain.Direction.BACKWARD,3);
        }
    }

    public void hitJewelRotate(int angle,double speed, int timeOutS){
        angle = Math.abs(angle);
        if(colorSensor.wrongColor()){
            driveTrain.rotateIMURamp(angle,speed,timeOutS,telemetry);
            Functions.waitFor(500);
            driveTrain.rotateIMURamp(-angle,speed,timeOutS,telemetry);
        }else{
            driveTrain.rotateIMURamp(-angle,speed,timeOutS,telemetry);
            Functions.waitFor(500);
            driveTrain.rotateIMURamp(angle,speed,timeOutS,telemetry);
        }
    }

    public void initHardware() {
        rF = hardwareMap.dcMotor.get("rF");
        rB = hardwareMap.dcMotor.get("rB");
        lF = hardwareMap.dcMotor.get("lF");
        lB = hardwareMap.dcMotor.get("lB");
        lB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rF.setDirection(DcMotor.Direction.FORWARD);
        rB.setDirection(DcMotor.Direction.FORWARD);
        lB.setDirection(DcMotor.Direction.REVERSE);
        lF.setDirection(DcMotor.Direction.REVERSE);
        jewelArm = hardwareMap.servo.get("jewelArm");
        jewelArm.setPosition(jewelArmInitPosition);
        adaImu = hardwareMap.get(BNO055IMU.class, "imu");
        imu = new IMU(adaImu);
        jewelColor = hardwareMap.colorSensor.get("jewelColor");
        //cryptoColor = hardwareMap.colorSensor.get("cryptoColor");
        colorSensor = new MRColorSensor(jewelColor, this);
        driveTrain = new DriveTrain(this);
        driveTrain.detectAmbientLight(jewelColor);
        vm = new VuMark(this);

    }

    public void options(){
        telemetry.addData("Team", "Blue");
        telemetry.update();
        boolean confirmed = false;
        red = false;
        while(!confirmed){
            if (gamepad1.a){
                red = true;
                colorSensor.team = Team.RED;

                telemetry.addData("Team", red ? "Red": "Blue");
            }
            if (gamepad1.b){
                red = false;
                colorSensor.team = Team.BLUE;

                telemetry.addData("Team", red ? "Red": "Blue");
            }
            if (gamepad1.x){
                startingPos = 0;

                telemetry.addData("Starting Position", startingPos ==0 ? "corner": "sandwich");
            }
            if (gamepad1.y){
                startingPos = 1;

                telemetry.addData("Starting Position", startingPos ==0 ? "corner": "sandwich");
            }
            telemetry.update();

            if (gamepad1.left_stick_button && gamepad1.right_stick_button){
                telemetry.addData("Team", red ? "Red" : "Blue");
                telemetry.addData("Starting Position", startingPos ==0 ? "corner": "sandwich");
                telemetry.addData("Confirmed!", "");
                telemetry.update();
                confirmed = true;
            }

        }
    }

    private void haltUntilPressStart() {
        while (!gamepad1.start  && !isStopRequested()) {
            Functions.waitFor(300);
        }
    }
}
