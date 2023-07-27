import java.awt.Color;

public class MovingWall extends Wall {
	
	private int[] pointsxnc;
	private int[] pointsync;
	private int[] pointsxo;
	private int[] pointsyo;
	private double velx;
	private double vely;
	private double x;
	private double y;
	private double direction;
	
	public MovingWall(int[] apointsx, int[] apointsy, Color acolor, double ax, double ay) {
		super(apointsx, apointsy, acolor);
		pointsxo = apointsx.clone();
		pointsyo = apointsy.clone();
		pointsxnc = apointsx.clone();
		pointsync = apointsy.clone();
		velx = 0;
		vely = 0;
		x = ax;
		y = ay;
		direction = 0;
	}
	
	public double getVelx() {
		return velx;
	}
	
	public double getVely() {
		return vely;
	}
	
	public double getx() {
		return x;
	}
	
	public double gety() {
		return y;
	}
	
	public int[] getPointsxo() {
		return pointsxo;
	}
	
	public int[] getPointsyo() {
		return pointsyo;
	}
	
	public int[] getPointsxnc() {
		return pointsxnc;
	}
	
	public int[] getPointsync() {
		return pointsync;
	}
	
	public double getDirection() {
		return direction;
	}
	
	public void setVelx (double avelx) {
		velx = avelx;
	}
	
	public void setVely (double avely) {
		vely = avely;
	}
	
	public void setx(double ax) {
		x = ax;
	}
	
	public void sety(double ay) {
		y = ay;
	}
	
	public void setPointsxo(int[] apointsxo) {
		pointsxo = apointsxo;
	}
	
	public void setPointsyo(int[] apointsyo) {
		pointsyo = apointsyo;
	}
	
	public void setPointsxnc(int[] apointsxnc) {
		pointsxnc = apointsxnc;
	}
	
	public void setPointsync(int[] apointsync) {
		pointsync = apointsync;
	}
	
	public void setDirection(double adirection) {
		direction = adirection;
	}
	
	public void setPosition(double ax, double ay) {
		x = ax;
		y = ay;
		for (int i=0; i<getPointsx().length; i++) {
			setPointsx((int)(pointsxo[i] + x), i);
			setPointsy((int)(pointsyo[i] + y), i);
		}
	}
	
	public void move(double amount, double angle) {
		setPosition(x + Math.cos(Math.toRadians(angle)) * amount, y + Math.sin(Math.toRadians(angle)) * amount);
	}
	
	public void pointTowards(double adirection) {
		direction = adirection;
		for (int i=0; i<getPointsx().length; i++) {
			pointsxo[i] = (int)(pointsxnc[i] * Math.cos(Math.toRadians(direction)) - pointsync[i] * Math.sin(Math.toRadians(direction)));
			pointsyo[i] = (int)(pointsxnc[i] * Math.sin(Math.toRadians(direction)) + pointsync[i] * Math.cos(Math.toRadians(direction)));
		}
	}
	
	public void update() {
		pointTowards(direction);
		setPosition(x + velx, y + vely);
	}
}
