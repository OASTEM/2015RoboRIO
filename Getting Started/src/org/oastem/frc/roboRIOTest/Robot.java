/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.oastem.frc.roboRIOTest;


import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.vision.USBCamera;

import org.oastem.frc.control.*;
//import org.oastem.frc.Debug;
import org.oastem.frc.sensor.*;
import org.oastem.frc.*;

/**
 * NOTES DURING PRESENTATION
 * 
 * TECH HAS TO:
 * 
 * determine if a tote is on the pulley system or not
 * be able to easily accelerate/deccelerate lifting mechanism
 * 
 * 
 * ideas:
 * check the voltage consumption of motors used
 */


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends SampleRobot {
    
    private DriveSystem ds;
    private Joystick js;
    private PowerDistributionPanel panel;
    private RobotState robostate;
    
    private CANJaguar motor1;
    private Jaguar motor2;
    private Jaguar motor3;
    private Jaguar motor4;
    
    private Accelerator accel;
    
    private Dashboard dashboard;
    
    private final int LEFT_FRONT_DRIVE_PORT = 1;
    private final int LEFT_BACK_DRIVE_PORT = 2;
    private final int RIGHT_FRONT_DRIVE_PORT = 3;
    private final int RIGHT_BACK_DRIVE_PORT = 4;
    
    private final int JOYSTICK = 0; 
    
    private final int WHEEL_CIRCUMFERENCE = 7;
    private final int ENCODER_CH_A = 0;
    private final int ENCODER_CH_B = 1;
    private final int ENC_JAG_CAN_ID = 3;
    private final int pulsesPerRev = 497;
    
    private String state;
    private boolean enabled;
    
    private QuadratureEncoder encoder;
    private ADW22307Gyro gyro;
    
    private Compressor comp;
    private DoubleSolenoid solen;
    
    private final int GYRO_PORT = 1;
    
    private final int SOLEN_FORWARD_CHANNEL = 3;  
    private final int SOLEN_BACKWARD_CHANNEL = 2;
    private final int PCM_MODULE_NO = 62;
    
    private final int SOL_FORWARD_BUTTON = 4;
    private final int SOL_REVERSE_BUTTON = 5;
    //*/
    
    
    public void robotInit()
    {
        /*ds = DriveSystem.getInstance();
        ds.initializeDrive(LEFT_FRONT_DRIVE_PORT, LEFT_BACK_DRIVE_PORT, 
                RIGHT_FRONT_DRIVE_PORT, RIGHT_BACK_DRIVE_PORT);
        
        motor1 = new Jaguar(LEFT_FRONT_DRIVE_PORT);

        motor2 = new Jaguar(LEFT_BACK_DRIVE_PORT);
        motor3 = new Jaguar(RIGHT_FRONT_DRIVE_PORT);
        motor4 = new Jaguar(RIGHT_BACK_DRIVE_PORT);
        */
        js = new Joystick(JOYSTICK);
        panel = new PowerDistributionPanel();

        motor1 = new CANJaguar(ENC_JAG_CAN_ID);
        
        accel = new Accelerator();
        
        dashboard = new Dashboard();
        
        robostate = new RobotState();
        
        /*server = CameraServer.getInstance();
        server.setQuality(50);
        //the camera name (ex "cam0") can be found through the roborio web interface
        server.startAutomaticCapture(new USBCamera("cam0"));
        //*/
        
        /*
        //encoder = new Encoder(ENCODER_CH_A, ENCODER_CH_B);
        encoder = new QuadratureEncoder(ENCODER_CH_A, ENCODER_CH_B, true, 4, 479);
        encoder.setDistancePerPulse(WHEEL_CIRCUMFERENCE);
        
        gyro = new ADW22307Gyro(GYRO_PORT);
        //*/
        //solen = new DoubleSolenoid(PCM_MODULE_NO, SOLEN_FORWARD_CHANNEL, SOLEN_BACKWARD_CHANNEL);
        
    }
    
    
    

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        long currentTime;
        long startTime = 0;
        double position = 0;
        boolean motorStart = false;
        boolean canPress = false;
        panel.clearStickyFaults();
        //motor1.setPositionMode(CANJaguar.kQuadEncoder, 497, -1000, -.002, 1000);
        //increasing (decreasing) Integral will increase the distance traveled but lessen error
        motor1.setSpeedMode(CANJaguar.kQuadEncoder, 497, 1000, .002, 1);
        //motor1.setPercentMode();
        motor1.enableControl();
        motor1.set(1);
        //position = motor1.getPosition() + 10.0;
        //motor1.set(position);
        //dashboard.putNumber("Original Position", motor1.getPosition());
        
        while(isEnabled() && isOperatorControl()){
        	//motor1.set(1);
            currentTime = System.currentTimeMillis();
            //ds.mecanumDrive(js.getX(), js.getY(), js.getZ(), gyro.getAngle());
            //motor1.set(position);
            
            if (robostate.isAutonomous()){
            	state = "Autonomous";
            }
            if (robostate.isDisabled()){
            	enabled = false;
            }
            if (robostate.isEnabled()){
            	enabled = true;
            }
            if (robostate.isOperatorControl()){
            	state = "Operator Control";
            }
            if (robostate.isTest()){
            	state = "Testing";
            }
            
            dashboard.putString("Robot State: ", state);
            dashboard.putBoolean("Robot Enabled?: ", enabled);
            dashboard.putData("ENC_JAG: ", motor1);
            dashboard.putNumber("Rate: ", motor1.getSpeed());
            System.out.println(motor1.getSpeed());
            dashboard.putNumber("Position var: ", position);
            
            if (js.getRawButton(11) && canPress)
            {
            	position += 2;
            	canPress = false;
            }
            else if (js.getRawButton(10) && canPress)
            {
            	position -= 2;
            	canPress = false;
            }
            if (!js.getRawButton(11) && !js.getRawButton(10))
            	canPress = true;
            /*
            dashboard.putData("Enc Jag:", motor1);
            dashboard.putNumber("Jag Position: ", motor1.getPosition());
            dashboard.putNumber("Position var", position);
            dashboard.putNumber("Current Position:", motor1.getPosition());
            System.out.println(motor1.getPosition());
            */
            // OUTPUT
                        
            dashboard.putNumber("Total power: ", panel.getTotalPower());
            
            dashboard.putData("PDP: ", panel);
            
            
            /*
            // ACCELERATION!!!
            // Set to percentMode()
            motor1.set(accel.accelerateValue(js.getY()));
            //motor1.set(js.getY());
            dashboard.putNumber("Acceleration currSpeed: ", accel.getSpeed());
            //*/
            
            
            /*
            if (js.getRawButton(SOL_FORWARD_BUTTON))
            {
                solen.set(DoubleSolenoid.Value.kForward);
                dashboard.putString("Piston", "Forward!");
            }
            else if (js.getRawButton(SOL_REVERSE_BUTTON))
            {
                solen.set(DoubleSolenoid.Value.kReverse);
                dashboard.putString("Piston", "REVERSE!");
            }
            else
            {
                solen.set(DoubleSolenoid.Value.kOff);
                dashboard.putString("Piston", "OFF!");
            }//*/
            
        }
    }
    
    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
    
    }
    
    public void disabled()
    {
        
    }
}
