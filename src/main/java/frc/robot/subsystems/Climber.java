// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
  /** Creates a new Climber. */
    private final SparkMax ClimberMotor = new SparkMax(13, MotorType.kBrushless);
     RelativeEncoder ClimberEncoder = ClimberMotor.getEncoder();
    double maxClimb = 287;
    double minClimb = 0;

  public Climber() {}

  public void ClimberMotorUp(){
    double climberangle = ClimberEncoder.getPosition();
    if(maxClimb >= climberangle) ClimberMotor.set(.7);

  SmartDashboard.putNumber("Climber Position", ClimberEncoder.getPosition()); // gets magnitude of left joystick
  SmartDashboard.putNumber("Climber Speed", ClimberEncoder.getVelocity());
  }
  public void ClimberMotorDown(){
    SmartDashboard.putBoolean("Climber Down", true); // gets magnitude of left joystick
    double climberangle = ClimberEncoder.getPosition();
    if(minClimb <= climberangle) ClimberMotor.set(-.7);
    SmartDashboard.putNumber("Climber Position", ClimberEncoder.getPosition()); // gets magnitude of left joystick
    SmartDashboard.putNumber("Climber Speed", ClimberEncoder.getVelocity());
  }
  public void ClimberMotorStop(){
    ClimberMotor.set(0);
    SmartDashboard.putBoolean("Climber Down", false); // gets magnitude of left joystick
    SmartDashboard.putBoolean("Climber Up", false); // gets magnitude of left joystick

  }

  public void climberUp(){
    SmartDashboard.putBoolean("Climber Up", true); // gets magnitude of left joystick
    ClimberMotor.set(.1);
    SmartDashboard.putNumber("Climber Position", ClimberEncoder.getPosition()); // gets magnitude of left joystick
    SmartDashboard.putNumber("Climber Speed", ClimberEncoder.getVelocity());

  }
  public void climberDown(){
    ClimberMotor.set(-.1);

    SmartDashboard.putNumber("Climber Position", ClimberEncoder.getPosition()); // gets magnitude of left joystick
    SmartDashboard.putNumber("Climber Speed", ClimberEncoder.getVelocity());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
