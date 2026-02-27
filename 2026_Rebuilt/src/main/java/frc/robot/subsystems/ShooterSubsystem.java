package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkFlexConfig;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class ShooterSubsystem extends SubsystemBase{

    private SparkFlex flywheel_1 = new SparkFlex(Constants.KFlywheelMotor_1, MotorType.kBrushless);
    private SparkFlex flywheel_2 = new SparkFlex(Constants.KFlywheelMotor_2, MotorType.kBrushless);
    private SparkFlex backRollers = new SparkFlex(Constants.KBackRollerMotor, MotorType.kBrushless);
    private SparkFlexConfig leadMotor = new SparkFlexConfig();
    private SparkFlexConfig flywheelFollower = new SparkFlexConfig();
    private SparkFlexConfig backRollerFollower = new SparkFlexConfig();

    
    // private SparkMax feedRollers = new SparkMax(13, MotorType.kBrushless);

    public ShooterSubsystem(){

        flywheelFollower.follow(flywheel_1, true);
        backRollerFollower.follow(backRollers, true);

        flywheel_1.configure(leadMotor, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        flywheel_2.configure(flywheelFollower, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        backRollers.configure(backRollerFollower, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);

    }


    public void shoot(){

        flywheel_1.set(-0.75);

    }

    public void stop(){

        flywheel_1.set(0);

    }

}
