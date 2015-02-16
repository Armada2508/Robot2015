package org.usfirst.frc.team2508.robot.components;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Dashboard {
    
    /**
     * Set a dashboard string.
     * @param key
     * @param value
     */
    public void put(String key, String value) {
        SmartDashboard.putString(key, value);
    }

    /**
     * Set a dashboard integer.
     * @param key
     * @param value
     */
    public void put(String key, double value) {
        SmartDashboard.putNumber(key, value);
    }

    /**
     * Set a dashboard boolean.
     * @param key
     * @param value
     */
    public void put(String key, boolean value) {
        SmartDashboard.putBoolean(key, value);
    }
    
}
