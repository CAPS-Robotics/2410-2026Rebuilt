// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SwerveDrivetrainSubsystem;
import frc.robot.subsystems.TransferSubsystem;
import frc.robot.subsystems.VisionSubsystem;



import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

  
  // The robot's subsystems and commands are defined here...
  public final SwerveDrivetrainSubsystem swerveDrivetrainSubsystem = new SwerveDrivetrainSubsystem();
  private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
  private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  private final TransferSubsystem transferSubsystem = new TransferSubsystem();
  // private final VisionSubsystem visionSubsystem = new VisionSubsystem(Constants.kCameraName, Constants.kField, Constants.kRobotToCam, swerveDrivetrainSubsystem::addVisionMeasurements);


  //Teleop OP Commands

  //Intake Commands
  private final RunCommand intake = new RunCommand(()-> this.intakeSubsystem.intake(), intakeSubsystem);
  private final InstantCommand stopIntake = new InstantCommand(()-> this.intakeSubsystem.stopIntake(), intakeSubsystem);
  private final Command outake = new RunCommand(()-> this.intakeSubsystem.outake() , intakeSubsystem).andThen(new RunCommand(()-> this.transferSubsystem.outakeTransfer(), transferSubsystem));
  private final RunCommand extendIntake = new RunCommand(()-> this.intakeSubsystem.extendIntake(), intakeSubsystem);
  private final RunCommand retractIntake = new RunCommand(()-> this.intakeSubsystem.retract(), intakeSubsystem);

  //Transfer Commands
  private final RunCommand reverseTransfer = new RunCommand(()-> this.transferSubsystem.outakeTransfer(), transferSubsystem);
  private final RunCommand Transfer = new RunCommand(() -> this.transferSubsystem.transfer(), transferSubsystem);
  private final InstantCommand stopTransfer = new InstantCommand(()-> this.transferSubsystem.stopTranser(), transferSubsystem);

  //Flywheel Commands
  private final RunCommand shoot = new RunCommand(()-> this.shooterSubsystem.setDistance(VisionSubsystem.distanceToAprilTag), shooterSubsystem);
  private final Command fire = new RunCommand(()-> this.shooterSubsystem.unstick(), shooterSubsystem).alongWith(Transfer);
  private final RunCommand IdleShooter = new RunCommand(()-> this.shooterSubsystem.idleMode(), shooterSubsystem);
  
  //Gyro Reset
  private final InstantCommand zero = new InstantCommand(()-> this.swerveDrivetrainSubsystem.zeroYaw(), swerveDrivetrainSubsystem);



 //Auto Commands 
  private final Command middleAuto = new RunCommand( ()-> this.swerveDrivetrainSubsystem.driveMiddle(113), swerveDrivetrainSubsystem);
  private final RunCommand sideMove = new RunCommand( ()-> this.swerveDrivetrainSubsystem.driveSide(182), swerveDrivetrainSubsystem);
  private final RunCommand turnRight = new RunCommand(() -> this.swerveDrivetrainSubsystem.turnRight(45), swerveDrivetrainSubsystem);
  private final RunCommand turnLeft = new RunCommand(() -> this.swerveDrivetrainSubsystem.turnLeft(45), swerveDrivetrainSubsystem);

  private final WaitCommand revWait = new WaitCommand(10);
  

  // Replace with CommandPS4Controller or CommandJoystick if needed
  public static final CommandJoystick m_driverController =
      new CommandJoystick(Constants.kDriverControllerPort);
  
  
  

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();

    // visionSubsystem.periodic();

    
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link 
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {

    // m_driverController.button(2).onTrue(stop);
    // m_driverController.button(1).onTrue(outake); 
    // m_driverController.button(3).onTrue(intake);
    // m_driverController.button(2).onTrue(stopIntake);
    // m_driverController.button(5).onTrue(extendIntake);
    // m_driverController.button(6).onTrue(retractIntake);
    m_driverController.button(8).whileTrue(Transfer);
    // m_driverController.button(8).onFalse(IdleShooter);
    m_driverController.button(4).onTrue(fire);
    m_driverController.button(9).onTrue(stopTransfer);


  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */


  public Command getAutonomousCommand() {
    
    switch(Constants.AutoPicker){
      case 1: 
          System.out.println("Left Auto Selected");
          return this.sideMove.withTimeout(5).alongWith(shoot)
                        .andThen(turnRight).withTimeout(5).alongWith(shoot)
                        .andThen(reverseTransfer).alongWith(fire).alongWith(shoot).withTimeout(5);


      case 2:
        System.out.println("Middle Auto Selected");
        return this.middleAuto.withTimeout(5).alongWith(shoot)
                              .andThen(revWait)
                              .andThen(reverseTransfer).alongWith(fire).alongWith(shoot);


      case 3:
        System.out.println("Right Auto Selected");
        return this.sideMove.withTimeout(5).alongWith(shoot)
                        .andThen(turnLeft).withTimeout(5).alongWith(shoot)
                        .andThen(reverseTransfer).alongWith(fire).alongWith(shoot).withTimeout(5);


      default:
        System.out.println("No Auto Selected");
        return null;

      }
    }
    


   



    

    }

