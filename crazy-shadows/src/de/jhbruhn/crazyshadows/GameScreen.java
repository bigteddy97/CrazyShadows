package de.jhbruhn.crazyshadows;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

import de.jhbruhn.crazyshadows.systems.BallTargetCollisionSystem;
import de.jhbruhn.crazyshadows.systems.LightSystem;
import de.jhbruhn.crazyshadows.systems.MovementSystem;
import de.jhbruhn.crazyshadows.systems.PlayerInputSystem;
import de.jhbruhn.crazyshadows.systems.ShapeRenderSystem;
import de.jhbruhn.crazyshadows.systems.SpriteRenderSystem;

public class GameScreen implements Screen {
	@SuppressWarnings("unused")
	private Game game;
	private World world;
	private OrthographicCamera camera;

	private SpriteRenderSystem spriteRenderSystem;
	private LightSystem lightSystem;
	private ShapeRenderSystem shapeRenderSystem;

	private com.badlogic.gdx.physics.box2d.World physicsWorld;

	static final float BOX_STEP = 1 / 120f;
	static final int BOX_VELOCITY_ITERATIONS = 8;
	static final int BOX_POSITION_ITERATIONS = 3;
	float accumulator;

	/*
	 * private HealthRenderSystem healthRenderSystem; private HudRenderSystem
	 * hudRenderSystem;
	 */

	public GameScreen(Game game) {
		this.game = game;
		this.camera = new OrthographicCamera(CrazyShadows.FRAME_WIDTH,
				CrazyShadows.FRAME_HEIGHT);
		physicsWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0,
				0), false);

		world = new World();

		world.setManager(new GroupManager());
		BallTargetCollisionSystem bTCS = new BallTargetCollisionSystem();
		world.setSystem(bTCS);
		world.setSystem(new PlayerInputSystem(camera));
		world.setSystem(new MovementSystem());
		spriteRenderSystem = world.setSystem(new SpriteRenderSystem(camera),
				true);
		lightSystem = world.setSystem(new LightSystem(camera, physicsWorld),
				true);
		shapeRenderSystem = world
				.setSystem(new ShapeRenderSystem(camera), true);

		physicsWorld.setContactListener(bTCS);

		world.initialize();

		EntityFactory.createPlayerEntity(world, physicsWorld, 0, 0)
				.addToWorld();

		for (int i = 0; i < 200; i++) {
			EntityFactory.createBallEntitiy(world, physicsWorld,
					(float) (Math.random() - 0.5) * 2000,
					(float) (Math.random() - 0.5) * 2000, i).addToWorld();
		}

		EntityFactory.createTargetEntity(world, physicsWorld, 50, 100, 50, 50,
				1).addToWorld();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT
				| (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV
						: 0));

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
