package com.tomasajt.kornr.util;

public class Toggleable {
	private boolean isOn = false;

	public void on() {
		isOn = true;
	}

	public void off() {
		isOn = false;
	}

	public boolean isOn() {
		return isOn;
	}
	
	public final void toggle() {
		if (isOn()) {
			off();
		} else {
			on();
		}
	}
}
