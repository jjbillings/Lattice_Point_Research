
public class LatticePoint extends Point{
	
	private int numTimesSeen;
	
	public LatticePoint(int x, int y){
		coord = new Coordinate(x,y);
		numTimesSeen = 0;
		name = "Lattice Point " + coord.toString();
	}
	
	public void seePoint(){
		numTimesSeen++;
	}
	
	public int getNumTimesSeen(){
		return numTimesSeen;
	}
}
