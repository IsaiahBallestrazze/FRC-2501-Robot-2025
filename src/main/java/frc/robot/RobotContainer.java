// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.NEOPixles;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import java.io.File;
import swervelib.SwerveInputStream;
import swervelib.math.SwerveMath;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very
 * little robot logic should actually be handled in the {@link Robot} periodic
 * methods (other than the scheduler calls).
 * Instead, the structure of the robot (including subsystems, commands, and
 * trigger mappings) should be declared here.
 */
public class RobotContainer {

  private final NEOPixles s_neo = new NEOPixles();
  private final Climber s_climber = new Climber();
  private final Elevator s_Elevator = new Elevator();
  private final Intake s_Intake = new Intake();



  
  // Replace with CommandPS4Controller or CommandJoystick if needed
  final CommandXboxController driverXbox = new CommandXboxController(0);
  final CommandXboxController controlXbox = new CommandXboxController(1);
  // The robot's subsystems and commands are defined here...
  private final SwerveSubsystem drivebase = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(),
      "swerve/neo"));

  /**
   * Converts driver input into a field-relative ChassisSpeeds that is controlled
   * by angular velocity.
   */
  SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
      () -> driverXbox.getLeftY() * -1,
      () -> driverXbox.getLeftX() * -1)
      .withControllerRotationAxis(driverXbox::getRightX)
      .deadband(OperatorConstants.DEADBAND)
      .scaleTranslation(0.8)
      .allianceRelativeControl(true);

  /**

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
    DriverStation.silenceJoystickConnectionWarning(true);
    NamedCommands.registerCommand("test", Commands.print("I EXIST"));
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be
   * created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with
   * an arbitrary predicate, or via the
   * named factories in
   * {@link edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses
   * for
   * {@link CommandXboxController
   * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller PS4}
   * controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick
   * Flight joysticks}.
   */
  private void configureBindings() {
    Command driveFieldOrientedAnglularVelocity = drivebase.driveFieldOriented(driveAngularVelocity);

    drivebase.setDefaultCommand(driveFieldOrientedAnglularVelocity);

    driverXbox.a().onTrue((Commands.runOnce(drivebase::zeroGyro)));
    driverXbox.b().whileTrue(
        drivebase.driveToPose(
            new Pose2d(new Translation2d(4, 4), Rotation2d.fromDegrees(0))));
    driverXbox.start().whileTrue(Commands.none());
    driverXbox.back().whileTrue(Commands.none());
    driverXbox.leftBumper().whileTrue(Commands.runOnce(drivebase::lock, drivebase).repeatedly());
    driverXbox.rightBumper().onTrue(Commands.none());

    controlXbox.x().whileTrue(new RunCommand(() -> s_Intake.AlgaeIntake(.3))); // resets relavtive
    controlXbox.x().whileFalse(new RunCommand(() -> s_Intake.AlgaeIntake(0))); // resets relavtive

    controlXbox.y().whileTrue(new RunCommand(() -> s_Intake.AlgaeIntake(-.3))); // resets relavtive
    controlXbox.y().whileFalse(new RunCommand(() -> s_Intake.AlgaeIntake(0))); // resets relavtive

    controlXbox.a().whileTrue(new RunCommand(() -> s_Intake.CoralIntake(.5,-1000))); // resets relavtive
    controlXbox.a().whileFalse(new RunCommand(() -> s_Intake.CoralIntake(0,-1000))); // resets relavtive

    controlXbox.b().whileTrue(new RunCommand(() -> s_Intake.CoralIntake(-.5,-3000))); // resets relavtive
    controlXbox.b().whileFalse(new RunCommand(() -> s_Intake.CoralIntake(0,-3000))); // resets relavtive

    controlXbox.rightBumper().whileTrue(new RunCommand(() -> s_Intake.CoralIntake(0,-5000))); // resets relavtive
    controlXbox.rightBumper().whileFalse(new RunCommand(() -> s_Intake.CoralIntake(0,-5000))); // resets relavtive

    // //D-Pad
    controlXbox.povUp().whileTrue(new RunCommand(() -> s_Intake.pivotUp()));
    controlXbox.povUp().whileFalse(new RunCommand(() -> s_Intake.pivotStop()));

    controlXbox.povDown().whileTrue(new RunCommand(() -> s_Intake.pivotDown()));
    controlXbox.povDown().whileFalse(new RunCommand(() -> s_Intake.pivotStop()));

    controlXbox.povLeft().whileTrue(new RunCommand(() -> s_climber.ClimberMotorUp()));
    controlXbox.povLeft().whileFalse(new RunCommand(() -> s_climber.ClimberMotorStop()));

    controlXbox.povRight().whileTrue(new RunCommand(() -> s_climber.ClimberMotorDown()));
    controlXbox.povRight().whileFalse(new RunCommand(() -> s_climber.ClimberMotorStop()));

    controlXbox.leftTrigger().whileTrue(new RunCommand(() -> s_Elevator.ElevatorMotorUp(.2)));
    controlXbox.leftTrigger().whileFalse(new RunCommand(() -> s_Elevator.ElevatorMotorUp(0)));

    controlXbox.rightTrigger().whileTrue(new RunCommand(() -> s_Elevator.ElevatorMotorDown(-.2)));
    controlXbox.rightTrigger().whileFalse(new RunCommand(() -> s_Elevator.ElevatorMotorDown(0)));


  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return drivebase.getAutonomousCommand("New Auto");
  }

  public void setMotorBrake(boolean brake) {
    drivebase.setMotorBrake(brake);
  }
}
