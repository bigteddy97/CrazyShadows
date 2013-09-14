package de.jhbruhn.crazyshadows;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObjectGroup;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import de.jhbruhn.crazyshadows.components.Timer;
import de.jhbruhn.crazyshadows.systems.BallTargetCollisionSystem;
import de.jhbruhn.crazyshadows.systems.BallTargetHideSystem;
import de.jhbruhn.crazyshadows.systems.LevelOverSystem;
import de.jhbruhn.crazyshadows.systems.LightSystem;
import de.jhbruhn.crazyshadows.systems.MovementSystem;
import de.jhbruhn.crazyshadows.systems.PlayerInputDragSystem;
import de.jhbruhn.crazyshadows.systems.PlayerInputSystem;
import de.jhbruhn.crazyshadows.systems.ShapeRenderSystem;
import de.jhbruhn.crazyshadows.systems.SpriteRenderSystem;
import de.jhbruhn.crazyshadows.utils.ContactListenerManager;
import de.jhbruhn.crazyshadows.utils.InputProcessorManager;

public class GameScreen implements Screen {
	static final float BOX_STEP = 1 / 120f;
	static final int BOX_VELOCITY_ITERATIONS = 8;
	static final int BOX_POSITION_ITERATIONS = 3;
	static final float MAX_LIGHT = .4f;

	private CrazyShadows game;
	private World world;
	private OrthographicCamera camera;

	private SpriteRenderSystem spriteRenderSystem;
	private LightSystem lightSystem;
	private ShapeRenderSystem shapeRenderSystem;

	private com.badlogic.gdx.physics.box2d.World physicsWorld;
	private ContactListenerManager contactListenerManager = new ContactListenerManager();
	private InputProcessorManager inputProcessorManager = new InputProcessorManager();
	private float accumulator;

	private Timer fadeInTimer = new Timer(2);
	private Timer fadeOutTimer = new Timer(2);

	private Color ambientLightColor = new Color(0, 0, 0, 0);

	private ShapeRenderer shapeRenderer = new ShapeRenderer();

	public boolean levelOver = false;

	private float curStand;

	/*
	 * private HealthRenderSystem healthRenderSystem; private HudRenderSystem
	 * hudRenderSystem;
	 */

	public GameScreen(CrazyShadows game, String levelname) {
		this.game = game;
		this.camera = new OrthographicCamera(CrazyShadows.FRAME_WIDTH,
				CrazyShadows.FRAME_HEIGHT);
		physicsWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0,
				0), false);

		world = new World();

		world.setManager(new GroupManager());
		BallTargetCollisionSystem bTCS = new BallTargetCollisionSystem();
		PlayerInputDragSystem pIDS = new PlayerInputDragSystem(physicsWorld);
		world.setSystem(pIDS);
		world.setSystem(bTCS);
		PlayerInputSystem pIS = world.setSystem(new PlayerInputSystem(camera));
		world.setSystem(new MovementSystem());
		world.setSystem(new BallTargetHideSystem());
		world.setSystem(new LevelOverSystem(this));
		spriteRenderSystem = world.setSystem(new SpriteRenderSystem(camera),
				true);
		lightSystem = world.setSystem(new LightSystem(camera, physicsWorld,
				game), true);
		shapeRenderSystem = world
				.setSystem(new ShapeRenderSystem(camera), true);

		contactListenerManager.getListeners().add(bTCS);
		contactListenerManager.getListeners().add(pIDS);

		physicsWorld.setContactListener(contactListenerManager);

		inputProcessorManager.getListeners().add(pIDS);
		inputProcessorManager.getListeners().add(pIS);
		Gdx.input.setInputProcessor(inputProcessorManager);

		world.initialize();

		loadMap(levelname);

	}

	private void loadMap(String mapName) {
		TiledMap map = TiledLoader.createMap(Gdx.files.internal("maps/"
				+ mapName + ".tmx"));
		for (TiledObjectGroup g : map.objectGroups) {

			// Important: we need to inverse the y-coordinates here because
			// tmx's system is different from libgdxs
			if (g.name.equals("walls")) {
				for (TiledObject o : g.objects) {
					EntityFactory.createWallEntity(world, physicsWorld, o.x,
							-o.y - o.height, o.width, o.height).addToWorld();
				}
			} else if (g.name.equals("player")) {
				for (TiledObject o : g.objects) {
					EntityFactory.createPlayerEntity(world, physicsWorld, o.x,
							-o.y).addToWorld();
				}
			} else if (g.name.equals("balls")) {
				for (TiledObject o : g.objects) {
					int id = Integer.valueOf(o.name);
					Color color = getColorForId(id);
					EntityFactory.createBallEntitiy(world, physicsWorld, o.x,
							-o.y, id, color).addToWorld();
				}
			} else if (g.name.equals("targets")) {
				for (TiledObject o : g.objects) {
					int id = Integer.valueOf(o.name);
					Color color = getColorForId(id);
					EntityFactory.createTargetEntity(world, physicsWorld, o.x,
							-o.y - o.height, o.width, o.height, id, color)
							.addToWorld();
				}
			}
		}
	}

	private Color getColorForId(int id) {
		switch (id) {
		case 1:
			return new Color(0, 1, 0, 1);
		case 2:
			return new Color(1, 0, 0, 1);
		case 3:
			return new Color(0, 0, 1, 1);

		}
		return new Color(0, 1, 0, 1);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT
				| (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV
						: 0));

		if (!fadeInTimer.isDone()) {
			float intVal = fadeInTimer.getPercentage();
			intVal = Interpolation.exp10In.apply(intVal);

			ambientLightColor.set(GameScreen.MAX_LIGHT * intVal,
					GameScreen.MAX_LIGHT * intVal, GameScreen.MAX_LIGHT
							* intVal, 1);
			game.ray.setAmbientLight(ambientLightColor);
			fadeInTimer.update(delta);
		}

		if (!fadeOutTimer.isDone() && levelOver) {
			float intVal = fadeOutTimer.getPercentage();
			if (curStand == -1f) {
				curStand = ambientLightColor.r;
			}
			intVal = 1 - Interpolation.exp10In.apply(intVal);
			ambientLightColor.set(curStand * intVal, curStand * intVal,
					curStand * intVal, 1);
			game.ray.setAmbientLight(ambientLightColor);
			fadeOutTimer.update(delta);
		}

		camera.update();

		world.setDelta(delta);

		world.process();

		accumulator += delta;
		while (accumulator > BOX_STEP) {
			physicsWorld.step(BOX_STEP, BOX_VELOCITY_ITERATIONS,
					BOX_POSITION_ITERATIONS);
			accumulator -= BOX_STEP;
		}

		shapeRenderSystem.process();
		spriteRenderSystem.process();
		lightSystem.process();

		if (fadeInTimer.getPercentage() < 0.33f) {
			Gdx.gl.glEnable(GL10.GL_BLEND);
			Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			shapeRenderer.begin(ShapeType.FilledRectangle);
			shapeRenderer
					.setColor(0, 0, 0, 1 - fadeInTimer.getPercentage() * 3);
			shapeRenderer.filledRect(-1000, -1000, 5000, 5000);
			shapeRenderer.end();
			Gdx.gl.glDisable(GL10.GL_BLEND);
		}

		if (fadeOutTimer.getPercentage() > 0) {
			Gdx.gl.glEnable(GL10.GL_BLEND);
			Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			shapeRenderer.begin(ShapeType.FilledRectangle);
			shapeRenderer.setColor(0, 0, 0, fadeOutTimer.getPercentage());
			shapeRenderer.filledRect(-1000, -1000, 5000, 5000);
			shapeRenderer.end();
			Gdx.gl.glDisable(GL10.GL_BLEND);
		}

		if (fadeOutTimer.getPercentage() == 1 && levelOver) {
			game.requestNextLevel();
		}
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportHeight = height;
		camera.viewportWidth = width;
	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

}
