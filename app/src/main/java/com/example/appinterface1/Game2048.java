package com.example.appinterface1;



import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
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

        gridLayout = findViewById(R.id.gridLayout);
        startGameButton = findViewById(R.id.startGameButton);
        leftSwipe = findViewById(R.id.leftSwipe);
        rightSwipe = findViewById(R.id.rightSwipe);
        upSwipe = findViewById(R.id.upSwipe);
        downSwipe = findViewById(R.id.downSwipe);
        scoreTextView = findViewById(R.id.scoreTextView);

        // Initialize gesture detector for swipe actions
        gestureDetector = new GestureDetector(this, new GestureListener());

        // Initialize the grid and start new game button
        initializeGrid();
        startGameButton.setOnClickListener(v -> startNewGame());

        // Set listeners for the swipe buttons (left, right, up, and down)
        leftSwipe.setOnClickListener(v -> {
            moveLeft();
            addRandomTile();
        });

        rightSwipe.setOnClickListener(v -> {
            moveRight();
            addRandomTile();
        });

        upSwipe.setOnClickListener(v -> {
            moveUp();
            addRandomTile();
        });

        downSwipe.setOnClickListener(v -> {
            moveDown();
            addRandomTile();
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
                tiles[i][j].setWidth(200);
                tiles[i][j].setHeight(200);
                tiles[i][j].setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                tiles[i][j].setTextColor(getResources().getColor(android.R.color.white));
                tiles[i][j].setTextSize(24);
                tiles[i][j].setGravity(View.TEXT_ALIGNMENT_CENTER);
                gridLayout.addView(tiles[i][j]);
            }
        }
    }

    private void startNewGame() {
        // Reset board and add two initial random tiles
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = 0;
                tiles[i][j].setText("");
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
    }

    private void updateScore(int points) {
        score += points;
        scoreTextView.setText("Score: " + score);
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

            if (isGameOver()) {
                Toast.makeText(Game2048.this, "Game Over!", Toast.LENGTH_SHORT).show();
            }

            return true;
        }
    }
}
