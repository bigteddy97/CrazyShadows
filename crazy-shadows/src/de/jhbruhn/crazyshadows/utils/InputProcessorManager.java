package de.jhbruhn.crazyshadows.utils;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.InputProcessor;

public class InputProcessorManager implements InputProcessor {
	private List<InputProcessor> listeners = new ArrayList<InputProcessor>();

	public List<InputProcessor> getListeners() {
		return listeners;
	}

	@Override
	public boolean keyDown(int keycode) {
		for (InputProcessor i : listeners) {
			i.keyDown(keycode);
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		for (InputProcessor i : listeners) {
			i.keyUp(keycode);
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		for (InputProcessor i : listeners) {
			i.keyTyped(character);
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		for (InputProcessor i : listeners) {
			i.touchDown(screenX, screenY, pointer, button);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		for (InputProcessor i : listeners) {
			i.touchUp(screenX, screenY, pointer, button);
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		for (InputProcessor i : listeners) {
			i.touchDragged(screenX, screenY, pointer);
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		for (InputProcessor i : listeners) {
			i.mouseMoved(screenX, screenY);
		}
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		for (InputProcessor i : listeners) {
			i.scrolled(amount);
		}
		return false;
	}

}
