/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import grid.SudokuGrid;

/**
 * Abstract class for common attributes or methods for solvers of standard
 * Sudoku.
 * Note it is not necessary to use this, but provided in case you wanted to do
 * so and then no need to change the hierarchy of solver types.
 */
public abstract class StdSudokuSolver extends SudokuSolver
{
	protected SudokuGrid grid;
	protected boolean[][] exactCoverMatrix;
	protected int constraints = 4;
	protected int numOfValues = 0;
	
	protected int rowDim = 0;
	protected int colDim = 0;
    
	protected int dimension;
	protected int boxSize;
	protected int empty = -1;
	
    protected void initialiseVariables(SudokuGrid grid) {
    	this.grid = grid;
    	this.dimension = grid.getDimension();
    	this.boxSize = (int) Math.sqrt(dimension);
    	this.numOfValues = grid.getValidIntegers().length;
    	this.rowDim = dimension * dimension * numOfValues;
    	this.colDim = dimension * dimension * constraints;
    }
	

	protected boolean[][] createExactCoverMatrix() {
    	
        boolean[][] matrix = new boolean[rowDim][colDim];

        int head = 0;
        head = cellConstraint(matrix, head);
        head = rowConstraint(matrix, head);
        head = colConstraint(matrix, head);
        boxConstraint(matrix, head);
        
        //update exact cover matrix to reflect the sudoku grid
        
        
        return matrix;
    }
	
	protected boolean[][] updateExactCoverMatrix(boolean exactCover[][]) {
		
		boolean[][] matrix = exactCover;
		
		for (int row = 1; row <= dimension; row++) {
            for (int column = 1; column <= dimension; column++) {

            	int n = grid.getCellValue(row - 1, column - 1);
            	
            	for(int i = 0; i < grid.getValidIntegers().length; i++) {
            		if(n == grid.getValidInteger(i)) {
            			n = i + 1;
            			break;
            		}
            	}
            	
                if (n != empty) {
                    for (int num = 1; num <= grid.getDimension(); num++) {
                        if (num != n) {

                        	
                        	for(int col = 0; col < (dimension*dimension*constraints); col++) {
                        		matrix[getIndexFromExactCoverMatrix(row, column, num)][col] = false;
                        	}
                        	
                        }
                    }
                }
            }
        }
		
		return matrix;
	}
	
	 /**
     * Creates the box constraints for the exact cover matrix.
     * @param matrix
     * @param h the column where the box constraints begin in the exact cover matrix
     * @return the column where the box constraints end in the exact cover matrix.
     */
    protected int boxConstraint(boolean[][] matrix, int h) {
    	
        for (int row = 1; row <= dimension; row += boxSize) {
            for (int column = 1; column <= dimension; column += boxSize) {
                for (int n = 1; n <= dimension; n++, h++) {
                    for (int rowDelta = 0; rowDelta < boxSize; rowDelta++) {
                        for (int columnDelta = 0; columnDelta < boxSize; columnDelta++) {
                            int index = getIndexFromExactCoverMatrix(row + rowDelta, column + columnDelta, n);
                            matrix[index][h] = true;
                        }
                    }
                }
            }
        }
        return h;
    }
    
    /**
     * creates the column constraints for the exact cover matrix.
     * @param matrix
     * @param h the column where the col constraints begin on the exact cover matrix.
     * @return the column where the col constraints end in the exact cover matrix.
     */
    protected int colConstraint(boolean[][] matrix, int h) {
        for (int column = 1; column <= dimension; column++) {
            for (int n = 1; n <= dimension; n++, h++) {
                for (int row = 1; row <= dimension; row++) {
                    int index = getIndexFromExactCoverMatrix(row, column, n);
                    matrix[index][h] = true;
                }
            }
        }
        return h;
    }
    
	/**
	 * Creates the row constraints for the exact cover matrix.
	 * @param matrix
	 * @param h the column where the row constraints begin on the exact cover matrix
	 * @return the column where the row constraints end on the exact cover matrix.
	 */
    protected int rowConstraint(boolean[][] matrix, int h) {
        for (int row = 1; row <= dimension; row++) {
            for (int n = 1; n <= dimension; n++, h++) {
                for (int column = 1; column <= dimension; column++) {
                    int index = getIndexFromExactCoverMatrix(row, column, n);
                    matrix[index][h] = true;
                }
            }
        }
        return h;
    }

	/**
	 * Creates the cell constraints for the exact cover matrix/
	 * @param matrix
	 * @param h the column where the cell constraints begin on the exact cover matrix
	 * @return the column where the row constraints end on the exact cover matrix
	 */
    protected int cellConstraint(boolean[][] matrix, int h) {
        for (int row = 1; row <= dimension; row++) {
            for (int column = 1; column <= dimension; column++, h++) {
                for (int n = 1; n <= dimension; n++) {
                    int index = getIndexFromExactCoverMatrix(row, column, n);
                    matrix[index][h] = true;
                }
            }
        }
        return h;
    }
    
	/**
	 * Given a row, col and value, it returns the corresponding row on the exact cover matrix.
	 * @param row The row on the sudoku grid
	 * @param col The column on the sudoku grid
	 * @param num The value of the cell at (row,col) on the sudoku grid.
	 * @return
	 */
    protected int getIndexFromExactCoverMatrix(int row, int col, int num) {
    	int indexToReturn = ((col - 1) * dimension + (num - 1)) + (dimension * dimension * (row - 1));
        return indexToReturn;
    }
    
} // end of class StdSudokuSolver
