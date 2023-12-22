import java.util.PriorityQueue;

public class ExeDemo {

	// main() for testing Joins
	public static void main(String[] args) {
		String sFile = "DataUnMBR1.txt";
		MBR m = new MBR(0, 10, 0, 10);
		DataGenerator g = new DataGenerator(100000, m, sFile);
		g.generateUniformMBR(0.1, 0.1);				
		Grid myGrid1 = new Grid(m, 50);
		myGrid1.loadGridMBR(sFile);
		//myGrid.printAllCells();
		sFile = "DataUnMBR2.txt";		
		g = new DataGenerator(100000, m, sFile);
		g.generateUniformMBR(0.1, 0.1);				
		Grid myGrid2 = new Grid(m, 50);
		myGrid2.loadGridMBR(sFile);

		long lStart = System.currentTimeMillis();
		myGrid1.pbsm(myGrid2);
		long lEnd = System.currentTimeMillis();
		System.out.println("PBMS no line sweep : "+(lEnd-lStart));
		
		lStart = System.currentTimeMillis();
		myGrid1.pbsmWLineSweep(myGrid2);
		lEnd = System.currentTimeMillis();
		System.out.println("PBMS with line sweep : "+(lEnd-lStart));
	}
/*	
	// main() for testing kNN
	public static void main(String[] args) {
		String sFile = "DataUn.txt";
		MBR m = new MBR(0, 10, 0, 10);
		DataGenerator g = new DataGenerator(100, m, sFile);
		g.generateUniform();				
		Grid myGrid = new Grid(m, 5);
		myGrid.loadGrid(sFile);
		//myGrid.printAllCells();
		//myGrid.rangeQuery(1,1,1);
		System.out.println("kNN Grid =============================");
		double qx = 5.0, qy = 5.0;
		PriorityQueue<MBR> pqKNN = myGrid.kNNQuery(qx, qy, 5);
		int i = 0;
		while (i < 5) {//!pqKNN.isEmpty()
			m = pqKNN.poll();
			System.out.println((++i)+" "+Helpers.euclidean(qx, qy, m.getxMin(), m.getyMin())+"-NN "+m.toString());
		}
		// Find and print kNN BRUTE-FORCE
		System.out.println("kNN Brute Force =============================");
		BruteForce bf = new BruteForce(sFile);
		PriorityQueue<MBR> pqKNNbf = bf.kNN(qx, qy, 5);
		i = 0;
		while (!pqKNNbf.isEmpty()) {
			m = pqKNNbf.poll();
			System.out.println((++i)+" "+Helpers.euclidean(qx, qy, m.getxMin(), m.getyMin())+"-NN "+m.toString());
		}
	}
*/
/* 
 	// main() for testing simple things
	public static void main(String[] args) {
		String sFile = "DataUn.txt";
		MBR m = new MBR(0, 10, 0, 10);
		DataGenerator g = new DataGenerator(100, m, sFile);
		g.generateUniform();				
		Grid myGrid = new Grid(m, 5);
		myGrid.loadGrid(sFile);
		/*ArrayList<Cell> arCells = myGrid.findCellsAtHop(5,5,2);
		for (int i=0 ; i < arCells.size() ; i++)
			System.out.println((i+1)+" "+arCells.get(i).toString());*/

		/*    	MBR myMBR = new MBR(1, 11, 1, 11);
        Grid myGrid = new Grid(myMBR, 4);

        //myGrid.printAllCells();

        //System.out.println(myGrid.findCell(20, 20));

        MBR testMBR = new MBR(1, 2, 1, 2);
        MBR testMBR2 = new MBR(2, 3, 7, 8);
        myGrid.insertMBR(testMBR);
        myGrid.insertMBR(testMBR2);

        myGrid.printAllCells();

        System.out.println("**************");
        myGrid.printCells(1, 4);
	}
*/
}