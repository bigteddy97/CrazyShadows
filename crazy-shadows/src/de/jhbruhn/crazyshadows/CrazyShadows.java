package de.jhbruhn.crazyshadows;

import com.badlogic.gdx.Game;

public class CrazyShadows extends Game {
	public static final float FRAME_WIDTH = 640;
	public static final float FRAME_HEIGHT = 480;

	@Override
	public void create() {

		setScreen(new GameScreen(this));
	}

}
