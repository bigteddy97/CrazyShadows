package de.jhbruhn.crazyshadows;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.jhbruhn.crazyshadows.utils.ImagePacker;

public class Main {
	public static void main(String[] args) {
		ImagePacker.run();
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Crazy Shadows";
		cfg.useGL20 = true;
		cfg.samples = 16;
		cfg.vSyncEnabled = true;

		new LwjglApplication(new CrazyShadows(), cfg);
	}
}
