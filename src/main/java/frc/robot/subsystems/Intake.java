// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
  /** Creates a new Intake. */
  private final SparkMax IntakeWheelLeft = new SparkMax(1, MotorType.kBrushless);
  private final SparkMax IntakeWheelRight = new SparkMax(2, MotorType.kBrushless); 

  private final SparkMax Coraltilt = new SparkMax(4, MotorType.kBrushless);
  private final SparkMax CoralWheel = new SparkMax(3, MotorType.kBrushless); 

     RelativeEncoder encodertilt = Coraltilt.getEncoder();
     RelativeEncoder encoderWheel = CoralWheel.getEncoder();

  PIDController CoralPID = new PIDController(0, 0, 0);

  double SetpointAngle = 200;

  public Intake() {}




  public void AlgaeIntake(double speed){
    IntakeWheelLeft.set(speed);
    IntakeWheelRight.set(speed);
  }

  public void CoralIntake(double intakeSpeed, double IntakeAngle){

    Double tiltSpeed = CoralPID.calculate(encodertilt.getPosition(), IntakeAngle);

    Coraltilt.set(tiltSpeed);
    CoralWheel.set(intakeSpeed);


  }



  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
