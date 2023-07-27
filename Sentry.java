import java.awt.Color;
import java.util.ArrayList;

public class Sentry extends Launcher {
	
	private double lastFireTime;
	private int fireRate;
	private static int size = 50;
	
	public Sentry(Color acolor, double ax, double ay, double adirection, double aintensity) {
		super(new int[] {(int)(size * 1.5), (int)(size * 1.5), -size/2, -size, -size/2}, new int[] {-size/2, size/2, (int)(size * 0.75), 0, -(int)(size * 0.75)}, acolor, ax, ay, adirection, aintensity);
		lastFireTime = 0;
		fireRate = 1000000000;
	}
	
	public double getFireRate() {
		return fireRate;
	}
	
	public void setFireRate(int afireRate) {
		fireRate = afireRate;
	}
	
	public void update(ArrayList<Projectile> projectileList, double px, double py) {
		if (Math.signum(getIntensity()) == -1) {
			pointTowards(Math.atan2(py - gety(), px - getx()) * 180.0/Math.PI);
		}
		super.update();
		if (System.nanoTime() >= lastFireTime + fireRate) {
			final int size = 15;
			Projectile projectile = new Projectile(new int[] {-size * 2, -size *2, 0, (int)(size * 0.75), size, (int)(size * 0.75), 0}, new int[] {size, -size, -size, -(int)(size * 0.75), 0, (int)(size * 0.75), size}, new Color(225, 0, 0), getx(), gety(), this);
			projectileList.add(projectile);
			
			lastFireTime = System.nanoTime();
		}
	}
}
