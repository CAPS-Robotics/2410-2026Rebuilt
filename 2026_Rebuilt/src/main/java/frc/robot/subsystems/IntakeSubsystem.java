package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.SparkFlexConfig;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase{

    private SparkMax intakeRoller = new SparkMax(Constants.KIntakeMotor, MotorType.kBrushless);
    private SparkFlex pivotMotor = new SparkFlex(Constants.kPivotMotor, MotorType.kBrushless);
    private SparkFlexConfig config = new SparkFlexConfig();
    private SparkClosedLoopController pidController = pivotMotor.getClosedLoopController();

    public IntakeSubsystem(){

        // config.closedLoop.p(0.1);
        // config.closedLoop.i(0.0001);
        // config.closedLoop.d(0.01);
        // config.closedLoop.outputRange(-1, 1);

        // pivotMotor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);


    }
    

    private PIDController pid = new PIDController(3, 0, 0);
    private double Tolerance = 5;

    private double extendSpeed;
    private double intakeError;


    public void intake(){
        intakeRoller.set(0.5);
    }

    public void outake(){
        intakeRoller.set(-0.5);
    }

     public void stopIntake(){
        intakeRoller.set(0);
    }

    public void extendIntake(){

        // pidController.setSetpoint(0, ControlType.kPosition);


        pid.setTolerance(Tolerance);
        extendSpeed = pid.calculate(0, 0);
        pivotMotor.set(extendSpeed);
        

        intakeError = 1 - Math.abs(pid.getError());

        if (intakeError <= pid.getErrorTolerance()){
            extendSpeed = 0;
        }

    }

}
