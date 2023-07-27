import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

public class ReadFile {

	public static ArrayList<String> getLineFromFile(String name) {
		ArrayList<String> lines = new ArrayList<String>();

		try {
			File file = new File(name);

			Scanner sc = new Scanner(file);

			while (sc.hasNextLine()) {
				lines.add(sc.nextLine());
			}
			sc.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
		return lines;
	}

	public static ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>>> decryptFilex(String name) {
		ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>>> blueprintx = new ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>>>();

		try {
			File file = new File(name);

			Scanner sc = new Scanner(file);

			String[] levels = {};
			String[] categories = {};
			String[] pointlists = {};
			String[] points = {};

			ArrayList<String> lines = new ArrayList<String>();

			while (sc.hasNextLine()) {
				lines.add(sc.nextLine());
			}
			sc.close();

			String[] linesArray = new String[lines.size()];
			for (int i = 0; i < lines.size(); i++) {
				linesArray[i] = lines.get(i);
			}
			for (int l = 0; l < lines.size(); l++) {
				if (l % 3 == 0) {
					blueprintx.add(new ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>>());
					levels = splitAdd("A", linesArray[l]);
					for (int a = 0; a < levels.length; a++) {
						blueprintx.get(l / 3).add(new ArrayList<ArrayList<ArrayList<Integer>>>());
						categories = splitAdd("B", levels[a]);
						for (int b = 0; b < categories.length; b++) {
							blueprintx.get(l / 3).get(a).add(new ArrayList<ArrayList<Integer>>());
							if (categories[b].length() > 0) {
								pointlists = splitAdd("C", categories[b]);
							} else {
								pointlists = new String[] {};
							}
							for (int c = 0; c < pointlists.length; c++) {
								blueprintx.get(l / 3).get(a).get(b).add(new ArrayList<Integer>());
								points = splitAdd("D", splitAdd("Z", pointlists[c])[0]);
								for (int d = 0; d < points.length; d++) {
									blueprintx.get(l / 3).get(a).get(b).get(c).add(Integer.parseInt(points[d]));
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		return blueprintx;
	}

	public static ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>>> decryptFiley(String name) {
		ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>>> blueprinty = new ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>>>();

		try {
			File file = new File(name);

			Scanner sc = new Scanner(file);

			String[] levels = {};
			String[] categories = {};
			String[] pointlists = {};
			String[] points = {};

			ArrayList<String> lines = new ArrayList<String>();

			while (sc.hasNextLine()) {
				lines.add(sc.nextLine());
			}
			sc.close();

			String[] linesArray = new String[lines.size()];
			linesArray = lines.toArray(linesArray);

			for (int l = 0; l < lines.size(); l++) {
				if (l % 3 == 1) {
					blueprinty.add(new ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>>());
					levels = splitAdd("A", linesArray[l]);
					for (int a = 0; a < levels.length; a++) {
						blueprinty.get(l / 3).add(new ArrayList<ArrayList<ArrayList<Integer>>>());
						categories = splitAdd("B", levels[a]);
						for (int b = 0; b < categories.length; b++) {
							blueprinty.get(l / 3).get(a).add(new ArrayList<ArrayList<Integer>>());
							if (categories[b].length() > 0) {
								pointlists = splitAdd("C", categories[b]);
							} else {
								pointlists = new String[] {};
							}
							for (int c = 0; c < pointlists.length; c++) {
								blueprinty.get(l / 3).get(a).get(b).add(new ArrayList<Integer>());
								points = splitAdd("D", splitAdd("Z", pointlists[c])[0]);
								for (int d = 0; d < points.length; d++) {
									blueprinty.get(l / 3).get(a).get(b).get(c).add(Integer.parseInt(points[d]));
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		return blueprinty;
	}

	public static ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>>> decryptFilep(String name) {
		ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>>> blueprintp = new ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>>>();

		try {
			File file = new File(name);

			Scanner sc = new Scanner(file);

			String[] levels = {};
			String[] categories = {};
			String[] pointlists = {};
			String[] points = {};

			String[] pointPara;
			String direction;
			String power;

			ArrayList<String> lines = new ArrayList<String>();

			while (sc.hasNextLine()) {
				lines.add(sc.nextLine());
			}
			sc.close();

			String[] linesArray = new String[lines.size()];
			linesArray = lines.toArray(linesArray);

			for (int l = 0; l < lines.size(); l++) {
				if (l % 3 == 0) {
					blueprintp.add(new ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>>());
					levels = splitAdd("A", linesArray[l]);
					for (int a = 0; a < levels.length; a++) {
						blueprintp.get(l / 3).add(new ArrayList<ArrayList<ArrayList<Integer>>>());
						categories = splitAdd("B", levels[a]);
						for (int b = 0; b < categories.length; b++) {
							blueprintp.get(l / 3).get(a).add(new ArrayList<ArrayList<Integer>>());
							if (categories[b].length() > 0) {
								pointlists = splitAdd("C", categories[b]);
							} else {
								pointlists = new String[] {};
							}
							for (int c = 0; c < pointlists.length; c++) {
								blueprintp.get(l / 3).get(a).get(b).add(new ArrayList<Integer>());
								pointPara = splitAdd("Z", pointlists[c]);
								points = splitAdd("D", pointPara[0]);
								direction = pointPara[1];
								power = pointPara[2];
								blueprintp.get(l / 3).get(a).get(b).get(c).add(Integer.parseInt(direction));
								blueprintp.get(l / 3).get(a).get(b).get(c).add(Integer.parseInt(power));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		return blueprintp;
	}

	public static String[] splitAdd(String splitter, String splittie) {
		String[] list = new String[splittie.split(splitter).length];
		for (int i = 0; i < list.length; i++) {
			list[i] = splittie.split(splitter)[i];
		}
		return list;
	}
}