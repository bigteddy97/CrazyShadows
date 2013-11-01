package de.jhbruhn.crazyshadows.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import de.jhbruhn.crazyshadows.components.Player;
import de.jhbruhn.crazyshadows.components.Position;
import de.jhbruhn.crazyshadows.components.Velocity;

public class PlayerInputSystem extends EntityProcessingSystem implements
		InputProcessor {

	private static final float VERTICAL_SPEED = 10000;
	private static final int VERTICAL_SPEED_MAX = 15000;

	@Mapper
	ComponentMapper<Position> pm;
	@Mapper
	ComponentMapper<Velocity> vm;

	private boolean up, down, left, right;

	private OrthographicCamera camera;
	private Vector3 mouseVector;

	@SuppressWarnings("unchecked")
	public PlayerInputSystem(OrthographicCamera camera) {
		super(Aspect.getAspectForAll(Position.class, Velocity.class,
				Player.class));
		this.camera = camera;
		this.mouseVector = new Vector3();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.A) {
			left = true;
		} else if (keycode == Input.Keys.D) {
			right = true;
		} else if (keycode == Input.Keys.W) {
			up = true;
		} else if (keycode == Input.Keys.S) {
			down = true;
		}

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.A) {
			left = false;
		} else if (keycode == Input.Keys.D) {
			right = false;
		} else if (keycode == Input.Keys.W) {
			up = false;
		} else if (keycode == Input.Keys.S) {
			down = false;
		}

		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		up = false;
		down = false;
		left = false;
		right = false;
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	protected void process(Entity e) {

		Position position = pm.get(e);
		Velocity velocity = vm.get(e);

		mouseVector.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(mouseVector);

		velocity.vectorX = 0;
		velocity.vectorY = 0;

		if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)) {
			velocity.vectorX = Gdx.input.getAccelerometerY() * world.getDelta()
					* VERTICAL_SPEED;
			velocity.vectorY = -Gdx.input.getAccelerometerX()
					* world.getDelta() * VERTICAL_SPEED;
		}

		if (up) {
			velocity.vectorY = MathUtils.clamp(
					velocity.vectorY + (world.getDelta() * VERTICAL_SPEED),
					-VERTICAL_SPEED_MAX, VERTICAL_SPEED_MAX);
		}
		if (down) {
			velocity.vectorY = MathUtils.clamp(
					velocity.vectorY - (world.getDelta() * VERTICAL_SPEED),
					-VERTICAL_SPEED_MAX, VERTICAL_SPEED_MAX);
		}

		if (left) {
			velocity.vectorX = MathUtils.clamp(
					velocity.vectorX - (world.getDelta() * VERTICAL_SPEED),
					-VERTICAL_SPEED_MAX, VERTICAL_SPEED_MAX);
		}

		if (right) {
			velocity.vectorX = MathUtils.clamp(
					velocity.vectorX + (world.getDelta() * VERTICAL_SPEED),
					-VERTICAL_SPEED_MAX, VERTICAL_SPEED_MAX);
		}

		camera.position.x = position.x;
		camera.position.y = position.y;
	}

}
