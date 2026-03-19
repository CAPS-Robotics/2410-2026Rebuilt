package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Newton;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkRelativeEncoder;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.SparkFlexConfig;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase{

    // private SparkMax intakeRoller = new SparkMax(Constants.KIntakeMotor, MotorType.kBrushless);
    private SparkMax roller;
    private SparkMax pivotMotor = new SparkMax(11, MotorType.kBrushless);
    private RelativeEncoder pivotEncoder;
    private SparkFlexConfig config = new SparkFlexConfig();
    private SparkClosedLoopController pidController = pivotMotor.getClosedLoopController();
    private DigitalInput pivotLimitSwitch = new DigitalInput(0);

    public IntakeSubsystem(){
        roller = new SparkMax(54, MotorType.kBrushless);
        pivotEncoder = pivotMotor.getEncoder();

        // config.closedLoop.p(0.1);
        // config.closedLoop.i(0.0001);
        // config.closedLoop.d(0.01);
        // config.closedLoop.outputRange(-1, 1);

        // pivotMotor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);


    }
    

    private PIDController pid = new PIDController(3, 0, 0);
    private double Tolerance = 5;

    private double extendSpeed = 0.25;
    private double intakeError;


    public void intake(){
        roller.set(-0.5);
        System.out.println("Encoder:" + pivotEncoder.getPosition());
        System.out.println("LIMIT SWITCH" +  pivotLimitSwitch.get());

    }

    public void outake(){
        roller.set(0.5
        );
        System.out.println("OUTAKE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

    }

     public void stopIntake(){
        roller.set(0);
        System.out.println("STOP!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    public void extendIntake(){


        extendSpeed = 0.15;

        
        if(pivotEncoder.getPosition() > 13){

            extendSpeed = 0; 

        }
        System.out.println("EXTENDING INTAKE !!!!!!!!!!!!!!!!!!");
        System.out.println("PIVOT MOTOR SPEED "+ extendSpeed);

        pivotMotor.set(extendSpeed); 

    }

    public void retract(){

        extendSpeed = -0.15;
        if(Math.abs((pivotEncoder.getPosition() - 0)) < 5 && !pivotLimitSwitch.get()){

            extendSpeed = 0;
        }
           

        

        System.out.println("RETRACTING INTAKE !!!!!!!!!!!!!!!!!!");
        System.out.println("PIVOT MOTOR SPEED "+ extendSpeed);

        pivotMotor.set(extendSpeed);



    }
        
            


    }

