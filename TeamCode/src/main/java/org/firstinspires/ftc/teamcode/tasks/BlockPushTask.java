package org.firstinspires.ftc.teamcode.tasks;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotutil.Functions;

/**
 * Created by pranav on 2/2/18.
 */

public class BlockPushTask extends TaskThread{

    private Servo blockPush;



    int pos = 0;
    ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    public BlockPushTask(LinearOpMode opMode) {
        this.opMode = opMode;
        initialize();
    }

    @Override
    public void run() {
        timer.reset();
        while (opMode.opModeIsActive() && running) {
            if(opMode.gamepad2.a){
                blockPush.setPosition(.5);
            }
            if (opMode.gamepad2.a){
                blockPush.setPosition(0);
            }

            Functions.waitFor(200);
            opMode.telemetry.addData("position: ",pos);
            opMode.telemetry.update();

        }


    }


    @Override
    public void initialize() {
        blockPush = opMode.hardwareMap.servo.get("blockPusher");
        blockPush.setDirection(Servo.Direction.FORWARD);
        blockPush.setPosition(0);
    }


}
