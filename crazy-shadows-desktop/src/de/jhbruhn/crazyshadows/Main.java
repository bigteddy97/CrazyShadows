package de.jhbruhn.crazyshadows;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.jhbruhn.crazyshadows.utils.ImagePacker;

public class Main {
	public static void main(String[] args) {
		ImagePacker.run();
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "crazy-shadows";
		cfg.useGL20 = false;
		cfg.samples = 16;

		new LwjglApplication(new CrazyShadows(), cfg);
	}
}
