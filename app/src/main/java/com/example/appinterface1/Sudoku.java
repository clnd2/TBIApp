package com.example.appinterface1;
// Sudoku.java
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class Sudoku extends AppCompatActivity {
    private int[][] board = new int[9][9];

    // Generate a valid solved Sudoku board using backtracking
    public void generateBoard() {
        // Initialize the board with zeros
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = 0; // Initially set all cells to 0
            }
        }

        solve(0, 0); // Start solving from the first cell
    }

    private boolean solve(int row, int col) {
        if (row == 9) {
            return true; // Reached the end of the board, solution found
        }

        // Move to the next column
        if (col == 9) {
            return solve(row + 1, 0); // Go to the next row
        }

        // Skip already filled cells
        if (board[row][col] != 0) {
            return solve(row, col + 1); // Skip filled cells
        }

        // Try numbers from 1 to 9
        for (int num = 1; num <= 9; num++) {
            if (isSafe(row, col, num)) {
                board[row][col] = num;

                // Recursively try to solve the next cell
                if (solve(row, col + 1)) {
                    return true;
                }

                // Backtrack if placing num did not lead to a solution
                board[row][col] = 0;
            }
        }

        return false; // No valid number found for this cell
    }


    // Check if it's safe to place a number in the cell
    private boolean isSafe(int row, int col, int num) {
        // Check if the number is already in the row
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num) {
                return false;
            }
        }

        // Check if the number is already in the column
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == num) {
                return false;
            }
        }

        // Check if the number is already in the 3x3 grid
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (board[i][j] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    // Remove numbers from the board to create a puzzle
    public void removeNumbers() {
        Random rand = new Random();

        // Let's remove a random number of cells
        int count = 40; // Remove 40 cells for the puzzle
        while (count > 0) {
            int row = rand.nextInt(9);
            int col = rand.nextInt(9);

            // If the cell is not already empty, remove the number
            if (board[row][col] != 0) {
                board[row][col] = 0;
                count--;
            }
        }
    }

    // Get the current board
    public int[][] getBoard() {
        return board;
    }

    // Print the board (for debugging purposes)
    public void printBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}
