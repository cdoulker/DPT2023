
public class Helpers {
	
	public static double euclidean(double x1, double y1, double x2, double y2) {
		double eucl = Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2);
		return Math.sqrt(eucl);
	}
}
