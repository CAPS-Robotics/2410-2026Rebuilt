package frc.robot.subsystems;

import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class ShooterSubsystem extends SubsystemBase{

    private SparkFlex flywheel = new SparkFlex(Constants.KFlywheelMotor, MotorType.kBrushless);
    private SparkFlex backRollers = new SparkFlex(Constants.KBackRollerMotor, MotorType.kBrushless);
    // private SparkMax feedRollers = new SparkMax(13, MotorType.kBrushless);


    public void shoot(){

        flywheel.set(-0.75);
        backRollers.set(-0.75);
        // feedRollers.set(0.5);
    }

    public void stop(){
        flywheel.set(0);
        backRollers.set(0);

        
    }

}
