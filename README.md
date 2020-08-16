# Algorithms_Sudoku_Assignment
This is a Java based Sudoku solver completed for an RMIT assignment. Within the Sudoku solver, there are 5 algorithms. 
    - Backtracking
    - AlgorX
    - Dancing Links
    - Backtracking Killer Sudoku Solver
    - Advanced Killer Sudoku Solver.

# How to Use
To compile the files, run the following command from the root directory (the directory
that RmitSudokuTester.java is in):
    javac *.java grid/*.java solver/*.java


To run the framework:
java RmitSudokuTester [puzzle fileName] [game type] [solver type] [visualisation] <output fileName>
where,
    * puzzle fileName: name of file containing the input puzzle/grid to solve.
    * game type: type of sudoku game, one of {sudoku, killer}.
    * solver type: Type of solver to use, depends on the game type.

    - If (standard) Sudoku is specifed (sudoku), then solver should be one of {backtracking,
      algorx, dancing}, where backtracking is the backtracking algorithm for stan-
      dard Sudoku, algorx and dancing are the exact cover approaches for standard
      Sudoku.
    - If Killer Sudoku is specifed (killer), then solver should be one of
      {backtracking, advanced}, where backtracking is the backtracking algorithm
      for Killer Sudoku and advanced is the most effcient algorithm you can devise
      for solving Killer Sudoku.
    - visualisation: whether to output grid before and another after solving, one of {n , y}.
    - output fileName: (optional) If specifed, the solved grid will be outputted to this file. 

# Puzzle file (input)
# Standard Sudoku (input) The exact format for standard Sudoku is as follows:
[size / dimensions of puzzle]
[list of valid symbols]
[tuples of row, column value, one tuple per line]
For instance, for the tuple
0,0 1
means there is a value 1 in cell (r = 0, c = 0).
Using the example from Figure 1a, the frst few lines of the input file corresponding
to this example would be:
9
1 2 3 4 5 6 7 8 9
0,0 5
0,1 3
0,4 7
1,0 6
1,3 1
. . .

# Killer Sudoku (input) The exact format for Killer Sudoku is as follows:
[size / dimensions of puzzle]
[list of valid symbols ]
[number of cages]
[Total of cages, list of row , column for each cage , one per line]
Using the example from Figure 2a, the first few lines of file corresponding to this
example would be:
9
1 2 3 4 5 6 7 8 9
29
3 0,0 0,1
15 0,2 0,3 0,4
