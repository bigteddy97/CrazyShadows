package de.jhbruhn.crazyshadows.systems;

import java.util.ArrayList;
import java.util.List;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.jhbruhn.crazyshadows.PhysicsBody;
import de.jhbruhn.crazyshadows.components.Ball;
import de.jhbruhn.crazyshadows.components.Target;

public class BallTargetCollisionSystem extends EntityProcessingSystem implements
		ContactListener {

	@Mapper
	ComponentMapper<PhysicsBody> pbm;
	@Mapper
	ComponentMapper<Ball> bm;
	@Mapper
	ComponentMapper<Target> tm;

	private List<Integer> currentCollisions = new ArrayList<Integer>();

	@SuppressWarnings("unchecked")
	public BallTargetCollisionSystem() {
		super(Aspect.getAspectForAll(PhysicsBody.class).one(Ball.class,
				Target.class));
	}

	@Override
	public void beginContact(Contact contact) {
		Object udA = contact.getFixtureA().getBody().getUserData();
		Object udB = contact.getFixtureB().getBody().getUserData();

		Ball ball = null;
		Target target = null;

		if ((udA instanceof Ball && udB instanceof Target)) {
			ball = (Ball) udA;
			target = (Target) udB;
		} else if ((udA instanceof Target && udB instanceof Ball)) {
			ball = (Ball) udB;
			target = (Target) udA;
		} else
			return;
		System.out.println(ball.id + " " + target.id);
		if (ball.id == target.id) {
			currentCollisions.add(ball.id);
		}

	}

	@Override
	public void endContact(Contact contact) {
		Object udA = contact.getFixtureA().getBody().getUserData();
		Object udB = contact.getFixtureB().getBody().getUserData();

		Ball ball = null;
		Target target = null;

		if ((udA instanceof Ball && udB instanceof Target)) {
			ball = (Ball) udA;
			target = (Target) udB;
		} else if ((udA instanceof Target && udB instanceof Ball)) {
			ball = (Ball) udB;
			target = (Target) udA;
		} else
			return;

		if (ball.id == target.id) {
			currentCollisions.remove(Integer.valueOf(ball.id));
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		Object udA = contact.getFixtureA().getBody().getUserData();
		Object udB = contact.getFixtureB().getBody().getUserData();
		if ((udA instanceof Ball && udB instanceof Target)) {
			if (((Ball) udA).id == ((Target) udB).id)
				contact.setEnabled(false);
		} else if ((udA instanceof Target && udB instanceof Ball)) {
			if (((Ball) udB).id == ((Target) udA).id)
				contact.setEnabled(false);
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void process(Entity arg0) {
		// TODO Auto-generated method stub

	}

}
