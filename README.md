# Algorithms_Sudoku_Assignment
This is a Java based Sudoku solver completed for an RMIT assignment. Within the Sudoku solver, there are 5 algorithms. 
The algorithms are:
- AlgorX
- Backtracking
- Dancing Links
- Backtracking killer sudoku
- Advanced killer sudoku

# Compiling and Executing
To compile the files, run the following command from the root directory (the directory that RmitSudokuTester.java is in):
    javac \*.java grid\/\*.java solver \/\*.java

To run the framework:
    java RmitSudokuTester [puzzle fileName] [game type] [solver type] [visualisation] <output fileName>
where,
    * puzzle fileName: name of file containing the input puzzle / grid to solve
    * game type: type of sudoku game, one of {sudoku, killer}
    * solver type: type of solver to use.
        * if standard sudoku is specified (sudoku), then solver should be one of {backtracking, algorx, dancing}
        * if killer sudoku is specified (killer) then solver should be one of the following {backtracking, advanced}
    * visualisation: whether to output the grid before and after solving, one of {n, y}
    output fileName: (optional) if specified, the solved grid will be outputted to this file.

# Details of puzzle files (input)
Examples of input files can be found in the project.
# Standard Sudoku
The exact format for standard sudoku is as follows:
[size or dimensions of puzzle]
[list of valid symbols]
[tuples of row, column value, one tuple per line]
For instance, for the tuple
    0,0 1
Means that there is a value 1 in cell (r = 0, C = 0)

# Killer Sudoku
The exact format for killer sudoku is as follows
[size or dimensions of puzzle]
[list of valis symbols]
[number of cages]
[total of cage, list of row, column for each cage, one per line]
