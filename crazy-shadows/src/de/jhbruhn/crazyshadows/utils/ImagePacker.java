package de.jhbruhn.crazyshadows.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class ImagePacker {

	public static void run() {
		Settings settings = new Settings();
		settings.paddingX = 2;
		settings.paddingY = 2;
		settings.minHeight = 1024;
		settings.minWidth = 1024;
		settings.filterMin = Texture.TextureFilter.Linear;
		settings.filterMag = Texture.TextureFilter.Linear;

		try {
			TexturePacker2.process(settings,
					"../crazy-shadows-android/assets/textures",
					"../crazy-shadows-android/assets/textures-packed", "pack");
			System.out.println("Packed!");
		} catch (Exception e) {
			System.out.println("Didn't pack!");
		}
	}

}
