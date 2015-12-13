import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MathHelper {
	
	public static double decimalToPercent(double dec){
		return round(dec*100,5);
	}
	
	/**
	 * This function checks to see how many numbers from 1...m are relatively prime to i
	 * @param i
	 * @param m
	 * @return - Quantity of numbers that are relatively prime to i, between 1...m
	 */
	public static int AdjustedPhi(int i, int m){
		int numTotients = 0;
		
		for(int j = 1; j <= m; ++j){
			if(gcd(i,j) == 1){
				numTotients++;
			}
		}
		return numTotients;
	}
	/**
	 * This function Sums up all the totients generated in a range, using the adjusted  phi function
	 * SEE COROLLARY 3.3 IN NEIL AND REBECCA'S PAPER!
	 * @param n - GridHeight
	 * @param m - GridWidth
	 * @return - number of Totients
	 */
	public static int AdjPhiSummation(int n, int m){
		int numTotients = 0;
		
		for(int i = 1; i <= n; ++i){
			numTotients += AdjustedPhi(i,m);
		}
		
		return numTotients;
	}
	
	/**
	 * See Corollary 3.5...
	 * @param a - Point's x-coordinate
	 * @param b - Points's y-coordinate
	 * @param m - grid width
	 * @param n - grid height
	 * @return - Lower bound for the number of points NOT seen by P(a,b)
	 */
	public static int generalizedNonViewableBound(int a, int b, int m, int n){
		
		int numPointsNotSeen = 0;
		
		for(int i = 1; i <= n; ++i){
			numPointsNotSeen += (AdjustedPhi(b-i,a-1)-AdjustedPhi(b-i,a-m-1));
		}
		
		return numPointsNotSeen;
	}
	
	/**
	 * @param n - Number to be rounded
	 * @param numPlaces - number of decimal places to round to
	 * @return - n, rounded to numPlaces
	 */
	public static double round(double n, int numPlaces){
		n = (n*(Math.pow(10, numPlaces)))+0.5;
		n = (int)n;
		n=(double)n;
		n /= Math.pow(10, numPlaces);
		
		return n;
	}
	
	/**
	 * Basic Distance Formula, given 2 points in a cartesian coordinate system
	 * @param c1 - Cartesian Coordinate 1
	 * @param c2 - Cartesian Coordinate 2 
	 * @return - The Distance between the two points (double)
	 */
	public static double calculateDistance(Coordinate c1, Coordinate c2){
		
		int x1 = c1.getX();
		int x2 = c2.getX();
		int y1 = c1.getY();
		int y2 = c2.getY();
		
		return Math.sqrt((Math.pow((x2-x1),2))+(Math.pow((y2-y1),2)));
	}
	
	/**
	 * Basic Distance Formula, given 2 x,y pairs
	 * @param x1 - Grid Width to give us the outermost point
	 * @param y1 - Grid Height to give us the outermost point
	 * @param x2 - X-Coord of our adjacency point
	 * @param y2 - Y-Coord of our adjacency point
	 * @return
	 */
	public static double calculateDistance(int x1, int y1, int x2, int y2){
		
		return Math.sqrt((Math.pow((x2-x1),2))+(Math.pow((y2-y1),2)));
	}
	
	public static boolean checkPrime(int n){
		boolean prime = true;
		int maxPrimeDivisor;
		//Find our max divisor
		double temp = Math.sqrt(n);
		maxPrimeDivisor = (int)temp;
		
		divisibilityCheck:
		for(int j = 2; j <= maxPrimeDivisor; j++){
			
			//If it passes the divisibility check then we know its composite
			if(checkDivisibility(n,j)){
				//COMPOSITE NUMBER
				prime = false;
				break divisibilityCheck;
			}
		}
		return prime;
	}
	
	public static int findNumFactors(int n){
	
		int numFactors = 1; // 1 is a factor;
		for(int i = 2; i <= n; i++){
			if(checkDivisibility(n,i)){
				numFactors++;
			}
		}
		
		return numFactors;
	}
	
	
	public static int findNumPrimeFactors(int n){
		int numPrimeFactors = 0;
		
		//Start at 2 because 1 is neither prime nor composite...
		for(int i = 2; i <= n; i++){
			if(checkDivisibility(n,i)){
				if(checkPrime(i)){
					numPrimeFactors++;
				}
			}
		}
		return numPrimeFactors;
	}
	
	/**
	 * Method that Takes two integers and determines their Greatest Common Divisor.
	 * @param n1
	 * @param n2
	 * @return The Greatest Common Divisor of n1 and n2
	 */
	public static int gcd(int n1, int n2){
		
		//initialize required variables
		int small = 0;
		int big = 0;
		int gcd = 0;
		ArrayList<Integer> smallFactors = new ArrayList();
		Map<Integer, Integer> bigFactors = new HashMap();
		
		//determine which one of those int's is the smaller of the two
		if(n1 > n2){
			small = n2;
			big = n1;
		}else{
			small = n1;
			big = n2;
		}
		
		//Find the factors of both big and small ints
		for(int i = 0; i < small; i++){
			if(checkDivisibility(small, i+1)){
				smallFactors.add(i+1);
			}
		}
		
		for(int i = 0; i < small; i++){
			if(checkDivisibility(big, i+1)){
				bigFactors.put(i+1, i+1);
			}
		}
		
		//At this point, all of the factors for both numbers are stored.
		
		for(int i = smallFactors.size(); i > 0; i--){
			
			//Store the current factor
			int fac = smallFactors.get(i-1);
			
			//If the small factor is found to also be a factor of the big number
			if(bigFactors.get(fac) != null){
				//System.out.println("The GCD is: " + fac);
				return fac;
			}
		}
		//Default return in case something goes horribly wrong. (Or if the GCD is 0)
		return gcd;
	}
	
	/**
	 * This function checks to see if divisor|num
	 * @param num
	 * @param divisor
	 * @return boolean value, dependent on whether divisor|num
	 */
	public static boolean checkDivisibility(int num, int divisor){
		
		double result = ((double)num)/divisor;
		
		//If the two numbers dont divide evenly it will spit out a new number.
		if(result != ((long)(result + 0.99999))){
			return false;
		}else{
			return true;
		}
	}
	
	public static double calculateDistanceFromGridPercent(int x, int y, int r, int s){
		return decimalToPercent(calculateDistance(x,y,r,s)/(Math.sqrt(2)*(r-1)));
	}
}
