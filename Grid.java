
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Grid {
	private MBR mbr;
	private int m;
	private Cell[][] cells;

	public Grid() {}

	public Grid(MBR mbr, int m) {
		this.mbr = new MBR(mbr);
		this.m = m;
		this.cells = new Cell[m][m];

		double dx = (this.mbr.getxMax() - this.mbr.getxMin()) / (double) m;
		double dy = (this.mbr.getyMax() - this.mbr.getyMin()) / (double) m;

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < m; j++) {
				cells[i][j] = new Cell(new MBR(this.mbr.getxMin() + i * dx, this.mbr.getxMin() + (i + 1) * dx, this.mbr.getyMin() + j * dy, this.mbr.getyMin() + (j + 1) * dy));
			}
		}
	}

	public MBR getMbr() {
		return mbr;
	}

	public void setMbr(MBR mbr) {
		this.mbr = mbr;
	}

	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
	}

	public Cell[][] getCells() {
		return cells;
	}

	public void setCells(Cell[][] cells) {
		this.cells = cells;
	}

	public void printAllCells() {
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < m; j++) {
				System.out.println("Cell[" + i + "][" + j + "]: " + cells[i][j]);
			}
		}
	}

	public void loadGrid(String sFile) {    	
		BufferedReader r;
		try {
			r = new BufferedReader(new FileReader(sFile));
			String sLine = "";
			while ((sLine=r.readLine()) != null) {
				String[] arLine = sLine.split(",");
				double x = Double.parseDouble(arLine[1]); 
				double y = Double.parseDouble(arLine[2]);    	
				Cell c = findCell(x, y);
				MBR m = new MBR(x, x, y, y);
				c.addMBR(m);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadGridMBR(String sFile) {    	
		BufferedReader r;
		try {
			r = new BufferedReader(new FileReader(sFile));
			String sLine = "";
			while ((sLine=r.readLine()) != null) {
				String[] arLine = sLine.split(",");
				double xMin = Double.parseDouble(arLine[1]);
				double xMax = Double.parseDouble(arLine[2]);
				double yMin = Double.parseDouble(arLine[3]);
				double yMax = Double.parseDouble(arLine[4]);
				MBR m = new MBR(xMin, xMax, yMin, yMax);
				ArrayList<Cell> a = findIntersectingCells(m);
				for (int i=0 ; i < a.size() ; i++) 					
					a.get(i).addMBR(m);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void pbsm(Grid g) {
		System.out.println("Executing PBSM...");
		int count = 0;
		for (int i=0 ; i < m ; i++) {
			for (int j=0 ; j < m ; j++) {
				Cell c1 = this.getCells()[i][j];
				Cell c2 = g.getCells()[i][j];
				for (int a=0 ; a < c1.getObjects().size() ; a++) {
					for (int b=0 ; b < c2.getObjects().size() ; b++) {
						MBR m1 = c1.getObjects().get(a);
						MBR m2 = c2.getObjects().get(b);
						if (m1.intersects(m2)) {}
							//System.out.println((++count)+" "+m1.toString()+" && "+m2.toString());
					}
				}
			}
		}
	}	

	public void pbsmWLineSweep(Grid g) {
		System.out.println("Executing PBSM with Line Sweep...");
		int count = 0;
		for (int i=0 ; i < m ; i++) {
			for (int j=0 ; j < m ; j++) {
				Cell c1 = this.getCells()[i][j];
				Cell c2 = g.getCells()[i][j];
				ArrayList<MBR> a1 = c1.getObjects();
				ArrayList<MBR> a2 = c2.getObjects();
				Collections.sort(a1, Comparator.comparingDouble(m -> m.getxMin()));
				Collections.sort(a2, Comparator.comparingDouble(MBR::getxMin));
				/*System.out.println("before Line Sweep");
				for (int a=0 ; a < a1.size() ; a++) {
					System.out.println(a1.get(a).toString());
				}*/
				count = lineSweep(a1, a2, count);
			}
		}
	}	

	// Implements Line Sweep, assumes a1 and a2 sorted on x-axis based on starting point
	private int lineSweep(ArrayList<MBR> a1, ArrayList<MBR> a2, int curCount) {
		int count = curCount;
		int startingj = 0;		
		for (int i=0 ; i < a1.size() ; i++) {
			boolean bFoundFirstIntersectingMRB = false;
			MBR m1 = a1.get(i);
			for (int j=startingj ; j < a2.size() ; j++) {
				MBR m2 = a2.get(j);
				if (m2.getxMin() > m1.getxMax())
					break;
				if (m1.intersectsX(m2)) {
					if (!bFoundFirstIntersectingMRB) {
						startingj = j;
						bFoundFirstIntersectingMRB = true;
					}									
					if (m1.intersectsY(m2)) {}
						//System.out.println((++count)+" "+m1.toString()+" && "+m2.toString());
				}				
			}
		}
		return count;
	}

	public Cell[] rangeQueryFilter(double qx, double qy, double r) {
		double dx = (this.mbr.getxMax() - this.mbr.getxMin()) / (double) m;
		double dy = (this.mbr.getyMax() - this.mbr.getyMin()) / (double) m;
		int imin = (int)Math.floor((qx - r - this.mbr.getxMin())/dx);
		int imax = (int)Math.floor((qx + r - this.mbr.getxMin())/dx);
		int jmin = (int)Math.floor((qy - r - this.mbr.getyMin())/dy);
		int jmax = (int)Math.floor((qy + r - this.mbr.getyMin())/dy);
		//System.out.println(imin+" "+imax+" "+jmin+" "+jmax+" ");
		if (imin < 0) imin = 0;
		if (imax > m-1) imax = m-1;
		if (jmin < 0) jmin = 0;
		if (jmax > m-1) jmax = m-1;
		//System.out.println(imin+" "+imax+" "+jmin+" "+jmax+" ");
		int nrCells = (imax-imin+1)*(jmax-jmin+1);
		Cell[] arCells =  new Cell[nrCells];
		int cnt=0;
		for (int i = imin ; i <= imax ; i++)
			for (int j = jmin ; j <= jmax ; j++)
				arCells[cnt++] = cells[i][j];
		return arCells;
	}
	public ArrayList<MBR> rangeQueryRefine(Cell[] arCells, double qx, double qy, double r) {
		ArrayList<MBR> arRes = new ArrayList<MBR>(); 
		for (int i=0 ; i < arCells.length ; i++) {
			Cell c = arCells[i];
			for (int j=0 ; j < c.getObjects().size() ; j++) {
				MBR p = c.getObjects().get(j);
				if (Helpers.euclidean(qx, qy, p.getxMin(), p.getyMin()) <= r)
					arRes.add(p);
			}
		} 			
		return arRes;
	}

	public ArrayList<MBR> rangeQuery(double qx, double qy, double r) {		
		// 1. Find intersecting cells
		Cell[] arCells = rangeQueryFilter(qx, qy, r);
		for (int i=0 ; i < arCells.length ; i++)
			System.out.println(arCells[i].toString());
		// 2. Find objects within these cells at distance <= r
		return rangeQueryRefine(arCells, qx, qy, r);
	}

	// Implements kNN query by incrementally searching the neighboring cells of the one that encloses q.
	// Stops when no further cell can return a closer neighbor.
	public PriorityQueue<MBR> kNNQuery(double qx, double qy, int k) {
		int hop = -1;
		boolean bStop = false;
		double threshold = Double.MAX_VALUE, dkthNei = 0.0;
		PriorityQueue<MBR> pq = new PriorityQueue<MBR>(k, new PointMBRComparator(qx, qy));
		while (!bStop) {
			hop++;
			// find neighboring cells
			ArrayList<Cell> arNei = findCellsAtHop(qx, qy, hop);			
			// add to PQ			
			for (int i=0 ; i < arNei.size() ; i++) {
				Cell c = arNei.get(i);
				for (int j=0 ; j < c.getObjects().size() ; j++) {
					MBR m = c.getObjects().get(j);
					if (Helpers.euclidean(qx, qy, m.getxMin(), m.getyMin()) < threshold) {
						// update threshold
						if (pq.size() == k) {
							// remove farthest point from Priority Queue
							pq.poll();
							// add object m to Priority Queue
							pq.add(m);
							MBR kthNei = pq.peek();
							dkthNei = Helpers.euclidean(qx, qy, kthNei.getxMin(), kthNei.getyMin());
							//System.out.println("kth distance = "+kthNei);
							//if (dkthNei < threshold)
							threshold = dkthNei;
						}
						else
							// add object m to Priority Queue
							pq.add(m);
					}
				}		
				//System.out.println("kth distance = "+dkthNei+" at hop: "+hop);
			} 
			if (hop == 1) break;
			// check to stop Search
			/*if (+++++) 
				bStop = true;*/
		}
		return pq;
	}

	public ArrayList<Cell> findCellsAtHop(double x, double y, int hop) {
		//System.out.println("findCellsAtHop(): "+hop);
		ArrayList<Cell> arCells = null;
		if (hop == 0) {
			Cell c = findCell(x, y);
			arCells = new ArrayList<Cell>(1);
			arCells.add(c);
		}
		else {
			double dx = (this.mbr.getxMax() - this.mbr.getxMin()) / (double) m;
			double dy = (this.mbr.getyMax() - this.mbr.getyMin()) / (double) m;			
			int ix = (int)Math.floor((x - this.mbr.getxMin())/dx);
			int jy = (int)Math.floor((y - this.mbr.getyMin())/dy);
			int imin = ix - hop;
			int imax = ix + hop;
			int jmin = jy - hop;
			int jmax = jy + hop;
			arCells = new ArrayList<Cell>();
			for (int i = imin ; i <= imax ; i++)
				for (int j = jmin ; j <= jmax ; j++)
					if (i == imin || i == imax || j == jmin || j == jmax) 
						arCells.add(cells[i][j]);
		}
		return arCells;
	}

	// Public API
	public Cell findCell(double x, double y) {
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < m; j++) {
				if ((cells[i][j].getMbr().getxMin() <= x) && (x <= cells[i][j].getMbr().getxMax()) &&
						(cells[i][j].getMbr().getyMin() <= y) && (y <= cells[i][j].getMbr().getyMax())) {
					return cells[i][j];
				}
			}
		}
		return null;
	}

	// Retrieves all Cells that intersect with the MBR: m 
	public ArrayList<Cell> findIntersectingCells(MBR mbr) {
		double dx = (this.mbr.getxMax() - this.mbr.getxMin()) / (double) m;
		double dy = (this.mbr.getyMax() - this.mbr.getyMin()) / (double) m;			
		int iminx = (int)Math.floor((mbr.getxMin() - this.mbr.getxMin())/dx);
		int jminy = (int)Math.floor((mbr.getyMin() - this.mbr.getyMin())/dy);
		int imaxx = (int)Math.floor((mbr.getxMax() - this.mbr.getxMin())/dx);
		int jmaxy = (int)Math.floor((mbr.getyMax() - this.mbr.getyMin())/dy);
		if (imaxx == m) imaxx = m-1;
		if (jmaxy == m) jmaxy = m-1;
		//System.out.println("findIntersectingCells() "+iminx+" "+imaxx+" "+jminy+" "+jmaxy+" "+mbr.toString());
		ArrayList<Cell> arCells = new ArrayList<Cell>();
		for (int i = iminx ; i <= imaxx ; i++)
			for (int j = jminy ; j <= jmaxy ; j++) {			
				arCells.add(cells[i][j]);
				//System.out.println("cell:"+i+","+j);
			}
		return arCells;
	}

	/**
	 * ����� �������� !
	 * @param x
	 * @param y
	 * @return
	 */
	public ArrayList<Cell> findCells(double x, double y) {
		ArrayList<Cell> arrayList = new ArrayList<>();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < m; j++) {
				if ((cells[i][j].getMbr().getxMin() <= x) && (x <= cells[i][j].getMbr().getxMax()) &&
						(cells[i][j].getMbr().getyMin() <= y) && (y <= cells[i][j].getMbr().getyMax())) {
					arrayList.add(cells[i][j]);
				}
			}
		}
		return arrayList;
	}

	public void printCells(double x, double y) {
		ArrayList<Cell> arrayList = findCells(x, y);
		for (Cell cell: arrayList) {
			System.out.println(cell);
		}
	}

	public void insertMBR(MBR mbr) {
		Cell cell = findCell(mbr.getxMin(), mbr.getyMin());
		if (cell != null) {
			cell.getObjects().add(mbr);
		}
		System.out.println(cell);
	}
}