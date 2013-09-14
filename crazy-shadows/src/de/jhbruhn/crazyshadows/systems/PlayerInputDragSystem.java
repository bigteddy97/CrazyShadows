package de.jhbruhn.crazyshadows.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;

import de.jhbruhn.crazyshadows.components.Ball;
import de.jhbruhn.crazyshadows.components.PhysicsBody;
import de.jhbruhn.crazyshadows.components.Player;

public class PlayerInputDragSystem extends EntityProcessingSystem implements
		ContactListener, InputProcessor {

	@Mapper
	ComponentMapper<Player> pm;

	@Mapper
	ComponentMapper<PhysicsBody> pbm;

	private Body currentPlayerContact;
	private Ball currentPlayerContactBall;
	private World physicsWorld;
	private Joint currentJoint;
	private boolean isDragging = false;

	@SuppressWarnings("unchecked")
	public PlayerInputDragSystem(World physicsWorld) {
		super(Aspect.getAspectForAll(Player.class, PhysicsBody.class));
		this.physicsWorld = physicsWorld;
	}

	@Override
	protected void process(Entity e) {
		PhysicsBody playerPhysics = pbm.get(e);
		if (isDragging && currentPlayerContact != null && currentJoint == null
				&& !currentPlayerContactBall.isInTarget) {
			DistanceJointDef def = new DistanceJointDef();
			def.bodyA = playerPhysics.body;
			def.bodyB = currentPlayerContact;
			def.type = JointDef.JointType.DistanceJoint;
			def.collideConnected = true;
			def.length = 25 * 2;
			currentJoint = physicsWorld.createJoint(def);
		} else if (!isDragging && currentJoint != null) {
			if (currentJoint.getBodyA() != null
					&& currentJoint.getBodyB() != null) {
				physicsWorld.destroyJoint(currentJoint);
			}
			currentJoint = null;
		}
	}

	@Override
	public void beginContact(Contact contact) {
		if (contact.getFixtureA().getBody().getUserData() instanceof Player) {
			if (contact.getFixtureB().getBody().getUserData() instanceof Ball) {
				currentPlayerContact = contact.getFixtureB().getBody();
				currentPlayerContactBall = (Ball) contact.getFixtureB()
						.getBody().getUserData();
			}
		} else if (contact.getFixtureA().getBody().getUserData() instanceof Ball) {
			if (contact.getFixtureB().getBody().getUserData() instanceof Player)
				currentPlayerContact = contact.getFixtureA().getBody();
			currentPlayerContactBall = (Ball) contact.getFixtureA().getBody()
					.getUserData();
		}
	}

	@Override
	public void endContact(Contact contact) {
		if (contact.getFixtureA() == null || contact.getFixtureB() == null) {
			return;
		}
		if (contact.getFixtureA().getBody().getUserData() instanceof Player) {
			if (contact.getFixtureB().getBody().getUserData() instanceof Ball) {
				currentPlayerContact = null;
				currentPlayerContactBall = null;
			}
		} else if (contact.getFixtureA().getBody().getUserData() instanceof Ball) {
			if (contact.getFixtureB().getBody().getUserData() instanceof Player) {
				currentPlayerContact = null;
				currentPlayerContactBall = null;
			}
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean keyDown(int keyCode) {
		if (keyCode == Input.Keys.SPACE) {
			isDragging = true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keyCode) {
		if (keyCode == Input.Keys.SPACE) {
			isDragging = false;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		isDragging = true;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		isDragging = false;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
