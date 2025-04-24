package com.example.appinterface1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;


import androidx.appcompat.app.AppCompatActivity;

public class Game2048 extends AppCompatActivity {

    private GridLayout gridLayout;
    private Button startGameButton;
    private Button leftSwipe, rightSwipe, upSwipe, downSwipe;
    private TextView[][] tiles = new TextView[4][4];
    private int[][] board = new int[4][4];
    private int score = 0;
    private TextView scoreTextView;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2048);

        // Initialize views
        gridLayout = findViewById(R.id.gridLayout);
        startGameButton = findViewById(R.id.startGameButton);
        leftSwipe = findViewById(R.id.leftSwipe);
        rightSwipe = findViewById(R.id.rightSwipe);
        upSwipe = findViewById(R.id.upSwipe);
        downSwipe = findViewById(R.id.downSwipe);
        scoreTextView = findViewById(R.id.scoreTextView);
        Button homeButton = findViewById(R.id.homeButton); // Initialize the Home button

        // Set listener for the Home button
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(Game2048.this, MainActivity.class); // Change MainActivity to your home activity
            startActivity(intent);
            finish(); // Optionally close this activity to prevent the user from coming back to it
        });

        // Initialize gesture detector and other game logic
        gestureDetector = new GestureDetector(this, new GestureListener());

        // Initialize the grid and start new game button
        initializeGrid();
        startGameButton.setOnClickListener(v -> startNewGame());

        // Set listeners for the swipe buttons (left, right, up, down)
        leftSwipe.setOnClickListener(v -> {
            moveLeft();
            addRandomTile();
            checkGameOver();
        });

        rightSwipe.setOnClickListener(v -> {
            moveRight();
            addRandomTile();
            checkGameOver();
        });

        upSwipe.setOnClickListener(v -> {
            moveUp();
            addRandomTile();
            checkGameOver();
        });

        downSwipe.setOnClickListener(v -> {
            moveDown();
            addRandomTile();
            checkGameOver();
        });

        // Set onTouchListener for detecting swipe gestures
        gridLayout.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

        startNewGame();
    }


    private void initializeGrid() {
        gridLayout.removeAllViews();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[i][j] = new TextView(this);

                // Set tile dimensions (modify if necessary)
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 200;
                params.height = 200;
                params.setMargins(5, 5, 5, 5); // Optional: margin to create spacing between tiles
                tiles[i][j].setLayoutParams(params);

                // Center the text inside each tile
                tiles[i][j].setGravity(Gravity.CENTER); // Centers text horizontally and vertically

                // Set background color and other styling
                tiles[i][j].setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                tiles[i][j].setTextColor(getResources().getColor(android.R.color.white));
                tiles[i][j].setTextSize(24); // Adjust text size if necessary

                gridLayout.addView(tiles[i][j]);
            }
        }
    }

    public void changeActivity(View v) {
        // when a button in main menu is pressed, will switch to corresponding activity

        Intent i;
        int id = v.getId();

        // if-else instead of switch-case because of non-final nature of resource IDs
        if (id == R.id.Home) {
            i = new Intent(this, MainActivity.class);
        }
    }
    private void startNewGame() {
        // Reset board and add two initial random tiles
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = 0;
                tiles[i][j].setText("");
                updateTileColor(tiles[i][j], board[i][j]);
            }
        }

        score = 0;
        scoreTextView.setText("Score: " + score);

        addRandomTile();
        addRandomTile();
    }

    private void addRandomTile() {
        // Add a tile with value 2 or 4 at a random empty position
        int emptyCells = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) {
                    emptyCells++;
                }
            }
        }

        if (emptyCells == 0) return;

        int randPos = (int) (Math.random() * emptyCells);
        int x = 0, y = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) {
                    if (randPos == 0) {
                        x = i;
                        y = j;
                    }
                    randPos--;
                }
            }
        }

        board[x][y] = (Math.random() < 0.9) ? 2 : 4;
        tiles[x][y].setText(String.valueOf(board[x][y]));
        updateTileColor(tiles[x][y], board[x][y]);
    }

    private void updateScore(int points) {
        score += points;
        scoreTextView.setText("Score: " + score);
    }

    private void updateTileColor(TextView tile, int value) {
        if (value == 0) {
            tile.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        } else {
            int color = getTileColor(value);
            tile.setBackgroundColor(color);
        }
    }

    private int getTileColor(int value) {
        switch (value) {
            case 2: return Color.parseColor("#eee4da");
            case 4: return Color.parseColor("#ede0c8");
            case 8: return Color.parseColor("#f2b179");
            case 16: return Color.parseColor("#f59563");
            case 32: return Color.parseColor("#f67c5f");
            case 64: return Color.parseColor("#f65e3b");
            case 128: return Color.parseColor("#edcf72");
            case 256: return Color.parseColor("#edcc61");
            case 512: return Color.parseColor("#edc850");
            case 1024: return Color.parseColor("#edc53f");
            case 2048: return Color.parseColor("#edc22e");
            default: return Color.parseColor("#3c3a32");
        }
    }

    private void moveLeft() {
        for (int i = 0; i < 4; i++) {
            int[] newRow = new int[4];
            int pos = 0;

            // Slide the numbers to the left
            for (int j = 0; j < 4; j++) {
                if (board[i][j] != 0) {
                    newRow[pos++] = board[i][j];
                }
            }

            // Merge the numbers
            for (int j = 0; j < 3; j++) {
                if (newRow[j] == newRow[j + 1] && newRow[j] != 0) {
                    newRow[j] *= 2;
                    newRow[j + 1] = 0;
                    updateScore(newRow[j]);
                    j++;
                }
            }

            // Slide the numbers again after merge
            pos = 0;
            for (int j = 0; j < 4; j++) {
                if (newRow[j] != 0) {
                    board[i][pos++] = newRow[j];
                } else {
                    board[i][pos++] = 0;
                }
            }

            // Update the grid display
            for (int j = 0; j < 4; j++) {
                if (board[i][j] != 0) {
                    tiles[i][j].setText(String.valueOf(board[i][j]));
                } else {
                    tiles[i][j].setText("");
                }
                updateTileColor(tiles[i][j], board[i][j]);
            }
        }
    }

    private void moveRight() {
        for (int i = 0; i < 4; i++) {
            int[] newRow = new int[4];
            int pos = 3;

            // Slide the numbers to the right
            for (int j = 3; j >= 0; j--) {
                if (board[i][j] != 0) {
                    newRow[pos--] = board[i][j];
                }
            }

            // Merge the numbers
            for (int j = 3; j > 0; j--) {
                if (newRow[j] == newRow[j - 1] && newRow[j] != 0) {
                    newRow[j] *= 2;
                    newRow[j - 1] = 0;
                    updateScore(newRow[j]);
                    j--;
                }
            }

            // Slide the numbers again after merge
            pos = 3;
            for (int j = 3; j >= 0; j--) {
                if (newRow[j] != 0) {
                    board[i][pos--] = newRow[j];
                } else {
                    board[i][pos--] = 0;
                }
            }

            // Update the grid display
            for (int j = 0; j < 4; j++) {
                if (board[i][j] != 0) {
                    tiles[i][j].setText(String.valueOf(board[i][j]));
                } else {
                    tiles[i][j].setText("");
                }
                updateTileColor(tiles[i][j], board[i][j]);
            }
        }
    }

    private void moveUp() {
        for (int j = 0; j < 4; j++) {
            int[] newCol = new int[4];
            int pos = 0;

            // Slide the numbers to the top
            for (int i = 0; i < 4; i++) {
                if (board[i][j] != 0) {
                    newCol[pos++] = board[i][j];
                }
            }

            // Merge the numbers
            for (int i = 0; i < 3; i++) {
                if (newCol[i] == newCol[i + 1] && newCol[i] != 0) {
                    newCol[i] *= 2;
                    newCol[i + 1] = 0;
                    updateScore(newCol[i]);
                    i++;
                }
            }

            // Slide the numbers again after merge
            pos = 0;
            for (int i = 0; i < 4; i++) {
                if (newCol[i] != 0) {
                    board[pos++][j] = newCol[i];
                } else {
                    board[pos++][j] = 0;
                }
            }

            // Update the grid display
            for (int i = 0; i < 4; i++) {
                if (board[i][j] != 0) {
                    tiles[i][j].setText(String.valueOf(board[i][j]));
                } else {
                    tiles[i][j].setText("");
                }
                updateTileColor(tiles[i][j], board[i][j]);
            }
        }
    }

    private void moveDown() {
        for (int j = 0; j < 4; j++) {
            int[] newCol = new int[4];
            int pos = 3;

            // Slide the numbers to the bottom
            for (int i = 3; i >= 0; i--) {
                if (board[i][j] != 0) {
                    newCol[pos--] = board[i][j];
                }
            }

            // Merge the numbers
            for (int i = 3; i > 0; i--) {
                if (newCol[i] == newCol[i - 1] && newCol[i] != 0) {
                    newCol[i] *= 2;
                    newCol[i - 1] = 0;
                    updateScore(newCol[i]);
                    i--;
                }
            }

            // Slide the numbers again after merge
            pos = 3;
            for (int i = 3; i >= 0; i--) {
                if (newCol[i] != 0) {
                    board[pos--][j] = newCol[i];
                } else {
                    board[pos--][j] = 0;
                }
            }

            // Update the grid display
            for (int i = 0; i < 4; i++) {
                if (board[i][j] != 0) {
                    tiles[i][j].setText(String.valueOf(board[i][j]));
                } else {
                    tiles[i][j].setText("");
                }
                updateTileColor(tiles[i][j], board[i][j]);
            }
        }
    }



    private boolean isGameOver() {
        // Check for any empty cells
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) return false;
            }
        }

        // Check for adjacent equal tiles (horizontal and vertical)
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i < 3 && board[i][j] == board[i + 1][j]) return false;  // Vertical
                if (j < 3 && board[i][j] == board[i][j + 1]) return false;  // Horizontal
            }
        }

        return true; // No valid moves left
    }

    // Add this method inside your Game2048 class

    private void checkGameOver() {
        if (isGameOver()) {
            Toast.makeText(Game2048.this, "Game Over!", Toast.LENGTH_SHORT).show();
           //enableSwipeButtons(false);  // Disable swipe buttons when game is over
        }
    }

    private void endGame() {
        Toast.makeText(Game2048.this, "Game Over!", Toast.LENGTH_SHORT).show();
        // Disable swipe buttons
        leftSwipe.setEnabled(false);
        rightSwipe.setEnabled(false);
        upSwipe.setEnabled(false);
        downSwipe.setEnabled(false);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float xDiff = e2.getX() - e1.getX();
            float yDiff = e2.getY() - e1.getY();

            if (Math.abs(xDiff) > Math.abs(yDiff)) {
                // Left or Right swipe
                if (xDiff > 0) {
                    moveRight();
                } else {
                    moveLeft();
                }
            } else {
                // Up or Down swipe
                if (yDiff > 0) {
                    moveDown();
                } else {
                    moveUp();
                }
            }

            addRandomTile(); // Add a new random tile after each move
            checkGameOver(); // Check if the game is over after each move

            return true;
        }
    }
}
