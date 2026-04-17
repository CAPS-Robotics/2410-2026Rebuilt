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

public class IntakeSubsystemClass extends SubsystemBase{

    // private SparkMax intakeRoller = new SparkMax(Constants.KIntakeMotor, MotorType.kBrushless);
    private SparkFlex intakeRollers = new SparkFlex(30, MotorType.kBrushless);
    private SparkMax pivotMotor = new SparkMax(11, MotorType.kBrushless);
    private RelativeEncoder pivotEncoder;
 
   
    private DigitalInput pivotLimitSwitch = new DigitalInput(0);

    public IntakeSubsystemClass(){
        
        pivotEncoder = pivotMotor.getEncoder();

        // config.closedLoop.p(0.1);wq
        // config.closedLoop.i(0.0001);
        // config.closedLoop.d(0.01);
        // config.closedLoop.outputRange(-1, 1);

        // pivotMotor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);


    }
    

    private double extendSpeed = 0.25;
    private double intakeError;


    public void intake(){
        intakeRollers.set(-0.45);
        System.out.println("Encoder:" + pivotEncoder.getPosition());
        System.out.println("LIMIT SWITCH" +  pivotLimitSwitch.get());
        System.out.println("INTAKE!!!!!!!!!!!!!");

    }

    public void outake(){
        intakeRollers.set(0.45);
        System.out.println("OUTAKE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

    }

     public void stopIntake(){
        intakeRollers.set(0);
        System.out.println("STOP!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    public void extendIntake(){


        extendSpeed = -0.25;

        
        if(pivotEncoder.getPosition() < -17){

            extendSpeed = 0; 

        }
        System.out.println("EXTENDING INTAKE !!!!!!!!!!!!!!!!!!");
        System.out.println("PIVOT MOTOR SPEED "+ extendSpeed);
        System.out.println("PIVOT ENCODER: "+ pivotEncoder.getPosition());

        pivotMotor.set(extendSpeed); 

    }

    public void retract(){

        extendSpeed = 0.25;
        if(Math.abs((pivotEncoder.getPosition() - 0)) < 10 && !pivotLimitSwitch.get()){

            extendSpeed = 0;
            pivotEncoder.setPosition(0);
        }
           

        

        System.out.println("RETRACTING INTAKE !!!!!!!!!!!!!!!!!!");
        System.out.println("PIVOT MOTOR SPEED "+ extendSpeed);
        System.out.println("LIMIT SWITCH BOOLEAN "+ pivotLimitSwitch.get());
        System.out.println("ECODER POSITION" + pivotEncoder.getPosition());

        pivotMotor.set(extendSpeed);



    }
        
            


    }

