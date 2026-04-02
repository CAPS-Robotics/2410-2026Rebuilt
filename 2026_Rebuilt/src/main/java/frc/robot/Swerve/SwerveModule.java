package frc.robot.Swerve;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.AnalogEncoder;
import frc.robot.Constants;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkFlex;


public class SwerveModule {


    //Motors
    public SparkMax steeringMotor;
    public SparkFlex driveMotor;

    //Modules States
    public SwerveModuleState moduleState;
    public SwerveModulePosition modulePosition;

    //Encoder
    public AnalogEncoder encoder;
    public RelativeEncoder driveEncoder;
    public double encoderValue;
    public double encoderOffset;

    //PID
    public PIDController pidController; 
    public double distanceMoved = 0;
    public double endpoint;
    public double pidSpeed;
    public double errorValue;
    public Rotation2d Angle = Rotation2d.fromDegrees(0);

    //Modifiers
    private int steeringModifier = 1;
    private int driveModifier = 1;
   


 

    public SwerveModule(int driveMotorPort,int steeringMotorPort, int encoderPort, double offset, boolean steeringModified, boolean driveModified)
    {

        //Motors
        steeringMotor = new SparkMax(steeringMotorPort, MotorType.kBrushless);
        driveMotor = new SparkFlex(driveMotorPort, MotorType.kBrushless);

        //Module State
        moduleState = new SwerveModuleState();
        modulePosition = new SwerveModulePosition(0,Rotation2d.fromDegrees(0));
        
       
        //Encoder
        encoder = new AnalogEncoder(encoderPort);
        driveEncoder = driveMotor.getEncoder();


        //PID 
        //original 3
        pidController = new PIDController(3, 0, 0);
        pidController.enableContinuousInput(0, 1);
        pidController.setTolerance(0.001);

        //Offset
        encoderOffset = offset;

        //Invert
        if (steeringModified == true){

            steeringModifier = -1;
        }

        if (driveModified == true){

            driveModifier = -1;
        }


         


    };

    

    /** 
    public SwerveModuleState getModuleState(){
        return moduleState;
    }
     */

    public SwerveModulePosition getModulePosition(){
        setModulePosition();
        return modulePosition = new SwerveModulePosition(distanceMoved, Angle);
    }
   
    
    public void setModuleState(SwerveModuleState state){

        
        moduleState = state;
    

        //Offset Calculations
        encoderValue = (this.encoder.get()+encoderOffset);
        if(encoderValue > 1){
            encoderValue -= 1;
        }
        if (encoderValue < 0 ){
            encoderValue += 1;
        }

       
        moduleState.optimize(Rotation2d.fromRadians((encoderValue/1)*(Math.PI*2)));


        endpoint = (moduleState.angle.getRadians()/(Math.PI*2));
        pidSpeed = pidController.calculate(encoderValue, endpoint);    
        errorValue = 1 - Math.abs(pidController.getError());

        if (errorValue <= pidController.getErrorTolerance()){
            pidSpeed = 0;
        }
 
        //Set Steering Speed and Driving Speed
        steeringMotor.set(steeringModifier*pidSpeed);
        driveMotor.set(driveModifier*moduleState.speedMetersPerSecond * Constants.kSwerveDampner);

       
        
    }

    public void setModulePosition(){

        distanceMoved = driveEncoder.getPosition()*(Units.inchesToMeters(4)*Math.PI)/(6.75);

        Angle = Rotation2d.fromRadians((encoder.get()/1)*(Math.PI*2));
    }

    public SwerveModuleState getState() {
        return this.getState();
    }

}

