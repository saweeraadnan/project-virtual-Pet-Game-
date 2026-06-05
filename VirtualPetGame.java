
package virtualpet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VirtualPetGame extends JFrame {

    private static final long serialVersionUID = 1L;
    private Pet pet;
    private StatsPanel statsPanel;
    private PetAnimationPanel animPanel;
    private JLabel moodLabel;
    private JLabel ageLabel;
    private JTextArea logArea;
    private Timer gameTimer;
    private Timer actionTimer;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VirtualPetGame game = new VirtualPetGame();
            game.showSetupAndStart();
        });
    }

    public VirtualPetGame() {
        setTitle("Virtual Pet");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 650);
        setMinimumSize(new Dimension(680, 600));
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void showSetupAndStart() {
        PetSetupDialog dialog = new PetSetupDialog(this);
        dialog.setVisible(true);
        if (!dialog.isConfirmed()) System.exit(0);
        pet = new Pet(dialog.getPetName(), dialog.getSpecies());
        buildUI();
        setVisible(true);
        startGameLoop();
    }

    private void buildUI() {
        setTitle("Virtual Pet - " + pet.getName());

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        root.setBackground(new Color(240, 240, 240));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setOpaque(false);

        moodLabel = new JLabel("Mood: " + pet.getMood(), SwingConstants.LEFT);
        moodLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        ageLabel = new JLabel("Age: " + pet.getAge() + " days", SwingConstants.RIGHT);
        ageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ageLabel.setForeground(new Color(100, 100, 100));

        topPanel.add(moodLabel);
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(ageLabel);
        root.add(topPanel, BorderLayout.NORTH);

        animPanel = new PetAnimationPanel(pet);
        animPanel.setPreferredSize(new Dimension(680, 300));
        animPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        animPanel.setBackground(new Color(255, 255, 255));
        root.add(animPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(8, 8));
        bottomPanel.setOpaque(false);

        statsPanel = new StatsPanel(pet);
        JPanel statsWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        statsWrapper.setOpaque(false);
        statsWrapper.add(statsPanel);
        statsWrapper.setBorder(BorderFactory.createTitledBorder("Status"));

        logArea = new JTextArea(2, 30);
        logArea.setEditable(false);
        logArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        logArea.setBackground(new Color(245, 245, 245));
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBorder(BorderFactory.createTitledBorder("Activity Log"));
        scroll.setPreferredSize(new Dimension(300, 65));

        JPanel statsLogPanel = new JPanel(new BorderLayout(8, 0));
        statsLogPanel.setOpaque(false);
        statsLogPanel.add(statsWrapper, BorderLayout.WEST);
        statsLogPanel.add(scroll, BorderLayout.CENTER);

        // --- FIXED BUTTON PANEL GRID IMPLEMENTATION ---
        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 8, 8));
        btnPanel.setOpaque(false);
        
        addActionButton(btnPanel, "🍖 Feed", () -> {
            animPanel.setAction("eating");
            String treat = pet.getSpecies().toLowerCase().contains("bunny") ? "crunchy carrot" : "tasty kibble";
            log(pet.getName() + " enthusiastically gobbles up some " + treat + "!");
            pet.feed(); 
            resetActionAfter(2500);
        });
        
        addActionButton(btnPanel, "🎾 Play", () -> {
            animPanel.setAction("playing");
            String toy = pet.getSpecies().toLowerCase().contains("cat") ? "pink yarn ball" : "bouncy toy";
            log(pet.getName() + " leaps around having fun with the " + toy + "!");
            pet.play();
            resetActionAfter(2500);
        });
        
        addActionButton(btnPanel, "💤 Sleep", () -> {
            animPanel.setAction("sleeping");
            log(pet.getName() + " curls into their cozy spot and drifts off to sleep.");
            pet.sleep();
            resetActionAfter(3500);
        });
        
        addActionButton(btnPanel, "💊 Medicine", () -> {
            log(pet.getName() + " reluctantly swallowed the healing vitamins.");
            pet.giveMedicine();
            resetActionAfter(1500);
        });

        bottomPanel.add(statsLogPanel, BorderLayout.NORTH);
        bottomPanel.add(btnPanel, BorderLayout.SOUTH);
        root.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private void addActionButton(JPanel panel, String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(240, 250, 240)); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(Color.WHITE); }
        });
        btn.addActionListener(e -> { action.run(); refreshUI(); });
        panel.add(btn);
    }

    private void resetActionAfter(int milliseconds) {
        if (actionTimer != null) actionTimer.stop();
        actionTimer = new Timer(milliseconds, e -> {
            animPanel.setAction("idle");
            ((Timer)e.getSource()).stop();
        });
        actionTimer.start();
    }

    private void startGameLoop() {
        gameTimer = new Timer(3000, e -> {
            pet.tick();
            refreshUI();
            if (!pet.isAlive()) {
                gameTimer.stop();
                animPanel.stopAnimation();
                JOptionPane.showMessageDialog(this,
                    pet.getName() + " passed away after " + pet.getAge() + " days!\nTake better care next time!",
                    "Game Over", JOptionPane.WARNING_MESSAGE);
            }
        });
        gameTimer.start();
    }

    private void refreshUI() {
        moodLabel.setText("Mood: " + pet.getMood());
        ageLabel.setText("Age: " + pet.getAge() + " days");
        statsPanel.repaint();
        animPanel.repaint();
    }

    private void log(String message) {
        logArea.append("• " + message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
}

