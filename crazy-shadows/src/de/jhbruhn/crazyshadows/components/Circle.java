package de.jhbruhn.crazyshadows.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

public class Circle extends Component {
	public float radius;
	public Color color;
	public boolean filled;
	public int segments = 360;
	public float borderSize;
	public Color borderColor;
}
