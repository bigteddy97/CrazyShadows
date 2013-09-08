package de.jhbruhn.crazyshadows.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;

import de.jhbruhn.crazyshadows.components.PhysicsBody;
import de.jhbruhn.crazyshadows.components.Position;
import de.jhbruhn.crazyshadows.components.Velocity;

public class MovementSystem extends EntityProcessingSystem {
	@Mapper
	ComponentMapper<Position> pm;
	@Mapper
	ComponentMapper<Velocity> vm;
	@Mapper
	ComponentMapper<PhysicsBody> pbm;

	@SuppressWarnings("unchecked")
	public MovementSystem() {
		super(Aspect.getAspectForAll(Position.class).one(Velocity.class,
				PhysicsBody.class));
	}

	@Override
	protected void process(Entity e) {
		if (!pbm.has(e)) {
			if (vm.has(e) && pm.has(e)) {
				Position position = pm.get(e);
				Velocity velocity = vm.get(e);

				position.x += velocity.vectorX * world.delta;
				position.y += velocity.vectorY * world.delta;
			}
		} else if (pbm.has(e) && vm.has(e) && pm.has(e)) {
			Velocity velocity = vm.get(e);
			Position position = pm.get(e);
			PhysicsBody physicsBody = pbm.get(e);

			physicsBody.body.setLinearVelocity(velocity.vectorX,
					velocity.vectorY);
			Vector2 pos = physicsBody.body.getPosition();
			position.x = pos.x;
			position.y = pos.y;
		}
	}

}
