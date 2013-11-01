package de.jhbruhn.crazyshadows;

import box2dLight.RayHandler;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CrazyShadows extends Game {
	public static final String PREFERENCES_NAME = "crazyshadows";
	public static final String PREF_CURRENT_LEVEL = "currentLevel";
	public static final float FRAME_WIDTH = 640;
	public static final float FRAME_HEIGHT = 480;
	public static final String[] LEVELS = { "level_1", "level_2", "level_3" };

	public RayHandler ray;
	private boolean batchOn = false;
	private SpriteBatch batch;
	private int currentLevel = 0;

	private Preferences p;

	@Override
	public void create() {
		batch = new SpriteBatch();

		p = Gdx.app.getPreferences(PREFERENCES_NAME);
		currentLevel = p.getInteger(PREF_CURRENT_LEVEL, 0);
		System.out.println(currentLevel);
		setScreen(new GameScreen(this, LEVELS[currentLevel]));
		p.putInteger(PREF_CURRENT_LEVEL, currentLevel);
		currentLevel += 1;
		p.flush();
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

		if (currentLevel < LEVELS.length) {
			setScreen(new GameScreen(this, LEVELS[currentLevel]));
			p.putInteger(PREF_CURRENT_LEVEL, currentLevel);
			currentLevel += 1;
			p.flush();

		}

	}

}
