import java.util.ArrayList;

//TODO: See if there's a way to elminate all of the Setters/Getters...

public class AdjacencyPoint extends Point{
	
	private int pointsSeen;
	private int pointsNotSeen;
	private int gcd;
	private int numFactors;
	private int numPrimeFactors;
	private int numFlagsWithin;
	private double distanceFromGrid;
	private double distancePercent;
	private double percentSeen;
	private double percentObscured;
	private ArrayList<LatticePoint> viewedPoints;
	private ArrayList<LatticePoint> nonviewedPoints;
	private ArrayList<Point> obscuredGraphData;
	private ArrayList<Point> graphData;
	//BinaryMatrix?
	
	public AdjacencyPoint(int x, int y){
		graphData = new ArrayList();
		viewedPoints = new ArrayList();
		nonviewedPoints = new ArrayList();
		obscuredGraphData = new ArrayList();
		coord = new Coordinate(x,y);
		pointsSeen = 0;
		pointsNotSeen = 0;
		distanceFromGrid = 0;
		distancePercent = 0.0;
		percentSeen = 0.0;
		percentObscured = 0.0;
		numFactors = 0;
		numPrimeFactors = 0;
		numFlagsWithin = 0;
		gcd = -1;
		name = "Adj Point " + coord.toString();
	}
	
	public void calculateDistance(int gridWidth, int gridHeight){
		distanceFromGrid = MathHelper.round(MathHelper.calculateDistance(gridWidth, gridHeight, coord.getX(), coord.getY()),6);
	}
	
	public void calculateDistancePercent(int gridWidth, int gridHeight){
		distancePercent = MathHelper.calculateDistanceFromGridPercent(coord.getX(), coord.getY(), gridWidth, gridHeight);
	}
	
	public void setPercentSeen(int gridWidth, int gridHeight){
		percentSeen = MathHelper.decimalToPercent((double)pointsSeen/(double)(gridWidth*gridHeight));
	}
	
	public void setPercentObscured(int gridWidth, int gridHeight){
		percentObscured = MathHelper.decimalToPercent((double)pointsNotSeen/(double)(gridWidth*gridHeight));
	}
	
	public void incrementFlags(){
		numFlagsWithin++;
	}
	
	public int getNumFlags(){
		return numFlagsWithin;
	}
	
	
	public double getPercentSeen(){
		return percentSeen;
	}
	
	public double getPercentObscured(){
		return percentObscured;
	}
	
	public double getDistancePercent(){
		return distancePercent;
	}
	
	public double getDistance(){
		return distanceFromGrid;
	}
	
	public void calculateGCD(int newGCD){
		gcd = newGCD;
	}
	
	public int getGCD(){
		return gcd;
	}
	
	public void setNumPrimeFactors(int n){
		numPrimeFactors = n;
	}
	
	public void setNumFactors(int n){
		numFactors = n;
	}
	
	public int getNumFactors(){
		return numFactors;
	}
	
	public int getNumPrimeFactors(){
		return numPrimeFactors;
	}
	
	public void seePoint(LatticePoint lp){
		viewedPoints.add(lp);
		graphData.add(lp);
		graphData.add(this);
		pointsSeen++;
	}
	
	public void ignorePoint(LatticePoint lp){
		pointsNotSeen++;
		nonviewedPoints.add(lp);
		obscuredGraphData.add(lp);
		obscuredGraphData.add(this);
	}
	
	public int getNumViewedPoints(){
		return pointsSeen;
	}
	
	public int getNumObscuredPoints(){
		return pointsNotSeen;
	}
	
	public ArrayList<Point> getGraphData(){
		return graphData;
	}
	public ArrayList<LatticePoint> getViewedPoints(){
		return viewedPoints;
	}
	public ArrayList<Point> getObscuredGraphData(){
		return obscuredGraphData;
	}
	public ArrayList<LatticePoint> getNonviewedPoints(){
		return nonviewedPoints;
	}
}
