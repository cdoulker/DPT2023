import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DataGenerator {

	private int nRows;
	private MBR mbr;
	private String sFile;

	public static void main(String[] args) {
		MBR m = new MBR(1, 11, 1, 11);
		DataGenerator g = new DataGenerator(100, m, "DataUnMBR.txt");
		//g.generateUniform();
		g.generateUniformMBR(0.1, 0.1);
	}
	
	public DataGenerator(int nRows, MBR mbr, String sFile) {
		this.nRows = nRows;
		this.mbr = new MBR(mbr);
		this.sFile = sFile;		
	}
	
	public void generateUniform() {
		double x, y;
		int id;		
		Random r = new Random();
		StringBuffer tmp = new StringBuffer();
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(sFile));
			for (int i=1 ; i <= nRows ; i++) {
				id = i;
				// random coordinates in [0,1)
				x = r.nextDouble();
				y = r.nextDouble();
				// normalize with respect to: mbr
				x = mbr.getxMin() + x * (mbr.getxMax() - mbr.getxMin());
				y = mbr.getyMin() + y * (mbr.getyMax() - mbr.getyMin());
				// write in file
				tmp.append(id).append(",").append(x).append(",").append(y).append("\r\n");
				w.write(tmp.toString());
				tmp.setLength(0);
			}
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void generateUniformMBR(double maxX, double maxY) {
		double x, y, xlength, ylength, xUpper, yUpper;
		int id;		
		Random r = new Random();
		StringBuffer tmp = new StringBuffer();
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(sFile));
			for (int i=1 ; i <= nRows ; i++) {
				id = i;
				// random coordinates in [0,1)
				x = r.nextDouble();
				y = r.nextDouble();
				// normalize with respect to: mbr
				x = mbr.getxMin() + x * (mbr.getxMax() - mbr.getxMin());
				y = mbr.getyMin() + y * (mbr.getyMax() - mbr.getyMin());
				// Now generate an MBR having lower left corner (x,y) and X-length/Y-length up to maxX/maxY 
				xlength = r.nextDouble();
				ylength = r.nextDouble();
				// normalize with respect to: maxX, maxY
				xlength = mbr.getxMin() + xlength * maxX * (mbr.getxMax() - mbr.getxMin());
				ylength = mbr.getyMin() + ylength * maxY * (mbr.getyMax() - mbr.getyMin());
				xUpper = x + xlength;
				yUpper = y + ylength;
				// ensure that it is inside the MBR
				if (xUpper > mbr.getxMax()) xUpper = mbr.getxMax();
				if (yUpper > mbr.getyMax()) yUpper = mbr.getyMax();
				// write in file
				tmp.append(id).append(",").append(x).append(",").append(xUpper).append(",").
											append(y).append(",").append(yUpper).append("\r\n");
				w.write(tmp.toString());
				tmp.setLength(0);
			}
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
}
