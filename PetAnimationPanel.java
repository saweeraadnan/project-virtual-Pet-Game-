package virtualpet;

import javax.swing.*;
import java.awt.*;

public class PetAnimationPanel extends JPanel {
    private Pet pet;
    private float animTick = 0;
    private Timer animTimer;
    private String currentAction = "idle";
    private float actionTimer = 0;

    public PetAnimationPanel(Pet pet) {
        this.pet = pet;
        setPreferredSize(new Dimension(500, 300));
        setOpaque(false);
        animTimer = new Timer(20, e -> { // Slightly smoother tick frame rate
            animTick += 0.12f;
            actionTimer += 0.12f;
            repaint();
        });
        animTimer.start();
    }

    public void setPet(Pet pet) { this.pet = pet; }
    public void setAction(String action) {
        this.currentAction = action;
        this.actionTimer = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawDynamicBackground(g2);
        
        int cx = getWidth() / 2 + 50;
        int cy = getHeight() - 75;

        if (!pet.isAlive()) {
            drawDead(g2, cx, cy);
            return;
        }

        drawMoodIndicators(g2);

        // Branch drawing logic dynamically based on selected animal species
        String species = pet.getSpecies().toLowerCase();
        if (species.contains("cat")) {
            drawCatWithAction(g2, cx, cy);
        } else if (species.contains("bunny") || species.contains("rabbit")) {
            drawBunnyWithAction(g2, cx, cy);
        } else {
            drawDogWithAction(g2, cx, cy);
        }
    }

    private void drawDynamicBackground(Graphics2D g2) {
        String species = pet.getSpecies().toLowerCase();

        if (species.contains("cat")) {
            // Cozy Indoor Cat Lounge Room
            g2.setColor(new Color(245, 222, 179)); // Warm wheat walls
            g2.fillRect(0, 0, getWidth(), getHeight() - 60);
            g2.setColor(new Color(139, 69, 19)); // Hardwood baseboard/floor
            g2.fillRect(0, getHeight() - 60, getWidth(), 60);
            drawCatTree(g2);
        } else if (species.contains("bunny")) {
            // Sunny Meadow for the Bunny
            g2.setColor(new Color(176, 224, 230)); // Soft Powder Blue Sky
            g2.fillRect(0, 0, getWidth(), getHeight() - 90);
            g2.setColor(new Color(50, 205, 50)); // Lime Meadow Grass
            g2.fillRect(0, getHeight() - 90, getWidth(), 90);
            
            g2.setColor(new Color(255, 255, 224, 150)); // Soft warm sun
            g2.fillOval(getWidth() - 90, 15, 50, 50);
            drawBunnyHutch(g2);
        } else {
            // Standard Outdoor Dog Yard
            g2.setColor(new Color(135, 206, 235)); // Sky Blue
            g2.fillRect(0, 0, getWidth(), getHeight() - 80);
            g2.setColor(new Color(34, 139, 34)); // Forest Green Grass
            g2.fillRect(0, getHeight() - 80, getWidth(), 80);
            
            g2.setColor(new Color(255, 215, 0)); // Golden Sun
            g2.fillOval(getWidth() - 80, 20, 60, 60);
            drawDogKennel(g2);
        }
    }

    private void drawNameplate(Graphics2D g2, int x, int y, int width) {
        String name = pet.getName().toUpperCase();
        g2.setFont(new Font("Arial", Font.BOLD, 11));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(name);
        
        int plateW = Math.max(textWidth + 12, 50);
        int plateH = 18;
        int plateX = x + (width - plateW) / 2;

        // Wooden board look
        g2.setColor(new Color(222, 184, 135));
        g2.fillRect(plateX, y, plateW, plateH);
        g2.setColor(new Color(101, 67, 33));
        g2.drawRect(plateX, y, plateW, plateH);
        
        // Text
        g2.setColor(Color.BLACK);
        g2.drawString(name, plateX + (plateW - textWidth) / 2, y + 13);
    }

    private void drawDogKennel(Graphics2D g2) {
        int x = 40, y = getHeight() - 170, w = 110, h = 90;
        g2.setColor(new Color(160, 82, 45)); // Brown base logs
        g2.fillRect(x, y, w, h);
        
        // Roof
        int[] rx = { x - 10, x + (w / 2), x + w + 10 };
        int[] ry = { y, y - 35, y };
        g2.setColor(new Color(128, 0, 0));
        g2.fillPolygon(rx, ry, 3);
        
        // Entrance
        g2.setColor(new Color(40, 20, 10));
        g2.fillRoundRect(x + 35, y + 35, 40, 55, 20, 20);

        drawNameplate(g2, x, y + 10, w);
    }

    private void drawCatTree(Graphics2D g2) {
        int x = 40, baseMaxY = getHeight() - 60;
        // Central Post
        g2.setColor(new Color(210, 180, 140));
        g2.fillRect(x + 35, baseMaxY - 130, 20, 130);
        // Scratching rope marks
        g2.setColor(new Color(139, 115, 85));
        for (int i = baseMaxY - 110; i < baseMaxY; i += 12) {
            g2.drawLine(x + 35, i, x + 55, i);
        }
        // Base box platform
        g2.setColor(new Color(186, 140, 99));
        g2.fillRect(x, baseMaxY - 60, 90, 60);
        g2.setColor(new Color(90, 50, 20));
        g2.fillOval(x + 30, baseMaxY - 45, 30, 30); // Cat condo door

        // High perch platform
        g2.setColor(new Color(205, 133, 63));
        g2.fillRoundRect(x + 15, baseMaxY - 145, 60, 15, 8, 8);

        drawNameplate(g2, x, baseMaxY - 55, 90);
    }

    private void drawBunnyHutch(Graphics2D g2) {
        int x = 40, y = getHeight() - 160, w = 120, h = 80;
        g2.setColor(new Color(210, 105, 30)); // Hutch wood frame
        g2.fillRect(x, y, w, h);
        
        // Stilt legs to keep it off ground
        g2.fillRect(x + 10, y + h, 12, 15);
        g2.fillRect(x + w - 22, y + h, 12, 15);

        // Mesh pattern wire window
        g2.setColor(new Color(230, 230, 230));
        g2.fillRect(x + 15, y + 25, 50, 45);
        g2.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(1));
        for (int i = x + 20; i < x + 65; i += 8) g2.drawLine(i, y + 25, i, y + 70);
        for (int j = y + 30; j < y + 70; j += 8) g2.drawLine(x + 15, j, x + 65, j);

        // Wooden solid side hatch door
        g2.setColor(new Color(139, 69, 19));
        g2.fillRect(x + 75, y + 25, 35, 45);

        // Roof slant
        int[] rx = { x - 5, x + w + 5, x + w + 5, x - 5 };
        int[] ry = { y, y - 15, y - 5, y + 5 };
        g2.setColor(new Color(100, 50, 15));
        g2.fillPolygon(rx, ry, 4);

        drawNameplate(g2, x, y + 5, w);
    }

    // ==========================================
    // CINEMATIC DOG ANIMATIONS
    // ==========================================
    private void drawDogWithAction(Graphics2D g2, int cx, int cy) {
        int mood = pet.getHappiness();
        switch(currentAction) {
            case "eating":
                float eatOffset = (float) Math.sin(animTick * 3.5f) * 6;
                g2.setColor(new Color(210, 155, 80));
                g2.fillRoundRect(cx - 40, cy - 15, 80, 45, 25, 25); // Body
                g2.fillOval(cx - 58, cy - 40 + (int)eatOffset, 46, 42); // Head pivoting down
                // Wagging tail dynamically
                float tailW = (float) Math.sin(animTick * 4) * 25;
                g2.setColor(new Color(180, 120, 60));
                g2.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(cx + 38, cy - 5, cx + 65, cy - 10 + (int)tailW);
                // Bowl
                g2.setColor(Color.RED);
                g2.fillArc(cx - 75, cy + 15, 30, 20, 0, 180);
                break;
            case "sleeping":
                g2.setColor(new Color(190, 140, 70));
                g2.fillOval(cx - 45, cy, 85, 40); // Curled body
                g2.fillOval(cx - 42, cy - 18, 38, 36); // Resting head
                drawZzz(g2, cx, cy);
                break;
            case "playing":
                float jump = (float) Math.abs(Math.sin(animTick * 3f)) * 24;
                g2.setColor(new Color(210, 155, 80));
                g2.fillRoundRect(cx - 35, cy - 30 - (int)jump, 75, 45, 20, 20);
                g2.fillOval(cx - 50, cy - 55 - (int)jump, 45, 42);
                // Ball icon floating
                g2.setColor(Color.ORANGE);
                g2.fillOval(cx - 90, cy - 20 - (int)(Math.sin(animTick * 2) * 10), 16, 16);
                break;
            default: // Idle Breathing
                float breathe = (float) Math.sin(animTick * 1.2f) * 3;
                g2.setColor(new Color(210, 155, 80));
                g2.fillRoundRect(cx - 40, cy - 20 + (int)breathe, 80, 50, 25, 25);
                g2.fillOval(cx - 55, cy - 48 + (int)breathe, 48, 44);
                // Floppy ears
                g2.setColor(new Color(150, 95, 40));
                g2.fillRoundRect(cx - 58, cy - 40 + (int)breathe, 12, 22, 6, 6);
                g2.fillRoundRect(cx - 24, cy - 40 + (int)breathe, 12, 22, 6, 6);
                // Eyes & Nose
                g2.setColor(Color.BLACK);
                g2.fillOval(cx - 48, cy - 38 + (int)breathe, 6, 6);
                g2.fillOval(cx - 34, cy - 38 + (int)breathe, 6, 6);
                g2.fillRoundRect(cx - 43, cy - 28 + (int)breathe, 10, 6, 4, 4);
                if (mood > 60) { // Happy panting tongue
                    g2.setColor(Color.PINK);
                    g2.fillOval(cx - 40, cy - 22 + (int)breathe, 8, 10);
                }
        }
    }

    // ==========================================
    // CINEMATIC CAT ANIMATIONS
    // ==========================================
    private void drawCatWithAction(Graphics2D g2, int cx, int cy) {
        switch(currentAction) {
            case "eating":
                float lick = (float) Math.abs(Math.sin(animTick * 4)) * 5;
                g2.setColor(new Color(180, 180, 180)); // Sleek silver cat
                g2.fillRoundRect(cx - 35, cy - 12, 70, 42, 20, 20);
                g2.fillOval(cx - 52, cy - 32 + (int)lick, 40, 36);
                // Milk bowl
                g2.setColor(new Color(220, 240, 255));
                g2.fillOval(cx - 72, cy + 15, 26, 14);
                break;
            case "sleeping":
                g2.setColor(new Color(160, 160, 160));
                g2.fillOval(cx - 40, cy + 5, 75, 35); // Tightly curled sphere
                g2.fillOval(cx - 28, cy - 8, 34, 32);
                // Sleek tail wrapped around body
                g2.setStroke(new BasicStroke(5));
                g2.drawArc(cx - 46, cy + 12, 40, 25, 90, 180);
                drawZzz(g2, cx, cy);
                break;
            case "playing":
                // Cat pouncing swipe animation
                float sweepX = (float) Math.sin(animTick * 3.5f) * 20;
                g2.setColor(new Color(190, 190, 190));
                g2.fillRoundRect(cx - 35 + (int)sweepX, cy - 20, 70, 45, 20, 20);
                g2.fillOval(cx - 48 + (int)sweepX, cy - 46, 42, 38);
                // Swiping paw line
                g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.setColor(Color.WHITE);
                g2.drawLine(cx - 40 + (int)sweepX, cy - 10, cx - 65 + (int)sweepX, cy - 25 + (int)(Math.sin(animTick * 6) * 10));
                // Yarn toy
                g2.setColor(new Color(255, 105, 180));
                g2.fillOval(cx - 85, cy + 10, 20, 20);
                break;
            default: // Idle Cat tail-sway and twitching
                float tailSway = (float) Math.sin(animTick * 1.8f) * 18;
                g2.setColor(new Color(180, 180, 180));
                g2.fillRoundRect(cx - 35, cy - 20, 70, 48, 22, 22); // Main Body
                g2.fillOval(cx - 48, cy - 45, 42, 38); // Head
                // Perked triangular ears
                int[] elx = { cx - 46, cx - 44, cx - 34 }; int[] ely = { cx - 42, cy - 58, cy - 44 };
                g2.fillPolygon(elx, ely, 3);
                int[] erx = { cx - 24, cx - 14, cx - 12 }; int[] ery = { cx - 44, cy - 58, cy - 42 };
                g2.fillPolygon(erx, ery, 3);
                // Eyes (Slits)
                g2.setColor(new Color(50, 200, 50)); // Bright green cat eyes
                g2.fillOval(cx - 40, cy - 35, 8, 10);
                g2.fillOval(cx - 24, cy - 35, 8, 10);
                g2.setColor(Color.BLACK);
                g2.fillRect(cx - 37, cy - 35, 2, 10);
                g2.fillRect(cx - 21, cy - 35, 2, 10);
                // Cinematic Tail Sway
                g2.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.setColor(new Color(150, 150, 150));
                g2.drawArc(cx + 25, cy - 15, 30, 40, 45 + (int)tailSway, 90);
        }
    }

    // ==========================================
    // CINEMATIC BUNNY ANIMATIONS
    // ==========================================
    private void drawBunnyWithAction(Graphics2D g2, int cx, int cy) {
        switch(currentAction) {
            case "eating":
                float crunch = (float) Math.abs(Math.sin(animTick * 5)) * 4;
                g2.setColor(new Color(245, 245, 220)); // Fluffy cream bunny
                g2.fillOval(cx - 35, cy - 10, 70, 44);
                g2.fillOval(cx - 48, cy - 32, 38, 36);
                // Crispy bright carrot
                g2.setColor(Color.ORANGE);
                g2.fillPolygon(new int[]{cx - 70, cx - 52, cx - 56}, new int[]{cy + 15, cy + 8 + (int)crunch, cy + 24}, 3);
                break;
            case "sleeping":
                g2.setColor(new Color(230, 230, 210));
                g2.fillOval(cx - 35, cy + 5, 70, 36);
                g2.fillOval(cx - 46, cy - 10, 34, 32);
                // Ears folded back down along body flatly
                g2.fillRoundRect(cx - 32, cy - 15, 24, 10, 6, 6);
                drawZzz(g2, cx, cy);
                break;
            case "playing":
                // Infinite hop leap animation loop
                float hopY = (float) Math.abs(Math.sin(animTick * 3.8f)) * 32;
                float tilt = (float) Math.sin(animTick * 3.8f) * 0.15f;
                g2.rotate(tilt, cx, cy);
                g2.setColor(new Color(245, 245, 220));
                g2.fillOval(cx - 35, cy - 25 - (int)hopY, 70, 44); // Body airborne
                g2.fillOval(cx - 48, cy - 50 - (int)hopY, 38, 36); // Head
                // Long rigid upright jumping ears
                g2.fillRoundRect(cx - 44, cy - 78 - (int)hopY, 8, 30, 5, 5);
                g2.fillRoundRect(cx - 32, cy - 78 - (int)hopY, 8, 30, 5, 5);
                g2.rotate(-tilt, cx, cy); // Reset canvas transformation rotation matrix
                break;
            default: // Idle hop/twitching nose
                float noseTwitch = (float) Math.abs(Math.sin(animTick * 6)) * 2;
                g2.setColor(new Color(245, 245, 220));
                g2.fillOval(cx - 35, cy - 15, 70, 46); // Fluffy main torso body
                g2.fillOval(cx - 48, cy - 38, 38, 36); // Head
                // Giant iconic rabbit ears standing tall
                g2.setColor(new Color(245, 245, 220));
                g2.fillRoundRect(cx - 44, cy - 66, 10, 30, 6, 6);
                g2.fillRoundRect(cx - 30, cy - 66, 10, 30, 6, 6);
                g2.setColor(new Color(255, 192, 203)); // Pink inner ear detailing
                g2.fillRoundRect(cx - 41, cy - 62, 4, 22, 4, 4);
                g2.fillRoundRect(cx - 27, cy - 62, 4, 22, 4, 4);
                // Face layout
                g2.setColor(Color.BLACK);
                g2.fillOval(cx - 42, cy - 28, 5, 5); // Beady eye
                g2.setColor(Color.PINK);
                g2.fillOval(cx - 49, cy - 22 + (int)noseTwitch, 4, 4); // Cute moving nose
                // Cotton tail puff balls
                g2.setColor(Color.WHITE);
                g2.fillOval(cx + 30, cy - 2, 14, 14);
        }
    }

    private void drawZzz(Graphics2D g2, int cx, int cy) {
        float zOffset = (animTick % 4) * 2.5f;
        g2.setColor(new Color(70, 130, 180, (int)(220 - zOffset * 20)));
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.drawString("Z", (int)(cx - 10 - zOffset), (int)(cy - 40 - zOffset * 4));
        g2.drawString("z", (int)(cx + 5 - zOffset), (int)(cy - 52 - zOffset * 4));
    }

    private void drawMoodIndicators(Graphics2D g2) {
        int mood = pet.getHappiness();
        int hunger = pet.getHunger();
        int energy = pet.getEnergy();
        int health = pet.getHealth();

        g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
        g2.drawString(getMoodEmoji(mood), 20, 40);

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        if (hunger > 70) g2.drawString("🍖", getWidth() - 60, 40);
        if (energy < 25) g2.drawString("💤", getWidth() - 40, 40);
        if (health < 30) g2.drawString("🤢", getWidth() - 80, 40);
    }

    private String getMoodEmoji(int mood) {
        if (mood >= 85) return "😍";
        if (mood >= 70) return "😊";
        if (mood >= 50) return "🙂";
        if (mood >= 30) return "😐";
        if (mood >= 20) return "😢";
        return "😠";
    }

    private void drawDead(Graphics2D g2, int cx, int cy) {
        g2.setColor(new Color(140, 140, 140));
        g2.fillRoundRect(cx - 40, cy - 15, 80, 45, 20, 20);
        g2.fillOval(cx - 55, cy - 40, 44, 40);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(cx - 48, cy - 26, cx - 42, cy - 20);
        g2.drawLine(cx - 42, cy - 26, cx - 48, cy - 20);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("R.I.P", cx - 15, cy + 60);
    }

    public void stopAnimation() { animTimer.stop(); }
}