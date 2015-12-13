import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class DataProcessor {
	
	private int gridWidth;
	private int gridHeight;
	private int adjacencyWidth;
	private int adjacencyHeight;
	
	private Scanner reader;
	private ExcelProcessor ep;
	
	private LatticePoint[][] latticeGrid;
	private AdjacencyPoint[][] adjacencySquare;
	
	
	public DataProcessor(){
		reader = new Scanner(System.in);
		gridWidth = 0;
		gridHeight = 0;
		adjacencyWidth = 0;
		adjacencyHeight = 0;
		ep = new ExcelProcessor();
	}
	
	/**
	 * GUI Run method for a single grid
	 * @param compiled - Do we want the compiled data?
	 * @param adj - Do we want each adjacency graph?
	 * @param percent - Do we want the percent seen diagrams?
	 * @param gw - Grid Width
	 * @param gh - Grid Height
	 */
	public void runGUI(boolean compiled, boolean adj, boolean percent, int gw, int gh){
		inputGrid(gw,gh);
		checkVisibility(adj);
		calculateNumFlags();
		if(percent){
			sortPercentSeen(2);
		}
		if(compiled){
			ep.generateRawDataXLSX(adjacencySquare, gw, gh);
		}
	}
	
	/**
	 * GUI Run method for a range of grids.
	 * @param compiled - Do we want the compiled data?
	 * @param adj - Do we want each adjacency graph
	 * @param percent - Do we want the percent seen diagram?
	 * @param sw - Starting Width
	 * @param sh - Starting Height
	 * @param fw - Final Width
	 * @param fh - Final Height
	 */
	public void runGUI(boolean compiled, boolean adj, boolean percent, int sw, int sh, int fw, int fh){
		boolean first = true;
		for(int i = sw; i <= fw; i++){
			for(int k = 2; k < i; k++){
				
				//I dont know what this does but I assume its important...
				if(first && i == sw){
					k = sh;
					first = false;
				}
				inputGrid(i,k);
				checkVisibility(adj);
				calculateNumFlags();
				if(percent){
					//sortPercentSeen(2) arranges them into groups that are
					//2 "percents" wide.
					//TODO: Allow the user to select how "fine" the groups should be.
					sortPercentSeen(2);
				}
				if(compiled){
					ep.generateRawDataXLSX(adjacencySquare, i, k);
				}
				if(i == fw && k == fh){
					return;
				}
			}
		}
	}
	
	public void run(){	
		
		inputGrid();
		checkVisibility(false);
		calculateNumFlags();
		ep.generateRawDataXLSX(adjacencySquare, gridWidth, gridHeight);
		sortPercentSeen(2);
	}
	public void run(boolean auto){
		for(int i = 3; i < 15; i++){
			for(int k = 2; k < i; k++){
				inputGrid(i,k);
				checkVisibility(true);
				calculateNumFlags();
				ep.generateRawDataXLSX(adjacencySquare, gridWidth, gridHeight);
				sortPercentSeen(2);
			}
		}
	}
	
	/**
	 * TODO: It appears as if this method is solely for the console version.
	 */
	public void inputGrid(){
		
		//Intro the Processor and input the width and height of our Lattice Grid
		System.out.println("********** Lattice Point Processor **********");
		System.out.print("Enter the Grid Width: ");
		gridWidth = reader.nextInt();
		System.out.print("Enter the Grid Height: ");
		gridHeight = reader.nextInt();
		
		//Check to see which one is bigger and use that to determine
		//the dimensions of the adjacency grid
		if(gridWidth > gridHeight){
			adjacencyWidth = adjacencyHeight = gridWidth;
		}else if(gridHeight >= gridWidth){
			adjacencyWidth = adjacencyHeight = gridHeight;
		}
		
		//initialize the lattice grid adjacency square arrays
		latticeGrid = new LatticePoint[gridWidth][gridHeight];
		adjacencySquare = new AdjacencyPoint[adjacencyWidth][adjacencyHeight];
		
		initializeGrid();
		System.out.println("paws");
		
	}
	/**
	 * This method is used in both console and GUI versions.
	 * This input grid method is used to generate mass data
	 * @param i
	 * @param k
	 */
	public void inputGrid(int i, int k){
			
			//Intro the Processor and input the width and height of our Lattice Grid
			//TODO: Delete these print statements.
			System.out.println("********** Lattice Point Processor **********");
			System.out.print("Enter the Grid Width: ");
			gridWidth = i;
			System.out.print("Enter the Grid Height: ");
			gridHeight = k;
			
			//Check to see which one is bigger and use that to determine
			//the dimensions of the adjacency grid
			if(gridWidth > gridHeight){
				adjacencyWidth = adjacencyHeight = gridWidth;
			}else if(gridHeight > gridWidth){
				adjacencyWidth = adjacencyHeight = gridHeight;
			}else{
				//we are supposed to be using rectangles, not squares...
				System.out.println("You shouldn't have a square Lattice Grid...");
				adjacencyWidth = adjacencyHeight = gridWidth;
			}
			
			//initialize the lattice grid adjacency square arrays
			latticeGrid = new LatticePoint[gridWidth][gridHeight];
			adjacencySquare = new AdjacencyPoint[adjacencyWidth][adjacencyHeight];
			
			initializeGrid();
			
		}
	
	/*
	 * 
	 * TODO: consider moving to the MathHelper class... meh.
	 * This is only accounting for the "Observed" Flags (m/1) and (1/m)...
	 * It ignores the inferred flags (3/2), (2/3)...
	 * TODO: Consider Revision...
	 */
	private void calculateNumFlags(){
					
		//For each adjacencyPoint
		for(int h = 0; h < adjacencyHeight; ++h){
			for(int w = 0; w < adjacencyWidth; ++w){
				int x = adjacencySquare[w][h].getCoord().getX();
				int y = adjacencySquare[w][h].getCoord().getY();
				//For each line
				for(double i = 1; i <= gridWidth - 1; ++i){
					
					double tryer = (((1/i)*(x-1)) + (gridHeight - 1));
					//Check to see if the point lie below the upper flags
					if(y <= tryer){
						adjacencySquare[w][h].incrementFlags();
					}
					
				}
				
				//Start at 2, because we already checked the (1/1) Adj Flag
				for(double i = 2; i <= gridHeight - 1; ++i){
					
					double tryer = (((i/1)*(x - (gridWidth - 1))) + 1);
					
					//Check to see if the point lie above the lower flags
					if(y >= tryer){
						adjacencySquare[w][h].incrementFlags();
					}
				}
			}
		}
	}
	
	private void initializeGrid(){
		
		//Fills the Lattice Grid with usable Lattice Points
		for(int i = 0; i < gridHeight; i++){
			for(int j = 0; j < gridWidth; j++){
				latticeGrid[j][i] = new LatticePoint(j+1,i+1);
			}
		}
		
		//Fills the Adjacency Square with usable AdjacencyPoints
		for(int i = 0; i < adjacencyHeight; i++){
			for(int j = 0; j < adjacencyWidth; j++){
				adjacencySquare[j][i] = new AdjacencyPoint(gridWidth + j, gridHeight + i);
				
				/*
				 * Calculates the adjacency Point's distance from the grid right when the
				 * Point is initialized... It IS POSSIBLE to put these in the adj point CTOR,
				 *  but I would have to pass 2 more ints as arguments (gw,gh,j,i)
				 */
				adjacencySquare[j][i].calculateDistance(gridWidth, gridHeight);
				adjacencySquare[j][i].calculateDistancePercent(gridWidth, gridHeight);
				//adjacencySquare[j][i].setNumFactors(MathHelper.findNumFactors(n));
				//WTF am I supposed to be factoring?
			}
		}
	}
	
	private void sortPercentSeen(int increment){
		ArrayList<ArrayList<AdjacencyPoint>> pSeen = new ArrayList();
		
		for(int i = 0; i < 50/increment; i++){
			pSeen.add(new ArrayList<AdjacencyPoint>());
		}
		
		//For every Adjacency Point
		for(int i = 0; i < adjacencyHeight; i++){
			for(int j = 0; j < adjacencyWidth; j++){
				int index = ((int)adjacencySquare[j][i].getPercentSeen()/increment) % (50/increment);
				pSeen.get(index).add(adjacencySquare[j][i]);
			}
		}
		//Write the data to an excel .CSV file.
		ep.generatePercentSeenDiagram(pSeen, latticeGrid, gridWidth, gridHeight);
		
	}
	
	private void setAdjacencyGCD(AdjacencyPoint ap){
		ap.calculateGCD(MathHelper.gcd(ap.getCoord().getX()-gridWidth, ap.getCoord().getY()-gridHeight));
	}
	
	private void determineVisibility(LatticePoint lp, AdjacencyPoint ap){
		int x = lp.getCoord().getX();
		int y = lp.getCoord().getY();
		
		int a = ap.getCoord().getX();
		int b = ap.getCoord().getY();
		
		int m = gridWidth;
		int n = gridHeight;
		
		//System.out.println("Applying Corollaries...");
		
		//If the first corollary holds...
		if(MathHelper.gcd(a-x,b-y) > 1){
			//Go on to the next two
			if((m-x) >= ((a-x)/MathHelper.gcd(a-x,b-y))){
				
				if((n-y) >= ((b-y)/MathHelper.gcd(a-x,b-y))){
					//If all three of the corollaries hold then the point is viewable.
					//System.out.println(lp.getName() + " Fails all 3 Corollaries");
					ap.ignorePoint(lp);
					return;
				}else{
					//System.out.println(lp.getName() + " is visible");
				}
			}else{
				//System.out.println(lp.getName() + " Is visible");
			}
		}else{
			//System.out.println(lp.getName() + " Is visible");
		}
		//If all 3 corollaries dont hold then the point is visible.
		lp.seePoint();
		ap.seePoint(lp);
	}
	


	private void checkVisibility(boolean graph){
		
		//For every Adjacency Point
		for(int i = 0; i < adjacencyHeight; i++){
			for(int j = 0; j < adjacencyWidth; j++){
				
				setAdjacencyGCD(adjacencySquare[j][i]);
				
				//Check Every Lattice Point
				for(int l = 0; l < gridHeight; l++){
					for(int w = 0; w < gridWidth; w++){
						
						
						//System.out.println("For " + adjacencySquare[j][i].getName());
						determineVisibility(latticeGrid[w][l], adjacencySquare[j][i]);
						
					}
				}
				adjacencySquare[j][i].setPercentSeen(gridWidth, gridHeight);
				adjacencySquare[j][i].setPercentObscured(gridWidth, gridHeight);
				//System.out.println("Generating Graph Data...");
				if(graph){
					ep.generateGraphCSV(adjacencySquare[j][i],gridWidth, gridHeight);
				}
				
			}
		}		
	}
}
