package frc.robot.subsystems;

import frc.robot.Constants;

import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;


/**
 * This subsystem calculates and sets the shooter's power.
 * @version v2.2.0
 */
public class ShooterSubsystem extends SubsystemBase
{
    // Measurement constants (in meters)
    private static final double FLYWHEEL_CIRCUMFERENCE = 0.3191858136;
    private static final double BACKWHEEL_CIRCUMFERENCE = 0.0797964534;
    private static final double GOAL_HEIGHT = 1.397; //difference between shooter height and goal height in meters 
    private static final double MINIMUM_FIRING_DISTANCE = 2.5; // This is pretty arbritary but it makes sure that setDistance() doesn't just try to shoot straight at the hoop
    private static final double MAXIMUM_FIRING_DISTANCE = 5.0; // Note: Can shoot much further, albeit not as accurately. Rough estimate based off backroller's max RPM
    // Other math-related constants
    private static final double GRAVITY = 9.806; //adjusted for kansas sea level
    private static final double ANGLE = 42; //what's the meaning of life?
    private static final double RPM_TOLERANCE = 0.05; //5% error allowed
    private static final double MOTOR_MAX_RPM = 6784;
    private static final double POWER_MULT = 1/0.9; //Approx reciprocal of the % of velocity transfered to the ball from the flywheel (efficiency)
    //                          ^ This doesn't apply to the backrollers, which is weird and implicit but provides backspin to the ball

    // The flywheel runs on two separate motors.
    private SparkFlex flywheel_1_motor = new SparkFlex(Constants.KFlywheelMotor_1, MotorType.kBrushless);
    private SparkClosedLoopController flywheel_1 = flywheel_1_motor.getClosedLoopController();
    private SparkFlex flywheel_2_motor = new SparkFlex(Constants.KFlywheelMotor_2, MotorType.kBrushless);
    private SparkClosedLoopController flywheel_2 = flywheel_2_motor.getClosedLoopController();
    //TODO: do PID tuning
    private double flywheelTargetRPM;

    // The backroller runs on only one motor.
    private SparkFlex backRollers_motor = new SparkFlex(Constants.KBackRollerMotor, MotorType.kBrushless);
    private SparkClosedLoopController backRollers = backRollers_motor.getClosedLoopController();
    private double backRollerTargetRPM;
    // TODO: do PID tuning

    //Follower motors.
    private SparkFlexConfig leadMotor = new SparkFlexConfig();
    private SparkFlexConfig flywheelFollower = new SparkFlexConfig();
    private SparkFlexConfig backRollerFollower = new SparkFlexConfig();
    // do these need to be PID tuned??

    public ShooterSubsystem()
    {
        flywheelFollower.follow(flywheel_1_motor, true);
        backRollerFollower.follow(backRollers_motor, true);

        flywheel_1_motor.configure(leadMotor, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        flywheel_2_motor.configure(flywheelFollower, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        backRollers_motor.configure(backRollerFollower, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    }

    /**
     * Checks if the actual RPM of the flywheel is close to the target RPM. (Backroller is negligible)
     * @return Whether the flywheel is ready to fire at full speed, calculated upon running this method
     * @since v2.0.0
     * @version v2.2.0
     */
    public boolean isReadyToFire()
    {
        // Gets current velocity from encoder
        double flywheelActualRPM = flywheel_1_motor.getEncoder().getVelocity();

        // Checks if flywheel target RPM is within tolerance
        if(Math.abs((flywheelActualRPM-flywheelTargetRPM)/flywheelTargetRPM) > RPM_TOLERANCE)
            return false;
        return true;
    }

    /**
     * Calculates velocity needed to make a goal from distance, then runs rev() method
     * @param distance The distance from the thing you're trying to shoot. (Minimum distance: 2 meters. Maximum (accurate) distance: 6 meters)
     * @since v1.0.0
     * @version v2.2.0
     */
    public void setDistance(double distance)
    { 
        // Checks if robot is too close to fire
        if(distance < MINIMUM_FIRING_DISTANCE)
        {
            System.out.println("Too close to goal to fire!! (Minimum distance is 2.5 meters)");
            return;
        }
        // Checks if robot is too far to fire accurately
        if(distance > MAXIMUM_FIRING_DISTANCE)
            System.out.println("Distance exceeds 5 meters, launcher will likely undershoot (not a big problem unless you're trying to make goals)");
        
        // Finds the velocity the ball needs to travel in order to make it in the goal. (TW: Math...)
        double velocity = Math.sqrt((GRAVITY*Math.pow(distance,2)) / (2 * Math.pow(Math.cos(Math.toRadians(ANGLE)),2) * (distance*Math.tan(Math.toRadians(ANGLE)) - GOAL_HEIGHT)));
        
        // Sets motors to fire at calculated velocity
        rev(velocity);
    }

    /**
     * Revs up the flywheel and backroller to fire at a set velocity.
     * @param velocity The velocity in m/s to fire the ball at. 
     * @since v2.0.0
     * @version v2.1.0
     */
    public void rev(double velocity)
    {
        // Converts velocity to target RPM
        flywheelTargetRPM = Math.min((velocity/FLYWHEEL_CIRCUMFERENCE)*60*POWER_MULT,MOTOR_MAX_RPM);
        backRollerTargetRPM = Math.min((velocity/BACKWHEEL_CIRCUMFERENCE)*60,MOTOR_MAX_RPM); // Note to self: if you need to reverse it do it here
        
        // Makes the flywheel motors spin at the RPM calculated 
        flywheel_1.setSetpoint(flywheelTargetRPM,ControlType.kVelocity);
        flywheel_2.setSetpoint(-flywheelTargetRPM,ControlType.kVelocity); // This one's in reverse

        // Then the backrollers (These will almost always have to fire near 100% velocity. Why??? Who designed this thing??)
        // It's too late to put a 3:2 gear ratio on it :cry: 
        backRollers.setSetpoint(backRollerTargetRPM,ControlType.kVelocity);
    }

    /**
     * Warms up the flywheel and backroller so that it takes less time for the flywheel to hit target speed.
     * @since v2.2.0
     * @version 2.2.0
     */
    public void preRev(double velocity)
    {
        // Sets target RPMs
        flywheelTargetRPM = 1500;
        backRollerTargetRPM = 6000;
        
        // Makes the flywheel motors spin
        flywheel_1.setSetpoint(flywheelTargetRPM,ControlType.kVelocity);
        flywheel_2.setSetpoint(-flywheelTargetRPM,ControlType.kVelocity); // This one's in reverse

        // Then the backrollers
        backRollers.setSetpoint(backRollerTargetRPM,ControlType.kVelocity);
    }

}
