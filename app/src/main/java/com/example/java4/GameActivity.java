package com.example.java4;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.java4.R;

public class GameActivity extends AppCompatActivity {

    private boolean xTurn = true;
    private int[][] board = new int[3][3];
    private int xWins = 0;
    private int oWins = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        if (savedInstanceState != null) {
            xWins = savedInstanceState.getInt("победил X");
            oWins = savedInstanceState.getInt("победил O");
            xTurn = savedInstanceState.getBoolean("очередь X");
            board = (int[][]) savedInstanceState.getSerializable("доска");
        }

        updateScore();
        setupBoard();
    }

    private void setupBoard() {
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            final int row = i / 3;
            final int col = i % 3;
            Button button = (Button) gridLayout.getChildAt(i);
            button.setText(board[row][col] == 1 ? "X" : board[row][col] == 2 ? "O" : "");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (board[row][col] == 0) {
                        board[row][col] = xTurn ? 1 : 2;
                        ((Button) v).setText(xTurn ? "X" : "O");
                        if (checkWin()) {
                            if (xTurn) {
                                xWins++;
                                showToast("X победил!");
                            } else {
                                oWins++;
                                showToast("O победил!");
                            }
                            resetBoard();
                        } else if (isBoardFull()) {
                            showToast("Это ничья!");
                            resetBoard();
                        } else {
                            xTurn = !xTurn;
                        }
                        updateScore();
                    }
                }
            });
        }
    }

    private boolean checkWin() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][1] == board[i][2])
                return true;
            if (board[0][i] != 0 && board[0][i] == board[1][i] && board[1][i] == board[2][i])
                return true;
        }
        return (board[0][0] != 0 && board[0][0] == board[1][1] && board[1][1] == board[2][2])
                || (board[0][2] != 0 && board[0][2] == board[1][1] && board[1][1] == board[2][0]);
    }

    private boolean isBoardFull() {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == 0) return false;
            }
        }
        return true;
    }

    private void resetBoard() {
        board = new int[3][3];
        setupBoard();
    }

    private void updateScore() {
        TextView textViewScore = findViewById(R.id.textViewScore);
        textViewScore.setText("X: " + xWins + " O: " + oWins);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("X победил", xWins);
        outState.putInt("O победил", oWins);
        outState.putBoolean("Ход X", xTurn);
        outState.putSerializable("доска", board);
    }
}