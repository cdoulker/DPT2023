import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.PriorityQueue;

public class BruteForce {

	String file;

	public BruteForce(String file) {
		this.file = file;
	}

	public PriorityQueue<MBR> kNN(double qx, double qy, int k) {
		PriorityQueue<MBR> pq = new PriorityQueue<MBR>(k, new PointMBRComparator(qx, qy));
		BufferedReader b;
		double threshold = Double.MAX_VALUE;
		String sLine = "";
		try {
			b = new BufferedReader(new FileReader(this.file));
			while ((sLine=b.readLine()) != null) {
				String[] arLine = sLine.split(",");
				double x = Double.parseDouble(arLine[1]);
				double y = Double.parseDouble(arLine[2]);
				double distance = Helpers.euclidean(qx, qy, x, y);
				if (distance < threshold) {
					MBR m = new MBR(x, x, y, y);
					// update threshold
					if (pq.size() == k) {
						// remove farthest point from Priority Queue
						pq.poll();
						// add object m to Priority Queue
						pq.add(m);
						MBR kthNei = pq.peek();
						double dkthNei = Helpers.euclidean(qx, qy, kthNei.getxMin(), kthNei.getyMin());
						//System.out.println("kth distance = "+kthNei);
						//if (dkthNei < threshold)
						threshold = dkthNei;
					}
					else
						// add object m to Priority Queue
						pq.add(m);
				}
			}
			b.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}				
		catch (IOException e) {
			e.printStackTrace();
		}
		return pq;
	} 
}
