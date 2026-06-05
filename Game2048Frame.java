/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkg2048game;

/**
 *
 * @author ACER
 */

import java.awt.Dimension;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Game2048Frame extends JFrame {

    private static final String PREF_BEST_SCORE = "bestScore";
    private static final String PREF_COINS = "coins";
    private static final int CONTINUE_COST = 5;
    private static final int RECORD_BONUS_COINS = 10;

    private final Preferences prefs = Preferences.userNodeForPackage(Game2048Frame.class);

    private final boolean versusMode;
    private final GameBoard player1 = new GameBoard();
    private final GameBoard player2;

    private final JLabel infoLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel timerLabel = new JLabel("Time: 0s", SwingConstants.RIGHT);

    private final JButton restartButton = new JButton("RESTART");
    private final JButton continueButton = new JButton("LANJUT 5 KOIN");
    private final JButton backButton = new JButton("KELUAR");

    private final Timer timer;
    private int elapsedSeconds = 0;

    private boolean gameEnded = false;
    private boolean waitingForContinue = false;
    private boolean p1GameOver = false;
    private boolean p2GameOver = false;

    private int bestScore = 0;
    private int coins = 0;

    private String overlayTitle = "";
    private String overlaySubtitle = "";

    public Game2048Frame(boolean versusMode) {
        Sound.playBGMusic();
        this.versusMode = versusMode;
        this.player2 = versusMode ? new GameBoard() : null;

        loadProgress();

        setTitle(versusMode ? "2048 - Versus" : "2048 - Solo");
        setSize(980, 760);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveProgress();
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setOpaque(false);

        infoLabel.setFont(GameStyle2048.INFO_FONT);
        timerLabel.setFont(GameStyle2048.INFO_FONT);

        infoLabel.setForeground(Color.WHITE);
        timerLabel.setForeground(Color.WHITE);

        topPanel.add(infoLabel, BorderLayout.CENTER);
        topPanel.add(timerLabel, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setOpaque(false);

        restartButton.setIcon(loadScaledIcon("/pkg2048game/tombolRestart.PNG", 200, 120));

        backButton.setIcon(loadScaledIcon("/pkg2048game/tombolKeluar.PNG", 200, 120));

        restartButton.setBorderPainted(false);
        restartButton.setContentAreaFilled(false);
        restartButton.setFocusPainted(false);
        restartButton.setOpaque(false);
        restartButton.setText("");

        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        backButton.setText("");

        continueButton.setIcon(loadScaledIcon("/pkg2048game/tombolRevive.PNG", 200, 120));

        continueButton.setBorderPainted(false);
        continueButton.setContentAreaFilled(false);
        continueButton.setFocusPainted(false);
        continueButton.setOpaque(false);
        continueButton.setText("");

        continueButton.setFont(GameStyle2048.BUTTON_FONT);
        restartButton.addActionListener(e -> restartGame());
        continueButton.addActionListener(e -> continueAfterGameOver());
        backButton.addActionListener(e -> backToMenu());

        bottomPanel.add(restartButton);
        bottomPanel.add(continueButton);
        bottomPanel.add(backButton);
        BoardPanel boardPanel = new BoardPanel();
        boardPanel.setLayout(new BorderLayout());

        boardPanel.add(topPanel, BorderLayout.NORTH);
        boardPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(boardPanel);

        timer = new Timer(1000, e -> onTimerTick());
        timer.start(); 

                installKeyBindings();
                
                updateContinueButton();
                updateLabels();
        }

            private void loadProgress() {
            bestScore = prefs.getInt(PREF_BEST_SCORE, 0);
            coins = prefs.getInt(PREF_COINS, 0);
        }


        private void saveProgress() {
            prefs.putInt(PREF_BEST_SCORE, bestScore);
            prefs.putInt(PREF_COINS, coins);
            try {
                prefs.flush();
            } catch (BackingStoreException ignored) {
            }
        }

        private void updateContinueButton() {
            continueButton.setVisible(!versusMode);
            continueButton.setEnabled(waitingForContinue && coins >= CONTINUE_COST);

            getContentPane().revalidate();
            getContentPane().repaint();
        }
        
        private void onTimerTick() {
            if (gameEnded || waitingForContinue) {
                return;
            }

            elapsedSeconds++;
            updateLabels();
        }

    private void installKeyBindings() {
        Map<Integer, Runnable> actions = new HashMap<>();
        

        if (!versusMode) {
            actions.put(KeyEvent.VK_W, () -> moveBoard(player1, GameBoard.UP));
            actions.put(KeyEvent.VK_A, () -> moveBoard(player1, GameBoard.LEFT));
            actions.put(KeyEvent.VK_S, () -> moveBoard(player1, GameBoard.DOWN));
            actions.put(KeyEvent.VK_D, () -> moveBoard(player1, GameBoard.RIGHT));

            actions.put(KeyEvent.VK_UP, () -> moveBoard(player1, GameBoard.UP));
            actions.put(KeyEvent.VK_LEFT, () -> moveBoard(player1, GameBoard.LEFT));
            actions.put(KeyEvent.VK_DOWN, () -> moveBoard(player1, GameBoard.DOWN));
            actions.put(KeyEvent.VK_RIGHT, () -> moveBoard(player1, GameBoard.RIGHT));
        } else {
            actions.put(KeyEvent.VK_W, () -> moveBoard(player1, GameBoard.UP));
            actions.put(KeyEvent.VK_A, () -> moveBoard(player1, GameBoard.LEFT));
            actions.put(KeyEvent.VK_S, () -> moveBoard(player1, GameBoard.DOWN));
            actions.put(KeyEvent.VK_D, () -> moveBoard(player1, GameBoard.RIGHT));

            actions.put(KeyEvent.VK_UP, () -> moveBoard(player2, GameBoard.UP));
            actions.put(KeyEvent.VK_LEFT, () -> moveBoard(player2, GameBoard.LEFT));
            actions.put(KeyEvent.VK_DOWN, () -> moveBoard(player2, GameBoard.DOWN));
            actions.put(KeyEvent.VK_RIGHT, () -> moveBoard(player2, GameBoard.RIGHT));

            actions.put(KeyEvent.VK_PAGE_UP, () -> moveBoard(player2, GameBoard.UP));
            actions.put(KeyEvent.VK_HOME, () -> moveBoard(player2, GameBoard.LEFT));
            actions.put(KeyEvent.VK_PAGE_DOWN, () -> moveBoard(player2, GameBoard.DOWN));
            actions.put(KeyEvent.VK_END, () -> moveBoard(player2, GameBoard.RIGHT));
        }
        actions.put(KeyEvent.VK_F3, () -> {
    player1.cheat1024();
    repaint();
});
        actions.put(KeyEvent.VK_P, () -> {
    player2.cheat1024();
    repaint();
});
        
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();

        for (Map.Entry<Integer, Runnable> entry : actions.entrySet()) {
            final Runnable action = entry.getValue();
            String actionName = "key_" + entry.getKey();
            im.put(KeyStroke.getKeyStroke(entry.getKey(), 0), actionName);
            am.put(actionName, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    action.run();
                }
            });
        }
    }

    private void moveBoard(GameBoard board, int direction) {
        
        if (board == null) {
            return;
        }

        if (gameEnded || waitingForContinue) {
            return;
        }

        if (versusMode) {
            if (board == player1 && p1GameOver) {
                return;
            }
            if (board == player2 && p2GameOver) {
                return;
            }
        }

        boolean moved;
        switch (direction) {
            case GameBoard.UP:
                Sound.playSwap();
                moved = board.moveUp();
                break;
            case GameBoard.DOWN:
                Sound.playSwap();
                moved = board.moveDown();
                break;
            case GameBoard.LEFT:
                Sound.playSwap();
                moved = board.moveLeft();
                break;
            case GameBoard.RIGHT:
                Sound.playSwap();
                moved = board.moveRight();
                break;
            default:
                moved = false;
        }

        if (moved) {
            Sound.playSwap();
            checkEndCondition();
            updateLabels();
            repaint();
        }
    }
    
    private void checkEndCondition() {
    if (!versusMode) {
        if (player1.isWin()) {
            Sound.playVictory();
            endGame("YOU WIN", "Kamu mencapai tile 2048");
            return;
        }

        if (player1.isGameOver()) {
            Sound.stopBGMusic();
            Sound.playGameOver();
            int finalScore = player1.getScore();

            if (finalScore > bestScore) {
                bestScore = finalScore;
                coins += RECORD_BONUS_COINS;
                saveProgress();
            }


            waitingForContinue = true;
            overlayTitle = "GAME OVER";
            overlaySubtitle = (coins >= CONTINUE_COST)
                    ? "Klik REVIVE atau RESTART"
                    : "Koin tidak cukup. Klik RESTART";

            timer.stop();
            updateContinueButton();
            updateLabels();
            repaint();
        }
        return;
    }

    if (player1.isWin()) {
        Sound.playVictory();
        endGame("THE WINNER IS PLAYER 1", "Klik RESTART untuk main lagi");
        return;
    }

    if (player2.isWin()) {
        Sound.playVictory();
        endGame("THE WINNER IS PLAYER 2", "Klik RESTART untuk main lagi");
        return;
    }

    if (!p1GameOver && player1.isGameOver()) {
        p1GameOver = true;
         Sound.playGameOver();
    }

    if (!p2GameOver && player2.isGameOver()) {
        p2GameOver = true;
         Sound.playGameOver();
    }

    if (p1GameOver && p2GameOver) {
        endGame("GAME OVER", "Klik RESTART untuk main lagi");
    }
}

    private void continueAfterGameOver() {
        if (!waitingForContinue || coins < CONTINUE_COST) {
            return;
        }
        
        Sound.playBGMusic();
        Sound.playRevive();
        coins -= CONTINUE_COST;
        saveProgress();

        player1.clearRandomCell();

        waitingForContinue = false;
        overlayTitle = "";
        overlaySubtitle = "";

        timer.start();
        updateContinueButton();
        updateLabels();
        repaint();
    }

    private void endGame(String title, String subtitle) {
        Sound.stopBGMusic();  // ← tambah ini
        Sound.playGameOver(); // ← tambah ini
        gameEnded = true;
        gameEnded = true;
        waitingForContinue = false;
        overlayTitle = title;
        overlaySubtitle = subtitle;
        timer.stop();
        updateContinueButton();
        updateLabels();
        repaint();
    }

    private void restartGame() {
        Sound.playBGMusic();
        Sound.playClick(); // ← tambah ini
        player1.resetScore();
    player1.initializeBoard();
        player1.initializeBoard();
        if (versusMode) {
            player2.initializeBoard();
        }

        elapsedSeconds = 0;
        gameEnded = false;
        waitingForContinue = false;
        p1GameOver = false;
        p2GameOver = false;
        overlayTitle = "";
        overlaySubtitle = "";

        timer.start();
        updateContinueButton();
        updateLabels();
        repaint();
    }

    private void backToMenu() {
        Sound.playBGMusic();
        Sound.playClick();   // ← tambah ini
        saveProgress();
        dispose();
        new Menu2048().setVisible(true);
        saveProgress();
        dispose();
        new Menu2048().setVisible(true);
    }

    private void updateLabels() {
        timerLabel.setText("Time: " + elapsedSeconds + "s");

        if (!versusMode) {
            infoLabel.setText(
                    "Score: " + player1.getScore() +
                    "   |   Best: " + bestScore +
                    "   |   Coins: " + coins
            );
        } else {
            infoLabel.setText(
                    "Player 1 Score: " + player1.getScore() +
                    "   |   Player 2 Score: " + player2.getScore()
            );
        }
    }
    
    private ImageIcon loadScaledIcon(String path, int w, int h) {
    ImageIcon icon = new ImageIcon(getClass().getResource(path));
    Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
    return new ImageIcon(img);
}

    private class BoardPanel extends JPanel {
        private final ImageIcon gif =
        new ImageIcon(getClass().getResource("/pkg2048game/BGFrame.GIF"));
        
        BoardPanel() {
    setOpaque(false);
    
        }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(
            gif.getImage(),
            0,
            0,
            getWidth(),
            getHeight(),
            this
        );
    Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (!versusMode) {
                
                int boardSize = GameStyle2048.SINGLE_BOARD_SIZE;
        int cellSize = GameStyle2048.SINGLE_CELL;

        int startX = (getWidth() - boardSize) / 2;
        int startY = (getHeight() - boardSize) / 2;

                        GameStyle2048.drawBoard(g2, player1, startX, startY, boardSize, cellSize, "PLAYER 1");
                    } else {

        int boardSize = GameStyle2048.VERSUS_BOARD_SIZE;
        int cellSize = GameStyle2048.VERSUS_CELL;

        int startY = 300; // naik-turun dikit kalau perlu

        int gapBetweenBoards = 60;
        int totalWidth = (boardSize * 2) + gapBetweenBoards;
        int leftX = (getWidth() - totalWidth) / 2;
        int rightX = leftX + boardSize + gapBetweenBoards;
                GameStyle2048.drawBoard(g2, player1, leftX, startY, boardSize, cellSize, "PLAYER 1");
                GameStyle2048.drawBoard(g2, player2, rightX, startY, boardSize, cellSize, "PLAYER 2");
                
                if (p1GameOver) {
                    GameStyle2048.drawBoardStatus(g2, "GAME OVER", leftX, startY - 50, boardSize);
                }
                if (p2GameOver) {
                    GameStyle2048.drawBoardStatus(g2, "GAME OVER", rightX, startY - 50, boardSize);


                }
            }

            if (waitingForContinue || gameEnded) {
                GameStyle2048.drawOverlay(g2, overlayTitle, overlaySubtitle, getWidth(), getHeight());
            }
        }
    }
}