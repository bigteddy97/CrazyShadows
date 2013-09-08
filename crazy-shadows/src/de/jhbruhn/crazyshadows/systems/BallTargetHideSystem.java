package de.jhbruhn.crazyshadows.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;

import de.jhbruhn.crazyshadows.components.Ball;
import de.jhbruhn.crazyshadows.components.Circle;
import de.jhbruhn.crazyshadows.components.Light;
import de.jhbruhn.crazyshadows.components.PhysicsBody;
import de.jhbruhn.crazyshadows.components.Rectangle;
import de.jhbruhn.crazyshadows.components.Sprite;
import de.jhbruhn.crazyshadows.components.Target;

public class BallTargetHideSystem extends EntityProcessingSystem {

	@Mapper
	ComponentMapper<Light> lm;
	@Mapper
	ComponentMapper<Ball> bm;
	@Mapper
	ComponentMapper<Target> tm;
	@Mapper
	ComponentMapper<Sprite> sm;
	@Mapper
	ComponentMapper<Rectangle> rm;
	@Mapper
	ComponentMapper<Circle> cm;
	@Mapper
	ComponentMapper<PhysicsBody> pbm;

	@SuppressWarnings("unchecked")
	public BallTargetHideSystem() {
		super(Aspect.getAspectForAll(Light.class, PhysicsBody.class)
				.one(Ball.class, Target.class)
				.one(Sprite.class, Rectangle.class, Circle.class));
	}

	@Override
	protected void process(Entity e) {
		boolean hide = false;
		if (bm.has(e)) {
			Ball b = bm.get(e);
			hide = b.isInTarget;
		} else if (tm.has(e)) {
			Target t = tm.get(e);
			hide = t.hasBall;
		}
		if (!hide)
			return;
		boolean remove = false;

		Light l = lm.get(e);

		l.color.a -= world.delta;

		if (sm.has(e)) {
			Sprite s = sm.get(e);
			s.a -= this.world.delta;
			if (s.a <= 0)
				remove = true;
		} else if (rm.has(e)) {
			Rectangle r = rm.get(e);
			r.color.a -= this.world.delta;
			if (r.color.a <= 0)
				remove = true;
		} else if (cm.has(e)) {
			Circle c = cm.get(e);
			c.color.a -= this.world.delta;
			if (c.color.a <= 0)
				remove = true;
		}
		if (remove) {
			e.deleteFromWorld();
		}
	}

}
