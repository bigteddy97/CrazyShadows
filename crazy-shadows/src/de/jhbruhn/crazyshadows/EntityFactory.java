package de.jhbruhn.crazyshadows;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import de.jhbruhn.crazyshadows.components.Ball;
import de.jhbruhn.crazyshadows.components.Light;
import de.jhbruhn.crazyshadows.components.Player;
import de.jhbruhn.crazyshadows.components.Position;
import de.jhbruhn.crazyshadows.components.Rectangle;
import de.jhbruhn.crazyshadows.components.Sprite;
import de.jhbruhn.crazyshadows.components.Target;
import de.jhbruhn.crazyshadows.components.Velocity;

public class EntityFactory {

	public static Entity createPlayerEntity(World world,
			com.badlogic.gdx.physics.box2d.World physicsWorld, float x, float y) {
		Entity e = world.createEntity();

		Position position = new Position();
		position.x = x;
		position.y = y;
		e.addComponent(position);

		Sprite sprite = new Sprite();
		sprite.name = "player";
		sprite.r = 93 / 255f;
		sprite.g = 255 / 255f;
		sprite.b = 129 / 255f;
		sprite.layer = Sprite.Layer.ACTORS_3;
		e.addComponent(sprite);

		Velocity velocity = new Velocity();
		velocity.vectorX = 0;
		velocity.vectorY = 0;
		e.addComponent(velocity);

		Light light = new Light();
		light.rays = 512 * 2;
		light.color = new Color(1, 1, 1, .75f);
		light.distance = 256f * 2;
		light.type = Light.LightType.POINT;
		e.addComponent(light);

		PhysicsBody physicsBody = new PhysicsBody();
		CircleShape shape = new CircleShape();
		shape.setRadius(25f);
		FixtureDef fDef = new FixtureDef();
		fDef.restitution = 0f;
		fDef.friction = 0.01f;
		fDef.shape = shape;
		fDef.density = 1f;
		BodyDef bDef = new BodyDef();
		bDef.type = BodyType.DynamicBody;
		bDef.position.x = x;
		bDef.position.y = y;
		Body b = physicsWorld.createBody(bDef);
		b.createFixture(fDef);
		physicsBody.body = b;
		e.addComponent(physicsBody);

		/*
		 * Bounds bounds = new Bounds(); bounds.radius = 43;
		 * e.addComponent(bounds);
		 */

		e.addComponent(new Player());

		world.getManager(GroupManager.class).add(e, Constants.Groups.PLAYER);

		return e;
	}

	public static Entity createBallEntitiy(World world,
			com.badlogic.gdx.physics.box2d.World physicsWorld, float x,
			float y, int id, Color color) {
		Entity e = world.createEntity();

		Position position = new Position();
		position.x = x;
		position.y = y;
		e.addComponent(position);

		Ball ball = new Ball();
		ball.id = id;
		e.addComponent(ball);

		Sprite sprite = new Sprite();
		sprite.name = "player";
		sprite.r = 0 / 255f;
		sprite.g = 255 / 255f;
		sprite.b = 0 / 255f;
		sprite.layer = Sprite.Layer.ACTORS_1;
		e.addComponent(sprite);

		PhysicsBody physicsBody = new PhysicsBody();

		CircleShape shape = new CircleShape();
		shape.setRadius(26f);
		FixtureDef fDef = new FixtureDef();
		fDef.restitution = 0f;
		fDef.friction = 0.01f;
		fDef.shape = shape;
		fDef.density = 1f;
		BodyDef bDef = new BodyDef();
		bDef.type = BodyType.DynamicBody;
		bDef.position.x = x;
		bDef.position.y = y;
		Body b = physicsWorld.createBody(bDef);
		b.setUserData(ball);
		b.createFixture(fDef);
		physicsBody.body = b;
		e.addComponent(physicsBody);

		Light light = new Light();
		light.rays = 128;
		light.color = color;
		light.distance = 64;
		light.type = Light.LightType.POINT;
		e.addComponent(light);

		e.addComponent(new Velocity());

		world.getManager(GroupManager.class).add(e, Constants.Groups.PLAYER);
		return e;
	}

	public static Entity createTargetEntity(World world,
			com.badlogic.gdx.physics.box2d.World physicsWorld, float x,
			float y, float width, float height, int id, Color color) {
		Entity e = world.createEntity();

		Target target = new Target();
		target.id = id;
		e.addComponent(target);

		Rectangle r = new Rectangle();
		r.width = width;
		r.height = height;
		r.color = Color.GREEN;
		r.filled = true;
		e.addComponent(r);

		Position pos = new Position();
		pos.x = x;
		pos.y = y;
		e.addComponent(pos);

		PhysicsBody pBody = new PhysicsBody();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2, height / 2);
		FixtureDef fDef = new FixtureDef();
		fDef.restitution = 0f;
		fDef.friction = 1.01f;
		fDef.shape = shape;
		fDef.density = 1f;
		// fDef.isSensor = true;
		BodyDef bDef = new BodyDef();
		bDef.type = BodyType.KinematicBody;
		bDef.position.x = x + width / 2;
		bDef.position.y = y + height / 2;
		Body b = physicsWorld.createBody(bDef);
		b.setUserData(target);
		b.createFixture(fDef);
		pBody.body = b;
		e.addComponent(pBody);

		Light light = new Light();
		light.rays = 256;
		light.color = color;
		light.distance = 128f;
		light.type = Light.LightType.POINT;
		e.addComponent(light);

		return e;
	}

	public static Entity createWallEntity(World world,
			com.badlogic.gdx.physics.box2d.World physicsWorld, float x,
			float y, float width, float height) {
		Entity e = world.createEntity();

		PhysicsBody physicsBody = new PhysicsBody();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2, height / 2);
		FixtureDef fDef = new FixtureDef();
		fDef.restitution = 0.9f;
		fDef.friction = 1.01f;
		fDef.shape = shape;
		fDef.density = 1f;
		BodyDef bDef = new BodyDef();
		bDef.type = BodyType.KinematicBody;
		bDef.position.x = x + width / 2;
		bDef.position.y = y + height / 2;
		Body b = physicsWorld.createBody(bDef);
		b.createFixture(fDef);
		physicsBody.body = b;
		e.addComponent(physicsBody);

		Rectangle rect = new Rectangle();
		rect.filled = true;
		rect.color = Color.RED;
		rect.width = width;
		rect.height = height;
		e.addComponent(rect);

		Position pos = new Position();
		pos.x = x;
		pos.y = y;
		e.addComponent(pos);

		return e;
	}
}
