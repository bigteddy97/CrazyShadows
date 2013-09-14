package de.jhbruhn.crazyshadows.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.jhbruhn.crazyshadows.components.Position;
import de.jhbruhn.crazyshadows.components.Text;

public class TextRenderSystem extends EntitySystem {
	private static final String FONT_NAME = "main";
	@Mapper
	ComponentMapper<Position> pm;
	@Mapper
	ComponentMapper<Text> tm;

	private BitmapFont font;
	private OrthographicCamera camera;
	private SpriteBatch batch;

	@SuppressWarnings("unchecked")
	public TextRenderSystem(OrthographicCamera camera) {
		super(Aspect.getAspectForAll(Position.class, Text.class));
		this.camera = camera;
		batch = new SpriteBatch();

		Texture texture = new Texture(Gdx.files.internal("fonts/" + FONT_NAME
				+ ".png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear); // linear
																		// filtering
																		// in
																		// nearest
																		// mipmap
																		// image
		font = new BitmapFont(
				Gdx.files.internal("fonts/" + FONT_NAME + ".fnt"),
				new TextureRegion(texture), false);
		// font = new BitmapFont()

	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		batch.begin();
		Gdx.gl10.glEnable(GL10.GL_ALPHA_TEST);
		Gdx.gl10.glAlphaFunc(GL10.GL_GREATER, 0.5f);
		batch.setProjectionMatrix(camera.combined);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			Position p = pm.get(e);
			Text t = tm.get(e);

			font.setColor(t.color);
			font.drawWrapped(batch, t.text, p.x, p.y, t.width);
		}
		Gdx.gl10.glDisable(GL10.GL_ALPHA_TEST);
		batch.end();
	}

}
