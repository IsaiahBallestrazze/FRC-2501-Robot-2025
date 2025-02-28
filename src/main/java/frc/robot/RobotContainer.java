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
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
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
  // final CommandXboxController controlXbox = new CommandXboxController(1);
  private final Joystick ButtonBox = new Joystick(1);

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

  SwerveInputStream driveAngularVelocityHalfSpeed = SwerveInputStream.of(drivebase.getSwerveDrive(),
      () -> (driverXbox.getLeftY() * -1) * 0.5,
      () -> (driverXbox.getLeftX() * -1) * 0.5)
      .withControllerRotationAxis(() -> driverXbox.getRightX() * 0.5)
      .deadband(OperatorConstants.DEADBAND)
      .scaleTranslation(0.8)
      .allianceRelativeControl(true);

      SwerveInputStream driveFieldOrientedQuarterSpeed = SwerveInputStream.of(drivebase.getSwerveDrive(),
      () -> (driverXbox.getLeftY() * -1) * 0.3,
      () -> (driverXbox.getLeftX() * -1) * 0.3)
      .withControllerRotationAxis(() -> driverXbox.getRightX() * 0.3)
      .deadband(OperatorConstants.DEADBAND)
      .scaleTranslation(0.8)
      .allianceRelativeControl(true);

  /**
   * 
   * /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the trigger bindings
    //  final Intake s_Intake = new Intake();
    NamedCommands.registerCommand("Coral Tilt", new RunCommand(() -> s_Intake.CoralTilt(3000)));
    NamedCommands.registerCommand("Coral Shoot", new RunCommand(() -> s_Intake.CoralIntake(.8)));
    NamedCommands.registerCommand("Elevator Mid", new RunCommand(() -> s_Elevator.ElevationSet(30)));
  // NamedCommands.registerCommand("Alage Go", new RunCommand(() -> s_Intake.CoralIntake(.8)));
  // NamedCommands.registerCommand("Alage Go", new RunCommand(() -> s_Intake.AlgaeIntake(.7)));


    configureBindings();
    DriverStation.silenceJoystickConnectionWarning(true);
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
    drivebase.setDefaultCommand(drivebase.driveFieldOriented(driveAngularVelocity));

    driverXbox.a().onTrue((Commands.runOnce(drivebase::zeroGyro)));
    driverXbox.b().whileTrue(Commands.none());
    driverXbox.start().whileTrue(Commands.none());
    driverXbox.back().whileTrue(Commands.none());
    driverXbox.button(7).whileTrue(Commands.runOnce(drivebase::lock, drivebase).repeatedly());
    driverXbox.rightBumper().whileTrue(drivebase.driveFieldOriented(driveAngularVelocityHalfSpeed));
    driverXbox.leftBumper().whileTrue(drivebase.driveFieldOriented(driveFieldOrientedQuarterSpeed));

    // climber on D-pad
    driverXbox.povUp().whileTrue(new RunCommand(() -> s_climber.ClimberMotorUp()));
    driverXbox.povUp().whileFalse(new RunCommand(() -> s_climber.ClimberMotorStop()));

    driverXbox.povDown().whileTrue(new RunCommand(() -> s_climber.ClimberMotorDown()));
    driverXbox.povDown().whileFalse(new RunCommand(() -> s_climber.ClimberMotorStop()));

    // // Button Box MK1
    // JoystickButton CoralUp = new JoystickButton(ButtonBox, 1);
    // JoystickButton CoralDown = new JoystickButton(ButtonBox, 2);
    // JoystickButton CoralOut = new JoystickButton(ButtonBox, 3);
    // // JoystickButton CoralIn = new JoystickButton(ButtonBox, 4);
    // JoystickButton AlgaeLevelIn = new JoystickButton(ButtonBox, 4);

    // JoystickButton AlgaeOut = new JoystickButton(ButtonBox, 5);
    // JoystickButton AlgaeIn = new JoystickButton(ButtonBox, 6);

    // JoystickButton Elevator0 = new JoystickButton(ButtonBox, 8);
    // JoystickButton Elevator1 = new JoystickButton(ButtonBox, 7);
    // JoystickButton Elevator2 = new JoystickButton(ButtonBox, 10);
    // JoystickButton Elevator3 = new JoystickButton(ButtonBox, 9);

    // Button Box MK2

    // elevator
    JoystickButton elevatorAlgaeIn = new JoystickButton(ButtonBox, 1);
    JoystickButton elevatorAlgae = new JoystickButton(ButtonBox, 2);
    JoystickButton PivotMidThing = new JoystickButton(ButtonBox, 5);
    JoystickButton elevatorIntake = new JoystickButton(ButtonBox, 4);
    JoystickButton elevatorlevel1 = new JoystickButton(ButtonBox, 6);
    JoystickButton elevatorLevel2 = new JoystickButton(ButtonBox, 8);
    JoystickButton elevatorHigh = new JoystickButton(ButtonBox, 10);

    // algae
    JoystickButton AlgaeOut = new JoystickButton(ButtonBox, 9);
    JoystickButton AlgaeIn = new JoystickButton(ButtonBox, 7);

    // Coral
    JoystickButton CoralOut = new JoystickButton(ButtonBox, 3);

    PivotMidThing.whileTrue(new RunCommand(() -> s_Intake.CoralTilt(3200)));




    AlgaeIn.whileTrue(new RunCommand(() -> s_Intake.AlgaeIntake(-.7))); // resets relavtive
    AlgaeIn.whileFalse(new RunCommand(() -> s_Intake.AlgaeIntake(0))); // resets relavtive

    AlgaeOut.whileTrue(new RunCommand(() -> s_Intake.AlgaeIntake(.7))); // resets relavtive
    AlgaeOut.whileFalse(new RunCommand(() -> s_Intake.AlgaeIntake(0))); // resets relavtive

    CoralOut.whileTrue(new RunCommand(() -> s_Intake.CoralIntake(.8))); // resets relavtive
    CoralOut.whileFalse(new RunCommand(() -> s_Intake.CoralIntake(0))); // resets relavtive

    // Elevator Things
    elevatorAlgaeIn.whileTrue(new RunCommand(() -> s_Elevator.ElevationSet(0)));
    elevatorAlgaeIn.whileTrue(new RunCommand(() -> s_Intake.AlgaeIntake(-.7)));
    elevatorAlgaeIn.whileFalse(new RunCommand(() -> s_Intake.AlgaeIntake(0)));

    elevatorAlgae.whileTrue(new RunCommand(() -> s_Elevator.ElevationSet(12)));
    elevatorAlgae.whileFalse(new RunCommand(() -> s_Elevator.ElevatorMotorUp(0)));

    elevatorIntake.whileTrue(new RunCommand(() -> s_Elevator.ElevationSet(22)));
    elevatorIntake.whileTrue(new RunCommand(() -> s_Intake.CoralTilt(1500)));
    elevatorIntake.whileTrue(new RunCommand(() -> s_Intake.CoralIntake(-.7)));
    elevatorIntake.whileFalse(new RunCommand(() -> s_Elevator.ElevatorMotorDown(0)));
    elevatorIntake.whileFalse(new RunCommand(() -> s_Intake.CoralIntake(0)));
    elevatorIntake.whileFalse(new RunCommand(() -> s_Intake.pivotStop()));

    elevatorlevel1.whileTrue(new RunCommand(() -> s_Elevator.ElevationSet(19)));
    elevatorlevel1.whileTrue(new RunCommand(() -> s_Intake.CoralTilt(2000)));
    elevatorlevel1.whileFalse(new RunCommand(() -> s_Elevator.ElevatorMotorDown(0)));

    elevatorLevel2.whileTrue(new RunCommand(() -> s_Elevator.ElevationSet(65)));
    elevatorLevel2.whileTrue(new RunCommand(() -> s_Intake.CoralTilt(2000)));
    elevatorLevel2.whileFalse(new RunCommand(() -> s_Elevator.ElevatorMotorDown(0)));

    elevatorHigh.whileTrue(new RunCommand(() -> s_Elevator.ElevationSet(100)));
    elevatorHigh.whileTrue(new RunCommand(() -> s_Intake.CoralTilt(2000)));
    elevatorHigh.whileFalse(new RunCommand(() -> s_Elevator.ElevatorMotorDown(0)));

    // CoralUp.whileTrue(new RunCommand(() -> s_Intake.CoralTilt(1500)));
    // CoralUp.whileTrue(new RunCommand(() -> s_Intake.CoralIntake(-.8)));

    // CoralUp.whileFalse(new RunCommand(() -> s_Intake.pivotStop()));
    // CoralUp.whileFalse(new RunCommand(() -> s_Intake.CoralIntake(0)));

    // CoralDown.whileTrue(new RunCommand(() -> s_Intake.CoralTilt(3000)));
    // CoralDown.whileFalse(new RunCommand(() -> s_Intake.pivotStop()));

    // // CoralUp.whileTrue(new RunCommand(() -> s_Intake.CoralPivotTest(1000)));

    // Elevator0.whileTrue(new RunCommand(() -> s_Elevator.ElevationSet(0)));
    // Elevator0.whileFalse(new RunCommand(() -> s_Elevator.ElevatorMotorUp(0)));

    // Elevator2.whileTrue(new RunCommand(() -> s_Elevator.ElevationSet(58)));
    // Elevator2.whileFalse(new RunCommand(() -> s_Elevator.ElevatorMotorUp(0)));

    // Elevator3.whileTrue(new RunCommand(() -> s_Elevator.ElevationSet(123)));
    // Elevator3.whileFalse(new RunCommand(() -> s_Elevator.ElevatorMotorUp(0)));
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
