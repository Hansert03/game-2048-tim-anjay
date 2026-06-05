package pkg2048game;
import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.*;

public class Menu2048 extends JFrame {
    public Menu2048() {
        Sound.playBGMusic();
        setTitle("2048 - Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 760);
        setLocationRelativeTo(null);
        setResizable(false);
        BackgroundPanel panel = new BackgroundPanel();
        panel.setLayout(null);
        setContentPane(panel);
        
        JButton soloButton   = createImageButton("/pkg2048game/soloMenu.PNG",   375, 250, 250, 150);
        JButton versusButton = createImageButton("/pkg2048game/versusMenu.PNG", 375, 350, 250, 150);
        JButton exitButton   = createImageButton("/pkg2048game/keluarMenu.PNG", 375, 450, 250, 150);
        
        soloButton.addActionListener(e -> openGame(false));
        versusButton.addActionListener(e -> openGame(true));
        exitButton.addActionListener(e -> System.exit(0));
        
        panel.add(soloButton);
        panel.add(versusButton);
        panel.add(exitButton);
    }

    private JButton createImageButton(String path, int x, int y, int w, int h) {
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(img));
        button.setBounds(x, y, w, h);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        return button;
    }

    private void openGame(boolean versusMode) {
        Sound.playClick();        // ← bunyi klik
        Sound.playBGMusic();  // ← musik mengecil dalam 2 detik
        
        // Tunggu fade selesai baru pindah ke game
        Timer delay = new Timer(2000, e -> {
            new Game2048Frame(versusMode).setVisible(true);
            dispose();
        });
        delay.setRepeats(false);
        delay.start();
    }

    class BackgroundPanel extends JPanel {
        private final Image bg = new ImageIcon(getClass().getResource("/pkg2048game/Backgroundmenu.PNG")).getImage();
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu2048().setVisible(true));
    }
}