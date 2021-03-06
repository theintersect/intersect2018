
package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.teamcode.robotutil.DriveTrain;
import org.firstinspires.ftc.teamcode.robotutil.Functions;
import org.firstinspires.ftc.teamcode.robotutil.IMU;
import org.firstinspires.ftc.teamcode.robotutil.MRColorSensor;
import org.firstinspires.ftc.teamcode.robotutil.Team;
import org.lasarobotics.vision.util.color.Color;

@Autonomous(name = "AutoFull")
public class AutoFull extends LinearOpMode {
    private double jewelArmInitPosition = 1, jewelArmDownPos = 0, getJewelArmUpPos = 0.4;
    static DcMotor rF, rB, lF, lB, flywheel1, flywheel2, sweeperLow;
    static GyroSensor gyro;
    static Servo jewelArm;
    boolean red = false;
    DriveTrain driveTrain;
    ColorSensor jewelColor;
    MRColorSensor colorSensor;
    char alliance;
    int state;
    private BNO055IMU adaImu;
    private IMU imu;
    public int crypHeading = 0;


    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        options();
        waitForStart();
        state = 0;// Todo:
        if (opModeIsActive()) {
            //Read cryptograph
            jewelColor.enableLed(false);
            jewelArm.setPosition(jewelArmDownPos);
            Functions.waitFor(5000);
            telemetry.addData("Red", colorSensor.getRed());
            telemetry.addData("Blue", colorSensor.getBlue());
            telemetry.update();
            Functions.waitFor(5000);
            hitJewel(0.2,2);
            //hitJewelRotate(20,0.2,);
            Functions.waitFor(5000);
            jewelArm.setPosition(0.1);
            Functions.waitFor(3000);
            driveTrain.encoderDriveIMU(0.4,30, DriveTrain.Direction.FORWARD,10);
            Functions.waitFor(200);
            driveTrain.encoderDriveIMU(0.4, 40, DriveTrain.Direction.LEFT, 10);
            jewelArm.setPosition(getJewelArmUpPos);
            Functions.waitFor(200);
            driveTrain.rotateIMURamp(90,0.3,5,telemetry);
            //move by the cryptogrph reading
            driveTrain.encoderDriveIMU(0.3, 15, DriveTrain.Direction.FORWARD, 7);
            driveTrain.rollersSetPower(0.6);
            Functions.waitFor(5000);
            driveTrain.rollersSetPower(0);
        }
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
        colorSensor = new MRColorSensor(jewelColor, this);
        //crypHeading = (int) imu.getAngle() - 90;
        driveTrain = new DriveTrain(this);
        driveTrain.detectAmbientLight(jewelColor);

        //driveTrain.calibrateGyro(telemetry);

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
            telemetry.update();

            if (gamepad1.left_stick_button && gamepad1.right_stick_button){
                telemetry.addData("Team", red ? "Red" : "Blue");
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
