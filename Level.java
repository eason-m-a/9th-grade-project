import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class Level {

	private ArrayList<Wall> walls = new ArrayList<Wall>();
	private ArrayList<Wall> hazards = new ArrayList<Wall>();
	private ArrayList<GravityField> gravityFields = new ArrayList<GravityField>();
	private ArrayList<Launcher> launchers = new ArrayList<Launcher>();
	private ArrayList<Sentry> sentries = new ArrayList<Sentry>();
	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

	private int x;
	private int y;
	private int width;
	private int height;
	private int type;
	private int checkx;
	private int checky;
	private boolean passed = false;

	public Level(int ax, int ay, int awidth, int aheight, int atype) {
		x = ax;
		y = ay;
		width = awidth;
		height = aheight;
		type = atype;
		if (type <= 2) {
			checkx = x + width;
			checky = y + height / 2;
		} else if (type <= 5) {
			checkx = x + width / 2;
			checky = y;
		} else if (type <= 8) {
			checkx = x;
			checky = y + height / 2;
		} else {
			checkx = x + width / 2;
			checky = y + height;
		}
	}

	public int getx() {
		return x + width / 2;
	}

	public int gety() {
		return y + height / 2;
	}

	public int getType() {
		return type;
	}

	public ArrayList<Wall> getWalls() {
		return walls;
	}

	public ArrayList<Wall> getHazards() {
		return hazards;
	}

	public ArrayList<GravityField> getGravityFields() {
		return gravityFields;
	}

	public ArrayList<Launcher> getLaunchers() {
		return launchers;
	}

	public ArrayList<Sentry> getSentries() {
		return sentries;
	}

	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

	public void addWall(Wall wall) {
		walls.add(wall);
	}

	public void addHazard(Wall hazard) {
		hazards.add(hazard);
	}

	public void addGravityField(GravityField gravityField) {
		gravityFields.add(gravityField);
	}

	public void addLauncher(Launcher launcher) {
		launchers.add(launcher);
	}

	public void addSentry(Sentry sentry) {
		sentries.add(sentry);
	}

	public void update(Player player) {
		for (int i = 0; i < launchers.size(); i++) {
			launchers.get(i).update();
		}

		for (int i = 0; i < sentries.size(); i++) {
			sentries.get(i).update(projectiles, player.getx(), player.gety());
		}

		for (int i = 0; i < projectiles.size(); i++) {
			if (Math.abs(projectiles.get(i).getx() - x - width / 2) > width / 2
					|| Math.abs(projectiles.get(i).gety() - y - height / 2) > height / 2) {
				projectiles.remove(i);
				i -= 1;
			}
		}

		for (int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).update();
		}

		if (Math.sqrt(Math.pow(checkx - player.getx(), 2) + Math.pow(checky - player.gety(), 2)) <= player.getRadius()
				* 2 && !passed) {
			player.setRespawnPoint(new Point(checkx, checky));
			player.respawn();
			player.setRooms(player.getRooms() + 1);
			player.setScore(player.getScore() + 1);
			passed = true;
		}

		if ((Math.abs(player.getx() - x - width / 2) > width / 2
				|| Math.abs(player.gety() - y - height / 2) > height / 2) && player.getRespawnPoint().getX() == checkx
				&& player.getRespawnPoint().getY() == checky) {
			player.respawn();
		}
	}

	public void draw(Graphics g, int viewx, int viewy, boolean faded) {
		if ((x - viewx < width || x - viewx > -width) && (y - viewy < height || y - viewy > -height)) {
			for (int i = 0; i < gravityFields.size(); i++) {
				gravityFields.get(i).draw(g, viewx, viewy, faded);
			}

			for (int i = 0; i < hazards.size(); i++) {
				hazards.get(i).draw(g, viewx, viewy, faded);
			}

			for (int i = 0; i < walls.size(); i++) {
				walls.get(i).draw(g, viewx, viewy, faded);
			}

			for (int i = 0; i < launchers.size(); i++) {
				launchers.get(i).draw(g, viewx, viewy, faded);
			}

			for (int i = 0; i < sentries.size(); i++) {
				sentries.get(i).draw(g, viewx, viewy, faded);
			}

			for (int i = 0; i < projectiles.size(); i++) {
				projectiles.get(i).draw(g, viewx, viewy, faded);
			}
			for (int i = 0; i < gravityFields.size(); i++) {
				int size = 100;
				int width = 10;
				int[] pointsx = { size, size, width, size, size, 0 };
				int[] pointsy = { size, size - width, 0, -size + width, -size, 0 };
				int prevx;
				for (int p = 0; p < pointsx.length; p++) {
					prevx = pointsx[i];
					pointsx[i] = (int) (pointsx[i] * Math.cos(Math.toRadians(gravityFields.get(i).getDirection()))
							- pointsy[i] * Math.sin(Math.toRadians(gravityFields.get(i).getDirection())))
							+ gravityFields.get(i).getPointsx()[0] - viewx;
					pointsy[i] = (int) (prevx * Math.sin(Math.toRadians(gravityFields.get(i).getDirection()))
							+ pointsy[i] * Math.cos(Math.toRadians(gravityFields.get(i).getDirection())))
							+ gravityFields.get(i).getPointsy()[0] - viewy;
				}
				g.setColor(Color.BLUE);
				//g.fillPolygon(pointsx, pointsy, 6);
			}
		}
	}

	public void deleteAll() {
		walls.clear();
		hazards.clear();
		gravityFields.clear();
		launchers.clear();
		sentries.clear();
		projectiles.clear();
	}

}
