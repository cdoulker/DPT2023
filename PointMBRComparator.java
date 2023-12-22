import java.util.Comparator;

public class PointMBRComparator implements Comparator<MBR>{
	double qx = 0.0;
	double qy = 0.0;
	
	public PointMBRComparator(double qx, double qy) {
		this.qx = qx;
		this.qy = qy;
	}
	 
	// Overriding compare()method of Comparator for descending order of points (represented as MBRs)
	public int compare(MBR m1, MBR m2) {		
		double d1q = Helpers.euclidean(qx, qy, m1.getxMin(), m1.getyMin());
		double d2q = Helpers.euclidean(qx, qy, m2.getxMin(), m2.getyMin());		
		if (d1q < d2q)
			return 1;
		else if (d1q > d2q)
			return -1;
		return 0;
	}
}


