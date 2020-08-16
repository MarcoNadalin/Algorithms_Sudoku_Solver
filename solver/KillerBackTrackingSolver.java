/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import grid.SudokuGrid;


/**
 * Backtracking solver for Killer Sudoku.
 */
public class KillerBackTrackingSolver extends KillerSudokuSolver
{
	public KillerBackTrackingSolver() {
		
	}

	@Override
	public boolean solve(SudokuGrid grid) {
		for (int row = 0; row < grid.getDimension(); row++) {
			for (int col = 0; col < grid.getDimension(); col++) {
				// we search an empty cell
				if (grid.getCellValue(row, col) == -1) {
					// we try possible numbers
					for (int number = 0; number < grid.getDimension(); number++) {
						if (validate(row, col, grid.getValidInteger(number), grid) 
								&& cageValid(grid.getGrid()[row][col].getCageNumber(), grid, grid.getValidInteger(number))) {
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
	return true; // sudoku solved
	} // end of solve()
} // end of class KillerBackTrackingSolver()
