// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.studica.frc.AHRS;
import com.studica.frc.AHRS.NavXComType;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.Kinematics;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PS5Controller;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.Swerve.SwerveModule;


/** Add your docs here. */
public class SwerveDrivetrainSubsystem extends SubsystemBase{


    SwerveModule frontRightModule = new SwerveModule(Constants.kFrontRightDrive, Constants.kFrontRightSteering, Constants.kFrontRightEncoder, Constants.kFrontRightEncoderOffset, true, false);
    SwerveModule frontLeftModule = new SwerveModule(Constants.kFrontLeftDrive, Constants.kFrontLeftSteering,Constants.kFrontLeftEncoder, Constants.kFrontLeftEncoderOffset, false, false);
    SwerveModule backRightModule = new SwerveModule(Constants.kBackRightDrive, Constants.kBackRightSteering,Constants.kBackRightEncoder, Constants.kBackRightEncoderOffset, false, false);
    SwerveModule backLeftModule = new SwerveModule(Constants.kBackLeftDrive, Constants.kBackLeftSteering, Constants.kBackLeftEncoder, Constants.kBackLeftEncoderOffset, true, true);

    
    SwerveModuleState states[];
    SwerveModulePosition[] position = {frontLeftModule.modulePosition.copy(), frontRightModule.modulePosition.copy(), backLeftModule.modulePosition.copy(), backRightModule.modulePosition.copy()};
    SwerveDrivePoseEstimator drivePoseEstimator;


    AHRS Navx = new AHRS(NavXComType.kMXP_SPI);
    
    // Uncomment to convert from double to Rotations2D
    Rotation2d Yaw;
    PIDController ResetToFusedHeading = new PIDController(10, 0, 0);

    Pose2d initialrobotPose2d = new Pose2d();
    

    Translation2d frontLeft = new Translation2d((Constants.chasisWidth/2), (Constants.chasisLength/2));
    Translation2d frontRight = new Translation2d((Constants.chasisWidth/2), (-Constants.chasisLength/2));
    Translation2d backLeft = new Translation2d((-Constants.chasisWidth/2), (Constants.chasisLength/2));
    Translation2d backRight = new Translation2d((-Constants.chasisWidth/2), (-Constants.chasisLength/2));

    Rotation2d FLCurrentAngle;
    Rotation2d FRCurrentAngle;
    Rotation2d BLCurrentAngle;
    Rotation2d BRCurrentAngle;


    SwerveDriveKinematics kinematics = new SwerveDriveKinematics(frontLeft, frontRight, backLeft, backRight);

    SwerveDrivePoseEstimator swerveDrivePoseEstimator; 
    
    double distanceAprilTag;
   
    

    static ChassisSpeeds chassisSpeeds = new ChassisSpeeds(); 
    
    
    
        public SwerveDrivetrainSubsystem(){   
             
            var stateStdDevs = VecBuilder.fill(0.1, 0.1, 0.1);
            var visionStdDevs = VecBuilder.fill(1, 1, 1);

            this.setDefaultCommand(new RunCommand(()-> this.driveSwerve(RobotContainer.m_driverController), this ));
            drivePoseEstimator = new SwerveDrivePoseEstimator(
                kinematics, 
                Navx.getRotation2d(), 
                position, 
                initialrobotPose2d,
                stateStdDevs,
                visionStdDevs);
            
    
        }
    
        // public double getCurrentYaw(){
    
        //     Yaw = Navx.getYaw();
        //     return Yaw;
    
        // }
    
        

    
        public void driveSwerve(CommandJoystick driveController ){

            Yaw = Navx.getRotation2d();

            double velocityX = 1 * driveController.getY();
            double velocityY = 1 * driveController.getX();
            double omega = 1 * driveController.getZ();
            

            if(Math.abs(velocityX) < 0.1) velocityX = 0;
            if(Math.abs(velocityY) < 0.1) velocityY = 0;
            if(Math.abs(omega) < 0.1) omega = 0;
    
       
    
            System.out.println("Velocity X: " + velocityX);
            System.out.println("Velocity Y: "+ velocityY);
            System.out.println("Omega: "+omega);
            System.out.println("FUSEDHEADING"+Navx.getFusedHeading()+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

    
            chassisSpeeds = new ChassisSpeeds();
            chassisSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(velocityX,velocityY, omega, Yaw);
    
            setSpeed(chassisSpeeds);
    
            
            position[0] = frontLeftModule.modulePosition;
            position[1] = frontRightModule.modulePosition;
            position[2] = backLeftModule.modulePosition;
            position[3] = backRightModule.modulePosition;
    
    
            // swerveDrivePoseEstimator.update(getCurrentYaw(), updatePositions());
    
            // SmartDashboard.putNumber("Front Left Module Enocoder", frontLeftModule.encoderValue);
            // SmartDashboard.putNumber("Front Right Module Enocoder", frontRightModule.encoderValue);
            // SmartDashboard.putNumber("Back Left Module Enocoder", backLeftModule.encoderValue);
            // SmartDashboard.putNumber("Back Right Module Enocoder", backRightModule.encoderValue);
            
    
    
    
        }

            
            
        public  void setSpeed(ChassisSpeeds speed){
            // System.out.println("Setting states " );
            System.out.println("Speed: "+speed);
            states = kinematics.toSwerveModuleStates(speed);
            

            // System.out.println("Setting states "+states );
            frontLeftModule.setModuleState(states[0]);
            frontRightModule.setModuleState(states[1]);
            backLeftModule.setModuleState(states[2]);
            backRightModule.setModuleState(states[3]);

            //Printing out yaw
            // SmartDashboard.putNumber("YAW", Yaw);   
            

            // System.out.println();
            // System.out.println("Encoder Value: "+backLeftModule.encoderValue);
            // System.out.println("Endpoint: "+backLeftModule.endpoint);
            // System.out.println("PID speed: "+backLeftModule.pidSpeed);
            // System.out.println("Error Tolerance: "+ backLeftModule.pidController.getErrorTolerance());
            // System.out.println();
            frontLeftModule.setModulePosition();
            frontRightModule.setModulePosition();
            backLeftModule.setModulePosition();
            backRightModule.setModulePosition();

            // swerveDrivePoseEstimator.update(getCurrentYaw(), updatePositions());
            System.out.println("Fused Heading "+Navx.getFusedHeading()+" !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("Current Yaw: "+ Navx.getYaw()+" !!!!!!!!!!!!!");

         
        }

        public void addVisionMeasurements(Pose2d estimateRobotPose, double timestampSeconds, Matrix<N3,N1> visionStdDevs){
            drivePoseEstimator.addVisionMeasurement(initialrobotPose2d, distanceAprilTag, visionStdDevs);
        }

        public SwerveDriveKinematics getSwerveKinematics(){
            return this.getSwerveKinematics();
        }


        public void periodic(){
            
            drivePoseEstimator.update(Navx.getRotation2d(), position);

        }       

}