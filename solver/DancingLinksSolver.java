/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import grid.SudokuGrid;


/**
 * Dancing links solver for standard Sudoku.
 */
public class DancingLinksSolver extends StdSudokuSolver
{    
    private Node headerColumnNode;
    private List<Node> solution;
    
    
    public DancingLinksSolver() {
    	solution = new LinkedList<>();
    } // end of AlgorXSolver()


    @Override
    public boolean solve(SudokuGrid grid) {
        //Implementation of the Algorithm X solver for standard Sudoku.
    	super.initialiseVariables(grid);
    	
    	exactCoverMatrix = super.createExactCoverMatrix();
    	exactCoverMatrix = super.updateExactCoverMatrix(exactCoverMatrix);
        headerColumnNode = createDancingLinksMatrix(exactCoverMatrix);

        return !algorX(0);
    } // end of solve()    

    /**
     * The algorithm X solver.
     * @param step The number of recursions called
     * @return True if the sudoku board has been solved.
     */
    private boolean algorX(int step) {
    	
    	//If all the columns have been covered, The sudoku grid has been solved. Return true.
        if (headerColumnNode.getRight() == headerColumnNode) {
            convertLinkedListToSolution(solution);
            return true;
        } else {
        	Node columnNode = null;
        	
        	int currLength = -1;
        	Node colNode = headerColumnNode.getRight();
            while (colNode != headerColumnNode) {
                if (colNode.getLength() < currLength || currLength == -1) {
                    currLength = colNode.getLength();
                    columnNode = colNode;
                }
                colNode = colNode.getRight();
            }
        	
            columnNode.cover();
            
            
            Node node = columnNode.getDown();
            while (node != columnNode) {
                solution.add(node);
                
                Node nodeRight = node.getRight();
                while(nodeRight != node) {
                    nodeRight.getColNode().cover();
                    
                    nodeRight = nodeRight.getRight();
                }

                algorX(step + 1);

                node = solution.remove(solution.size() - 1);
                columnNode = node.getColNode();
                
                Node nodeLeft = node.getLeft();
                while (nodeLeft != node) {
                    nodeLeft.getColNode().unCover();
                    
                    nodeLeft = nodeLeft.getLeft();
                }
                
                node = node.getDown();
            }
            columnNode.unCover();
        }
        return false;
    }


    private Node createDancingLinksMatrix(boolean[][] matrix) {
        int numberOfCols = matrix[0].length;

        Node headerNode = new Node("header");
        List<Node> columnNodes = new ArrayList<>();

        for (int i = 0; i < numberOfCols; i++) {
            Node columnNode = new Node(Integer.toString(i));
            columnNodes.add(columnNode);
            headerNode = (Node) headerNode.connectRight(columnNode);
        }
        
        headerNode = headerNode.getRight().getColNode();

        for (boolean[] value : matrix) {
            Node previous = null;
            
            for (int i = 0; i < numberOfCols; i++) {
                if (value[i]) {
                    Node columnNode = columnNodes.get(i);
                    Node newNode = new Node(columnNode);
                    
                    if (previous == null) {
                        previous = newNode;
                    }
                    
                    columnNode.getUp().connectDown(newNode);
                    previous = previous.connectRight(newNode);
                    columnNode.setLength(columnNode.getLength() + 1);
                }
            }
        }
        headerNode.setLength(numberOfCols);

        return headerNode;
    }

    private void convertLinkedListToSolution(List<Node> solution) {
    	
        for(Node node : solution) {
            Node rowColNode = node;
            int min = Integer.parseInt(rowColNode.getColNode().getID());
            
            Node temp = node.getRight();
            while(temp != node) {
                int val = Integer.parseInt(temp.getColNode().getID());
                if (val < min) {
                    min = val;
                    rowColNode = temp;
	            }
                
                temp = temp.getRight();
	        }
            int val = (Integer.parseInt(rowColNode.getRight().getColNode().getID()) % dimension) + 1;
	        grid.setCell(((Integer.parseInt(rowColNode.getColNode().getID()) / dimension)), 
	        		((Integer.parseInt(rowColNode.getColNode().getID()) % dimension)), 
	        		(grid.getValidInteger(val - 1)));	        
        }
    }

}

class Node {
    private Node left;
    private Node right;
    private Node up;
    private Node down;
    
    private Node column;
    
    private int length;
    private String id;
    
    Node() {
        left = this;
        right = this;
        up = this;
        down = this;
    }

    Node(Node colNode) {
        this();
        column = colNode;
    }
    
    Node(String id) {
    	left = right = up = down = this;
        length = 0;
        this.id = id;
        column = this;
    }
    
    void cover() {
        disconnectLeftRight();
        
        Node nodeDown = this.down;
        while (nodeDown != this) {
        	Node nodeRight = nodeDown.right;
            while (nodeRight != nodeDown) {
                nodeRight.disconnectUpDown();
                nodeRight.column.setLength(nodeRight.column.getLength() - 1);
                
                nodeRight = nodeRight.right;
            }
            
            nodeDown = nodeDown.down;
        }
    }

    void unCover() {
    	Node nodeUp = this.up;
        while (nodeUp != this) {
        	Node nodeLeft = nodeUp.left;
            while (nodeLeft != nodeUp) {
                nodeLeft.column.setLength(nodeLeft.column.getLength() + 1);
                nodeLeft.connectUpDown();
                
                nodeLeft = nodeLeft.left;
            }
            
            nodeUp = nodeUp.up;
        }
        connectLeftRight();
    }
    
    Node connectDown(Node node) {
        assert (this.column == node.column);
        node.down = this.down;
        node.down.up = node;
        node.up = this;
        this.down = node;
        return node;
    }

    Node connectRight(Node node) {
        node.right = this.right;
        node.right.left = node;
        node.left = this;
        this.right = node;
        return node;
    }

    void disconnectLeftRight() {
        this.left.right = this.right;
        this.right.left = this.left;
    }

    void connectLeftRight() {
        this.left.right = this.right.left = this;
    }

    void disconnectUpDown() {
        this.up.down = this.down;
        this.down.up = this.up;
    }

    void connectUpDown() {
        this.up.down = this.down.up = this;
    }
    
    public Node getLeft() {
    	return this.left;
    }
    
    public Node getRight() {
    	return this.right;
    }
    
    public Node getUp() {
    	return this.up;
    }
    
    public Node getDown() {
    	return this.down;
    }
    
    public Node getColNode() {
    	return this.column;
    }
    
    public String getID() {
    	return this.id;
    }
    
    public int getLength() {
    	return this.length;
    }
    
    public void setLength(int length) {
    	this.length = length;
    }
}
