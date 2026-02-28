// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 **/
public final class Constants {


    //Controllers
    public static final int kDriverControllerPort = 0;
    public static final int kFunctionsControllerPort = 1;


    //Shooter Motors 
    public static final int KFlywheelMotor_1 = 15;
    public static final int KFlywheelMotor_2 = 16;
    public static final int KBackRollerMotor = 10;
    

    //Intake
    public static final int KIntakeMotor = 11;
    public static final int kPivotMotor = 12;

    //Transfer 
    public static final int kTransferMotor_1 = 13;
    public static final int kTransferMotor_2 = 14;

    

    //Drive Motors
    public static final int kFrontLeftDrive = 5;
    public static final int kFrontRightDrive = 3;
    public static final int kBackLeftDrive  = 9;
    public static final int kBackRightDrive = 7;


    //Steering Motors
    public static final int kFrontLeftSteering = 4;
    public static final int kFrontRightSteering = 2;
    public static final int kBackLeftSteering  = 8;
    public static final int kBackRightSteering = 6;

<<<<<<< Updated upstream
=======
    //Shooter Motor
    public static final double flywheelCircumference = 0.3191858136;
    public static final double backwheelCircumference = 0.0797964534;
    public static final double shooterAngle = 42; //What's the meaning of life?

    //Shooter Math
    public static final double hoopHeight = 1.8288; //Later, adjust this a bit so we don't keep landing rim shots (in theory)
    public static final double gravity = 9.81;
    public static final double gravity_kansas = 9.806; //adjusted for KANSAS SEA LEVEL BECAUSE I AM A TRYHARD
    
>>>>>>> Stashed changes
    //Encoders
    public static final int kFrontLeftEncoder = 1;
    public static final int kFrontRightEncoder = 0;
    public static final int kBackLeftEncoder = 2;
    public static final int kBackRightEncoder = 3;

    //Encoder Offset 
    public static final double kFrontLeftEncoderOffset = 0.15;
    public static final double kFrontRightEncoderOffset = -0.21;
    public static final double kBackLeftEncoderOffset = 0.25;
    public static final double kBackRightEncoderOffset = 0.07;


    //Dampners 
    public static final double kSwerveDampner = 0.05;
    public static final double kElevatorDampner = 0.5;
    public static final double kClimbDampner = 0.5;
    public static final double kAlgaeDampner = 0.2;


    //Chasis Length
    public static final double chasisWidth = Units.inchesToMeters(20);
    public static final double chasisLength = Units.inchesToMeters(24);




    //Camera
    public static final AprilTagFields kField = AprilTagFields.kDefaultField;
    public static final String kCameraName = "NAME";
    public static final Transform3d kRobotToCam = new Transform3d(
        Units.inchesToMeters(10), 0, Units.inchesToMeters(10), 
        new Rotation3d(0   , 0, 0)
    ); 


    //Standard Deviations
    public static final Matrix<N3, N1> kSingleTagStdDevs = VecBuilder.fill(4, 4, 8);
    public static final Matrix<N3, N1> kMultiTagStdDevs = VecBuilder.fill(0.5, 0.5, 1);

}
