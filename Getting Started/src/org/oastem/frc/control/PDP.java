package org.oastem.frc.control;

import edu.wpi.first.wpilibj.*;

/**
 * @author Mandarker
 */

public class PDP {
	// creates a PDP(Power Distribution Panel) object
	private PowerDistributionPanel panel;
	
	/**
	 * The PDP provides the electricity for all of the other components of the robot.
	 * It is able to measure the currents from all of the connected components, which is
	 * extremely useful for motors. This allows the user to know the level of stress the
	 * motor is taking since the motor needs extra electricity to run.
	 * @param channel: the port number on the PDP
	 */
	// initializes the panel
	public PDP(){
		panel = new PowerDistributionPanel();
	}
	
	// removes the PDP sticky faults (problems that make the LED light "stick")
	public void clearStickyFaults(){
		panel.clearStickyFaults();
	}
	
	// returns the current of a channel
	public double getCurrent(int channel){
		return panel.getCurrent(channel);
	}
	
	// returns a String that the SmartDashboard can use to display the PDP
	public String getSmartDashboardType(){
		return panel.getSmartDashboardType();
	}
	
	// returns the temperature of the PDP
	public double getTemperature(){
		return panel.getTemperature();
	}
	
	// returns the total amount of current of all of the channels
	public double getTotalCurrent(){
		return panel.getTotalCurrent();
	}
	
	// returns the total amount of power of all of the channels
	public double getTotalPower(){
		return panel.getTotalPower();
	}
	
	// returns the total amount of voltage of all of the channels
	public double getVoltage(){
		return panel.getVoltage();
	}
	
	// sets the total amount of energy to 0
	public void resetTotalEnergy(){
		panel.resetTotalEnergy();
	}
}
