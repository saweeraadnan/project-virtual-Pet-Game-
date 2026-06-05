package virtualpet;

import javax.swing.*;
import java.awt.*;

public class PetSetupDialog extends JDialog {

    private JTextField nameField;
    private JComboBox<String> speciesBox;
    private boolean confirmed = false;

    public PetSetupDialog(JFrame parent) {
        super(parent, "Create Your Pet", true);
        setLayout(new BorderLayout(10, 10));
        setSize(340, 220);
        setLocationRelativeTo(parent);
        setResizable(false);

        JLabel title = new JLabel("Welcome to Virtual Pet!", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        form.add(new JLabel("Pet Name:"));
        nameField = new JTextField("Fluffy");
        form.add(nameField);

        form.add(new JLabel("Species:"));
        speciesBox = new JComboBox<>(new String[]{"Cat", "Dog", "Bunny"});
        form.add(speciesBox);
        add(form, BorderLayout.CENTER);

        JButton start = new JButton("Start Game");
        start.setFont(new Font("Segoe UI", Font.BOLD, 13));
        start.setBackground(new Color(80, 170, 110));
        start.setForeground(Color.WHITE);
        start.setFocusPainted(false);
        start.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a name!");
                return;
            }
            confirmed = true;
            dispose();
        });

        JPanel btnPanel = new JPanel();
        btnPanel.add(start);
        add(btnPanel, BorderLayout.SOUTH);
    }

    public boolean isConfirmed() { return confirmed; }

    public String getPetName() { return nameField.getText().trim(); }

    public String getSpecies() {
        String s = (String) speciesBox.getSelectedItem();
        if (s.equals("Cat"))   return "cat";
        if (s.equals("Dog"))   return "dog";
        return "bunny";
    }
}