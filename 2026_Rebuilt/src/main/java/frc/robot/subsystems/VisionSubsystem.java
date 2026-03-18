// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.Optional;
import java.util.Vector;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;


import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;

import java.lang.annotation.Target;
import java.util.List;
import org.photonvision.targeting.PhotonTrackedTarget;


public class VisionSubsystem extends SubsystemBase {

  /** Creates a new PoseEstimatorSubsystem. */

  public final PhotonCamera camera;
  public final PhotonPoseEstimator visionEstimator;
  private Matrix<N3, N1> curStdDevs;
  private final EstimateConsumer estConsumer;
  private PhotonTrackedTarget HubTarget;
  public static double distanceToAprilTag;
  



  public VisionSubsystem(String cameraName, AprilTagFields tagLayout, Transform3d CamtoRobot, EstimateConsumer estimateConsumer) {
    estConsumer = estimateConsumer;
    AprilTagFieldLayout fieldLayout = AprilTagFieldLayout.loadField(tagLayout); 
    visionEstimator = new PhotonPoseEstimator(fieldLayout, CamtoRobot);
    camera = new PhotonCamera(cameraName);
  }

  

  @Override
  public void periodic() {
    Optional<EstimatedRobotPose> visionEst = Optional.empty();
        for (var result : camera.getAllUnreadResults()) {
            visionEst = visionEstimator.estimateCoprocMultiTagPose(result);
            if (visionEst.isEmpty()) {
                visionEst = visionEstimator.estimateLowestAmbiguityPose(result);
            }
            updateEstimationStdDevs(visionEst, result.getTargets());

            for(var id : result.getTargets()){
                if(id.getFiducialId() == 9){
                    HubTarget = id;
                    distanceToAprilTag = this.distanceToAprilTag(HubTarget);
                    continue;

                }else if (id.getFiducialId() == 10 ){
                    HubTarget = id;
                    distanceToAprilTag = this.distanceToAprilTag(HubTarget);
                    continue;
                
                }else if (id.getFiducialId() == 21){
                    HubTarget = id;
                    distanceToAprilTag = this.distanceToAprilTag(HubTarget);
                    continue;


                }else if(id.getFiducialId() == 22){
                    HubTarget = id;
                    distanceToAprilTag = this.distanceToAprilTag(HubTarget);
                    continue;

                }
            }

        }

  visionEst.ifPresent(
                    est -> {
                        // Change our trust in the measurement based on the tags we can see
                        var estStdDevs = getEstimationStdDevs();

                        estConsumer.accept(est.estimatedPose.toPose2d(), est.timestampSeconds, estStdDevs);
                    });
}

private void updateEstimationStdDevs(
            Optional<EstimatedRobotPose> estimatedPose, List<PhotonTrackedTarget> targets) {
        if (estimatedPose.isEmpty()) {
            // No pose input. Default to single-tag std devs
            curStdDevs = Constants.kSingleTagStdDevs;

        } else {
            // Pose present. Start running Heuristic
            var estStdDevs = Constants.kSingleTagStdDevs;
            int numTags = 0;
            double avgDist = 0;

            // Precalculation - see how many tags we found, and calculate an average-distance metric
            for (var tgt : targets) {
                var tagPose = visionEstimator.getFieldTags().getTagPose(tgt.getFiducialId());
                if (tagPose.isEmpty()) continue;
                numTags++;
                avgDist +=
                        tagPose
                                .get()
                                .toPose2d()
                                .getTranslation()
                                .getDistance(estimatedPose.get().estimatedPose.toPose2d().getTranslation());
            }

            if (numTags == 0) {
                // No tags visible. Default to single-tag std devs
                curStdDevs = Constants.kSingleTagStdDevs;
            } else {
                // One or more tags visible, run the full heuristic.
                avgDist /= numTags;
                // Decrease std devs if multiple targets are visible
                if (numTags > 1) estStdDevs = Constants.kMultiTagStdDevs;
                // Increase std devs based on (average) distance
                if (numTags == 1 && avgDist > 4)
                    estStdDevs = VecBuilder.fill(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
                else estStdDevs = estStdDevs.times(1 + (avgDist * avgDist / 30));
                curStdDevs = estStdDevs;
            }
        }
    }



public Matrix<N3, N1> getEstimationStdDevs(){
  return curStdDevs;
}

public double distanceToAprilTag(PhotonTrackedTarget apriltag){
    PhotonTrackedTarget target = apriltag;
    Transform3d vector = target.getBestCameraToTarget();
    double x_component_distance = vector.getMeasureX().abs(Units.Meters);
    double y_component_distance = vector.getMeasureY().abs(Units.Meters);
    double distance = Math.sqrt(Math.pow(x_component_distance, 2) + Math.pow(y_component_distance, 2));
    return distance;


}

@FunctionalInterface
public static interface EstimateConsumer {
        public void accept(Pose2d pose, double timestamp, Matrix<N3, N1> estimationStdDevs);
}

}


