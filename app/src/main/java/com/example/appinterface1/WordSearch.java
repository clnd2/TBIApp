package com.example.appinterface1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WordSearch extends AppCompatActivity {

    private GridLayout gridLayout;
    private TextView wordListTextView, scoreTextView;
    private int score = 0;
    private char[][] letterGrid = new char[10][10]; // 10x10 grid for the word search
    private List<String> wordsToFind = new ArrayList<>();
    private List<String> foundWords = new ArrayList<>();
    private List<Button> selectedButtons = new ArrayList<>(); // To keep track of selected buttons

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_search);

        gridLayout = findViewById(R.id.gridLayout);
        wordListTextView = findViewById(R.id.wordListTextView);
        scoreTextView = findViewById(R.id.scoreTextView);


        // Randomly select 5 words for the game
        List<String> randomWords = getRandomWords(getAllWords(), 3);

        // Display the randomly selected words
        wordListTextView.setText("Words to find: " + String.join(", ", randomWords));

        // Set the selected words as the new list of wordsToFind
        wordsToFind = randomWords;

        // Initialize the grid with random letters
        initializeGrid();

        // Place words in the grid
        placeWordsInGrid();

        // Populate the grid layout with buttons
        populateGridLayout();

        Button restartButton = findViewById(R.id.restartButton);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame(); // Call the restartGame() method without any arguments
            }
        });

    }
    private List<String> getAllWords() {
        List<String> words = new ArrayList<>();
        words.add("APPLE");
        words.add("BANANA");
        words.add("OCEAN");
        words.add("LION");
        words.add("ZEBRA");
        words.add("BIRD");
        words.add("MONKEY");
        words.add("FIRE");
        words.add("WATER");
        words.add("EARTH");
        words.add("SUN");
        words.add("MOON");
        words.add("STARS");
        words.add("RIVER");
        words.add("DESERT");
        words.add("JUNGLE");
        words.add("FOREST");
        words.add("SNOW");
        words.add("ICE");
        words.add("FROST");
        words.add("TIGER");
        words.add("SHARK");
        words.add("OXYGEN");
        words.add("GARDEN");
        words.add("HOUSE");
        words.add("KEY");
        words.add("CAR");
        words.add("SCHOOL");
        words.add("PENCIL");
        words.add("TEACHER");
        words.add("STUDENT");
        words.add("PIZZA");
        words.add("BURGER");
        words.add("CAKE");
        words.add("COFFEE");
        words.add("TEA");
        words.add("SWEET");
        words.add("SALTY");
        words.add("HOT");
        words.add("COLD");
        words.add("GUITAR");
        words.add("PIANO");
        words.add("VIOLIN");
        words.add("DRUM");
        words.add("BASS");
        words.add("BAND");
        words.add("MUSIC");
        return words;
    }


    // Method to get a specified number of random words from a list
    private List<String> getRandomWords(List<String> words, int count) {
        List<String> randomWords = new ArrayList<>();
        Random random = new Random();

        // Ensure count is not greater than the number of words available
        int wordsCount = Math.min(count, words.size());

        // Shuffle the list to randomize the order
        Collections.shuffle(words, random);

        // Select the first 'wordsCount' words from the shuffled list
        for (int i = 0; i < wordsCount; i++) {
            randomWords.add(words.get(i));
        }

        return randomWords;
    }


    private void initializeGrid() {
        // Initialize the grid with empty spaces
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                letterGrid[i][j] = ' ';  // Empty space
            }
        }
    }



    // Place words in the grid, either horizontally or vertically
    // After all words have been placed, fill in the remaining spaces with random letters
    private void placeWordsInGrid() {
        for (String word : wordsToFind) {
            placeWord(word);
        }

        // Now that words are placed, fill the remaining empty spaces with random letters
        populateGridWithRandomLetters();
    }

    private boolean canPlaceWord(String word, int startRow, int startCol, int direction) {
        if (direction == 0) { // Horizontal
            if (startCol + word.length() > 10) return false;  // Check if the word fits
            for (int i = 0; i < word.length(); i++) {
                if (letterGrid[startRow][startCol + i] != ' ' && letterGrid[startRow][startCol + i] != word.charAt(i)) {
                    return false;
                }
            }
        } else if (direction == 1) { // Vertical
            if (startRow + word.length() > 10) return false;
            for (int i = 0; i < word.length(); i++) {
                if (letterGrid[startRow + i][startCol] != ' ' && letterGrid[startRow + i][startCol] != word.charAt(i)) {
                    return false;
                }
            }
        } else if (direction == 2) { // Diagonal (Top-left to Bottom-right)
            if (startRow + word.length() > 10 || startCol + word.length() > 10) return false;
            for (int i = 0; i < word.length(); i++) {
                if (letterGrid[startRow + i][startCol + i] != ' ' && letterGrid[startRow + i][startCol + i] != word.charAt(i)) {
                    return false;
                }
            }
        } else if (direction == 3) { // Diagonal (Top-right to Bottom-left)
            if (startRow + word.length() > 10 || startCol - word.length() < -1) return false;
            for (int i = 0; i < word.length(); i++) {
                if (letterGrid[startRow + i][startCol - i] != ' ' && letterGrid[startRow + i][startCol - i] != word.charAt(i)) {
                    return false;
                }
            }
        }
        return true;
    }

    // Place a single word in the grid
    private void placeWord(String word) {
        Random random = new Random();
        boolean placed = false;

        while (!placed) {
            // Randomly choose horizontal, vertical, or diagonal placement
            int direction = random.nextInt(4); // 0 = horizontal, 1 = vertical, 2 = diagonal (top-left to bottom-right), 3 = diagonal (top-right to bottom-left)

            // Randomly choose starting point for the word
            int startRow = random.nextInt(10);
            int startCol = random.nextInt(10);

            // Check if the word can be placed at the starting position and direction
            if (canPlaceWord(word, startRow, startCol, direction)) {
                // Place the word in the grid
                for (int i = 0; i < word.length(); i++) {
                    if (direction == 0) { // Horizontal
                        letterGrid[startRow][startCol + i] = word.charAt(i);
                    } else if (direction == 1) { // Vertical
                        letterGrid[startRow + i][startCol] = word.charAt(i);
                    } else if (direction == 2) { // Diagonal (top-left to bottom-right)
                        letterGrid[startRow + i][startCol + i] = word.charAt(i);
                    } else if (direction == 3) { // Diagonal (top-right to bottom-left)
                        letterGrid[startRow + i][startCol - i] = word.charAt(i);
                    }
                }
                placed = true; // Successfully placed the word
            }
        }
    }





    private void populateGridWithRandomLetters() {
        Random random = new Random();

        // Fill the grid with random letters
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (letterGrid[row][col] == ' ') {
                    letterGrid[row][col] = (char) ('A' + random.nextInt(26));  // Random letter
                }
            }
        }
    }






    // Populate the grid layout with buttons
    private void populateGridLayout() {
        gridLayout.removeAllViews(); // Remove all previous views (buttons) in the grid

        // Create buttons for the new grid
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Button button = new Button(this);
                button.setText(String.valueOf(letterGrid[row][col]));
                button.setTextSize(15);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLetterClicked((Button) v);
                    }
                });

                // Set the size of the buttons to fit the grid
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 100;
                params.height = 100;
                button.setLayoutParams(params);

                // Add the button to the grid layout
                gridLayout.addView(button);
            }
        }
    }
    private boolean isAdjacent(int row1, int col1, int row2, int col2) {
        // Check if the buttons are adjacent (horizontally, vertically, or diagonally)
        return Math.abs(row1 - row2) <= 1 && Math.abs(col1 - col2) <= 1;
    }
    private void onLetterClicked(Button button) {
        // If the button is already selected, deselect it and reset its background color
        if (selectedButtons.contains(button)) {
            selectedButtons.remove(button);
            button.setBackgroundColor(getResources().getColor(android.R.color.darker_gray)); // Reset to gray
        } else {
            // If it's not selected, add it to the list and highlight it
            selectedButtons.add(button);
            button.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light)); // Highlight in blue
        }

        // After updating selected buttons, check if the sequence is valid
        if (isValidSequence(selectedButtons)) {
            // If valid, proceed to check the word
            checkAndUpdateWord();
        } else {
            // If invalid, only reset the background of selected buttons
            resetSelectedButtonBackgrounds();
        }
    }



    public void restartGame() {
        // Reset the score
        score = 0;
        scoreTextView.setText("Score: " + score);

        // Clear found words
        foundWords.clear();

        // Reassign the original list of words to wordsToFind
        List<String> allWords = getAllWords();  // This is a method to return your full list of words.
        if (allWords != null && !allWords.isEmpty()) {
            // Ensure that wordsToFind is populated correctly
            wordsToFind = getRandomWords(allWords, 3); // Pick 3 random words, or adjust based on your logic
        }

        // Update the word list text view
        if (wordListTextView != null && wordsToFind != null) {
            wordListTextView.setText("Words to find: " + String.join(", ", wordsToFind));
        }

        // Initialize the grid with random letters
        initializeGrid();

        // Place the words in the grid
        placeWordsInGrid();

        // Clear the selected buttons
        selectedButtons.clear();

        // Populate the grid layout with the new grid
        populateGridLayout();

        // Reset button backgrounds
        resetSelectedButtonBackgrounds();
    }

    public void changeActivity(View view) {
        // Navigate to the Home Activity or perform any action for going to the home screen
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }




    // Check if a valid word is formed and update score
    private void checkAndUpdateWord() {
        StringBuilder selectedWord = new StringBuilder();

        // Build the word from the selected buttons
        for (Button button : selectedButtons) {
            selectedWord.append(button.getText().toString());
        }

        // Check if the selected word is valid
        if (wordsToFind.contains(selectedWord.toString())) {
            // If the word is valid, increase score and reset selection
            score += 10;
            foundWords.add(selectedWord.toString());
            wordsToFind.remove(selectedWord.toString());
            scoreTextView.setText("Score: " + score);
            selectedButtons.clear(); // Clear selected buttons
            resetSelectedButtonBackgrounds(); // Reset button backgrounds

            if (wordsToFind.isEmpty()) {
                wordListTextView.setText("All words found!");
                Toast.makeText(this, "Congratulations! You've found all the words!", Toast.LENGTH_SHORT).show();
                disableGrid();
            }

        }
    }

    private void disableGrid() {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            Button button = (Button) gridLayout.getChildAt(i);
            button.setEnabled(false); // Disable all buttons
        }
    }
    // Check if the sequence of selected buttons is valid (horizontally, vertically, or diagonally)
    // Check if the sequence of selected buttons is valid (horizontally, vertically, or diagonally)
    // Check if the sequence of selected buttons is valid (horizontally, vertically, or diagonally)
    private boolean isValidSequence(List<Button> selectedButtons) {
        if (selectedButtons.size() < 2) {
            return true; // Allow single button selection (just start)
        }

        for (int i = 1; i < selectedButtons.size(); i++) {
            Button prevButton = selectedButtons.get(i - 1);
            Button currentButton = selectedButtons.get(i);

            int prevRow = getRow(prevButton);
            int prevCol = getCol(prevButton);
            int currentRow = getRow(currentButton);
            int currentCol = getCol(currentButton);

            // Check if the buttons are adjacent
            if (!isAdjacent(prevRow, prevCol, currentRow, currentCol)) {
                return false; // Buttons are not adjacent, invalid sequence
            }
        }

        return true; // If all buttons are adjacent
    }


    // Helper method to check if two positions (row, col) are adjacent



    // Helper methods to get row and column of a button in the grid
    // Method to get the row of the button
    private int getRow(Button button) {
        // Iterate over the children of the gridLayout and find the button's index
        int index = gridLayout.indexOfChild(button);
        return index / 10; // 10 because the grid is 10x10
    }

    // Method to get the column of the button
    private int getCol(Button button) {
        // Iterate over the children of the gridLayout and find the button's index
        int index = gridLayout.indexOfChild(button);
        return index % 10; // 10 because the grid is 10x10
    }



    // Reset the background color for all buttons
    private void resetSelectedButtonBackgrounds() {
        // Loop through the selected buttons and reset their background color to default
        for (Button button : selectedButtons) {
            button.setBackgroundColor(getResources().getColor(android.R.color.darker_gray)); // Default unselected color
        }

        // Clear the list of selected buttons
        selectedButtons.clear();
    }


}
