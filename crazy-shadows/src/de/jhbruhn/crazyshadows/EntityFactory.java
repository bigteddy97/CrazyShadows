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
import de.jhbruhn.crazyshadows.components.Circle;
import de.jhbruhn.crazyshadows.components.Light;
import de.jhbruhn.crazyshadows.components.PhysicsBody;
import de.jhbruhn.crazyshadows.components.Player;
import de.jhbruhn.crazyshadows.components.Position;
import de.jhbruhn.crazyshadows.components.Rectangle;
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

		// Sprite sprite = new Sprite();
		// sprite.name = "player";
		// sprite.r = 93 / 255f;
		// sprite.g = 255 / 255f;
		// sprite.b = 129 / 255f;
		// sprite.layer = Sprite.Layer.ACTORS_3;
		// e.addComponent(sprite);

		Circle c1 = new Circle();
		c1.color = Color.BLUE.cpy();
		c1.filled = true;
		c1.radius = 25f;
		c1.segments = 360 * 4;
		c1.borderSize = 2.5f;
		c1.borderColor = Color.RED.cpy();
		e.addComponent(c1);

		Velocity velocity = new Velocity();
		velocity.vectorX = 0;
		velocity.vectorY = 0;
		e.addComponent(velocity);

		Light light = new Light();
		light.rays = 512 * 2;
		light.color = Color.BLUE.cpy();
		light.distance = 256f * 2;
		light.type = Light.LightType.POINT;
		e.addComponent(light);

		Player player = new Player();
		e.addComponent(player);

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
		b.setUserData(player);
		b.createFixture(fDef);
		physicsBody.body = b;
		e.addComponent(physicsBody);

		/*
		 * Bounds bounds = new Bounds(); bounds.radius = 43;
		 * e.addComponent(bounds);
		 */

		world.getManager(GroupManager.class).add(e, Constants.Groups.PLAYER);

		return e;
	}

	public static Entity createBallEntitiy(World world,
			com.badlogic.gdx.physics.box2d.World physicsWorld, float x,
			float y, int id, Color color, float radius) {
		Entity e = world.createEntity();

		Position position = new Position();
		position.x = x;
		position.y = y;
		e.addComponent(position);

		Ball ball = new Ball();
		ball.id = id;
		e.addComponent(ball);

		Circle c = new Circle();
		c.color = color;
		c.filled = true;
		c.radius = radius;
		c.segments = 360 * 4;
		e.addComponent(c);

		PhysicsBody physicsBody = new PhysicsBody();

		CircleShape shape = new CircleShape();
		shape.setRadius(radius);
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
		light.color = color.cpy();
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
		r.color = color.cpy();
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
		light.color = color.cpy();
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
		fDef.restitution = 0f;
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
		rect.color = Color.BLACK.cpy();
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
