import java.util.ArrayList;

public class PolyCollision {
	
	public static boolean isInWall(int[] pointsx, int[] pointsy, ArrayList<Wall> walls) {
		for (int i = 0; i < walls.size(); i++) {
			if (polygonsCollide(pointsx, pointsy, walls.get(i).getPointsx(), walls.get(i).getPointsy())) {
				return true;
			}
		}
		return false;
	}

	public static boolean isHit(int[] pointsx, int[] pointsy, ArrayList<Projectile> projectiles) {
		for (int i = 0; i < projectiles.size(); i++) {
			if (polygonsCollide(pointsx, pointsy, projectiles.get(i).getPointsx(), projectiles.get(i).getPointsy())) {
				return true;
			}
		}
		return false;
	}

	public static void applyGravity(Entity sprite, ArrayList<GravityField> fields) {
		for (int i = 0; i < fields.size(); i++) {
			if (polygonsCollide(sprite.getPointsx(), sprite.getPointsy(), fields.get(i).getPointsx(),
					fields.get(i).getPointsy())) {
				sprite.setGravityx(
						(int) Math.cos(Math.toRadians(fields.get(i).getDirection())) * fields.get(i).getIntensity());
				sprite.setGravityy(
						(int) Math.sin(Math.toRadians(fields.get(i).getDirection())) * fields.get(i).getIntensity());
			}
		}
	}

	public static void applyLaunchers(Entity sprite, ArrayList<Launcher> launchers) {
		for (int i = 0; i < launchers.size(); i++) {
			if (polygonsCollide(sprite.getPointsx(), sprite.getPointsy(), launchers.get(i).getPointsx(),
					launchers.get(i).getPointsy())) {
				sprite.setVelx(
						Math.cos(Math.toRadians(launchers.get(i).getDirection())) * launchers.get(i).getIntensity());
				sprite.setVely(
						Math.sin(Math.toRadians(launchers.get(i).getDirection())) * launchers.get(i).getIntensity());
			}
		}
	}

	public static boolean polygonsCollide(int[] points1x, int[] points1y, int[] points2x, int[] points2y) {
		int[] xpoints = new int[4];
		int[] ypoints = new int[4];

		int ind1 = 0;
		int ind2 = 0;

		for (int f = 0; f < points1x.length; f++) {
			ind1 += 1;
			if (ind1 == points1x.length) {
				ind1 = 0;
			}
			for (int s = 0; s < points2x.length; s++) {
				ind2 += 1;
				if (ind2 == points2x.length) {
					ind2 = 0;
				}

				xpoints = new int[] { points1x[f], points1x[ind1], points2x[s], points2x[ind2] };
				ypoints = new int[] { points1y[f], points1y[ind1], points2y[s], points2y[ind2] };

				if (linesCollide(xpoints, ypoints)) {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean linesCollide(int[] pointsx, int[] pointsy) {
		double slope1;
		double slope2;
		if ((pointsx[1] - pointsx[0]) == 0) {
			slope1 = 999999;
		} else {
			slope1 = (pointsy[1] - pointsy[0] + 0.0) / (pointsx[1] - pointsx[0]);
		}
		if ((pointsx[3] - pointsx[2]) == 0) {
			slope2 = 999999;
		} else {
			slope2 = (pointsy[3] - pointsy[2] + 0.0) / (pointsx[3] - pointsx[2]);
		}
		double inter1 = pointsy[0] - slope1 * pointsx[0];
		double inter2 = pointsy[2] - slope2 * pointsx[2];

		int crossx = (int) ((inter2 - inter1) / (slope1 - slope2));
		int crossy = (int) ((slope1 * (inter2 - inter1) / (slope1 - slope2)) + inter1);

		if (slope1 == slope2) {
			return false;
		}

		if (Math.min(pointsx[0], pointsx[1]) <= crossx + 1 && crossx - 1 <= Math.max(pointsx[0], pointsx[1])
				&& Math.min(pointsx[2], pointsx[3]) <= crossx + 1 && crossx - 1 <= Math.max(pointsx[2], pointsx[3])) {
			if (Math.min(pointsy[0], pointsy[1]) <= crossy + 1 && crossy - 1 <= Math.max(pointsy[0], pointsy[1])
					&& Math.min(pointsy[2], pointsy[3]) <= crossy + 1 && crossy - 1 <= Math.max(pointsy[2], pointsy[3])) {
				return true;
			}
		}

		return false;
	}

}
