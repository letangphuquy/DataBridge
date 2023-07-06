package View;

import java.awt.*;

import javax.swing.*;

class HoverItem extends HoverPanel {
    protected JLabel icon = null;
    protected JLabel content = null;
    
    HoverItem() { super(); }

    HoverItem(ImageIcon icon, String name, int y) {
        setColor(new Color(0x2b2d31), new Color(0xbdc5cf));

        this.icon = new JLabel(icon);
        this.icon.setBounds(30,0,32,50);

        content = new JLabel(name);
        content.setForeground(new Color(0x949ba4));
        content.setFont(new Font("Comfortaa",Font.PLAIN,24));
        content.setBounds(70,0,130,50);

        add(content);
        add(this.icon);
        setLayout(null);
        setBounds(0,y*50+50,230,50);
        //setVisible(true);
    }

    HoverItem(String name, int y) {
        setNormalColor(new Color(0x2b2d31));
        setHoverColor (new Color(0xbdc5cf));

        content = new JLabel(name);
        content.setForeground(new Color(0x949ba4));
        content.setFont(new Font("Comfortaa",Font.PLAIN,24));
        content.setBounds(50,0,130,50);

        add(content);
        setLayout(null);
        setBounds(0,y*50+50,230,50);
        //setVisible(true);
    }
}
