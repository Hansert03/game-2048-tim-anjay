
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkg2048game;

/**
 *
 * @author ACER
 */

import java.awt.*;

public class GameStyle2048 {

    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 32);
    public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 22);
    public static final Font INFO_FONT = new Font("Arial", Font.BOLD, 18);
    public static final Font PLAYER_FONT = new Font("Arial", Font.BOLD, 20);
    public static final Font OVERLAY_FONT = new Font("Arial", Font.BOLD, 54);
    public static final Font OVERLAY_SUB_FONT = new Font("Arial", Font.PLAIN, 20);
    public static final Font BOARD_STATUS_FONT = new Font("Arial", Font.BOLD, 22);

    public static final Color PANEL_BG = new Color(245, 245, 245);
    public static final Color BOARD_BG = Color.WHITE;
    public static final Color EMPTY_TILE = new Color(255, 250, 245);
    public static final Color OVERLAY_BG = new Color(0, 0, 0, 120);
    public static final Color STATUS_RED = Color.RED;

    public static final int SINGLE_BOARD_SIZE = 430;
    public static final int VERSUS_BOARD_SIZE = 350;

    public static final int SINGLE_CELL = 95;
    public static final int VERSUS_CELL = 75;

    public static final int GAP = 10;
    public static final int TOP_Y = 80;

    private GameStyle2048() {
    }

    public static void drawBoard(Graphics2D g2, GameBoard board, int startX, int startY, int boardSize, int cellSize, String playerLabel) {
        g2.setColor(BOARD_BG);
        g2.fillRoundRect(startX, startY, boardSize, boardSize, 18, 18);

        g2.setColor(Color.WHITE);
        g2.setFont(PLAYER_FONT);
        g2.drawString(playerLabel, startX, startY - 18);

        for (int r = 0; r < GameBoard.SIZE; r++) {
            for (int c = 0; c < GameBoard.SIZE; c++) {
                int value = board.getTile(r, c);

                int x = startX + GAP + c * (cellSize + GAP);
                int y = startY + GAP + r * (cellSize + GAP);

                g2.setColor(tileColor(value));
                g2.fillRoundRect(x, y, cellSize, cellSize, 16, 16);

                if (value != 0) {
                    g2.setColor(value <= 4 ? Color.DARK_GRAY : Color.WHITE);
                    g2.setFont(new Font("Arial", Font.BOLD, value < 100 ? 26 : 22));

                    String text = String.valueOf(value);
                    FontMetrics fm = g2.getFontMetrics();
                    int tx = x + (cellSize - fm.stringWidth(text)) / 2;
                    int ty = y + (cellSize + fm.getAscent() - fm.getDescent()) / 2;
                    g2.drawString(text, tx, ty);
                }
            }
        }
    }

    public static void drawBoardStatus(Graphics2D g2, String text, int startX, int startY, int boardSize) {
        g2.setFont(BOARD_STATUS_FONT);
        g2.setColor(STATUS_RED);
        FontMetrics fm = g2.getFontMetrics();
        int tx = startX + (boardSize - fm.stringWidth(text)) / 2;
        g2.drawString(text, tx, startY);
    }

    public static void drawOverlay(Graphics2D g2, String title, String subtitle, int width, int height) {
        g2.setColor(OVERLAY_BG);
        g2.fillRect(0, 0, width, height);

        g2.setColor(Color.WHITE);
        g2.setFont(OVERLAY_FONT);
        FontMetrics fm = g2.getFontMetrics();
        int tx = (width - fm.stringWidth(title)) / 2;
        int ty = height / 2;
        g2.drawString(title, tx, ty);

        if (subtitle != null && !subtitle.isEmpty()) {
            g2.setFont(OVERLAY_SUB_FONT);
            FontMetrics fm2 = g2.getFontMetrics();
            int tx2 = (width - fm2.stringWidth(subtitle)) / 2;
            g2.drawString(subtitle, tx2, ty + 40);
        }
    }

    public static Color tileColor(int value) {
        switch (value) {
            case 0: return EMPTY_TILE;
            case 2: return new Color(255, 248, 240);
            case 4: return new Color(255, 242, 210);
            case 8: return new Color(255, 190, 120);
            case 16: return new Color(255, 155, 90);
            case 32: return new Color(255, 120, 85);
            case 64: return new Color(255, 90, 60);
            case 128: return new Color(255, 220, 100);
            case 256: return new Color(255, 210, 80);
            case 512: return new Color(255, 200, 60);
            case 1024: return new Color(255, 190, 40);
            case 2048: return new Color(255, 215, 0);
            
            default: return new Color(60, 58, 50);
        }
    }
}