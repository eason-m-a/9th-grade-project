import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import java.util.ArrayList;
import java.util.Arrays;

public class AnimationProject extends JPanel {

	private static final Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int WIDTH = (int)size.getWidth();
	private static final int HEIGHT = (int)size.getHeight();
	private static final int LEVELWIDTH = 1536;
	private static final int LEVELHEIGHT = 864;
	private static final Color BG = new Color(0, 200, 0);
	private static final long STARTTIME = System.nanoTime();

	private long gameStart;
	private int[] heldKeys = new int[5]; // W A S D SPACE
	private int viewx = 0;
	private int viewy = 0;
	private int cksize = 6;
	private int ccIndex = 0;
	private int[] dirConvert = { 1, 2, 3, 2, 3, 0, 3, 0, 1, 0, 1, 2 };
	private boolean invin = false;

	private double difficulty = 1;

	private boolean playing = false;
	private boolean gameover = false;

	private ArrayList<int[][][][][]> chunkchunks = new ArrayList<int[][][][][]>();

	ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>>> blueprintx = ReadFile
			.decryptFilex("AnimationProjectLevelFileEM.txt");
	ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>>> blueprinty = ReadFile
			.decryptFiley("AnimationProjectLevelFileEM.txt");
	ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>>> blueprintp = ReadFile
			.decryptFilep("AnimationProjectLevelFileEM.txt");

	private BufferedImage image;
	private Graphics g;
	private Timer timer;

	// declare stuff
	private Player player;
	private MovingWall lava;
	private ArrayList<Level> levels = new ArrayList<Level>();

	public AnimationProject() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = image.getGraphics();

		addKeyListener(new Keyboard());
		setFocusable(true);

		timer = new Timer(5, new TimerListener());
		timer.start();
	}

	private class Keyboard implements KeyListener {

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				System.exit(ABORT);
			}
			if (e.getKeyCode() == KeyEvent.VK_R) {
				if (playing) {
					player.respawn();
				} else if (gameover) {
					start();
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_W) {
				heldKeys[0] = 1;
			}
			if (e.getKeyCode() == KeyEvent.VK_A) {
				heldKeys[1] = 1;
			}
			if (e.getKeyCode() == KeyEvent.VK_S) {
				heldKeys[2] = 1;
			}
			if (e.getKeyCode() == KeyEvent.VK_D) {
				heldKeys[3] = 1;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				heldKeys[4] = 1;
				if (!playing && !gameover) {
					start();
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_G) {
				invin = !invin;
			}
		}

		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_W) {
				heldKeys[0] = 0;
			}
			if (e.getKeyCode() == KeyEvent.VK_A) {
				heldKeys[1] = 0;
			}
			if (e.getKeyCode() == KeyEvent.VK_S) {
				heldKeys[2] = 0;
			}
			if (e.getKeyCode() == KeyEvent.VK_D) {
				heldKeys[3] = 0;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				heldKeys[4] = 0;
			}
		}

		public void keyTyped(KeyEvent e) {

		}
	}

	private class TimerListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// draw background / clear screen
			g.setColor(BG);
			g.fillRect(0, 0, WIDTH, HEIGHT);

			if (playing && !gameover) {
				if ((System.nanoTime() - gameStart) / 1000000000 < 3) {
					player.respawn();
				}
				// move stuff / update locations
				for (int i = 0; i < levels.size(); i++) {
					levels.get(i).update(player);
				}

				PolyCollision.applyLaunchers(player, levels.get(Math.max(player.getRooms() - 1, 0)).getLaunchers());
				PolyCollision.applyGravity(player, levels.get(Math.max(player.getRooms() - 1, 0)).getGravityFields());

				if (invin) {
					player.setGravityx(0);
					player.setGravityy(0);
				}

				player.update(levels.get(Math.max(player.getRooms() - 1, 0)).getWalls(), heldKeys);

				if (PolyCollision.isInWall(player.getPointsx(), player.getPointsy(),
						levels.get(Math.max(player.getRooms() - 1, 0)).getHazards())
						|| PolyCollision.isHit(player.getPointsx(), player.getPointsy(),
								levels.get(Math.max(player.getRooms() - 1, 0)).getProjectiles())) {
					if (!invin) {
						player.respawn();
					}
				}

				viewx = (int) (player.getx() - WIDTH / 2);
				viewy = (int) (player.gety() - HEIGHT / 2);
				if (dirConvert[levels.get(Math.max(player.getRooms() - 1, 0)).getType()] == 1
						|| dirConvert[levels.get(Math.max(player.getRooms() - 1, 0)).getType()] == 3) {
					viewx = levels.get(Math.max(player.getRooms() - 1, 0)).getx() - WIDTH/2;
				} else {
					viewy = levels.get(Math.max(player.getRooms() - 1, 0)).gety() - HEIGHT/2;
				}

				if ((ccIndex + 1) * LEVELWIDTH * cksize - LEVELWIDTH < player.getx()) {
					ccIndex += 1;
					chunkchunks.add(generateChunkChunk(cksize, 0, chunkchunks.get(ccIndex - 1)[4][0][0][0][0], 2, 0,
							cksize - 1, chunkchunks.get(ccIndex - 1)[2][0][0][0][0]));
					loadChunkChunk(chunkchunks.get(chunkchunks.size() - 1));
				}

				lava.move(player.getRooms() * difficulty,
						Math.atan2(levels.get(0).gety() - lava.gety(), levels.get(0).getx() - lava.getx()) * 180.0
								/ Math.PI);
				if (Math.sqrt(Math.pow(levels.get(0).getx() - lava.getx(), 2)
						+ Math.pow(levels.get(0).gety() - lava.gety(), 2)) <= player.getRooms() * difficulty) {
					lava.setPosition(levels.get(0).getx(), levels.get(0).gety());
					levels.remove(0);
					player.setRooms(player.getRooms() - 1);
				}

				if (Math.abs(lava.getx() - player.getx()) < LEVELWIDTH / 2 + player.getRadius()
						&& Math.abs(lava.gety() - player.gety()) < LEVELHEIGHT / 2 + player.getRadius()) {
					gameover = true;
				}
			}

			if (playing) {
				lava.setColor(new Color(255,
						(int) (Math.sin(Math.toRadians(System.nanoTime() / 5000000)) * 127.5 + 127.5), 0));

				// draw stuff
				for (int i = 0; i < levels.size(); i++) {
					if (player.getRooms() - 1 == i) {
						levels.get(i).draw(g, viewx, viewy, false);
					} else {
						levels.get(i).draw(g, viewx, viewy, true);
					}
				}

				final int size = 100 + (int) (Math.sin(System.nanoTime() / 1000000000.0) * 50);
				final int cx = (int) player.getRespawnPoint().getX() - viewx;
				final int cy = (int) player.getRespawnPoint().getY() - viewy;
				g.setColor(Color.WHITE);
				g.fillPolygon(
						new int[] { cx + size, cx + size / 10, cx, cx - size / 10, cx - size, cx - size / 10, cx,
								cx + size / 10 },
						new int[] { cy, cy + size / 10, cy + size, cy + size / 10, cy, cy - size / 10, cy - size,
								cy - size / 10 },
						8);

				lava.draw(g, viewx, viewy, false);
				player.draw(g, viewx, viewy, false);
				g.setColor(Color.BLACK);
				g.setFont(new Font("Arial", Font.BOLD, 40));

				String message = "You are %s ";
				if (player.getRooms() - 1 == 1) {
					message += "room away from the lava";
				} else {
					message += "rooms away from the lava";
				}
				if (player.getRooms() - 1 <= 1) {
					message += "!!!!";
				}
				if (player.getRooms() - 1 <= 5) {
					message += "!";
				}
				g.drawString(String.format(message, player.getRooms() - 1), WIDTH / 2 - 340, 50);
				g.drawString("#" + player.getScore(), 10, HEIGHT - 20);
				if ((System.nanoTime() - gameStart) / 1000000000 < 4) {
					g.setFont(new Font("Arial", Font.BOLD, 100));
					if ((System.nanoTime() - gameStart) / 1000000000 >= 3) {
						g.drawString("Go!", WIDTH / 2 - 70, HEIGHT - 20);
					} else if ((System.nanoTime() - gameStart) / 1000000000 >= 2) {
						g.drawString("Set", WIDTH / 2 - 90, HEIGHT - 20);
					} else if ((System.nanoTime() - gameStart) / 1000000000 >= 1) {
						g.drawString("Ready", WIDTH / 2 - 150, HEIGHT - 20);
					}

				}
			}

			if (gameover) {
				if (player.getPointsxnc()[0] < 3000) {
					final int warp = 5;

					int[] pointsxn = new int[player.getPointsx().length];
					int[] pointsyn = new int[player.getPointsx().length];

					for (int i = 0; i < player.getPointsx().length; i++) {
						pointsxn[i] = (int) (player.getPointsxnc()[i] * 1.05 + Math.random() * warp * 2 - warp);
						pointsyn[i] = (int) (player.getPointsync()[i] * 1.05 + Math.random() * warp * 2 - warp);
					}

					player.setPointsxnc(pointsxn);
					player.setPointsync(pointsyn);
					player.pointTowards(0);
					player.setPosition(player.getx(), player.gety());

				} else {
					g.setColor(Color.BLACK);
					g.fillRect(0, 0, WIDTH, HEIGHT);
					g.setColor(Color.WHITE);
					g.setFont(new Font("Arial", Font.BOLD, 100));
					g.drawString("GAME OVER", WIDTH / 2 - 310, 110);
					final long drawTime = System.nanoTime();
					for (int i = 10; i >= 0; i--) {
						g.setColor(new Color(255 - 25 * i, 255 - 25 * i, 255 - 25 * i));
						g.drawString("Press R to retry", WIDTH / 2 - 380, HEIGHT / 2 + 110
								+ (int) (Math.sin(Math.toRadians((drawTime - 50000000 * i) / 20000000)) * 50));
					}
					g.drawString("SCORE: " + player.getScore(), WIDTH / 2 - 250, HEIGHT - 50);
					playing = false;
				}
			}

			if (!playing && !gameover) {
				int tcolor = (int) (Math.sin(Math.toRadians(System.nanoTime() / 5000000)) * 127.5 + 127.5);
				g.setColor(new Color(tcolor, tcolor, tcolor));
				g.setFont(new Font("Arial", Font.BOLD, 100));

				if ((System.nanoTime() - STARTTIME) / 1000000000 >= 2) {
					g.drawString("SPACE TO START", WIDTH / 2 - 425, HEIGHT - 100);
				} else {
					g.drawString("SPACE TO START", WIDTH / 2 - 425,
							HEIGHT - (int) (((System.nanoTime() - STARTTIME) / 1000000000.0) * 100) + 100);
				}

				String[] title = { "Run", "From", "the", "Lava" };
				g.setColor(Color.BLACK);
				for (int i = 0; i < title.length; i++) {
					g.drawString(title[i], WIDTH / 2 + 200 * i - 400, HEIGHT / 2
							+ (int) (Math.sin(Math.toRadians((System.nanoTime() - STARTTIME) / 4000000) + 90 * i)
									* 100));
				}
			}

			repaint();
		}

	}

	public int[][][] generateChunk(int side, int startx, int starty, int startdir, int enddir) {
		int direction;
		boolean ublock;
		boolean dblock;
		boolean lblock;
		boolean rblock;

		int[][] chunk = new int[side][side];
		for (int y = 0; y < side; y++) {
			for (int x = 0; x < side; x++) {
				chunk[y][x] = -1;
			}
		}
		int curx = startx;
		int cury = starty;
		int prevdir = startdir;
		chunk[starty][startx] = -2;

		while (true) {
			direction = (int) (Math.random() * 4);
			ublock = false;
			dblock = false;
			lblock = false;
			rblock = false;

			if (cury == side - 1) {
				if (direction == enddir && enddir == 1) {
					chunk[cury][curx] = new int[][] { { -1, 0, 1, 2 }, { 5, -1, 3, 4 }, { 7, 8, -1, 6 },
							{ 9, 10, 11, -1 } }[prevdir][direction];
					break;
				} else {
					ublock = true;
				}
			} else if (chunk[cury + 1][curx] != -1) {
				ublock = true;
			}
			if (cury == 0) {
				if (direction == enddir && enddir == 3) {
					chunk[cury][curx] = new int[][] { { -1, 0, 1, 2 }, { 5, -1, 3, 4 }, { 7, 8, -1, 6 },
							{ 9, 10, 11, -1 } }[prevdir][direction];
					break;
				} else {
					dblock = true;
				}
			} else if (chunk[cury - 1][curx] != -1) {
				dblock = true;
			}
			if (curx == 0) {
				if (direction == enddir && enddir == 2) {
					chunk[cury][curx] = new int[][] { { -1, 0, 1, 2 }, { 5, -1, 3, 4 }, { 7, 8, -1, 6 },
							{ 9, 10, 11, -1 } }[prevdir][direction];
					break;
				} else {
					lblock = true;
				}
			} else if (chunk[cury][curx - 1] != -1) {
				lblock = true;
			}
			if (curx == side - 1) {
				if (direction == enddir && enddir == 0) {
					chunk[cury][curx] = new int[][] { { -1, 0, 1, 2 }, { 5, -1, 3, 4 }, { 7, 8, -1, 6 },
							{ 9, 10, 11, -1 } }[prevdir][direction];
					break;
				} else {
					rblock = true;
				}
			} else if (chunk[cury][curx + 1] != -1) {
				rblock = true;
			}

			if (ublock && dblock && lblock && rblock) {
				chunk = new int[side][side];
				for (int y = 0; y < side; y++) {
					for (int x = 0; x < side; x++) {
						chunk[y][x] = -1;
					}
				}
				curx = startx;
				cury = starty;
				prevdir = startdir;
				chunk[starty][startx] = -2;
			} else if (!new boolean[] { rblock, ublock, lblock, dblock }[direction]) {
				chunk[cury][curx] = new int[][] { { -1, 0, 1, 2 }, { 5, -1, 3, 4 }, { 7, 8, -1, 6 },
						{ 9, 10, 11, -1 } }[prevdir][direction];
				prevdir = new int[] { 2, 3, 0, 1 }[direction];

				if (direction == 1) {
					cury += 1;
				} else if (direction == 3) {
					cury -= 1;
				} else if (direction == 2) {
					curx -= 1;
				} else {
					curx += 1;
				}
			}
		}
		return new int[][][] { chunk, { { direction } }, { { curx } }, { { cury } } };
	}

	public int[][][][][] generateChunkChunk(int side, int startx, int starty, int startdir, int enddir, int startprevx,
			int startprevy) {
		int[][][] cc2d = generateChunk(side, startx, starty, startdir, enddir);
		int[][][][] chunkchunk = new int[side][side][side][side];

		for (int yy = 0; yy < chunkchunk.length; yy++) {
			for (int xx = 0; xx < chunkchunk[yy].length; xx++) {
				for (int y = 0; y < chunkchunk[yy][xx].length; y++) {
					for (int x = 0; x < chunkchunk[yy][xx][y].length; x++) {
						chunkchunk[yy][xx][y][x] = -1;
					}
				}
			}
		}

		int[][][] curchunk;
		int curx = startx;
		int cury = starty;
		int prevx = startprevx;
		int prevy = startprevy;
		int prevdir = startdir;
		int[] dirOp = { 2, 3, 0, 1 };

		String[] dirStrings = { "RU", "RL", "RD", "UL", "UD", "UR", "LD", "LR", "LU", "DR", "DU", "DL" };

		while (true) {
			if (prevdir == 1 || prevdir == 3) {
				if (prevy == 0) {
					prevy = side - 1;
				} else {
					prevy = 0;
				}
			} else {
				if (prevx == 0) {
					prevx = side - 1;
				} else {
					prevx = 0;
				}
			}

			curchunk = generateChunk(side, prevx, prevy, prevdir, dirConvert[cc2d[0][cury][curx]]);

			prevx = curchunk[2][0][0];
			prevy = curchunk[3][0][0];
			prevdir = dirOp[dirConvert[cc2d[0][cury][curx]]];

			chunkchunk[cury][curx] = curchunk[0];

			if (curx == cc2d[2][0][0] && cury == cc2d[3][0][0]) {
				break;
			}

			if (curchunk[1][0][0] == 1) {
				cury += 1;
			} else if (curchunk[1][0][0] == 3) {
				cury -= 1;
			} else if (curchunk[1][0][0] == 2) {
				curx -= 1;
			} else {
				curx += 1;
			}
		}

		return new int[][][][][] { chunkchunk, { { { { startprevy } } } }, { { { { prevy } } } },
				{ { { { starty } } } }, { { { { cury } } } } };

	}

	public void loadChunkChunk(int[][][][][] chunkchunk) {
		int startxx = 0;
		int startyy = chunkchunk[3][0][0][0][0];
		int startx = 0;
		int starty = chunkchunk[1][0][0][0][0];
		int curxx = 0;
		int curyy = startyy;
		int curx = 0;
		int cury = starty;
		int prevdir;
		int size = chunkchunk[0].length;

		while (true) {

			while (true) {
				loadRandLevel(chunkchunk[0][curyy][curxx][cury][curx],
						curx * LEVELWIDTH + curxx * size * LEVELWIDTH + size * size * LEVELWIDTH * ccIndex,
						-cury * LEVELHEIGHT + -curyy * size * LEVELHEIGHT);
				if (dirConvert[chunkchunk[0][curyy][curxx][cury][curx]] == 1) {
					cury += 1;
					prevdir = 1;
				} else if (dirConvert[chunkchunk[0][curyy][curxx][cury][curx]] == 3) {
					cury -= 1;
					prevdir = 3;
				} else if (dirConvert[chunkchunk[0][curyy][curxx][cury][curx]] == 2) {
					curx -= 1;
					prevdir = 2;
				} else {
					curx += 1;
					prevdir = 0;
				}

				if (cury < 0 || cury >= size || curx < 0 || curx >= size) {
					break;
				}
			}

			if (prevdir == 1) {
				cury = 0;
				curyy += 1;
			} else if (prevdir == 3) {
				cury = size - 1;
				curyy -= 1;
			} else if (prevdir == 2) {
				curx = size - 1;
				curxx -= 1;
			} else {
				curx = 0;
				curxx += 1;
			}

			if (curyy < 0 || curyy >= size || curxx < 0 || curxx >= size) {
				break;
			}

		}

	}

	public void loadRandLevel(int type, int disx, int disy) {
		int[] loadpointsx = {};
		int[] loadpointsy = {};
		int[] loadpointsp = {};
		int levelind = (int) (Math.random() * blueprintx.get(type).size());
		Level level = new Level(disx, disy, LEVELWIDTH, LEVELHEIGHT, type);

		for (int c = 0; c < blueprintx.get(type).get(levelind).size(); c++) {
			for (int d = 0; d < blueprintx.get(type).get(levelind).get(c).size(); d++) {
				loadpointsx = new int[blueprintx.get(type).get(levelind).get(c).get(d).size()];
				for (int e = 0; e < blueprintx.get(type).get(levelind).get(c).get(d).size(); e++) {
					loadpointsx[e] = blueprintx.get(type).get(levelind).get(c).get(d).get(e) + disx;
				}
				loadpointsy = new int[blueprinty.get(type).get(levelind).get(c).get(d).size()];
				for (int e = 0; e < blueprinty.get(type).get(levelind).get(c).get(d).size(); e++) {
					loadpointsy[e] = blueprinty.get(type).get(levelind).get(c).get(d).get(e) + disy;
				}
				loadpointsp = new int[blueprintp.get(type).get(levelind).get(c).get(d).size()];
				for (int e = 0; e < blueprintp.get(type).get(levelind).get(c).get(d).size(); e++) {
					loadpointsp[e] = blueprintp.get(type).get(levelind).get(c).get(d).get(e);
				}

				if (c == 0) {
					level.addWall(new Wall(loadpointsx, loadpointsy, Color.GREEN));
				} else if (c == 1) {
					level.addHazard(new Wall(loadpointsx, loadpointsy, Color.RED));
				} else if (c == 2) {
					level.addGravityField(
							new GravityField(loadpointsx, loadpointsy, Color.CYAN, loadpointsp[0], loadpointsp[1]));
				} else if (c == 3) {
					level.addLauncher(
							new Launcher(Color.YELLOW, loadpointsx[0], loadpointsy[0], loadpointsp[0], loadpointsp[1]));
				} else if (c == 4) {
					level.addSentry(new Sentry(new Color(225, 0, 0), loadpointsx[0], loadpointsy[0], loadpointsp[0],
							loadpointsp[1]));
				}
			}
		}
		levels.add(level);
	}

	public void start() {
		chunkchunks.clear();
		levels.clear();
		ccIndex = 0;
		chunkchunks.add(generateChunkChunk(cksize, 0, 0, 2, 0, cksize - 1, 0));

		loadChunkChunk(chunkchunks.get(chunkchunks.size() - 1));

		int radius = 50;
		int sides = 36;
		int[] pointsx = new int[sides];
		int[] pointsy = new int[sides];

		for (int i = 0; i < sides; i++) {
			pointsx[i] = (int) (Math.cos(2.0 * Math.PI / sides * (i)) * radius);
			pointsy[i] = (int) (Math.sin(2.0 * Math.PI / sides * (i)) * radius);
		}

		player = new Player(pointsx, pointsy, Color.BLACK, 0, LEVELHEIGHT / 2);

		lava = new MovingWall(new int[] { LEVELWIDTH / 2, LEVELWIDTH / 2, -LEVELWIDTH / 2, -LEVELWIDTH / 2 },
				new int[] { LEVELHEIGHT / 2, -LEVELHEIGHT / 2, -LEVELHEIGHT / 2, LEVELHEIGHT / 2 }, Color.ORANGE, -LEVELHEIGHT * 2, LEVELHEIGHT / 2);
		lava.setVelx(1);

		playing = true;
		gameover = false;
		gameStart = System.nanoTime();
	}

	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}

	public static void main(String[] args) {

		JFrame frame = new JFrame("Run From the Lava");
		frame.setSize(WIDTH, HEIGHT);
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new AnimationProject());
		frame.setVisible(true);
	}
}