package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkFlexConfig;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class TransferSubsystem extends SubsystemBase{


    private SparkMax transferLeadMotor = new SparkMax(Constants.kTransferMotor_1, MotorType.kBrushless);
    private SparkMax transferFollowerMotor = new SparkMax(Constants.KFlywheelMotor_2, MotorType.kBrushless);
    private SparkFlexConfig transferLead = new SparkFlexConfig();
    private SparkFlexConfig transferFollower = new SparkFlexConfig();
    
    public TransferSubsystem(){
        
        transferFollower.follow(transferLeadMotor, true);

        transferLeadMotor.configure(transferLead, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        transferFollowerMotor.configure(transferFollower, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);

    }

  
    public void transfer(){

        transferLeadMotor.set(0.5);

    }

    public void stopTranser(){

        transferLeadMotor.set(0);

    }

    public void outakeTransfer(){

        transferLeadMotor.set(-0.5);

    }

}
    