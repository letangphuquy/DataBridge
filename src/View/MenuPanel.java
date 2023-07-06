package View;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import Rules.GUI;

class MenuPanel extends JPanel {
    private HoverPanel driveButton = new HoverPanel();
    private HoverPanel chatButton = new HoverPanel();
    private HoverPanel profileButton = new HoverPanel();

    MenuPanel() {
        JLabel driveIcon = new JLabel(new ImageIcon("Images\\box 32.png"));
            driveIcon.setBounds(4, 0, 32, 32);
        driveButton.add(driveIcon);
        driveButton.setBounds(19, 550, 40, 40);
        driveButton.setNormalColor(new Color(0x1e1f22));
        driveButton.setHoverColor(new Color(0x46484d));
        driveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Drive();
            }
        });

        JLabel chatIcon = new JLabel(new ImageIcon("Images\\chat 32.png"));
            chatIcon.setBounds(4, 0, 32, 32);
        chatButton.add(chatIcon);
        chatButton.setBounds(19, 600, 40, 40);
        chatButton.setNormalColor(new Color(0x1e1f22));
        chatButton.setHoverColor(new Color(0x46484d));
        chatButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Chat();
            }
        });

        JLabel userIcon = new JLabel(new ImageIcon("Images\\user 32.png"));
            userIcon.setBounds(4, 0, 32, 32);
        profileButton.add(userIcon);
        profileButton.setBounds(19, 650, 40, 40);
        profileButton.setNormalColor(new Color(0x1e1f22));
        profileButton.setHoverColor(new Color(0x46484d));
        profileButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Profile();
            }
        });

        setBackground(new Color(0x1e1f22));
        setBounds(0, 0, 70, GUI.HEIGHT);
        setLayout(null);
        add(driveButton);
        add(chatButton);
        add(profileButton);
    }

    public void select(int x) {
        JPanel line = new JPanel();
            line.setBackground(new Color(0x949ba4));
            line.setBounds(0, 495 + 50*x, 5, 42);
        this.add(line);
    }
}