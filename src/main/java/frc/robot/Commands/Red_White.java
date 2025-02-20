// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Commands;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.NEOPixles;

public class Red_White extends Command {
  private final NEOPixles s_NeoPixels;
  public Red_White(NEOPixles d_NeoPixels) {
    s_NeoPixels = d_NeoPixels;
    addRequirements(s_NeoPixels);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    s_NeoPixels.setRGBs();
    s_NeoPixels.setRGB(0, 255, 0, 0);
    s_NeoPixels.setRGB(1, 255, 255, 255);
    s_NeoPixels.setRGB(2, 255, 0, 0);
    s_NeoPixels.setRGB(3, 255, 255, 255);
    s_NeoPixels.setRGB(4, 255, 0, 0);
    s_NeoPixels.setRGB(5, 255, 255, 255);
    s_NeoPixels.setRGB(6, 255, 0, 0);
    s_NeoPixels.setRGB(7, 255, 255, 255);
    s_NeoPixels.setRGB(8, 255, 0, 0);
    s_NeoPixels.setRGB(9, 255, 255, 255);
    s_NeoPixels.setRGB(10, 255, 0, 0);
    s_NeoPixels.setRGB(11, 255, 255, 255);
    s_NeoPixels.setRGB(12, 255, 0, 0);
    s_NeoPixels.setRGB(13, 255, 255, 255);
    s_NeoPixels.setRGB(14, 255, 0, 0);
    s_NeoPixels.setRGB(15, 255, 255, 255);
    s_NeoPixels.setRGB(16, 255, 0, 0);
    s_NeoPixels.setRGB(17, 255, 255, 255);
    s_NeoPixels.setRGB(18, 255, 0, 0);
    s_NeoPixels.setRGB(19, 255, 255, 255);
    s_NeoPixels.setRGB(20, 255, 0, 0);
    s_NeoPixels.setRGB(21, 255, 255, 255);
    s_NeoPixels.setRGB(22, 255, 0, 0);
    s_NeoPixels.setRGB(23, 255, 255, 255);
    s_NeoPixels.setRGB(24, 255, 0, 0);
    s_NeoPixels.setRGB(25, 255, 255, 255);
    s_NeoPixels.setRGB(26, 255, 0, 0);
    s_NeoPixels.setRGB(27, 255, 255, 255);
    s_NeoPixels.setRGB(28, 255, 0, 0);
    s_NeoPixels.setRGB(29, 255, 255, 255);
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}