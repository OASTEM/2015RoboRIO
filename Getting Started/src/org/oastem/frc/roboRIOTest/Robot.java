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
    private final double goalRate = 10;
    
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
        motor1.setPositionMode(CANJaguar.kQuadEncoder, 497, -1000, -.002, 1000);
        
        accel = new Accelerator();
        
        dashboard = new Dashboard();
        
        robostate = new RobotState();
        
        enabled = true;
        /*server = CameraServer.getInstance();
        server.setQuality(50);
        //the camera name (ex "cam0") can be found through the roborio web interface
        server.startAutomaticCapture(new USBCamera("cam0"));
        //*/
        
        
        //encoder = new Encoder(ENCODER_CH_A, ENCODER_CH_B);
        encoder = new QuadratureEncoder(ENCODER_CH_A, ENCODER_CH_B, false, 4, 497);
        //encoder.setDistancePerPulse(WHEEL_CIRCUMFERENCE);
        /*
        gyro = new ADW22307Gyro(GYRO_PORT);
        //*/
        //solen = new DoubleSolenoid(PCM_MODULE_NO, SOLEN_FORWARD_CHANNEL, SOLEN_BACKWARD_CHANNEL);
        
    }
    
    public void autonomous(){
    	dashboard.putString("Robot State: ", "Autonomous");
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
        
        motor1.enableControl(0);

        
        while(isEnabled() && isOperatorControl()){
            currentTime = System.currentTimeMillis();
            //ds.mecanumDrive(js.getX(), js.getY(), js.getZ(), gyro.getAngle());
            motor1.setPositionMode(CANJaguar.kQuadEncoder, 497, -1000, -.002, 1000);
            motor1.enableControl();
            motor1.set(position);
            state = "Operator Control";
            
            dashboard.putString("Robot State: ", state);
            dashboard.putBoolean("Robot Enabled?: ", enabled);
            dashboard.putData("ENC_JAG: ", motor1);
            dashboard.putNumber("Position var: ", position);
            dashboard.putNumber("Jag Position: ", motor1.getPosition());
            
            if (js.getRawButton(11) && canPress)
            {
            	position += 10;
            	canPress = false;
            }
            else if (js.getRawButton(10) && canPress)
            {
            	position -= 10;
            	canPress = false;
            }
            if (!js.getRawButton(11) && !js.getRawButton(10))
            	canPress = true;
            
            
            if (((position - .25) < motor1.getPosition()) && (motor1.getPosition() < (position + .25)))
            {
            	motor1.set(position);
            }
            else if (((position - 2) < motor1.getPosition()) && (motor1.getPosition() < (position + 2)))
            {
            	motor1.setPercentMode(CANJaguar.kQuadEncoder, 497);
            	motor1.enableControl();
            	// we have to assume that .get() will return a nonzero value immediately
            	//check for that
            	dashboard.putNumber("Percentage: ", motor1.get());
            	motor1.set(accel.decelerateValue(motor1.get(), 0));
            }
            	
            	
            
                        
            dashboard.putNumber("Total power: ", panel.getTotalPower());
            
            dashboard.putData("PDP: ", panel);
            
            
            
            
            
            
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
    	dashboard.putString("Robot State: ", "Testing");
    }
    
    public void disabled()
    {
        
    }
}
