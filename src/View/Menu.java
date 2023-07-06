import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

class Menu {
    private JPanel panel;
    private HoverPanel Chat1 = new HoverPanel();
    private HoverPanel Box1 = new HoverPanel();
    private HoverPanel User1 = new HoverPanel();

    Menu() {

        JLabel chat = new JLabel(new ImageIcon("Icon\\chat 32.png"));
            chat.setBounds(4, 0, 32, 32);
        Chat1.add(chat);
        Chat1.setBounds(19, 550, 40, 40);
        Chat1.setNormalColor(new Color(0x1e1f22));
        Chat1.setHoverColor(new Color(0x46484d));
        Chat1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    new Chat();
                }
            });

        JLabel box = new JLabel(new ImageIcon("Icon\\box 32.png"));
            box.setBounds(4, 0, 32, 32);
        Box1.add(box);
        Box1.setBounds(19, 600, 40, 40);
        Box1.setNormalColor(new Color(0x1e1f22));
        Box1.setHoverColor(new Color(0x46484d));
        Box1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    new Drive();
                }
            });

        JLabel user = new JLabel(new ImageIcon("Icon\\user 32.png"));
            user.setBounds(4, 0, 32, 32);
        User1.add(user);
        User1.setBounds(19, 650, 40, 40);
        User1.setNormalColor(new Color(0x1e1f22));
        User1.setHoverColor(new Color(0x46484d));
        User1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    new Profile();
                }
            });

        panel = new JPanel();
            panel.setBackground(new Color(0x1e1f22));
            panel.setBounds(0, 0, 70, 750);
            panel.setLayout(null);
            // panel.add(select);
            panel.add(Chat1);
            panel.add(Box1);
            panel.add(User1);
    }

    public void select(int x) {
        JPanel line = new JPanel();
            line.setBackground(new Color(0x949ba4));
            line.setBounds(0, 495 + 50*x, 5, 42);
        panel.add(line);
    }

    public JPanel getPanel() {
        return panel;
    }
}