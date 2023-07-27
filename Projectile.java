import java.awt.Color;

public class Projectile extends MovingWall {
	
	private Sentry owner;
	
	public Projectile(int[] apointsx, int[] apointsy, Color acolor, double ax, double ay, Sentry aowner) {
		super(apointsx, apointsy, acolor, ax, ay);
		owner = aowner;
		setDirection(owner.getDirection());
	}
	
	public Sentry getOwner() {
		return owner;
	}
	
	public void setOwner(Sentry aowner) {
		owner = aowner;
	}
	
	public void update() {
		pointTowards(getDirection());
		move(Math.abs(owner.getIntensity()), getDirection()); 
	}
	
}
