package virtualpet;

import javax.swing.*;
import java.awt.*;

public class StatsPanel extends JPanel {

    private Pet pet;

    public StatsPanel(Pet pet) {
        this.pet = pet;
        setOpaque(false);
        setPreferredSize(new Dimension(300, 160));
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawStat(g2, "Hunger",    pet.getHunger(),     0,  invertColor(pet.getHunger()));
        drawStat(g2, "Happiness", pet.getHappiness(),  40, happinessColor(pet.getHappiness()));
        drawStat(g2, "Energy",    pet.getEnergy(),     80, energyColor(pet.getEnergy()));
        drawStat(g2, "Health",    pet.getHealth(),    120, healthColor(pet.getHealth()));
    }

    private void drawStat(Graphics2D g2, String label, int value, int y, Color barColor) {
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        g2.setColor(new Color(60, 60, 60));
        g2.drawString(label, 0, y + 14);

        int barX = 130, barY = y + 2, barW = 160, barH = 16;
        g2.setColor(new Color(220, 220, 220));
        g2.fillRoundRect(barX, barY, barW, barH, 8, 8);

        int filled = (int)(barW * (value / 100.0));
        g2.setColor(barColor);
        if (filled > 0) g2.fillRoundRect(barX, barY, filled, barH, 8, 8);

        g2.setColor(new Color(80, 80, 80));
        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        g2.drawString(value + "%", barX + barW + 6, y + 14);
    }

    private Color invertColor(int v) {
        if (v < 30) return new Color(100, 200, 100);
        if (v < 60) return new Color(255, 200, 50);
        return new Color(220, 80, 80);
    }

    private Color happinessColor(int v) {
        if (v >= 60) return new Color(100, 190, 100);
        if (v >= 30) return new Color(255, 200, 50);
         return new Color(220, 80, 80);
    }

    private Color energyColor(int v) {
        if (v >= 50) return new Color(80, 160, 230);
        if (v >= 25) return new Color(255, 200, 50);
        return new Color(220, 80, 80);
    }

    private Color healthColor(int v) {
        if (v >= 60) return new Color(220, 80, 120);
        if (v >= 30) return new Color(255, 200, 50);
        return new Color(180, 40, 40);
    }
}