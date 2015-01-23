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
    
    private Jaguar motor1;
    private Jaguar motor2;
    private Jaguar motor3;
    private Jaguar motor4;
    
    private Dashboard dashboard;
    
    //String[] debug = new String[6];
    
    private final int LEFT_FRONT_DRIVE_PORT = 1;
    private final int LEFT_BACK_DRIVE_PORT = 2;
    private final int RIGHT_FRONT_DRIVE_PORT = 3;
    private final int RIGHT_BACK_DRIVE_PORT = 4;
    
    private final int JOYSTICK = 0; 
    
    private final int WHEEL_CIRCUMFERENCE = 7;
    private final int ENCODER_CH_A = 0;
    private final int ENCODER_CH_B = 1;
    private final int ENC_JAG_PORT = 0;

    
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
    
    CameraServer server;
    
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
        

        motor1 = new Jaguar(ENC_JAG_PORT);
        
        dashboard = new Dashboard();

        /*
        server = CameraServer.getInstance();
        server.setQuality(50);
        //the camera name (ex "cam0") can be found through the roborio web interface
        server.startAutomaticCapture();
        //*/
        
        //encoder = new Encoder(ENCODER_CH_A, ENCODER_CH_B);
        encoder = new QuadratureEncoder(ENCODER_CH_A, ENCODER_CH_B, true, 4, 479);
        encoder.setDistancePerPulse(WHEEL_CIRCUMFERENCE);
        
        gyro = new ADW22307Gyro(GYRO_PORT);
        
        //solen = new DoubleSolenoid(PCM_MODULE_NO, SOLEN_FORWARD_CHANNEL, SOLEN_BACKWARD_CHANNEL);
        
        //Debug.clear();
        //Debug.log(1, 1, "Robot initialized.");
    }
    
    
    

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        long currentTime;
        long startTime = 0;
        boolean motorStart = false;
        encoder.reset();
        panel.clearStickyFaults();
        //Debug.clear();
        //js = new Joystick(JOYSTICK);
        //solen = new DoubleSolenoid(PCM_MODULE_NO, SOLEN_FORWARD_CHANNEL, SOLEN_BACKWARD_CHANNEL);
        
        while(isEnabled() && isOperatorControl()){
        	
            //Debug.clear();
            currentTime = System.currentTimeMillis();
            //debug[0] = "Drive Speed: " + js.getY();
            //ds.mecanumDrive(js.getX(), js.getY(), js.getZ(), gyro.getAngle());
            motor1.set(js.getY());
            
            // OUTPUT
            
            dashboard.putNumber("Enc: ", encoder.get());
            dashboard.putNumber("Temp: ", panel.getTemperature());
            dashboard.putNumber("Total current: ", panel.getTotalCurrent());
            dashboard.putNumber("Total power: ", panel.getTotalPower());
            dashboard.putNumber("Voltage: ", panel.getVoltage());
            
            dashboard.putData("PDP: ", panel);
            System.out.println(panel.getCurrent(3));
            
            // GET DIRECTION
            if (encoder.isGoingForward() == true)
                dashboard.putString("Going Forward?", "Yes");
            else
                dashboard.putString("Going Forward?", "No");
            //
            
            
            // get VS getRaw
            dashboard.putString("rawEnc: ", encoder.getRaw() + "");
            
            
            // distancePerPulse
            //ACTIVATE LINE AT TOP OF METHOD
            dashboard.putString("Distance: ", encoder.getDistance() + "");
            
            
            // getRate
            dashboard.putString("Rate: ", encoder.getRate() + "");
            
            
            
            // encodingScale
            //ACTIVATE LINE AT INIT
            //look at how enc.get() is different
            //also compare with getRaw()
            //*/
            
            /*
            if (js.getRawButton(SOL_FORWARD_BUTTON))
            {
                dashboard.putString("Piston", "Forward");
            }
            else if (js.getRawButton(SOL_REVERSE_BUTTON))
            {
                dashboard.putString("Piston", "Backward");
            }
            else
            {
                dashboard.putString("Piston", "Stable swag");
            }//*/
            
            /*
            if (js.getRawButton(SOL_FORWARD_BUTTON))
            {
                solen.set(DoubleSolenoid.Value.kForward);
                System.out.println("Forward!");
                //debug[1] = "solen FORWARD";
            }
            else if (js.getRawButton(SOL_REVERSE_BUTTON))
            {
                solen.set(DoubleSolenoid.Value.kReverse);
                System.out.println("REVERSE!");
                //debug[1] = "solen REVERSE";
            }
            else
            {
                solen.set(DoubleSolenoid.Value.kOff);
                System.out.println("OFF!");
                //debug[1] = "solen OFF";
            }//*/
            
            
            
            //debug[1] = "Gyro: " + gyro.getAngle();
            //Debug.log(debug);
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
