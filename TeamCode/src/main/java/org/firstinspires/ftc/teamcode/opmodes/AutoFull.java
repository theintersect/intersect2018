package org.firstinspires.ftc.teamcode.opmodes;
import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Func;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import org.firstinspires.ftc.teamcode.robotutil.DriveTrain;
import org.firstinspires.ftc.teamcode.robotutil.Functions;
import org.firstinspires.ftc.teamcode.robotutil.IMU;
import org.firstinspires.ftc.teamcode.robotutil.MRColorSensor;
import org.firstinspires.ftc.teamcode.robotutil.Team;
import org.firstinspires.ftc.teamcode.robotutil.VuMark;
@Autonomous(name = "TouchAuto")
public class AutoFull extends LinearOpMode {
    static double jewelArmInitPosition = .3, jewelArmDownPos = 0.9, jewelArmUpPos = 0.35 , cryptoDownPos = 0, cryptoUpPos = .5;
    static DcMotor rF, rB, lF, lB;
    static GyroSensor gyro;
    static Servo jewelArm;
    static Servo flipServo;
    static Servo jewelFinger;
    boolean red = false;
    DriveTrain driveTrain;
    ColorSensor jewelColor;
    MRColorSensor colorSensor;
    StartingPositions startingPos = StartingPositions.CORNER;
    int state;
    private BNO055IMU adaImu;
    private IMU imu;
    public int crypHeading = 0;
    VuMark vm;
    RelicRecoveryVuMark vumark;
    Boolean garb = false;
    TouchSensor touch;

    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
//       options();
        red = true;
        colorSensor.team = Team.RED;
        startingPos = StartingPositions.CORNER;
//        default is center
        vumark = RelicRecoveryVuMark.CENTER;
        while(opModeIsActive()){
            vumark = vm.detectColumn(5);
            telemetry.addData("vumark", vumark);
            Functions.waitFor(2000);
        }
        waitForStart();
        long time = System.currentTimeMillis();
        driveTrain.touch.setMode(DigitalChannel.Mode.INPUT);

        if (opModeIsActive()) {

            vumark = vm.detectColumn(5);
            telemetry.addData("vumark", vumark);

            jewel();

//            jewelArm.setPosition(jewelArmDownPos);
//            jewelArm.setPosition(jewelArmInitPosition - .15);
//            jewelArm.setPosition(jewelArmInitPosition);
            Functions.waitFor(200);
            driveTrain.encoderDrive(.2, 5,DriveTrain.Direction.FORWARD, 5);
            jewelArm.setPosition(jewelArmInitPosition);
            driveTrain.encoderDrive(.5, 21,DriveTrain.Direction.FORWARD, 5);
            Functions.waitFor(100);
            driveTrain.rotateIMURamp(-90, .5, 5, this.telemetry);
            Functions.waitFor(100);
            driveTrain.encoderDrive(.5,30, DriveTrain.Direction.BACKWARD,10);
            driveTrain.encoderDrive(.5,3, DriveTrain.Direction.FORWARD,10);
//            driveTrain.strafeImuEncoderPDD(DriveTrain.Direction.LEFT, .3, 8, 4);
//            Functions.waitFor(200);
//            driveTrain.strafeImuEncoderPDD(DriveTrain.Direction.RIGHT, .2, 2, 2);
//            Functions.waitFor(100);
//            driveTrain.encoderDrive(.4, 6.35, DriveTrain.Direction.BACKWARD, 4);
//            Functions.waitFor(100);
            driveTrain.touchServoRight.setPosition(driveTrain.touchDownPos);
            Functions.waitFor(1300);
            driveTrain.strafeImuPDD(DriveTrain.Direction.RIGHT, .4, 4);
            Functions.waitFor(200);
            driveTrain.touchServoRight.setPosition(driveTrain.touchUpPos);
            Functions.waitFor(200);
            driveTrain.columnBlockRed(vumark);



//            if (red) {
//
//                if (startingPos == StartingPositions.CORNER) {
//                    driveTrain.encoderDrive(0.4, 10, DriveTrain.Direction.FORWARD, 3);
//                    vumark = vm.detectColumn(5);
//                    driveTrain.encoderDrive(.4, 10, DriveTrain.Direction.FORWARD, 4);
//                    driveTrain.rotateIMURamp(-90, .4,1, this.telemetry);
//                    driveTrain.moveRightUntilTouch(.2,5000);
//                    driveTrain.columnBlockRed(vumark);
//
//                } else {
//
//                    driveTrain.encoderDrive(0.5, 10, DriveTrain.Direction.BACKWARD, 10);
//                    vumark = vm.detectColumn(5);
//                }
//
//            } else {
//                if (startingPos == StartingPositions.CORNER) {
//                    driveTrain.encoderDrive(0.4, 10, DriveTrain.Direction.FORWARD, 3);
//                    vumark = vm.detectColumn(5);
//                    driveTrain.encoderDrive(.4, 25, DriveTrain.Direction.BACKWARD, 4);
//                    driveTrain.rotateIMURamp(-90, .5, 5, telemetry);
//                    driveTrain.moveUntilTouchBlue(5000);
//                    driveTrain.columnBlockBlue(vumark);
//
//                } else {
//                    flipServo.setPosition(0);
//                    driveTrain.encoderDrive(0.5, 60, DriveTrain.Direction.BACKWARD, 10);
//                    driveTrain.encoderDrive(0.5, 10, DriveTrain.Direction.FORWARD, 10);
//                }
//
//
//            }
        }
    }
    public void telemetry(String field1,String field2){
        telemetry.addData(field1 + ": ",field2);
        telemetry.update();
    }
    public void telemetry(String field1,int field2){
        telemetry.addData(field1 + ": ",field2);
        telemetry.update();
    }
    public void telemetry(String field1,double field2){
        telemetry.addData(field1 + ": ",field2);
        telemetry.update();
    }
    public void jewel(){

        driveTrain.encoderDrive(0.1,1.5, DriveTrain.Direction.FORWARD,5);
        Functions.waitFor(500);
        driveTrain.rotateIMURamp(6 ,0.4,5,telemetry);
        Functions.waitFor(500);
        jewelArm.setPosition(jewelArmDownPos);
        Functions.waitFor(1000);
//        colorSensor.telemetryDebug(this);
        if(colorSensor.correctColor()){
            telemetry("color","red");
            knockJewel(DriveTrain.Direction.BACKWARD);
        }else {
            telemetry("color","Blue");
            knockJewel(DriveTrain.Direction.FORWARD);
        }
//        Functions.waitFor(3000);
    }

    public void redCorner(){
        driveTrain.encoderDrive(.6,27,DriveTrain.Direction.FORWARD,10);
        driveTrain.rotateIMURamp(-90,.4,10,telemetry);
        driveTrain.encoderDrive(.6,27,DriveTrain.Direction.BACKWARD,10);
    }
    public void blueCorner(){
        driveTrain.encoderDrive(0.4, 26, DriveTrain.Direction.BACKWARD, 10);
        driveTrain.rotateIMURamp(-90, .5, 5, telemetry);
        flipServo.setPosition(0);
        driveTrain.encoderDrive(0.5, 20, DriveTrain.Direction.BACKWARD, 10);
        driveTrain.encoderDrive(0.5, 10, DriveTrain.Direction.FORWARD, 10);
    }
    public void redSandwich(){
        flipServo.setPosition(0);
        driveTrain.encoderDrive(0.5, 60, DriveTrain.Direction.BACKWARD, 10);
        driveTrain.encoderDrive(0.5, 10, DriveTrain.Direction.FORWARD, 10);
    }
    public void blueSandwich(){
        flipServo.setPosition(0);
        driveTrain.encoderDrive(0.5, 60, DriveTrain.Direction.BACKWARD, 10);
        driveTrain.encoderDrive(0.5, 10, DriveTrain.Direction.FORWARD, 10);
    }

    public enum StartingPositions {
        CORNER, SANDWICH;
    }
    public void knockJewel(DriveTrain.Direction direction) {

        jewelArm.setPosition(jewelArmDownPos-.05);
        Functions.waitFor(100);


        if(direction == DriveTrain.Direction.BACKWARD) {
            //COUNTERINTUITIVE, BUT THIS CONTROLS THE KNOCK FORWARDS (COUNTERCLOCKWISE)
            driveTrain.rotateIMURamp(-7,0.4,5,telemetry);
//            jewelArm.setPosition(jewelArmInitPosition);
            Functions.waitFor(500);
        } else{
            //COUNTERINTUITIVE, BUT THIS CONTROLS THE KNOCK BACKWARDS (CLOCKWISE)

            driveTrain.rotateIMURamp(10,0.4,5,telemetry);
            jewelArm.setPosition(jewelArmInitPosition);
            Functions.waitFor(500);
            driveTrain.rotateIMURamp(-20,0.4,5,telemetry);
        }
    }

    public void dumpBlock(double slidePower, double rollerPower, int slideTime, int rollerTime){
        driveTrain.moveSlidesTime(slidePower,slideTime);
        driveTrain.moveRollersTime(rollerPower,slideTime);
        driveTrain.moveSlidesTime(-slidePower,slideTime/2);
    }


    public void fullJewel(){
        jewelArm.setPosition(jewelArmDownPos);
        jewelArm.setPosition(jewelArmUpPos);
    }

//    public void moveToCrypto(){
//        if (colorSensor.team == colorSensor.team.BLUE) {
//            driveTrain.encoderDriveIMU(0.3,30, DriveTrain.Direction.BACKWARD,10);
//        }
//    }

    public void dumpBlock(){

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

//        cryptoArm = hardwareMap.servo.get("cryptoArm");
        jewelArm = hardwareMap.servo.get("jewelArm");
        jewelArm.setDirection(Servo.Direction.REVERSE);
        jewelColor = hardwareMap.colorSensor.get("jewelColor");

        adaImu = hardwareMap.get(BNO055IMU.class, "imu");

        flipServo = hardwareMap.servo.get("flipServo");
        flipServo.setDirection(Servo.Direction.REVERSE);
        flipServo.setPosition(1);



       
        jewelArm.setPosition(jewelArmInitPosition);

        imu = new IMU(adaImu);

        colorSensor = new MRColorSensor(jewelColor, this);
        driveTrain = new DriveTrain(this);
        driveTrain.touchServoRight.setPosition(driveTrain.touchUpPos);

//        driveTrain.detectAmbientLight(jewelColor);
        vm = new VuMark(this);

    }

//    public void options(){
//        telemetry.addData("Team", "Blue");
//        telemetry.update();
//        boolean confirmed = false;
//        red = false;
//        while(!confirmed){
//            if (gamepad1.a){
//                red = true;
//                colorSensor.team = Team.RED;
//
//                telemetry.addData("Team", red ? "Red": "Blue");
//            }
//            if (gamepad1.b){
//                red = false;
//                colorSensor.team = Team.BLUE;
//
//                telemetry.addData("Team", red ? "Red": "Blue");
//            }
//            if (gamepad1.x){
//                startingPos = StartingPositions.CORNER;
//
//                telemetry.addData("Starting Position", startingPos == StartingPositions.CORNER ? "corner": "sandwich");
//            }
//            if (gamepad1.y){
//                startingPos = StartingPositions.SANDWICH;
//
//                telemetry.addData("Starting Position", startingPos == StartingPositions.CORNER ? "corner": "sandwich");
//            }
//
//            telemetry.update();
//
//            if (gamepad1.left_stick_button && gamepad1.right_stick_button){
//                telemetry.addData("Team", red ? "Red" : "Blue");
//                telemetry.addData("Starting Position", startingPos == StartingPositions.CORNER ? "corner": "sandwich");
//                telemetry.addData("Confirmed!", "");
//                telemetry.update();
//                confirmed = true;
//            }
//
//        }
//    }

    private void haltUntilPressStart() {
        while (!gamepad1.start  && !isStopRequested()) {
            Functions.waitFor(300);
        }
    }
}
