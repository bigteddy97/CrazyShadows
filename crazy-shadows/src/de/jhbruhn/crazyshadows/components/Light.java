package de.jhbruhn.crazyshadows.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

public class Light extends Component {
	public enum LightType {
		POINT, DIRECTIONAL, CONE;
	}

	public int rays;
	public Color color;
	public float distance;
	public float directionAngle;
	public float coneAngle;
	public LightType type;
}
