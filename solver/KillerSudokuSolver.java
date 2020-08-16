/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;
import java.util.List;

import grid.SudokuGrid;

/**
 * Abstract class for common attributes or methods for solvers of Killer
 * Sudoku.
 * Note it is not necessary to use this, but provided in case you wanted to do
 * so and then no need to change the hierarchy of solver types.
 */
public abstract class KillerSudokuSolver extends SudokuSolver
{
	// we check if a possible number is already in a row
		protected boolean validateRow(int row, int number, SudokuGrid grid) {
			for (int i = 0; i < grid.getDimension(); i++)
				if (grid.getCellValue(row, i) == number)
					return true;

			return false;
		}

		// we check if a possible number is already in a column
		protected boolean validateCol(int col, int number, SudokuGrid grid) {
			for (int i = 0; i < grid.getDimension(); i++)
				if (grid.getCellValue(i, col) == number)
					return true;

			return false;
		}

		// we check if a possible number is in its 3x3 box
		protected boolean validateBlock(int row, int col, int number, SudokuGrid grid) {
			int r = row - row % (int) Math.sqrt(grid.getDimension());
			int c = col - col % (int) Math.sqrt(grid.getDimension());

			for (int i = r; i < r + (int) Math.sqrt(grid.getDimension()); i++)
				for (int j = c; j < c + (int) Math.sqrt(grid.getDimension()); j++)
					if (grid.getCellValue(i, j) == number)
						return true;

			return false;
		}
		
		protected boolean cageValid(int cageId, SudokuGrid grid, int number) {

			List<Integer> valuesOfNodes = new ArrayList<Integer>();
			int numOfNodes = 0;

			int cageSum = 0;


			for(int r = 0; r < grid.getDimension(); r++) {
				for(int c = 0; c < grid.getDimension(); c++) {
					if(grid.getGrid()[r][c].getCageNumber() == cageId) {
						cageSum = grid.getGrid()[r][c].getcageVal();
						valuesOfNodes.add(grid.getGrid()[r][c].getValue());
						numOfNodes++;
					} 
				}
			}
			
			
			//If contains duplicate value
			for(int i = 0; i < valuesOfNodes.size(); i++){
				if(valuesOfNodes.get(i) == number) {
					return false;
				}
			}

			int emptyCells = 0;
			//if cage has more than 1 empty space
			for(int i = 0; i < numOfNodes; i++) {
				if(valuesOfNodes.get(i) == -1) {
					emptyCells++;
				}
			}
			
			if(emptyCells > 1) {
				return true;
			}

			int sum = 0;
			//if cage sum does not add up correctly, return false.
			for(int i = 0; i < numOfNodes; i++) {
				if(valuesOfNodes.get(i) != -1) {
					sum = sum + valuesOfNodes.get(i);
				}			
			}
			
			sum += number;
			
			if(sum != cageSum) {
				return false;
			}

			return true;
		}


		// combined method to check if a number possible to a row,col position is ok
		protected boolean validate(int row, int col, int number, SudokuGrid grid) {
			return !validateRow(row, number, grid)  &&  !validateCol(col, number, grid)  &&  !validateBlock(row, col, number, grid);
		}
} // end of class KillerSudokuSolver
