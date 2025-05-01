package com.example.otfapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;  // Use this import for Android colors
import java.util.ArrayList;
import java.util.Collections;

public class SudokuGame extends AppCompatActivity {

    private final int[][] solvedBoard = new int[9][9]; // Store the solved Sudoku board (correct solution)

    private final EditText[][] cells = new EditText[9][9];
    private final int[][] board = new int[9][9]; // Store the numbers in the board (including the empty ones)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        Button btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the home screen, or perform an action like going to a different Activity
                Intent intent = new Intent(SudokuGame.this, MainActivity.class);
                startActivity(intent);
            }
        });
        // Initialize the grid with EditTexts (cells are now editable)
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                EditText cell = new EditText(this);

                // Adjusting size of the cells
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0; // This makes it fill the available width proportionally
                params.height = 0; // Same for height
                params.rowSpec = GridLayout.spec(i, 1f); // Each row will take equal height
                params.columnSpec = GridLayout.spec(j, 1f); // Each column will take equal width


                // Apply the appropriate border (thicker for subgrid boundaries)
                if (i % 3 == 0 && j % 3 == 0) {
                    // Apply thicker border for 3x3 subgrid boundaries
                    cell.setBackgroundResource(R.drawable.cell_border_thick); // Thicker border for subgrid boundaries
                } else {
                    // Apply normal border to other cells
                    cell.setBackgroundResource(R.drawable.cell_border); // Normal border for other cells
                }


                cell.setLayoutParams(params);
                cell.setTextSize(18f); // Make text larger
                cell.setTextAlignment(EditText.TEXT_ALIGNMENT_CENTER);
                cell.setPadding(10, 10, 10, 10);
                cell.setBackgroundResource(R.drawable.cell_border); // Optional border if you created a cell_border drawable
                cell.setFocusable(true);
                cell.setClickable(true);
                cell.setInputType(android.text.InputType.TYPE_CLASS_NUMBER); // Only numbers allowed

                // Store the reference to the cell
                cells[i][j] = cell;

                // Add the cell to the grid
                gridLayout.addView(cell);
            }
        }

        // New game button setup (populate a new puzzle)
        Button btnNewGame = findViewById(R.id.btnNewGame);
        btnNewGame.setOnClickListener(v -> generateSudokuPuzzle());

        // Check solution button setup
        Button btnCheckSolution = findViewById(R.id.btnCheckSolution);
        btnCheckSolution.setOnClickListener(v -> checkSolution());

        // Initialize a random Sudoku puzzle
        generateSudokuPuzzle();
    }


    // Function to clear the grid before a new puzzle is generated
    private void clearGrid() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = 0; // Clear board
                cells[i][j].setText(""); // Clear cell text
                cells[i][j].setEnabled(true); // Enable editing
                cells[i][j].setBackgroundResource(R.drawable.cell_border); // Reset to default background
            }
        }
    }

    private boolean generateSudokuGrid() {
        return generateGrid(0, 0);
    }

    // Function to generate a fully filled valid Sudoku grid using backtracking
    private void generateSudokuPuzzle() {
        clearGrid(); // Ensure the grid is cleared first

        Sudoku sudoku = new Sudoku();  // Create an instance of the Sudoku class
        sudoku.generateBoard();  // Generate a solved Sudoku board

        int[][] solvedPuzzleBoard = sudoku.getBoard();  // Get the solved Sudoku board

        // Now copy the solved board to solvedBoard (this will be used for comparison later)
        for (int i = 0; i < 9; i++) {
            // Copy the solved board to solvedBoard
            System.arraycopy(solvedPuzzleBoard[i], 0, solvedBoard[i], 0, 9);
        }

        sudoku.removeNumbers();  // Remove numbers to create a puzzle

        // Now populate the board with the puzzle version (with some numbers removed)
        int[][] puzzleBoard = sudoku.getBoard();  // Get the puzzle board after removing some numbers

        // Copy the puzzle state into the game board
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = puzzleBoard[i][j]; // Copy the puzzle state into the board

                if (puzzleBoard[i][j] != 0) {
                    // If it's a pre-filled number, disable editing and set the number
                    cells[i][j].setText(String.valueOf(puzzleBoard[i][j]));
                    cells[i][j].setEnabled(false);  // Disable editing for pre-filled numbers
                } else {
                    // For empty cells, enable editing
                    cells[i][j].setText("");  // Keep it empty
                    cells[i][j].setEnabled(true);  // Enable editing for empty cells
                }
            }
        }
    }









    // Backtracking function to fill the grid
    private boolean generateGrid(int row, int col) {
        if (row == 9) {
            return true; // Finished all rows
        }

        if (col == 9) {
            return generateGrid(row + 1, 0); // Move to the next row
        }

        // Try all numbers from 1 to 9
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers); // Shuffle to ensure randomness

        for (int num : numbers) {
            if (isSafe(row, col, num)) {
                board[row][col] = num;
                cells[row][col].setText(String.valueOf(num));
                cells[row][col].setEnabled(false); // Disable pre-filled cells
                if (generateGrid(row, col + 1)) {
                    return true;
                }
                board[row][col] = 0; // Backtrack
            }
        }

        return false; // No valid number found
    }

    // Function to check if placing num at (row, col) is valid
    private boolean isSafe(int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false; // Check row and column
            }
        }

        // Check 3x3 subgrid
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

    // Function to remove some numbers to create a puzzle
    private void removeNumbersForPuzzle() {
        int cellsToRemove = 45; // Number of cells to leave empty (half the grid)
        while (cellsToRemove > 0) {
            int row = (int) (Math.random() * 9);
            int col = (int) (Math.random() * 9);
            if (board[row][col] != 0) {
                board[row][col] = 0;
                cells[row][col].setText("");
                cells[row][col].setEnabled(true); // Allow user to edit this cell
                cellsToRemove--;
            }
        }
    }

    // Function to check if the user's solution is correct
    private void checkSolution() {
        boolean isCorrect = true;

        // Iterate through each cell to check if user input matches the correct number
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String input = cells[i][j].getText().toString().trim(); // Get the input from the user, trimming whitespace

                // Only check cells that have user input and are not empty
                if (!input.isEmpty()) {  // Only check if the cell is not empty
                    try {
                        int userInput = Integer.parseInt(input); // Parse user input to integer

                        // Log both the user input and the expected value from the solved board for comparison
                        Log.d("SudokuGame", "Comparing: User Input = " + userInput + " vs Solved Board Value = " + solvedBoard[i][j]);

                        // Compare user input to the correct value from the solved board
                        if (userInput == solvedBoard[i][j]) {
                            // Correct answer, mark it as green
                            cells[i][j].setBackgroundColor(Color.GREEN); // Correct answer, color green
                        } else {
                            // Incorrect answer, mark it as red
                            cells[i][j].setBackgroundColor(Color.RED); // Incorrect answer, color red
                            isCorrect = false; // Mark as incorrect
                        }
                    } catch (NumberFormatException e) {
                        // If the input is not a valid number, mark it as incorrect
                        cells[i][j].setBackgroundColor(Color.RED); // Incorrect, color red
                        isCorrect = false; // Mark as incorrect
                    }
                } else {
                    // If the cell is empty (no user input), reset the background color to default
                    cells[i][j].setBackgroundResource(R.drawable.cell_border); // Default border
                }
            }
        }

        // If the board is completely filled, check if the solution is correct
        if (isBoardFull()) {
            if (isCorrect) {
                // Display a "Game Over: You solved it correctly!" message
                Toast.makeText(this, "🎉 Game Over: You solved it correctly!", Toast.LENGTH_LONG).show();
            } else {
                // Display a "Game Over: Incorrect solution." message
                Toast.makeText(this, "❌ Game Over: Incorrect solution.", Toast.LENGTH_LONG).show();
            }

            // Lock the board by disabling all cells (game over)
            disableAllCells();
        } else {
            // If the board is not full, display appropriate message based on correctness
            if (isCorrect) {
                Toast.makeText(this, "So far so good!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Some answers are incorrect. Keep going!", Toast.LENGTH_SHORT).show();
            }
        }
    }



    // Helper method to disable all cells (lock the board when game is over)
    private void disableAllCells() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j].setEnabled(false); // Disable editing of all cells
            }
        }
    }






    private boolean isBoardFull() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (cells[i][j].getText().toString().trim().isEmpty()) {
                    return false; // Found an empty cell
                }
            }
        }
        return true; // No empty cells found
    }





    // Example method to handle user input on a given row and column
    public void handleUserInput(int userInput, int row, int col) {
        // Ensure userInput is not 0
        if (userInput != 0) {
            boolean isCorrect = isValidInput(userInput, row, col);

            // Update the UI or give feedback based on the result
            if (isCorrect) {
                updateCellColor(row, col, true);  // Correct, color green
                System.out.println("Correct!");
            } else {
                updateCellColor(row, col, false); // Incorrect, color red
                System.out.println("Incorrect, try again.");
            }
        } else {
            System.out.println("Please enter a valid number.");
        }
    }

    // Validation method that compares user input with the board value
    public boolean isValidInput(int userInput, int row, int col) {
        int boardValue = board[row][col];  // Get the value from the board

        // If the cell is empty (board value is 0), allow any user input
        if (boardValue == 0) {
            return true;  // Allow user to input any value in an empty space
        }

        // Otherwise, check if the user's input matches the board value
        return userInput == boardValue;
    }



    // Update UI (assuming you have a way to access the GUI component for the cell)
    // Update the color of the cell
    public void updateCellColor(int row, int col, boolean isCorrect) {
        if (isCorrect) {
            // Set background color to green if the answer is correct
            cells[row][col].setBackgroundColor(Color.GREEN);
        } else {
            // Set background color to red if the answer is incorrect
            cells[row][col].setBackgroundColor(Color.RED);
        }
    }


}
