package View;

import java.awt.*;
import javax.swing.*;

class ListPanel extends JPanel {
    ListPanel() {
        setBackground(new Color(0x2b2d31));
        setBounds(70, 0, 230, 750);
        setLayout(null);
    }
    JPanel selector = null;
    private void deselect() {
        if (selector != null) remove(selector);
    }
    public void select(int y) {
        deselect();
        selector = new JPanel();
        selector.setBackground(new Color(0xedf2fa));
        selector.setBounds(0, y*50+50, 230, 50);
        add(selector);
    }

    public void addUser(JPanel user) {
        add(user);
    }
    public void addButton(JButton plus) {
        add(plus);
    }
    public void addItem(HoverItem partners) {
        add(partners);
    }
}