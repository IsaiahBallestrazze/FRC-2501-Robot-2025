package frc.robot.subsystems;
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class NEOPixles extends SubsystemBase {

   private final AddressableLED NEOP = new AddressableLED(9);
 private final AddressableLEDBuffer NEOPS  = new AddressableLEDBuffer(30);


  public void setRGB(int index, int r, int g, int b){
   NEOPS.setRGB(index, r, g, b);
  }

  public void setRGBs(){
    NEOP.setData(NEOPS);
  }


  public NEOPixles() {
  NEOP.setLength(NEOPS.getLength());
  NEOP.start();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
