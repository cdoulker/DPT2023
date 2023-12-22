
import java.util.ArrayList;

public class Cell {
    private MBR mbr;
    private ArrayList<MBR> objects;

    public Cell() {}

    public Cell(MBR mbr) {
        this.mbr = new MBR(mbr);
        objects = new ArrayList<>();
    }    
    
    public void addMBR(MBR mbr) {
    	if (objects != null)
    		objects.add(mbr);
    	else
    		System.out.println("Cell: arraylist objects is null");
    }
    
    public MBR getMbr() {
        return mbr;
    }

    public void setMbr(MBR mbr) {
        this.mbr = mbr;
    }

    public ArrayList<MBR> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<MBR> objects) {
        this.objects = objects;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "mbr=" + mbr +
                ", objects=" + objects +
                '}';
    }
}