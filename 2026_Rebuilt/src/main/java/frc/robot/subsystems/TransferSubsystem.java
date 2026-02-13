package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class TransferSubsystem extends SubsystemBase{


    private SparkMax transfer = new SparkMax(14, MotorType.kBrushless);


    public void transfer(){
        transfer.set(1);
    }

}
    