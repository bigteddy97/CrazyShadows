package de.jhbruhn.crazyshadows.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import de.jhbruhn.crazyshadows.components.Circle;
import de.jhbruhn.crazyshadows.components.Position;
import de.jhbruhn.crazyshadows.components.Rectangle;

public class ShapeRenderSystem extends EntitySystem {
	@Mapper
	ComponentMapper<Position> pm;
	@Mapper
	ComponentMapper<Circle> cm;
	@Mapper
	ComponentMapper<Rectangle> rm;

	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;

	@SuppressWarnings("unchecked")
	public ShapeRenderSystem(OrthographicCamera camera) {
		super(Aspect.getAspectForAll(Position.class).one(Circle.class,
				Rectangle.class));
		this.camera = camera;
	}

	@Override
	protected void initialize() {
		shapeRenderer = new ShapeRenderer();

	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	@Override
	protected void begin() {
		shapeRenderer.setProjectionMatrix(camera.combined);
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; entities.size() > i; i++) {
			process(entities.get(i));
		}
	}

	protected void process(Entity e) {
		if (pm.has(e)) {
			Position position = pm.getSafe(e);

			if (cm.has(e)) {
				Circle circle = cm.getSafe(e);

				shapeRenderer.setColor(circle.color);

				if (circle.filled) {
					shapeRenderer.begin(ShapeType.FilledCircle);
					shapeRenderer.filledCircle(position.x, position.y,
							circle.radius);
				} else {
					shapeRenderer.begin(ShapeType.Circle);
					shapeRenderer.circle(position.x, position.y, circle.radius);
				}

				shapeRenderer.end();
			}
			if (rm.has(e)) {

				Rectangle rectangle = rm.getSafe(e);

				shapeRenderer.setColor(rectangle.color);

				if (rectangle.filled) {
					shapeRenderer.begin(ShapeType.FilledRectangle);
					shapeRenderer.filledRect(position.x, position.y,
							rectangle.width, rectangle.height);
				} else {
					shapeRenderer.begin(ShapeType.Rectangle);
					shapeRenderer.rect(position.x, position.y, rectangle.width,
							rectangle.height);
				}

				shapeRenderer.end();
			}
		}
	}

}
