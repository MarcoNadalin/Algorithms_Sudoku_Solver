/*

 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package solver;

import java.util.HashMap;
import java.util.Map;

import grid.SudokuGrid;

/**
 * Algorithm X solver for standard Sudoku.
 */
public class AlgorXSolver extends StdSudokuSolver
{    
	//HashMap which holds all the covered columns.
    private Map<Integer, Integer> deletedCols = new HashMap<Integer, Integer>();
    //HashMap which coveres all covered rows.
    private Map<Integer, Integer> deletedRows = new HashMap<Integer, Integer>();
    
    public AlgorXSolver() {
        
    } // end of AlgorXSolver()


    @Override
    public boolean solve(SudokuGrid grid) {
    	boolean solved = false;
    	super.initialiseVariables(grid);
    	//Create the exact cover matrix with the constraints.
		exactCoverMatrix = super.createExactCoverMatrix();	
		
		//Cover rows which correspond to the pre existing cells in the sudoku grid.
    	coverPreExistingRows(grid);
    	    	
    	//Solve the sudoku puzzle using algorithmX to solve the exact cover matrix
    	solved = algorithmX(grid);
    	//Convert the covered rows to the sudoku grid.
    	convertDeletedRowsToGrid(grid);
        return solved;
    }
    
    
    public boolean algorithmX(SudokuGrid grid) {
    	
    	//If the number of deleted columns is equal to the amount of columns in the 
    	//exact cover matrix, then a solution has been found.
    	if(deletedCols.size() == colDim) {
    		return true;
    	}
    	
    	//loop through all the rows on the exact cover matrix to find the 
    	//row corresponding to the next cell that hasnt been covered.
    	for(int row = 0; row < dimension; row++) {
    		for(int col = 0; col < dimension; col++) {
    			
    			int startingPos = getIndexFromExactCoverMatrix(row + 1, col + 1, 1);
    			int endingPos = getIndexFromExactCoverMatrix(row + 1, col + 1, dimension);
    			
    			//If the cell has not been already covered
    			if(!cellCovered(startingPos, endingPos)) {
    				
    				//loop through all the possible values for the cell.
    				//
    				for(int val = 1; val <= dimension; val++) {
    					int matrixRow = getIndexFromExactCoverMatrix(row + 1, col + 1, val);
    					//if the exact cover row which corresponds to Row,Col,Val is
    					//allowed to be covered, cover it and call algorithmX recursively. 
    					if(coverRowAndCols(matrixRow) && algorithmX(grid)) {
    						//convertDeletedRowsToGrid(grid);

    						return true;
    					//If its not allowed to be covered, or algorithmX returns false,
    					//uncover what was recently covered
    					} else {
    						//convertDeletedRowsToGrid(grid);
    						uncoverRowAndCols(matrixRow);
    					}
    				}
    				//Only returns false when it has looped through all possible
    				//values for a cell and find that all values dont meet
    				//all constraints.
    				return false;    				
    			}
    			
    			
    		}
    	}
    	
    	return false;
    }
    
    /**
     * @return true if the cell in the sudoku grid hasnt been covered
     * @param start, the row on the exact cover matrix where this cell begins
     * @param end, the row on the exact cover matrix where this cell ends.
     */
    private boolean cellCovered(int start, int end) {
    	
    	for(int row = start; row <= end; row++) {
    		if(deletedRows.containsKey(row)) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    /**
     * Coveres all the rows / cols which have been pre placed within the sudoku grid.
     * @param grid, A sudoku grid
     */
    private void coverPreExistingRows(SudokuGrid grid) {
    	
    	for(int row = 0; row < dimension; row++) {
    		for(int col = 0; col < dimension; col++) {
    			if(grid.getCellValue(row, col) != -1) {
    				int matrixVal = -1;
    				for(int i = 0; i < grid.getValidIntegers().length; i++) {
    					if(grid.getCellValue(row, col) == grid.getValidInteger(i)) {
    						matrixVal = i + 1;
    					}
    				}
    				
    				int matrixRow = getIndexFromExactCoverMatrix(row + 1, col + 1, matrixVal);
    				coverRowAndCols(matrixRow);
    			}
    		}
    	}
    	
    }

    
    /**
     * Converts the hashmap of deleted rows into a corresponding SudokuGrid
     * @param grid
     */
    private void convertDeletedRowsToGrid(SudokuGrid grid) {
    	for(int row = 0; row < rowDim; row++) {
    		
    		if(deletedRows.containsKey(row) == true) {
    			int matrixRow = deletedRows.get(row);
        		
        		int gridRow = convertMatrixRowToGridRow(matrixRow);
        		int gridCol = convertMatrixRowToGridCol(matrixRow);
        		int val = getValueOfMatrixRow(matrixRow);

        		grid.setCell(gridRow - 1, gridCol - 1, grid.getValidInteger(val - 1));
    		}  		
    		
    	}
    }
    
    /**
     * Given a row on the exact cover matrix, it covers the row and all corresponding
     * columns. 
     * Returns true if the row can be covered. returns false if it cannot be covered.
     * @param matrixRow
     * @return boolean to signify whether the row can be covered or not
     */
    private boolean coverRowAndCols(int matrixRow) {
    	/*
    	 * constraints[0] = matrixRow
    	 * constraints[1] = cellConstraint
    	 * constraints[2] = rowConstraint
    	 * constraints[3] = colConstraint
    	 * constraints[4] = boxConstraint
    	 */
		int constraints[] = {0,0,0,0};
		int count = -1;
		
		if(deletedRows.containsKey(matrixRow)) {
			return false;
		}
		
		for(int col = 0; col < colDim; col++) {
			if(exactCoverMatrix[matrixRow][col] == true) {
				if(deletedCols.containsKey(col) == false) {
					count++;
					for(int constraint = 0; constraint < constraints.length; constraint++) {
						if(count == constraint) {
							constraints[constraint] = col;
						}
					}
				} else {
					return false;
				}
			}
		}

		deletedRows.put(matrixRow, matrixRow);

		for(int index = 0; index < constraints.length; index++) {
			deletedCols.put(constraints[index], constraints[index]);
		}

		return true;
	}
    
    /**
     * Given a row on the exact cover matrix, it uncovers the row and all corresponding
     * columns. 
     * Returns true if the row can be uncovered. returns false if it cannot be uncovered.
     * @param matrixRow
     * @return boolean to signify whether the row can be uncovered or not
     */
    private boolean uncoverRowAndCols(int matrixRow) {
    	/*
    	 * constraints[0] = matrixRow
    	 * constraints[1] = cellConstraint
    	 * constraints[2] = rowConstraint
    	 * constraints[3] = colConstraint
    	 * constraints[4] = boxConstraint
    	 */
    	int constraints[] = {0,0,0,0};
		int count = -1;

		for(int col = 0; col < colDim; col++) {
			if(exactCoverMatrix[matrixRow][col] == true) {
				if(deletedCols.containsKey(col) == true) {
					count++;

					for(int constraint = 0; constraint < constraints.length; constraint++) {
						if(count == constraint) {
							constraints[constraint] = col;
						}
					}
				} else {
					return false;
				}
			}
		}

		//uncover the row matrixRow.
		deletedRows.remove(matrixRow, matrixRow);
		
		//Delete all the cols covered by the row matrixRow
		for(int index = 0; index < constraints.length; index++) {
			deletedCols.remove(constraints[index], constraints[index]);
		}

		return true;
	}
    
    
    /**
     * Given a matrixRow, it returns the corresponding row on the sudoku grid
     * @param matrixRow
     * @return the corresponding row on the sudoku grid.
     */
    private int convertMatrixRowToGridRow(int matrixRow) {
    	if(matrixRow != 0) {
    		return (int)(matrixRow / (int)Math.pow(dimension, 2)) + 1;
    	} else {
    		return 1;
    	}
    }
    

    /**
     * Given a matrix row, it returns the corresponding value of the cell.
     * @param matrixRow
     * @return integer corresponding to the value of the cell.
     */
    private int getValueOfMatrixRow(int matrixRow) {
    	return (matrixRow%dimension) + 1;
    }
    
    
    /**
     * Given a matrix row, it returns the corresponding column of the sudoku grid.
     * @param matrixRow
     * @return integer corresponding to the column of the grid
     */
    private int convertMatrixRowToGridCol(int matrixRow) { 
    	int boardSquared = dimension * dimension;
    	int a = matrixRow%boardSquared; 
    	int b = a/dimension;
  	
    	
    	if(a == 0) {
    		return 1;
    	} else {
    		return b + 1;
    	}

    }
} // end of class AlgorXSolver
