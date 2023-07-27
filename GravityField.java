import java.awt.Color;
import java.awt.Graphics;

public class GravityField extends Wall {

	private double direction;
	private double intensity;

	public GravityField(int[] apointsx, int[] apointsy, Color acolor, int adirection, double aintensity) {
		super(apointsx, apointsy, acolor);
		direction = adirection;
		intensity = aintensity;
	}

	public double getDirection() {
		return direction;
	}

	public double getIntensity() {
		return intensity;
	}

	public void setDirection(double adirection) {
		direction = adirection;
	}

	public void setIntensity(double aintensity) {
		intensity = aintensity;
	}

	public void draw(Graphics g, int viewx, int viewy, boolean faded) {
		super.draw(g, viewx, viewy, faded);
	}

}
