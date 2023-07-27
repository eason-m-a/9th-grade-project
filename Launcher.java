import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Launcher extends MovingWall {

	private double intensity;
	private static int size = 50;

	public Launcher(Color acolor, double ax, double ay, double adirection, double aintensity) {
		super(new int[] { (int) (size * 1.5), (int) (size * 1.5), -size / 2, -size, -size / 2 },
				new int[] { -size, size, (int) (size * 0.75), 0, -(int) (size * 0.75) }, acolor, ax, ay);
		setDirection(adirection);
		intensity = aintensity;
	}

	public Launcher(int[] apointsx, int[] apointsy, Color acolor, double ax, double ay, double adirection,
			double aintensity) {
		super(apointsx, apointsy, acolor, ax, ay);
		setDirection(adirection);
		intensity = aintensity;
	}

	public double getIntensity() {
		return intensity;
	}

	public void setIntensity(double aintensity) {
		intensity = aintensity;
	}

}
