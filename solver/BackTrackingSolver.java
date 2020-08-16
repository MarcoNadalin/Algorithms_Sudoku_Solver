/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;


import grid.SudokuGrid;


/**
 * Backtracking solver for standard Sudoku.
 */
public class BackTrackingSolver extends StdSudokuSolver
{
    // TODO: Add attributes as needed.

    public BackTrackingSolver() {
 
    } // end of BackTrackingSolver()


    @Override
    public boolean solve(SudokuGrid grid) {
    	
    	//Loop through all cells within the grid.
    	for (int row = 0; row < grid.getDimension(); row++) {
            for (int col = 0; col < grid.getDimension(); col++) {
            	 /* Only modify the value of a cell if its original value is -1
            	 * Check if the value of the current cell is -1 (empty)
            	 */
                if (grid.getCellValue(row, col) == -1) {
                	 /* Increment the cells value to the next valid integer.
                	 */
                    for (int val = 0; val < grid.getValidIntegers().length; val++) {
                    	grid.setCell(row, col, grid.getValidInteger(val));                    	
                    	/* check if the grid is valid. If it is, call the solve method recursively.
                    	 * If the validate method does not return true, increment
                    	 * the current cell to the next valid integer.
                    	 */
                        if (grid.validate() && solve(grid)) {
                            return true;
                        }
                        grid.setEmpty(row, col);
                    }
                    return false;
                }
                
            }
        }
        return true;
    } // end of solve()
} // end of class BackTrackingSolver()
