import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
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
import javax.swing.Timer;

import java.util.ArrayList;
import java.util.Arrays;

public class LevelBuilder extends JPanel {

	private static final int WIDTH = 1536;
	private static final int HEIGHT = 864;
	private static final Color LIGHT_BLUE = new Color(108, 110, 247);
	private static final Color[] CURSOR_COLORS = { Color.BLUE, Color.ORANGE, Color.MAGENTA, Color.LIGHT_GRAY,
			Color.GRAY };
	private static final int GRIDSPACE = 32;

	private int mousex = 0;
	private int mousey = 0;
	private int type = 0;
	private int direction = 0;
	private int power = 0;
	private String uinput = "";
	private boolean enterType = true;
	private boolean menu = true;
	private int leveltype;
	private int importInd;
	private ArrayList<Integer> pointsxcur = new ArrayList<Integer>();
	private ArrayList<Integer> pointsycur = new ArrayList<Integer>();

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
	private ArrayList<Wall> walls = new ArrayList<Wall>();
	private ArrayList<Wall> hazards = new ArrayList<Wall>();
	private ArrayList<GravityField> gravityFields = new ArrayList<GravityField>();
	private ArrayList<Launcher> launchers = new ArrayList<Launcher>();
	private ArrayList<Sentry> sentries = new ArrayList<Sentry>();
	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

	public LevelBuilder() {

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = image.getGraphics();

		g.setColor(LIGHT_BLUE);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// instantiate stuff

		addKeyListener(new Keyboard());
		setFocusable(true);

		addMouseListener(new Mouse());

		timer = new Timer(5, new TimerListener());
		timer.start();
	}

	private class Mouse implements MouseListener {

		public void mousePressed(MouseEvent e) {
			if (!menu) {
				pointsxcur.add(mousex);
				pointsycur.add(mousey);
			}
		}

		public void mouseReleased(MouseEvent e) {

		}

		public void mouseEntered(MouseEvent e) {

		}

		public void mouseExited(MouseEvent e) {

		}

		public void mouseClicked(MouseEvent e) {

		}

	}

	private class Keyboard implements KeyListener {

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				System.out.println("EXPORT:");
				export();
				System.out.println();
				System.exit(ABORT);
			}

			if (enterType) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					uinput += "U";
					if (uinput.length() == 2) {
						if (uinput.charAt(0) == 'U') {
							uinput = "";
						}
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					uinput += "D";
					if (uinput.length() == 2) {
						if (uinput.charAt(0) == 'D') {
							uinput = "";
						}
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					uinput += "L";
					if (uinput.length() == 2) {
						if (uinput.charAt(0) == 'L') {
							uinput = "";
						}
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					uinput += "R";
					if (uinput.length() == 2) {
						if (uinput.charAt(0) == 'R') {
							uinput = "";
						}
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (uinput.length() == 2) {
						walls.clear();
						hazards.clear();
						gravityFields.clear();
						launchers.clear();
						sentries.clear();
						pointsxcur.clear();
						pointsycur.clear();
						enterType = false;

						String[] typeIndex = { "RU", "RL", "RD", "UL", "UD", "UR", "LD", "LR", "LU", "DR", "DU", "DL" };
						for (int i = 0; i < typeIndex.length; i++) {
							if (uinput.equals(typeIndex[i])) {
								leveltype = i;
								break;
							}
						}

						int bdw = 21;
						int bdh = 11;
						walls.add(new Wall(new int[] { 0, 0, GRIDSPACE, GRIDSPACE, GRIDSPACE * bdw, GRIDSPACE * bdw },
								new int[] { 0, GRIDSPACE * bdh, GRIDSPACE * bdh, GRIDSPACE, GRIDSPACE, 0 },
								Color.GREEN));
						walls.add(new Wall(new int[] { 0, 0, GRIDSPACE, GRIDSPACE, GRIDSPACE * bdw, GRIDSPACE * bdw },
								new int[] { HEIGHT, HEIGHT - GRIDSPACE * bdh, HEIGHT - GRIDSPACE * bdh,
										HEIGHT - GRIDSPACE, HEIGHT - GRIDSPACE, HEIGHT },
								Color.GREEN));
						walls.add(new Wall(
								new int[] { WIDTH, WIDTH, WIDTH - GRIDSPACE, WIDTH - GRIDSPACE, WIDTH - GRIDSPACE * bdw,
										WIDTH - GRIDSPACE * bdw },
								new int[] { 0, GRIDSPACE * bdh, GRIDSPACE * bdh, GRIDSPACE, GRIDSPACE, 0 },
								Color.GREEN));
						walls.add(new Wall(
								new int[] { WIDTH, WIDTH, WIDTH - GRIDSPACE, WIDTH - GRIDSPACE, WIDTH - GRIDSPACE * bdw,
										WIDTH - GRIDSPACE * bdw },
								new int[] { HEIGHT, HEIGHT - GRIDSPACE * bdh, HEIGHT - GRIDSPACE * bdh,
										HEIGHT - GRIDSPACE, HEIGHT - GRIDSPACE, HEIGHT },
								Color.GREEN));

						if (!uinput.contains("U")) {
							walls.add(new Wall(
									new int[] { GRIDSPACE * bdw, GRIDSPACE * bdw, WIDTH - GRIDSPACE * bdw,
											WIDTH - GRIDSPACE * bdw },
									new int[] { 0, GRIDSPACE, GRIDSPACE, 0 }, Color.GREEN));
						}
						if (!uinput.contains("D")) {
							walls.add(new Wall(
									new int[] { GRIDSPACE * bdw, GRIDSPACE * bdw, WIDTH - GRIDSPACE * bdw,
											WIDTH - GRIDSPACE * bdw },
									new int[] { HEIGHT, HEIGHT - GRIDSPACE, HEIGHT - GRIDSPACE, HEIGHT }, Color.GREEN));
						}
						if (!uinput.contains("L")) {
							walls.add(new Wall(
									new int[] { 0, GRIDSPACE, GRIDSPACE, 0 }, new int[] { GRIDSPACE * bdh,
											GRIDSPACE * bdh, HEIGHT - GRIDSPACE * bdh, HEIGHT - GRIDSPACE * bdh },
									Color.GREEN));
						}
						if (!uinput.contains("R")) {
							walls.add(new Wall(new int[] { WIDTH, WIDTH - GRIDSPACE, WIDTH - GRIDSPACE, WIDTH },
									new int[] { GRIDSPACE * bdh, GRIDSPACE * bdh, HEIGHT - GRIDSPACE * bdh,
											HEIGHT - GRIDSPACE * bdh },
									Color.GREEN));
						}

						menu = false;
						uinput = "";
					}
				}
			} else {
				if (e.getKeyCode() == KeyEvent.VK_I && uinput.length() != 0) {
					if (blueprintx.get(leveltype).size() > Integer.parseInt(uinput)
							&& blueprintx.get(leveltype).get(0).get(0).size() > 0) {
						importInd = Integer.parseInt(uinput);

						walls.clear();
						hazards.clear();
						gravityFields.clear();
						launchers.clear();
						sentries.clear();
						pointsxcur.clear();
						pointsycur.clear();

						int[] loadpointsx;
						int[] loadpointsy;
						int[] loadpointsp;

						for (int a = 0; a < blueprintx.get(leveltype).get(Integer.parseInt(uinput)).size(); a++) {
							if (blueprintx.get(leveltype).get(Integer.parseInt(uinput)).get(a).size() > 0) {
								for (int b = 0; b < blueprintx.get(leveltype).get(Integer.parseInt(uinput)).get(a)
										.size(); b++) {
									if (blueprintx.get(leveltype).get(Integer.parseInt(uinput)).get(a).get(b)
											.size() == 1) {
										loadpointsx = new int[] { blueprintx.get(leveltype)
												.get(Integer.parseInt(uinput)).get(a).get(b).get(0) };
										loadpointsy = new int[] { blueprinty.get(leveltype)
												.get(Integer.parseInt(uinput)).get(a).get(b).get(0) };

										loadpointsp = new int[blueprintp.get(leveltype).get(Integer.parseInt(uinput))
												.get(a).get(b).size()];
										loadpointsp = new int[blueprintp.get(leveltype).get(Integer.parseInt(uinput))
												.get(a).get(b).size()];
										for (int c = 0; c < blueprintp.get(leveltype).get(Integer.parseInt(uinput))
												.get(a).get(b).size(); c++) {
											loadpointsp[c] = blueprintp.get(leveltype).get(Integer.parseInt(uinput))
													.get(a).get(b).get(c);
										}
									} else {
										loadpointsx = new int[blueprintx.get(leveltype).get(Integer.parseInt(uinput))
												.get(a).get(b).size()];
										for (int c = 0; c < blueprintx.get(leveltype).get(Integer.parseInt(uinput))
												.get(a).get(b).size(); c++) {
											loadpointsx[c] = blueprintx.get(leveltype).get(Integer.parseInt(uinput))
													.get(a).get(b).get(c);
										}
										loadpointsy = new int[blueprinty.get(leveltype).get(Integer.parseInt(uinput))
												.get(a).get(b).size()];
										for (int c = 0; c < blueprinty.get(leveltype).get(Integer.parseInt(uinput))
												.get(a).get(b).size(); c++) {
											loadpointsy[c] = blueprinty.get(leveltype).get(Integer.parseInt(uinput))
													.get(a).get(b).get(c);
										}
										loadpointsp = new int[blueprintp.get(leveltype).get(Integer.parseInt(uinput))
												.get(a).get(b).size()];
										for (int c = 0; c < blueprintp.get(leveltype).get(Integer.parseInt(uinput))
												.get(a).get(b).size(); c++) {
											loadpointsp[c] = blueprintp.get(leveltype).get(Integer.parseInt(uinput))
													.get(a).get(b).get(c);
										}
									}
									if (loadpointsx.length > 0) {
										if (a == 0) {
											walls.add(new Wall(loadpointsx, loadpointsy, Color.GREEN));
										} else if (a == 1) {
											hazards.add(new Wall(loadpointsx, loadpointsy, Color.RED));
										} else if (a == 2) {
											gravityFields.add(new GravityField(loadpointsx, loadpointsy, Color.CYAN,
													loadpointsp[0], loadpointsp[1]));
										} else if (a == 3) {
											launchers.add(new Launcher(Color.YELLOW, loadpointsx[0], loadpointsy[0],
													loadpointsp[0], loadpointsp[1]));
											launchers.get(launchers.size() - 1).update();
										} else if (a == 4) {
											sentries.add(new Sentry(new Color(225, 0, 0), loadpointsx[0],
													loadpointsy[0], loadpointsp[0], loadpointsp[1]));
											sentries.get(sentries.size() - 1).update(projectiles, 0, 0);
										}
									}
								}
							}
						}
						System.out.println("OPENED: TYPE - " + leveltype + " INDEX - " + uinput);
						export();
						System.out.println("\n");
					}
					uinput = "";
				}
				if (e.getKeyCode() == KeyEvent.VK_MINUS && uinput.length() == 0) {
					uinput += "-";
				}
				if (e.getKeyCode() == KeyEvent.VK_0) {
					uinput += "0";
				}
				if (e.getKeyCode() == KeyEvent.VK_1) {
					uinput += "1";
				}
				if (e.getKeyCode() == KeyEvent.VK_2) {
					uinput += "2";
				}
				if (e.getKeyCode() == KeyEvent.VK_3) {
					uinput += "3";
				}
				if (e.getKeyCode() == KeyEvent.VK_4) {
					uinput += "4";
				}
				if (e.getKeyCode() == KeyEvent.VK_5) {
					uinput += "5";
				}
				if (e.getKeyCode() == KeyEvent.VK_6) {
					uinput += "6";
				}
				if (e.getKeyCode() == KeyEvent.VK_7) {
					uinput += "7";
				}
				if (e.getKeyCode() == KeyEvent.VK_8) {
					uinput += "8";
				}
				if (e.getKeyCode() == KeyEvent.VK_9) {
					uinput += "9";
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER && !uinput.equals("-") && uinput.length() != 0) {
					power = Integer.parseInt(uinput);
				}
			}

			if (e.getKeyCode() == KeyEvent.VK_SLASH && !menu) {
				uinput = "";

				enterType = !enterType;
			}

			if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				if (uinput.length() > 0) {
					uinput = uinput.substring(0, uinput.length() - 1);
				}
			}

			if (e.getKeyCode() == KeyEvent.VK_Q) {
				type = 0; // WALLS
			}
			if (e.getKeyCode() == KeyEvent.VK_W) {
				type = 1; // HAZARDS
			}
			if (e.getKeyCode() == KeyEvent.VK_E) {
				type = 2; // GRAVITY FIELDS
			}
			if (e.getKeyCode() == KeyEvent.VK_R) {
				type = 3; // LAUNCHERS
			}
			if (e.getKeyCode() == KeyEvent.VK_T) {
				type = 4; // SENTRIES
			}
			if (e.getKeyCode() == KeyEvent.VK_D) {
				direction += 15;
				if (direction > 360) {
					direction = direction - 360;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_A) {
				direction -= 15;
				if (direction < 0) {
					direction = 360 - 15;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_Z) {
				if (pointsxcur.size() > 0) {
					pointsxcur.remove(pointsxcur.size() - 1);
					pointsycur.remove(pointsycur.size() - 1);
				} else {
					if (type == 0 && walls.size() > 0) {
						walls.remove(walls.size() - 1);
					}
					if (type == 1 && hazards.size() > 0) {
						hazards.remove(hazards.size() - 1);
					}
					if (type == 2 && gravityFields.size() > 0) {
						gravityFields.remove(gravityFields.size() - 1);
					}
					if (type == 3 && launchers.size() > 0) {
						launchers.remove(launchers.size() - 1);
					}
					if (type == 4 && sentries.size() > 0) {
						sentries.remove(sentries.size() - 1);
					}
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_C) {
				pointsxcur.clear();
				pointsycur.clear();
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				if (pointsxcur.size() > 2) {
					int[] newpointsx = new int[pointsxcur.size()];
					int[] newpointsy = new int[pointsycur.size()];
					for (int i = 0; i < pointsxcur.size(); i++) {
						newpointsx[i] = pointsxcur.get(i);
						newpointsy[i] = pointsycur.get(i);
					}
					if (type == 0) {
						walls.add(new Wall(newpointsx, newpointsy, Color.GREEN));
						pointsxcur.clear();
						pointsycur.clear();
					} else if (type == 1) {
						hazards.add(new Wall(newpointsx, newpointsy, Color.RED));
						pointsxcur.clear();
						pointsycur.clear();
					} else if (type == 2) {
						gravityFields.add(new GravityField(newpointsx, newpointsy, Color.CYAN, direction + 180, power));
						pointsxcur.clear();
						pointsycur.clear();
					}
				} else if (pointsxcur.size() == 1) {
					if (type == 3) {
						launchers.add(
								new Launcher(Color.YELLOW, pointsxcur.get(0), pointsycur.get(0), direction, power));
						launchers.get(launchers.size() - 1).update();
						pointsxcur.clear();
						pointsycur.clear();
					} else if (type == 4) {
						sentries.add(new Sentry(new Color(205, 0, 0), pointsxcur.get(0), pointsycur.get(0), direction,
								power));
						sentries.get(sentries.size() - 1).update(projectiles, 0, 0);
						pointsxcur.clear();
						pointsycur.clear();
					}
				}
			}
		}

		public void keyReleased(KeyEvent e) {

		}

		public void keyTyped(KeyEvent e) {

		}
	}

	private class TimerListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// draw background / clear screen
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, WIDTH, HEIGHT);

			if (!menu) {
				// move stuff / update locations
				mousex = (int) ((MouseInfo.getPointerInfo().getLocation().getX()) + GRIDSPACE * 0.5) / GRIDSPACE
						* GRIDSPACE;
				mousey = (int) ((MouseInfo.getPointerInfo().getLocation().getY()) + GRIDSPACE * 0.5) / GRIDSPACE
						* GRIDSPACE;
				// draw stuff

				for (int s = 0; s < gravityFields.size(); s++) {
					gravityFields.get(s).draw(g, 0, 0, false);
				}
				for (int s = 0; s < hazards.size(); s++) {
					hazards.get(s).draw(g, 0, 0, false);
				}
				for (int s = 0; s < walls.size(); s++) {
					walls.get(s).draw(g, 0, 0, false);
				}
				for (int s = 0; s < launchers.size(); s++) {
					launchers.get(s).draw(g, 0, 0, false);
					g.setFont(new Font("Arial", Font.PLAIN, 50));
					g.setColor(Color.WHITE);
					g.drawString(String.valueOf((int) launchers.get(s).getIntensity()), (int) launchers.get(s).getx(),
							(int) launchers.get(s).gety());
				}
				for (int s = 0; s < sentries.size(); s++) {
					sentries.get(s).draw(g, 0, 0, false);
					g.setFont(new Font("Arial", Font.PLAIN, 50));
					g.setColor(Color.WHITE);
					g.drawString(String.valueOf((int) sentries.get(s).getIntensity()), (int) sentries.get(s).getx(),
							(int) sentries.get(s).gety());
				}
				g.setColor(CURSOR_COLORS[type]);
				g.fillOval(mousex - 15, mousey - 15, 30, 30);
				g.fillOval(mousex - 10 + (int) (Math.cos(Math.toRadians(direction)) * 40),
						mousey - 10 + (int) (Math.sin(Math.toRadians(direction)) * 40), 20, 20);

				g.setColor(Color.WHITE);
				for (int i = 0; i < pointsxcur.size(); i++) {
					g.fillOval(pointsxcur.get(i) - 10, pointsycur.get(i) - 10, 20, 20);
				}

				for (int y = 0; y <= HEIGHT; y += GRIDSPACE) {
					if ((y / GRIDSPACE) % 9 == 0) {
						g.setColor(Color.RED);
					} else if ((y / GRIDSPACE) % 3 == 0) {
						g.setColor(Color.GREEN);
					} else {
						g.setColor(Color.WHITE);
					}
					if (y == HEIGHT) {
						g.setColor(Color.RED);
						y -= 1;
					}
					g.drawLine(0, y, (int) WIDTH, y);
				}

				for (int x = 0; x <= WIDTH; x += GRIDSPACE) {
					if ((x / GRIDSPACE) % 12 == 0) {
						g.setColor(Color.RED);
					} else if ((x / GRIDSPACE) % 6 == 0) {
						g.setColor(Color.GREEN);
					} else {
						g.setColor(Color.WHITE);
					}
					if (x == WIDTH) {
						g.setColor(Color.RED);
						x -= 1;
					}
					g.drawLine(x, 0, x, (int) HEIGHT);
				}
				if (uinput.length() > 0 && !uinput.equals("-") && !enterType) {
					if (Integer.parseInt(uinput) == power) {
						g.setColor(new Color(225, 225, 225));
					}
				}
			}

			if (menu) {
				g.setColor(Color.WHITE);
				String[] instructions = "Escape to export to console and close program0Input level type with arrow keys and enter0Import with I and by entering numbers to select level index0Use slash to swap between inputting numbers and level type0Q selects walls, W selects hazards0E selects gravity fields, R selects launchers, T selects sentries0A and D to change direction of cursor0Z to undo0C to clear0Click to place point0Space to place object once multiple points have been placed0(Launchers and sentries only use 1 point)0(Input will affect power of some objects)0(Negative input for sentry makes it turn towards player)"
						.split("0");
				for (int i = 0; i < instructions.length; i++) {
					g.drawString(instructions[i], 10, i * 50 + 50);
				}
			}

			if (g.getColor().getRed() != 225) {
				g.setColor(new Color(125, 125, 125));
			}
			if (enterType) {
				g.setColor(Color.RED);
			}

			g.setFont(new Font("Arial", Font.BOLD, 50));
			g.drawString(uinput,
					(int) (Math.cos(Math.toRadians(System.nanoTime() / 10000000)) * 100) + (int) (WIDTH / 2),
					(int) (Math.sin(Math.toRadians(System.nanoTime() / 10000000)) * 100) + (int) (HEIGHT / 2));

			if (enterType) {
				g.setColor(new Color(255, 255, 255,
						(int) (Math.cos(Math.toRadians(System.nanoTime() / 10000000)) * 70 + 70)));
				g.fillRect(0, 0, WIDTH, HEIGHT);
			}

			repaint();
		}

	}

	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}

	public void export() {
		String exportx = "";
		String exporty = "";
		for (int c = 0; c < 5; c++) {
			if (c == 0) {
				for (int i = 0; i < walls.size(); i++) {
					for (int w = 0; w < walls.get(i).getPointsx().length; w++) {
						exportx += walls.get(i).getPointsx()[w] + "D";
						exporty += walls.get(i).getPointsy()[w] + "D";
					}
					exportx += "Z0Z0C";
					exporty += "Z0Z0C";
				}
			} else if (c == 1) {
				for (int i = 0; i < hazards.size(); i++) {
					for (int w = 0; w < hazards.get(i).getPointsx().length; w++) {
						exportx += hazards.get(i).getPointsx()[w] + "D";
						exporty += hazards.get(i).getPointsy()[w] + "D";
					}
					exportx += "Z0Z0C";
					exporty += "Z0Z0C";
				}
			} else if (c == 2) {
				for (int i = 0; i < gravityFields.size(); i++) {
					for (int w = 0; w < gravityFields.get(i).getPointsx().length; w++) {
						exportx += gravityFields.get(i).getPointsx()[w] + "D";
						exporty += gravityFields.get(i).getPointsy()[w] + "D";
					}
					exportx += "Z" + (int) gravityFields.get(i).getDirection() + "Z"
							+ (int) gravityFields.get(i).getIntensity() + "C";
					exporty += "Z" + (int) gravityFields.get(i).getDirection() + "Z"
							+ (int) gravityFields.get(i).getIntensity() + "C";
				}
			} else if (c == 3) {
				for (int i = 0; i < launchers.size(); i++) {
					exportx += (int) launchers.get(i).getx() + "D";
					exporty += (int) launchers.get(i).gety() + "D";
					exportx += "Z" + (int) launchers.get(i).getDirection() + "Z" + (int) launchers.get(i).getIntensity()
							+ "C";
					exporty += "Z" + (int) launchers.get(i).getDirection() + "Z" + (int) launchers.get(i).getIntensity()
							+ "C";
				}
			} else if (c == 4) {
				for (int i = 0; i < sentries.size(); i++) {
					exportx += (int) sentries.get(i).getx() + "D";
					exporty += (int) sentries.get(i).gety() + "D";
					exportx += "Z" + (int) sentries.get(i).getDirection() + "Z" + (int) sentries.get(i).getIntensity()
							+ "C";
					exporty += "Z" + (int) sentries.get(i).getDirection() + "Z" + (int) sentries.get(i).getIntensity()
							+ "C";
				}
			}
			exportx += "B";
			exporty += "B";
		}
		exportx += "A";
		exporty += "A";

		for (int i = 0; i < ReadFile.getLineFromFile("AnimationProjectLevelFileEM.txt").get(leveltype * 3)
				.split("A").length; i++) {
			if (importInd == i) {
				System.out.print(exportx);
			} else {
				System.out.print(
						ReadFile.getLineFromFile("AnimationProjectLevelFileEM.txt").get(leveltype * 3).split("A")[i]
								+ "A");
			}
		}
		System.out.println();
		for (int i = 0; i < ReadFile.getLineFromFile("AnimationProjectLevelFileEM.txt").get(leveltype * 3 + 1)
				.split("A").length; i++) {
			if (importInd == i) {
				System.out.print(exporty);
			} else {
				System.out.print(
						ReadFile.getLineFromFile("AnimationProjectLevelFileEM.txt").get(leveltype * 3 + 1).split("A")[i]
								+ "A");
			}
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Build");
		frame.setSize(WIDTH, HEIGHT);
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new LevelBuilder());
		frame.setVisible(true);
	}
}