/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;
import java.util.List;

import grid.SudokuGrid;


/**
 * Backtracking solver for Killer Sudoku.
 */
public class KillerAdvancedSolver extends KillerSudokuSolver
{
	private int numCages = 0;
	private boolean init = false;
	
	public KillerAdvancedSolver() {

	} // end of KillerBackTrackingSolver()


	@Override
	public boolean solve(SudokuGrid grid) {
		init(grid);
		
		for(int cage = 0; cage < numCages; cage++) {
			for (int row = 0; row < grid.getDimension(); row++) {
				for (int col = 0; col < grid.getDimension(); col++) {
					// we search an empty cell
					if (grid.getCellValue(row, col) == -1 && grid.getGrid()[row][col].getCageNumber() == cage) {
						// we try possible numbers
						for (int number = 0; number < grid.getDimension(); number++) {
							if (validate(row, col, grid.getValidInteger(number), grid) && cageValid(cage, grid, grid.getValidInteger(number))) {
								// number ok. it respects sudoku constraints
								grid.setCell(row, col, grid.getValidInteger(number));
								if (solve(grid)) { // we start backtracking recursively
									return true;
								} else { // if not a solution, we empty the cell and we continue
									grid.setCell(row, col, -1);
								}
							}
						}

						return false; // we return false
					}
				}
			}
		}
		return true; // sudoku solved

	} // end of solve()

	private void init(SudokuGrid grid) {
		if(init == false) {
			int numCage = 0;
			for(int row = 0; row < grid.getDimension(); row++) {
				for(int col = 0; col < grid.getDimension(); col++) {
					if(grid.getGrid()[row][col].getCageNumber() > numCage) {
						numCage = grid.getGrid()[row][col].getCageNumber();
					}
				}
			}
			
			this.numCages = numCage + 1;
		}
	}


} // end of class KillerBackTrackingSolver()
