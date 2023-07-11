package View;

import java.awt.*;
import javax.swing.*;

import Client.Controllers;
import Rules.GUI;

public class MenuPanel extends JPanel {
    public HoverPanel driveButton = new HoverPanel();
    public HoverPanel chatButton = new HoverPanel();
    public HoverPanel profileButton = new HoverPanel();

    MenuPanel() {
        setBackground(GUI.COLOR_MENU);
        setBounds(0, 0, 50, GUI.HEIGHT);
        setLayout(null);

        JLabel driveIcon = new JLabel(new ImageIcon("Images\\box 32.png"));
            driveIcon.setBounds(4, 0, 32, 32);
        driveButton.add(driveIcon);
        driveButton.setBounds(5, 550, 40, 40);
        driveButton.setNormalColor(new Color(0x1e1f22));
        driveButton.setHoverColor(new Color(0x46484d));

        JLabel chatIcon = new JLabel(new ImageIcon("Images\\chat 32.png"));
            chatIcon.setBounds(4, 0, 32, 32);
        chatButton.add(chatIcon);
        chatButton.setBounds(5, 600, 40, 40);
        chatButton.setNormalColor(new Color(0x1e1f22));
        chatButton.setHoverColor(new Color(0x46484d));

        JLabel userIcon = new JLabel(new ImageIcon("Images\\user 32.png"));
            userIcon.setBounds(4, 0, 32, 32);
        profileButton.add(userIcon);
        profileButton.setBounds(5, 650, 40, 40);
        profileButton.setNormalColor(new Color(0x1e1f22));
        profileButton.setHoverColor(new Color(0x46484d));

      
        add(driveButton);
        add(chatButton);
        add(profileButton);
        Controllers.MenuController.addMenuController(this);
    }

    public void select(int x) {
        JPanel line = new JPanel();
            line.setBackground(new Color(0x949ba4));
            line.setBounds(0, 495 + 50*x, 5, 42);
        this.add(line);
    }
}