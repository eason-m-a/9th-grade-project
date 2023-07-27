import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class Entity extends MovingWall {
	
	private int radius;
	private double gravityx;
	private double gravityy;
	private double friction;
	private double bounce;
	
	public Entity(int[] apointsx, int[] apointsy, Color acolor, double ax, double ay) {
		super(apointsx, apointsy, acolor, ax, ay);
		radius = getPointsxo()[0];
		gravityx = 0;
		gravityy = -1;
		friction = 0.9;
		bounce = 0.75;
	}
	
	public double getGravityx() {
		return gravityx;
	}
	
	public double getGravityy() {
		return gravityy;
	}
	
	public double getFriction() {
		return friction;
	}
	
	public double getBounce() {
		return bounce;
	}
	
	public int getRadius() {
		return radius;
	}
	
	public void setGravityx(double agravityx) {
		gravityx = agravityx;
		if (gravityx == -0) {
			gravityx = 0;
		}
	}
	
	public void setGravityy(double agravityy) {
		gravityy = agravityy;
		if (gravityy == -0) {
			gravityy = 0;
		}
	}
	
	public void setFriction(double afriction) {
		friction = afriction;
	}
	
	public void setBounce(double abounce) {
		bounce = abounce;
	}
	
	public void update(ArrayList<Wall> walls) {
		setVelx(getVelx() - gravityx);
		setVely(getVely() - gravityy);
		
		if (Math.sqrt(Math.pow(getVelx(), 2) + Math.pow(getVely(), 2)) > radius * 2) {
			double ang = Math.atan2(getVely(), getVelx());
			setVelx(Math.cos(ang) * radius * 2);
			setVely(Math.sin(ang) * radius * 2);
		}
		
		if (gravityx == 0) {
			setVelx(getVelx() * friction);
		}
		if (gravityy == 0) {
			setVely(getVely() * friction);
		}
		
		super.update();
		
		CompCollision.push(this, walls, 1);
	}
	
}
