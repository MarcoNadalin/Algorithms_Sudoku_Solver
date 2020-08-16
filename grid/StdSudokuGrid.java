/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.util.HashMap;


/**
 * Class implementing the grid for standard Sudoku.
 * Extends SudokuGrid (hence implements all abstract methods in that abstract
 * class).
 * You will need to complete the implementation for this for task A and
 * subsequently use it to complete the other classes.
 * See the comments in SudokuGrid to understand what each overriden method is
 * aiming to do (and hence what you should aim for in your implementation).
 */
public class StdSudokuGrid extends SudokuGrid
{
    private Node grid[][];
    private int validIntegers[];
    private int dimension = 0;

    public StdSudokuGrid() {
        super();
    } // end of StdSudokuGrid()


    /* ********************************************************* */


    @Override
    public void initGrid(String filename)
        throws FileNotFoundException, IOException
    {
    	try {
	        BufferedReader br = new BufferedReader(new FileReader(filename));
	        
	        //First line of input file is the dimensions of the grid.
	        dimension = Integer.parseInt(br.readLine());
	        grid = new Node[dimension][dimension];
	        fillGridWithNegativeInteger(grid, dimension);
	        
	        //Second line of input is the valid integers
	        String[] validInts = br.readLine().split(" ");
	        //store validInts into integer array validIntegers
	        validIntegers = new int[validInts.length];
	        for(int i = 0; i < validInts.length; i++ ) {
	        	validIntegers[i] = Integer.parseInt(validInts[i]);
	        }
	        
	        //read in the rest of the input file and insert the values into the grid array
	        String line;
	        while((line=br.readLine())!=null){
	            String[] coordinatesAndValue = line.split("\\s|,");
	            //The length of 'coordinatesAndValue' should only have three elements.
	            grid[Integer.parseInt(coordinatesAndValue[0])][Integer.parseInt(coordinatesAndValue[1])]
	            		= new Node(Integer.parseInt(coordinatesAndValue[2]));
	        }
	        
	        br.close();
    	}
        catch (FileNotFoundException e) {
        	e.printStackTrace();
        }
    	catch (IOException e) {
    		e.printStackTrace();
    	}
        
    } // end of initBoard()


    @Override
    public void outputGrid(String filename)
        throws FileNotFoundException, IOException
    {
    	try {
    		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
    		writer.write(this.toString());
    		writer.close();
    	} 
    	catch (FileNotFoundException e) {
         	e.printStackTrace();
        }
     	catch (IOException e) {
     		e.printStackTrace();
     	}
        
    } // end of outputBoard()


    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
		
		for(int x = 0; x < dimension; x++) {
			for(int y = 0; y < dimension; y++) {
				//If space in grid is -1 (or empty) print out a '-' to indicate its empty
				if(grid[x][y].getValue() == -1) {
					sb.append("-" + ",");
				} else {
					sb.append(grid[x][y].getValue() + ",");	
				}				
			}
			sb.append("\n");
		}		
        return sb.toString();
    } // end of toString()


    @Override
    public boolean validate() {

    	//Checks all Rows
    	for(int row = 0; row < dimension; row++) {
    		if(!isRowValid(row)) {
    			return false;
    		}
    	}
    	
    	//Checks all Cols
    	for(int col = 0; col < dimension; col++) {
    		if(!isColumnValid(col)) {
    			return false;
    		}
    	}
    	
    	//Checks if all integers are valid.
    	for(int row = 0; row < dimension; row++) {
    		for(int col = 0; col < dimension; col++) {
    			if(!integerIsWithinValidIntegers(grid[row][col].getValue())) {
    				return false;
    			}
    		}
    	}
    	
    	if(!allBlocksValid()) {
    		return false;
    	}
    	
        return true;
    } // end of validate()

    @Override
    public int getValidInteger(int index) {
    	if(index >= 0 && index < validIntegers.length) {
    		return validIntegers[index];
    	}
    	return -1;
    }
    
	@Override
	public void setEmpty(int row, int col) {
		this.grid[row][col].setValue(-1);		
	}


	@Override
	public void setCell(int row, int col, int value) {
		this.grid[row][col].setValue(value);			
	}
	
	@Override
	public int getCellValue(int row, int col) {
		return this.grid[row][col].getValue();			
	}
    
    @Override
    public int getDimension() {
    	return this.dimension;
    }
    
    @Override
    public int[] getValidIntegers() {
    	return this.validIntegers;
    }
    
    
    //////////////////////////////////////////////////////////////////////////////////////////
    //--------------------------------PRIVATE METHODS---------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////////    
    
    /*
     * Fill the grid with the integer -1 to indicate that this space on the grid is 'empty'
     */
    private void fillGridWithNegativeInteger(Node[][] grid, int dimensions) {
    	for(int x = 0; x < dimensions; x++) {
    		for(int y = 0; y < dimensions; y++) {
        		grid[x][y] = new Node(-1);
        	}
    	}
    }
    
    /*
     * Checks to see if the integer i exists within the array of valid integers. 
     * If not, it returns false.
     */
    private boolean integerIsWithinValidIntegers(int integer) {
    	for(int i = 0; i < validIntegers.length; i++) {
    		if(integer == -1 || integer == validIntegers[i]) {
    			return true;
    		}
    	}
    	//System.out.println(integer);
    	return false;
    }
    
    /*
     * Checks if a row is valid by checking if each row does not contain
     * duplicate values.
     */
    private boolean isRowValid(int row) {

    	for (int i = 0; i < dimension; i++) { 
    		for (int j = i + 1 ; j < dimension; j++) { 
    			if (grid[row][i].getValue() != -1 && grid[row][i].getValue() == grid[row][j].getValue()) { 
    				return false;
    			}
    		}
    	}

    	return true;
    }
    
    /*
     * Checks if a col is valid by checking if each col does not contain 
     * duplicate values.
     */
    private boolean isColumnValid(int col) {
    	
    	for (int i = 0; i < dimension; i++) { 
    		for (int j = i + 1 ; j < dimension; j++) { 
    			if (grid[i][col].getValue() != -1 && (grid[i][col].getValue() == grid[j][col].getValue())) { 
    				return false;
    			}
    		}
    	}

    	return true;
    }
    
    
    private boolean allBlocksValid() {
    	int blockSize = (int)Math.sqrt(dimension);
    	HashMap<Integer, Integer> blockValues = new HashMap<Integer, Integer>();
    	//Checks each block to see if all integers within a block are unique
    	for(int blockR = 0; blockR < blockSize; blockR+=blockSize) {
    		for(int blockC = 0; blockC < blockSize; blockC+=blockSize) {
    			
    			for(int r = blockR; r < (blockR + blockSize); r++) {
    				for(int c = blockC; c < (blockC + blockSize); c++) {
    					if(!blockValues.containsKey(grid[r][c].getValue())) {
    						if(grid[r][c].getValue() != -1) {
    							blockValues.put(grid[r][c].getValue(), grid[r][c].getValue());
    						}    						
    					} else {
    						return false;
    					}
    				}
    			}
    			blockValues.clear();
    		}
    	}
    	return true;
    }


	@Override
	public Node[][] getGrid() {
		return this.grid;
	}




} // end of class StdSudokuGrid
