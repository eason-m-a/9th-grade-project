import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class Player extends Entity {

	private double accel;
	private double speedcap;
	private double jump;
	private Point respawnPoint;
	private int rooms;
	private int score;

	public Player(int[] apointsx, int[] apointsy, Color acolor, double ax, double ay) {
		super(apointsx, apointsy, acolor, ax, ay);
		accel = 4;
		speedcap = 10;
		jump = 20;
		respawnPoint = new Point((int) ax, (int) ay);
		rooms = 0;
	}

	public double getAccel() {
		return accel;
	}

	public double getSpeedcap() {
		return speedcap;
	}

	public double getJump() {
		return jump;
	}

	public Point getRespawnPoint() {
		return respawnPoint;
	}

	public int getRooms() {
		return rooms;
	}

	public int getScore() {
		return score;
	}
	
	public void setAccel(double aaccel) {
		accel = aaccel;
	}

	public void setSpeedcap(double aspeedcap) {
		speedcap = aspeedcap;
	}

	public void setJump(double ajump) {
		jump = ajump;
	}

	public void setRespawnPoint(Point point) {
		respawnPoint = point;
	}

	public void setRooms(int arooms) {
		rooms = arooms;
	}
	
	public void setScore(int ascore) {
		score = ascore;
	}

	public void respawn() {
		setVelx(0);
		setVely(0);
		setPosition(respawnPoint.getX(), respawnPoint.getY());
	}

	public void update(ArrayList<Wall> walls, int[] heldKeys) {
		int h = (heldKeys[3] - heldKeys[1]);
		int v = -(heldKeys[0] - heldKeys[2]);
		if (getGravityx() == 0) {
			if ((Math.signum(getVelx()) != h || Math.abs(getVelx()) < getSpeedcap()) && h != 0) {
				setVelx(getVelx() + h * getAccel());
			}
		}
		if (getGravityy() == 0) {
			if ((Math.signum(getVely()) != v || Math.abs(getVely()) < getSpeedcap()) && v != 0) {
				setVely(getVely() + v * getAccel());
			}
		}

		if (heldKeys[4] == 1) {
			final int ang = (int) (Math.atan2(-getGravityy(), -getGravityx()) * 180.0 / Math.PI);
			move(getRadius() * 1.0 / 2.0, ang);
			if (PolyCollision.isInWall(getPointsx(), getPointsy(), walls)) {
				if (getGravityx() != 0) {
					setVelx(-Math.cos(Math.toRadians(ang)) * getJump());
				}
				if (getGravityy() != 0) {
					setVely(-Math.sin(Math.toRadians(ang)) * getJump());
				}
			}

			move(getRadius() * 1.0 / 2.0, ang + 180);
		}

		super.update(walls);
	}
}
