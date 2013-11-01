package de.jhbruhn.crazyshadows;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.jhbruhn.crazyshadows.utils.ImagePacker;

public class Main {
	public static void main(String[] args) {
		ImagePacker.run();
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Crazy Shadows";
		cfg.useGL20 = false;
		cfg.samples = 4;
		cfg.vSyncEnabled = true;
		cfg.depth = 0;	

		new LwjglApplication(new CrazyShadows(), cfg);
	}
}
