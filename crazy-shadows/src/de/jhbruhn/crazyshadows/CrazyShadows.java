package de.jhbruhn.crazyshadows;

import box2dLight.RayHandler;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CrazyShadows extends Game {
	public static final float FRAME_WIDTH = 640;
	public static final float FRAME_HEIGHT = 480;
	public static final String[] LEVELS = { "level_1" };
	
	public RayHandler ray;
	private boolean batchOn = false;
	private SpriteBatch batch;
	private int currentLevel = 0;

	@Override
	public void create() {
		batch = new SpriteBatch();

		setScreen(new GameScreen(this, LEVELS[currentLevel++]));
	}

	public void startBatch() {
		if (!batchOn) {
			getBatch().begin();
			batchOn = true;
		}
	}

	public void endBatch() {
		if (batchOn) {
			getBatch().end();
			batchOn = false;
		}
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public void requestNextLevel() {
		if (LEVELS.length < currentLevel)
			setScreen(new GameScreen(this, LEVELS[currentLevel++]));

	}

}
