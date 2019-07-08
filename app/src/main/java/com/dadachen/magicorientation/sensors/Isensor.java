package com.dadachen.magicorientation.sensors;

public interface Isensor {
	
	public boolean isSupport();
	
	public void on(int speed);
	
	public void off();

	public float getMaximumRange();
}
