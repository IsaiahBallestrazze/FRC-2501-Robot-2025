// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Elevator extends SubsystemBase {
      private final SparkMax ElevatorMotor = new SparkMax(14, MotorType.kBrushless);

      RelativeEncoder elevatorEncoder = ElevatorMotor.getEncoder();

      double elevatorMin= 0;
      double elevatorMax= 110;

      PIDController ElevatorPID = new PIDController(0.02, 0, 0);


  /** Creates a new Elevator. */
  public Elevator() {}

  public void ElevatorMotorUp(double speed){
    double elavatorposition = Math.abs(elevatorEncoder.getPosition());
    SmartDashboard.putNumber("elevator Position", elevatorEncoder.getPosition()); // gets magnitude of left joystick
    if(elevatorMax >= elavatorposition){
      SmartDashboard.putBoolean("Elevator Safety", true); // gets magnitude of left joystick
      ElevatorMotor.set(speed);
    } else{
      SmartDashboard.putBoolean("Elevator Safety", false); // gets magnitude of left joystick
    }
  }
  public void ElevatorMotorDown(double speed){
    SmartDashboard.putNumber("elevator Position", elevatorEncoder.getPosition()); // gets magnitude of left joystick
    double elavatorposition = Math.abs(elevatorEncoder.getPosition());
    if(elevatorMin <= elavatorposition){
      ElevatorMotor.set(speed);
      SmartDashboard.putBoolean("Elevator Safety", true); // gets magnitude of left joystick

    } else{
      SmartDashboard.putBoolean("Elevator Safety", false); // gets magnitude of left joystick

    }
  }

  public void ElevatorMotorStop(){
ElevatorMotor.set(0); 
}

public void ElevatorEncoderTest(){
  ElevatorMotor.set(0); 
      SmartDashboard.putNumber("Elevator Position", elevatorEncoder.getPosition()); // gets magnitude of left joystick
    SmartDashboard.putNumber("Elevator Speed", elevatorEncoder.getVelocity());
  }

public void AutoElevator(double speed, double distance){
  ElevatorMotor.set(speed);
  double elevatorPosition = Math.abs(elevatorEncoder.getPosition());
  if(elevatorPosition >= (distance - 3)){
    return;
  }
}


public void ElevationSet(double elevationHeight){
  Double ElevationSpeed = ElevatorPID.calculate(Math.abs(elevatorEncoder.getPosition()), elevationHeight);
  if(ElevationSpeed > 1) ElevationSpeed = 1.0;
  if(ElevationSpeed < -1) ElevationSpeed = -1.0;

  ElevatorMotor.set(-ElevationSpeed);
 SmartDashboard.putNumber("Elevator SPEEEED", ElevationSpeed); // gets magnitude of left joystick
  SmartDashboard.putNumber("Elevator Position", elevatorEncoder.getPosition()); // gets magnitude of left joystick
  SmartDashboard.putNumber("Elevator Speed", elevatorEncoder.getVelocity());
}

//PID SetPositions








  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
