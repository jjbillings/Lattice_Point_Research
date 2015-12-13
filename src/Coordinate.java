
public class Coordinate {

	private int x;
	private int y;
	
	public Coordinate(int xc, int yc){
		x = xc;
		y = yc;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	//MAYBE DELETE setX/setY (I dont't think they're used at all).
	public void setX(int xc){
		x = xc;
	}
	
	public void setY(int yc){
		y = yc;
	}
	
	public String toString(){
		return "(" + x + "," + y + ")";
	}
	
}
