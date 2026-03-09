// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * This class raises or lowers a arm when a limit switch is pressed. 
 */
public class ClimbSubsystem extends SubsystemBase
{
  // motor
  private static SparkMax motor = new SparkMax(23, MotorType.kBrushless);
  // limit switch
  private static DigitalInput limitSwitch = new DigitalInput(8);

  /**
   * Constructor.
   */
  public ClimbSubsystem()
  {
    //yaya!!
  }

  /**
   * A method querying a boolean state of the [limiter switch] (for example, the limit switch).
   *
   * @return whether it's closed or not
   */
  public boolean isActivated()
  {
    return limitSwitch.get();
  }

  /**
   * Raises the thing.
   */
  public void raise()
  {
    motor.set(0.1);
  }

  /**
   * Lowers the thing.
   */
  public void lower()
  {
    motor.set(-0.1);
  }

  /**
   * Stops the thing.
   */
  public void stop()
  {
    motor.set(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
