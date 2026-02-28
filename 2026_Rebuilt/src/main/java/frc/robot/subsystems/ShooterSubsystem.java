package frc.robot.subsystems;

import java.util.logging.Logger;
import java.util.logging.Level;

import frc.robot.Constants;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;


import edu.wpi.first.wpilibj2.command.SubsystemBase;


/**
 * Shoots balls currently loaded in the machine.
 * @version v2.0.0
 */
public class ShooterSubsystem extends SubsystemBase
{
    // logga
    private static final Logger logger = Logger.getLogger("ShooterSubsystem");
    // Math-related constants
    private static final double FLYWHEEL_CIRCUMFERENCE = 0.3191858136;
    private static final double BACKWHEEL_CIRCUMFERENCE = 0.0797964534;
    private static final double GRAVITY = 9.806;
    private static final double GOAL_HEIGHT = 1.8288;
    private static final double ANGLE = 42;

    // Motors
    private SparkMax flywheel = new SparkMax(0, MotorType.kBrushless); //TODO: do PID tuning and get the CAN ID's
    private SparkMax backRollers = new SparkMax(0, MotorType.kBrushless);
    private SparkMax feedRollers = new SparkMax(0, MotorType.kBrushless);


    // getter land
    /**
     * Checks if the 
     * @return if error between target RPM and actual RPM is below 3%, return true. otherwise return false
     */
    public boolean isReadyToFire()
    {
        return true; //TODO: add an encoder and complete this method
    }

    /**
     * Sets the rollers to spin to make it into a goal at the target distance.
     * @param distance The distance from the thing you're trying to shoot.
     * @since v1.0.0
     * @version v2.0.0
     */
    public void setDistance(double distance)
    { 

        if(distance < GOAL_HEIGHT+0.5){
            logger.info("Too close to goal to fire!! (Minimum distance is about 2.5 meters)");
            return;
        }
        // Finds the velocity the ball needs to travel in order to make it in the goal. (TW: Math...)
        double velocity = Math.sqrt((GRAVITY*Math.pow(distance,2)) / (2 * Math.pow(Math.cos(Math.toRadians(ANGLE)),2) * (distance*Math.tan(Math.toRadians(ANGLE)) - GOAL_HEIGHT)));
        
        rev(velocity);
    }

    /**
     * Revs up the flywheel and backroller to fire at a set velocity.
     * @param velocity The velocity in m/s to fire the ball at. 
     * @since v2.0.0
     * @version v2.0.0
     */
    public void rev(double velocity)
    {
        // Converts velocity to target RPM
        double flywheelRPM = -(velocity/FLYWHEEL_CIRCUMFERENCE)*60;
        double backwheelRPM = -(velocity/BACKWHEEL_CIRCUMFERENCE)*60;
        
        // Makes the motors spin at the RPM calculated 
        flywheel.setReference(flywheelRPM,ControlType.kVelocity);
        backRollers.setReference(backwheelRPM,ControlType.kVelocity);
    }

    /**
     * FIRE!!!! (make sure to rev up the flywheels first)
     * @since v2.0.0
     * @version v2.0.0
     */
    public void shoot()
    {
        feedRollers.setReference(120,ControlType.kVelocity);
    }

    /**
     * ok now stop shooting
     * @since v2.0.0
     * @version v2.0.0
     */
    public void stopShooting()
    {
        feedRollers.setReference(0,ControlType.kVelocity);
    }

    // It's so cold here in hell, where all deprecated methods go when they die 
    /**
     * Shoots at default power level of 50% voltage. Good for testing purposes
     * @since v0.0.0
     * @version v1.3.1
     */
    public void testShot()
    {
        flywheel.set(-0.5);
        backRollers.set(-0.5);
        setFeedRollers();
    }

    /**
     * Sets feedRollers to 0 if not firing so that it doesn't get jammed (will it get jammed otherwise?? idk, probably).
     * @since v1.0.0
     * @version v1.0.0
     * @deprecated
     */
    public void setFeedRollers()
    {
        if(flywheel.get() < 0.05 || backRollers.get() < 0.05)
            feedRollers.set(0);
        else
           feedRollers.set(0.5); 
    }
}
