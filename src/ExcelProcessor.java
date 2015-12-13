import java.io.*;
import java.util.*;

import org.apache.poi.xssf.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.*;

/*
 * TODO: Create a method of processing the data so that the excel file is written do AS THE DATA IS PROCESSED.
 * Hopefully that would eliminate the OutofMemoryError: GC overhead bs.
 * TODO: Maybe consider making all of these methods static, pass them the file to
 * be used, that way we can use a jfilechooser or something of that sort.
 */


public class ExcelProcessor {

	private PrintWriter viewedWriter;
	
	public ExcelProcessor(){
	}
	/**
	 * This is the XLSX version of the generate data function. It uses the 2007 Excel functionality
	 * which allows us to achieve over a million rows as opposed to 65500 rows.
	 * @param as
	 * @param gridWidth
	 * @param gridHeight
	 */
	public void  generateRawDataXLSX(AdjacencyPoint[][] as, int gridWidth, int gridHeight){
		Workbook wb = new XSSFWorkbook();
		 CreationHelper createHelper = wb.getCreationHelper();
		    Sheet sheet = wb.createSheet("new sheet");
		    int rowNum = 0;
		    
		    Row row = sheet.createRow(rowNum);
		    row.createCell(0).setCellValue(createHelper.createRichTextString("x"));
		    row.createCell(1).setCellValue(createHelper.createRichTextString("y"));
		    row.createCell(2).setCellValue(createHelper.createRichTextString("Distance from Grid"));
		    row.createCell(3).setCellValue(createHelper.createRichTextString("Seen"));
		    row.createCell(4).setCellValue(createHelper.createRichTextString("Not Seen"));
		    row.createCell(5).setCellValue(createHelper.createRichTextString("% Distance from Grid"));
		    row.createCell(6).setCellValue(createHelper.createRichTextString("% Seen"));
		    row.createCell(7).setCellValue(createHelper.createRichTextString("% Not Seen"));
		    row.createCell(8).setCellValue(createHelper.createRichTextString("GCD(x-m,y-n)"));
		    row.createCell(9).setCellValue(createHelper.createRichTextString("Number of Flags Within"));
		    
		    System.out.println("Beginning Excel Data Write...");
		    
		    for(int i = 0; i < as.length; i++){
				for(int j = 0; j < as[0].length; j++){
					//TODO: Figure out how to create the rows without fucking up...
					//System.out.println("Writing row " + rowNum + "...");
					rowNum++;
					Row apRow = sheet.createRow(rowNum);
					apRow.createCell(0).setCellValue(as[j][i].getCoord().getX());
				    apRow.createCell(1).setCellValue(as[j][i].getCoord().getY());
				    apRow.createCell(2).setCellValue(as[j][i].getDistance());
				    apRow.createCell(3).setCellValue(as[j][i].getNumViewedPoints());
				    apRow.createCell(4).setCellValue(as[j][i].getNumObscuredPoints());
				    apRow.createCell(5).setCellValue(as[j][i].getDistancePercent());
				    apRow.createCell(6).setCellValue(as[j][i].getPercentSeen());
				    apRow.createCell(7).setCellValue(as[j][i].getPercentObscured());
				    apRow.createCell(8).setCellValue(as[j][i].getGCD());
				    apRow.createCell(9).setCellValue(as[j][i].getNumFlags());
				}
		    }
		    
		    try {
		    	new File("C:\\Users\\lenovo\\Documents\\School\\College\\Lattice Point Research\\Generated Graphs\\[" + gridWidth + "x" + gridHeight + "]\\Viewed Graphs").mkdirs();
		    	// Write the output to a file
		    	File outputFile = new File("C:\\Users\\lenovo\\Documents\\School\\College\\Lattice Point Research\\Generated Graphs\\[" + gridWidth + "x" + gridHeight + "]\\[" + gridWidth + "x" + gridHeight + "] Data.xlsx");
			    FileOutputStream fileOut = new FileOutputStream(outputFile);
				wb.write(fileOut);
				fileOut.close();
				System.out.println("Grid Data written successfully...");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    
	}
	
	public void generateGraphCSV(AdjacencyPoint ap, int gridWidth, int gridHeight){
		new File("C:\\Users\\lenovo\\Documents\\School\\College\\Lattice Point Research\\Generated Graphs\\[" + gridWidth + "x" + gridHeight + "]\\Viewed Graphs").mkdirs();
		File outputFile = new File("C:\\Users\\lenovo\\Documents\\School\\College\\Lattice Point Research\\Generated Graphs\\[" + gridWidth + "x" + gridHeight + "]\\Viewed Graphs\\" + ap.getName() + ".csv");
		try {
			viewedWriter = new PrintWriter(outputFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//Output data for viewable points
		for(int i = 0; i < ap.getGraphData().size(); i++){
			viewedWriter.println(ap.getGraphData().get(i).getCoord().getX() + "," + ap.getGraphData().get(i).getCoord().getY());
		}
		//Output data for nonviewable points
		for(int i = 0; i < ap.getObscuredGraphData().size(); i++){
			viewedWriter.println(ap.getObscuredGraphData().get(i).getCoord().getX() + ", ," + ap.getObscuredGraphData().get(i).getCoord().getY());
		}
		
		System.out.println("Graph Data written Successfully to .CSV file...");
		viewedWriter.close();
	}
	
	/**
	 * This function takes an "organized" list of adjacency points which are sorted based on the Percent
	 * of Lattice Points they can see, and then writes them to a graph data file (.CSV). It also writes the
	 * whole lattice grid for reference.
	 * @param as - Sorted set of all adjacency points based on their percent of seen points
	 * @param lg - Lattice Grid.
	 * @param gridWidth
	 * @param gridHeight
	 */
	public void generatePercentSeenDiagram(ArrayList<ArrayList<AdjacencyPoint>> as, LatticePoint[][] lg, int gridWidth, int gridHeight){
		new File("C:\\Users\\lenovo\\Documents\\School\\College\\Lattice Point Research\\Generated Graphs\\[" + gridWidth + "x" + gridHeight + "]\\Viewed Graphs").mkdirs();
		File outputFile = new File("C:\\Users\\lenovo\\Documents\\School\\College\\Lattice Point Research\\Generated Graphs\\[" + gridWidth + "x" + gridHeight + "]\\" + gridWidth + "x" + gridHeight + " Percent Seen Diagram.csv");
		try {
			viewedWriter = new PrintWriter(outputFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//We finna use a StringBuilder to reduce overhead.
		StringBuilder sb = new StringBuilder();
		
		//currentCell is going to correspond to the Point Series we're writing to.
		int currentCell = 1;
		
		//Write the Lattice Grid to ONE Point Series
		for(int l = 0; l < gridHeight; l++){
			for(int w = 0; w < gridWidth; w++){
				sb.append(lg[w][l].getCoord().getX() + "," + lg[w][l].getCoord().getY());
				viewedWriter.println(sb.toString());
				sb.setLength(0);
			}
		}		
		
		/*
		 * This part of the code uses the advanced for loop, so that if I change the dimensions
		 * of the ArrayList (sorting by 5% instead of 10%) i dont need to change any of this code.
		 */
		for(ArrayList<AdjacencyPoint> a: as){
			
			//Increment currentCell so that a new Point Series is written to
			currentCell++;
	    	for(AdjacencyPoint ap: a){
	    
	    		sb.append(ap.getCoord().getX());
	    		
	    		//Add the appropriate number of commas for the respective Point Series.
	    		for(int i = 0; i < currentCell; i++){
	    			sb.append(",");
	    		}
	    		sb.append(ap.getCoord().getY());
	    		viewedWriter.println(sb.toString());
	    		
	    		//Reset the StringBuilder.
	    		sb.setLength(0);
	    	}
	    }
		
		//Close that bitch
		viewedWriter.close();
	}
}
