// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class PoseEstimatorSubsystem extends SubsystemBase {

  /** Creates a new PoseEstimatorSubsystem. */

  public static final AprilTagFieldLayout kTagLayout = AprilTagFieldLayout.loadField(AprilTagFields.kDefaultField);
  public static final Transform3d kRobotToCam = new Transform3d(new Translation3d(0, 0.0, 0), new Rotation3d(0, 0, 0));



  public PoseEstimatorSubsystem() {
      PhotonPoseEstimator poseEstimator = new PhotonPoseEstimator(kTagLayout, kRobotToCam); 
      PhotonCamera cam = new PhotonCamera("Name");

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
