/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

 package grid;

 import java.io.*;


/**
 * Abstract class representing the general interface for a Sudoku grid.
 * Both standard and Killer Sudoku extend from this abstract class.
 */
public abstract class SudokuGrid
{

    /**
     * Load the specified file and construct an initial grid from the contents
     * of the file.  See assignment specifications and sampleGames to see
     * more details about the format of the input files.
     *
     * @param filename Filename of the file containing the intial configuration
     *                  of the grid we will solve.
     *
     * @throws FileNotFoundException If filename is not found.
     * @throws IOException If there are some IO exceptions when openning or closing
     *                  the files.
     */
    public abstract void initGrid(String filename)
        throws FileNotFoundException, IOException;


    /**
     * Write out the current values in the grid to file.  This must be implemented
     * in order for your assignment to be evaluated by our testing.
     *
     * @param filename Name of file to write output to.
     *
     * @throws FileNotFoundException If filename is not found.
     * @throws IOException If there are some IO exceptions when openning or closing
     *                  the files.
     */
    public abstract void outputGrid(String filename)
        throws FileNotFoundException, IOException;


    /**
     * Converts grid to a String representation.  Useful for displaying to
     * output streams.
     *
     * @return String representation of the grid.
     */
    public abstract String toString();


    /**
     * Checks and validates whether the current grid satisfies the constraints
     * of the game in question (either standard or Killer Sudoku).  Override to
     * implement game specific checking.
     *
     * @return True if grid satisfies all constraints of the game in question.
     */
    public abstract boolean validate();
    
    
    public abstract int getDimension();
    
    public abstract int[] getValidIntegers();
    
    public abstract void setEmpty(int row, int col);
    
    public abstract void setCell(int row, int col, int value);
    
    public abstract int getCellValue(int row, int col);
    
    public abstract int getValidInteger(int index);
    
    
    /**
     * Returns the grid array
     * @return 2D Array of integers which represents the grid.
     */
    public abstract Node[][] getGrid();
    
    
    public class Node {
    	private int value;
    	private int cageNumber;
    	private int cageVal;
    	private boolean cageValidated = false;
    	
    	public Node(int value) {
    		this.value = value;
    	}
    	
    	public Node(int value, int cageNumber, int cageVal) {
    		this.value = value;
    		this.cageNumber = cageNumber;
    		this.cageVal = cageVal;
    	}
    	
    	public int getValue() {
    		return this.value;
    	}
    	
    	public int getCageNumber() {
    		return this.cageNumber;
    	}
    	
    	public int getcageVal() {
    		return this.cageVal;
    	}
    	
    	public void setValue(int value) {
    		this.value = value;
    	}
    	
    	public boolean isCageValidated() {
    		return cageValidated;
    	}
    	
    	public void setCageValidated(boolean valid) {
    		this.cageValidated = valid;
    	}
    }

} // end of abstract class SudokuGrid
