import java.awt.Color;
import java.util.ArrayList;

public class CompCollision {
	
	public static void getOut(Entity sprite, ArrayList<Wall> walls) {
		double xo = sprite.getx();
		double yo = sprite.gety();
		
		int amount = 1;
		int attempts = 0;
		while (true) {
			for (int i=0; i<360; i+=45) {
				sprite.setPosition(xo, yo);
				sprite.move(amount, i);
				if (!PolyCollision.isInWall(sprite.getPointsx(), sprite.getPointsy(), walls)) {
					break;
				}
			}
			amount *= 2;
			attempts += 1;
			if (attempts > 10) {
				break;
			}
		}
	}
	
	public static void nudge(Entity sprite, ArrayList<Wall> walls, int usex, int usey) {
		final int fine1 = 10;
		final int fine2 = 100;
		
		if (PolyCollision.isInWall(sprite.getPointsx(), sprite.getPointsy(), walls)) {
			for (int i=0; i<fine1; i++) {
				if (!PolyCollision.isInWall(sprite.getPointsx(), sprite.getPointsy(), walls)) {
					break;
				}
				sprite.setPosition(sprite.getx() - sprite.getVelx()/fine1 * usex, sprite.gety() - sprite.getVely()/fine1 * usey);
			}
			for (int i=0; i<fine2; i++) {
				if (PolyCollision.isInWall(sprite.getPointsx(), sprite.getPointsy(), walls)) {
					break;
				}
				sprite.setPosition(sprite.getx() + sprite.getVelx()/fine2 * usex, sprite.gety() + sprite.getVely()/fine2 * usey);
			}
			sprite.setPosition(sprite.getx() - sprite.getVelx()/fine2 * usex, sprite.gety() - sprite.getVely()/fine2 * usey);
		}
	}
	
	public static void push(Entity sprite, ArrayList<Wall> walls, int isBall) {
		if (PolyCollision.isInWall(sprite.getPointsx(), sprite.getPointsy(), walls)) {
	        int inc = 10;

            double xrec = 0;
            double xo = sprite.getx();
            
            double yrec = 0;
            double yo = sprite.gety();
            
            sprite.setPosition(xo - sprite.getVelx(), yo - sprite.getVely());
            for (int i=0; i<inc; i++) {
            	sprite.setPosition(sprite.getx() + sprite.getVelx() * 1.0/inc, sprite.gety());
            	if (PolyCollision.isInWall(sprite.getPointsx(), sprite.getPointsy(), walls)) {
                    xrec = i;
                    break;
            	}
                if (i == inc - 1) {
                    xrec = i;
                }
            }
            
            sprite.setPosition(xo - sprite.getVelx(), yo - sprite.getVely());
            for (int i=0; i<inc; i++) {
            	sprite.setPosition(sprite.getx(), sprite.gety() + sprite.getVely() * 1.0/inc);
            	if (PolyCollision.isInWall(sprite.getPointsx(), sprite.getPointsy(), walls)) {
                    yrec = i;
                    break;
            	}
                if (i == inc - 1) {
                    yrec = i;
                }
            }
            
            sprite.setPosition(xo - sprite.getVelx(), yo - sprite.getVely());
            if (xrec >= yrec) {
                sprite.setPosition(sprite.getx() + sprite.getVelx(), sprite.gety());
                nudge(sprite, walls, 1, 0);
            	sprite.setPosition(sprite.getx(), sprite.gety() + sprite.getVely());
                nudge(sprite, walls, 0, 1);
            } else {
            	sprite.setPosition(sprite.getx(), sprite.gety() + sprite.getVely());
                nudge(sprite, walls, 0, 1);
                sprite.setPosition(sprite.getx() + sprite.getVelx(), sprite.gety());
                nudge(sprite, walls, 1, 0);
            }
            
            if (PolyCollision.isInWall(sprite.getPointsx(), sprite.getPointsy(), walls)) {
            	//System.out.println("BAD COLLISION");
            	//System.exit(0);
            	getOut(sprite, walls);
            }
            
            if (isBall == 1) {
            	ballBounceCalc(sprite, walls, sprite.getBounce());
            }
		}
	}
	
	public static void ballBounceCalc(Entity sprite, ArrayList<Wall> walls, double bounce) {
        final double ox = sprite.getx();
        final double oy = sprite.gety();
        double angcur = 0;
        double angstart = 0;
        double angend = 0;
        
        final double strictness = sprite.getRadius() * 1.0/8.0;
        sprite.move(strictness, angcur);

        final int q = 6;
        if (PolyCollision.isInWall(sprite.getPointsx(), sprite.getPointsy(), walls)) {
        	for (int i=0; i<360; i+=q) {
        		sprite.setPosition(ox, oy);
        		sprite.move(strictness, angcur);
        		if (!PolyCollision.isInWall(sprite.getPointsx(), sprite.getPointsy(), walls)) {
        			angcur -= q;
        			angstart = angcur;
        			break;
        		}
                angcur += q;
        	}
        	for (int i=0; i<360; i+=q) {
        		sprite.setPosition(ox, oy);
        		sprite.move(strictness, angcur);
        		if (!PolyCollision.isInWall(sprite.getPointsx(), sprite.getPointsy(), walls)) {
        			angcur += q;
        			angend = angcur;
        			break;
        		}
                angcur -= q;
        	}
        } else {
        	for (int i=0; i<360; i+=q) {
        		sprite.setPosition(ox, oy);
        		sprite.move(strictness, angcur);
        		if (PolyCollision.isInWall(sprite.getPointsx(), sprite.getPointsy(), walls)) {
        			angstart = angcur;
        			break;
        		}
                angcur += q;
        	}
        	
        	for (int i=0; i<360; i+=q) {
        		sprite.setPosition(ox, oy);
        		sprite.move(strictness, angcur);
        		if (!PolyCollision.isInWall(sprite.getPointsx(), sprite.getPointsy(), walls)) {
        			angcur -= q;
        			angend = -(360 - angcur);
        			break;
        		}
                angcur += q;
        	}
        }
        sprite.setPosition(ox, oy);
        
        angstart = (angstart + 360 * 2) % 360;
        angend = (angend + 360 * 2) % 360;
        
        double normal = ((angend + angstart) * 0.5 + 360 * 2) % 360.0;

        double angnew = Math.atan2(sprite.getVely(), sprite.getVelx()) * 180.0/Math.PI;
        double angadj = 0;
        
        if (normal > 180) {
            angadj -= normal - 180;
        } else {
            angadj = 180 - normal;
        }
        angnew += angadj;
        
        angnew = (angnew + 360 * 2) % 360;
        angadj = (angadj + 360 * 2) % 360;
        
        if (angnew > 180) {
        	angnew -= (angnew - 180.0) * 2;
        } else {
        	angnew += (180.0 - angnew) * 2;
        }
        angnew -= angadj - 180;
        
        angnew = (angnew + 360 * 2) % 360;
        angadj = (angadj + 360 * 2) % 360;
        
        double dist = Math.sqrt(Math.pow(sprite.getVelx(), 2) + Math.pow(sprite.getVely(), 2));

        final int leeway = 30;
        if (Math.abs(angnew - (Math.atan2(sprite.getVely(), sprite.getVelx())) * 180.0/Math.PI) % 360 < leeway || Math.abs(angnew - (Math.atan2(sprite.getVely(), sprite.getVelx())) * 180.0/Math.PI) % 360 > 360 - leeway) {
            sprite.setVelx(Math.cos(Math.toRadians(angnew)) * dist);
            sprite.setVely(Math.sin(Math.toRadians(angnew)) * dist);
        } else {
            sprite.setVelx(Math.cos(Math.toRadians(angnew)) * dist * bounce);
            sprite.setVely(Math.sin(Math.toRadians(angnew)) * dist * bounce);
		}
	}
	
}
