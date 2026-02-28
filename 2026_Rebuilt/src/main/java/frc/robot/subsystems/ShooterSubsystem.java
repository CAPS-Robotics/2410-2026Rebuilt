package frc.robot.subsystems;

import java.util.logging.Logger;
import java.util.logging.Level;

import frc.robot.Constants;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;


import edu.wpi.first.wpilibj2.command.SubsystemBase;


/**
 * Shoots balls currently loaded in the machine.
 * @version v1.3.1
 */
public class ShooterSubsystem extends SubsystemBase{
    private static final Logger logger = Logger.getLogger("ShooterSubsystem");
    
    private static final double MOTOR_RPM_MULT = Constants.shoooterPowerConversion;
    private static final double GRAVITY = Constants.gravity_kansas;
    private static final double GOAL_HEIGHT = Constants.hoopHeight;
    private static final double ANGLE = Constants.shooterAngle;
    private SparkMax flywheel = new SparkMax(0, MotorType.kBrushless);
    private SparkMax backRollers = new SparkMax(0, MotorType.kBrushless);
    private SparkMax feedRollers = new SparkMax(0, MotorType.kBrushless);

    /**
     * Shoots at default power level of 50%.
     * @since v0.0.0
     * @version v1.0.0
     */
    public void shoot(){
        flywheel.set(0.5);
        backRollers.set(0.5);
        setFeedRollers();
    }
    
    /**
     * Shoots balls at manually set power level.
     * @param powerLevel The power level to set the flywheels to, range 0.0 to -1.0.
     * @since v1.0.0
     * @version v1.3.1
     */
    public void shoot(double powerLevel){
        // Create power level variable that we can actually change, since java no like when parameter changed
        double power = powerlevel;
        
        if(powerLevel > 0){
            logger.info("Power level exceeds maximum output, set to -1.0");
            power = -1;
        }
        if(powerLevel < 0){
            logger.warning("for some reason you passed a positive value so i set the power to 0 since i dont want it to get jammed");
            power = 0;
        }
        
        flywheel.set(powerLevel);
        backRollers.set(powerLevel);
        setFeedRollers();
    }
    
    /**
     * Shoots balls at power level dependent on distance, [insert maximum shooting distance here].
     * @param distance The distance from the thing you're trying to shoot.
     * @since v1.0.0
     * @version v1.3.0
     */
    public void shootAtDistance(double distance){ 

        if(distance < GOAL_HEIGHT+1){
            logger.info("Too close to goal to fire!! (Minimum distance is 2.83 meters)");
            return;
        }
        // Finds the velocity the ball needs to travel in order to make it in the goal. (TW: Math...)
        double velocity = Math.sqrt((GRAVITY*Math.pow(distance,2)) / (2 * Math.pow(Math.cos(Math.toRadians(ANGLE)),2) * (distance*Math.tan(Math.toRadians(ANGLE)) - GOAL_HEIGHT)));
        // Converts velocity to target RPM (takes wheels into consideration)
        double rpm = velocity*MOTOR_RPM_MULT; //motor needs to rotate X number of times so that in 1 second it spins [velocity] meters
        // TODO: add a fancy log here telling you about the maximum distance if you exceed it, and then abort firing
        
        // Makes the motors spin at the RPM calculated 
        flywheel.setReference(rpm,ControlType.kVelocity);
        backRollers.setReference(rpm,ControlType.kVelocity);
        setFeedRollers();
    }

    /**
     * Sets feedRollers to 0 if not firing so that it doesn't get jammed (will it get jammed otherwise?? idk, probably).
     * @since v1.0.0
     * @version v1.0.0
     */
    public void setFeedRollers(){
        if(flywheel.get() < 0.05 || backRollers.get() < 0.05)
            feedRollers.set(0);
        else
           feedRollers.set(0.5); 
    }
}
