package com.fish.chip8emu.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.fish.chip8emu.chip8emu;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();


		config.height = 360;
		config.width = 640;
		new LwjglApplication(new chip8emu(), config);
	}
}
