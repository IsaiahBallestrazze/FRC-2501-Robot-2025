// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Elevator extends SubsystemBase {
      private final SparkMax ElevatorMotor = new SparkMax(14, MotorType.kBrushless);

      RelativeEncoder elevatorEncoder = ElevatorMotor.getEncoder();

      double elevatorMin= -200;
      double elevatorMax= 200;

      PIDController ElevatorPID = new PIDController(0, 0, 0);


  /** Creates a new Elevator. */
  public Elevator() {}

  public void ElevatorMotorUp(double speed){
    double elavatorposition = elevatorEncoder.getPosition();
    if(elevatorMax <= elavatorposition) ElevatorMotor.set(speed);
  }
  public void ElevatorMotorDown(double speed){
    double elavatorposition = elevatorEncoder.getPosition();
    if(elevatorMax <= elavatorposition) ElevatorMotor.set(speed);
  }

  public void ElevatorMotorStop(){
ElevatorMotor.set(0); 
}

public void ElevationSet(double elevationHeight){
  Double ElevationSpeed = ElevatorPID.calculate(elevatorEncoder.getPosition(), elevationHeight);
  ElevatorMotor.set(ElevationSpeed);
}




  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
