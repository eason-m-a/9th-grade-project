import java.awt.Color;
import java.awt.Graphics;

public class Wall {

	private int[] pointsx;
	private int[] pointsy;
	private Color color;

	public Wall(int[] apointsx, int[] apointsy, Color acolor) {
		pointsx = apointsx;
		pointsy = apointsy;
		color = acolor;
	}

	public int[] getPointsx() {
		return pointsx;
	}

	public int[] getPointsy() {
		return pointsy;
	}

	public Color getColor() {
		return color;
	}

	public void setPointsx(int apoint, int i) {
		pointsx[i] = apoint;
	}

	public void setPointsy(int apoint, int i) {
		pointsy[i] = apoint;
	}

	public void setColor(Color acolor) {
		color = acolor;
	}

	public void draw(Graphics g, int viewx, int viewy, boolean faded) {
		if (faded) {
			g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
		} else {
			g.setColor(color);
		}

		int[] pointsxd = pointsx.clone();
		int[] pointsyd = pointsy.clone();
		for (int i = 0; i < pointsx.length; i++) {
			pointsxd[i] -= viewx;
			pointsyd[i] -= viewy;
		}
		g.fillPolygon(pointsxd, pointsyd, pointsx.length);
	}

}
