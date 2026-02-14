// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

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

    //Intake
    public static final int KIntakeMotor = 11;
    public static final int KFlywheelMotor = 15;
    public static final int KBackRollerMotor = 10;


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

}
