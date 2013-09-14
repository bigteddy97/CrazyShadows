package de.jhbruhn.crazyshadows.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;

import de.jhbruhn.crazyshadows.GameScreen;
import de.jhbruhn.crazyshadows.components.Ball;
import de.jhbruhn.crazyshadows.components.Target;

public class LevelOverSystem extends EntitySystem {
	private GameScreen gameScreen;

	@SuppressWarnings("unchecked")
	public LevelOverSystem(GameScreen gameScreen) {
		super(Aspect.getAspectForOne(Ball.class, Target.class));
		this.gameScreen = gameScreen;
	}

	@Override
	protected boolean checkProcessing() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		if (entities.size() <= 0)
			gameScreen.levelOver = true;
	}

}
