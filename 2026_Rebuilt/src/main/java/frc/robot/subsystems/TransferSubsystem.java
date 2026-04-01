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


    private SparkMax transferLeadMotor = new SparkMax(23, MotorType.kBrushless);
    
    
    public TransferSubsystem(){
        
       

    }

  
    public void transfer(){

        transferLeadMotor.set(-0.2);

    }

    public void stopTranser(){

        transferLeadMotor.set(0);

    }

    public void outakeTransfer(){

        transferLeadMotor.set(0.2 );

    }

}
    