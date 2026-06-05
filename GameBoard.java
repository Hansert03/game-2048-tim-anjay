/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkg2048game;

/**
 *
 * @author ACER
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameBoard {
    public static final int SIZE = 4;

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    private final int[][] board = new int[SIZE][SIZE];
    private final Random random = new Random();
    private int score;

    public GameBoard() {
        initializeBoard();
    }
    
    public void resetScore() {
    score = 0;
}
    
    

    public void initializeBoard() {
        score = 0;
        for (int r = 0; r < SIZE; r++) {
            Arrays.fill(board[r], 0);
        }
        spawnTile();
        spawnTile();
    }

    public int getScore() {
        return score;
    }

    public int getTile(int row, int col) {
        return board[row][col];
    }

    public int getMaxTile() {
        int max = 0;
        for (int[] row : board) {
            for (int v : row) {
                if (v > max) {
                    max = v;
                }
            }
        }
        return max;
    }

    public boolean isWin() {
        return getMaxTile() >= 2048;
    }

    public boolean isGameOver() {
        return !canMove();
    }

    public boolean moveLeft() {
        return move(LEFT);
    }

    public boolean moveRight() {
        return move(RIGHT);
    }

    public boolean moveUp() {
        return move(UP);
    }

    public boolean moveDown() {
        return move(DOWN);
    }

    public void mergeTiles() {
        // Sudah di-handle saat moveLeft/moveRight/moveUp/moveDown
    }

    public void spawnTile() {
        List<int[]> empty = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (board[r][c] == 0) {
                    empty.add(new int[]{r, c});
                }
            }
        }

        if (empty.isEmpty()) {
            return;
        }

        int[] cell = empty.get(random.nextInt(empty.size()));
        board[cell[0]][cell[1]] = random.nextInt(10) == 0 ? 4 : 2;
    }

    public void clearRandomCell() {
        List<int[]> occupied = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (board[r][c] != 0) {
                    occupied.add(new int[]{r, c});
                }
            }
        }

        if (occupied.isEmpty()) {
            return;
        }

        int[] cell = occupied.get(random.nextInt(occupied.size()));
        board[cell[0]][cell[1]] = 0;
    }

    private boolean move(int direction) {
        int[][] before = copyBoard();

        for (int i = 0; i < SIZE; i++) {
            int[] line = getLine(direction, i);
            int[] merged = mergeLine(line);
            setLine(direction, i, merged);
        }

        boolean changed = !sameBoard(before, board);
        if (changed) {
            spawnTile();
        }

        return changed;
    }

    private int[] getLine(int direction, int index) {
        int[] line = new int[SIZE];

        switch (direction) {
            case LEFT:
                for (int i = 0; i < SIZE; i++) {
                    line[i] = board[index][i];
                }
                break;
            case RIGHT:
                for (int i = 0; i < SIZE; i++) {
                    line[i] = board[index][SIZE - 1 - i];
                }
                break;
            case UP:
                for (int i = 0; i < SIZE; i++) {
                    line[i] = board[i][index];
                }
                break;
            case DOWN:
                for (int i = 0; i < SIZE; i++) {
                    line[i] = board[SIZE - 1 - i][index];
                }
                break;
        }

        return line;
    }

    private void setLine(int direction, int index, int[] line) {
        switch (direction) {
            case LEFT:
                for (int i = 0; i < SIZE; i++) {
                    board[index][i] = line[i];
                }
                break;
            case RIGHT:
                for (int i = 0; i < SIZE; i++) {
                    board[index][SIZE - 1 - i] = line[i];
                }
                break;
            case UP:
                for (int i = 0; i < SIZE; i++) {
                    board[i][index] = line[i];
                }
                break;
            case DOWN:
                for (int i = 0; i < SIZE; i++) {
                    board[SIZE - 1 - i][index] = line[i];
                }
                break;
        }
    }

    private int[] mergeLine(int[] line) {
        List<Integer> values = new ArrayList<>();
        for (int v : line) {
            if (v != 0) {
                values.add(v);
            }
        }

        List<Integer> merged = new ArrayList<>();
        int i = 0;
        while (i < values.size()) {
            int current = values.get(i);
            if (i + 1 < values.size() && current == values.get(i + 1)) {
                int newValue = current * 2;
                merged.add(newValue);
                score += newValue;
                i += 2;
            } else {
                merged.add(current);
                i++;
            }
        }

        int[] result = new int[SIZE];
        for (int j = 0; j < merged.size(); j++) {
            result[j] = merged.get(j);
        }
        return result;
    }
    
    public void cheat1024() {
    for (int r = 0; r < SIZE; r++) {
        for (int c = 0; c < SIZE; c++) {
            board[r][c] = 0;
        }
    }

    board[0][0] = 1024;
    board[0][1] = 1024;
}
    
    private int[][] copyBoard() {
        int[][] copy = new int[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            System.arraycopy(board[r], 0, copy[r], 0, SIZE);
        }
        return copy;
    }

    private boolean sameBoard(int[][] a, int[][] b) {
        for (int r = 0; r < SIZE; r++) {
            if (!Arrays.equals(a[r], b[r])) {
                return false;
            }
        }
        return true;
    }

    private boolean canMove() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (board[r][c] == 0) {
                    return true;
                }
                if (r < SIZE - 1 && board[r][c] == board[r + 1][c]) {
                    return true;
                }
                if (c < SIZE - 1 && board[r][c] == board[r][c + 1]) {
                    return true;
                }
            }
        }
        return false;
    }
}