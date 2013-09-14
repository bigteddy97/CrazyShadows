package de.jhbruhn.crazyshadows.systems;

import java.util.HashMap;
import java.util.Map;

import box2dLight.ConeLight;
import box2dLight.DirectionalLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;

import de.jhbruhn.crazyshadows.CrazyShadows;
import de.jhbruhn.crazyshadows.components.Light;
import de.jhbruhn.crazyshadows.components.PhysicsBody;
import de.jhbruhn.crazyshadows.components.Position;
import de.jhbruhn.crazyshadows.components.Rectangle;

public class LightSystem extends EntitySystem {

	@Mapper
	ComponentMapper<Position> pm;
	@Mapper
	ComponentMapper<Light> lm;

	private OrthographicCamera camera;
	private Map<Light, box2dLight.Light> lights = new HashMap<Light, box2dLight.Light>();
	private World world;
	private CrazyShadows game;

	@SuppressWarnings("unchecked")
	public LightSystem(OrthographicCamera camera, World world, CrazyShadows game) {
		super(Aspect.getAspectForAll(Position.class, Light.class));
		this.camera = camera;
		this.world = world;
		this.game = game;
	}

	@Override
	protected void initialize() {
		RayHandler.useDiffuseLight(true);
		RayHandler.setGammaCorrection(true);
		game.ray = new RayHandler(world);
		game.ray.setCombinedMatrix(camera.combined);
		game.ray.setBlurNum(3);
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; entities.size() > i; i++) {
			process(entities.get(i));
		}
		game.ray.setCombinedMatrix(camera.combined);
		game.ray.updateAndRender();
	}

	protected void process(Entity e) {
		if (pm.has(e)) {
			Position position = pm.getSafe(e);
			float x = position.x;
			float y = position.y;
			// We need to center the light for rectangles because of
			// reasons
			Rectangle r = (e.getComponent(Rectangle.class));
			if (r != null) {
				x += r.width / 2;
				y += r.height / 2;
			}
			Light light = lm.getSafe(e);

			if (!lights.containsKey(light)) {
				box2dLight.Light posLight = null;
				if (light.type == Light.LightType.POINT) {

					posLight = new PointLight(game.ray, light.rays,
							light.color, light.distance, x, y);
				} else if (light.type == Light.LightType.DIRECTIONAL) {
					posLight = new DirectionalLight(game.ray, light.rays,
							light.color, light.directionAngle);
				} else if (light.type == Light.LightType.CONE) {
					posLight = new ConeLight(game.ray, light.rays, light.color,
							light.distance, x, y, light.directionAngle,
							light.coneAngle);
				}
				lights.put(light, posLight);
			} else {
				box2dLight.Light posLight = lights.get(light);
				posLight.setPosition(x, y);
				posLight.setColor(light.color);
				posLight.setDirection(light.directionAngle);
				posLight.setDistance(light.distance);
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		game.ray.dispose();
	}

	@Override
	protected void removed(Entity e) {
		super.removed(e);
		world.destroyBody(e.getComponent(PhysicsBody.class).body);
		lights.get(e.getComponent(Light.class)).remove();
	}

}
