package View;

import java.awt.*;
import javax.swing.*;

class Mess {
    private HoverPanel Panel = new HoverPanel();
    private JLabel Icon;
    private JLabel Mess;
    private int Y;

    public void setColor(Color normalColor, Color hoverColor) {
        Panel.setNormalColor(normalColor);
        Panel.setHoverColor (hoverColor);
    }

    public int getY() {
        return Y;
    }
    public void setY(int y) {
        Y = y;
        Panel.setBounds(30,Y*50+50,550,50);
    }

    Mess(ImageIcon icon, String tn, int y){

        setColor(new Color(0x313338), new Color(0x575b64));
        Y=y;

        Icon = new JLabel(icon);
        Icon.setBounds(0,0,32,50);

        Mess = new JLabel(tn);
        Mess.setForeground(new Color(0xFFFFFF));
        Mess.setFont(new Font("Comfortaa",Font.PLAIN,24));
        Mess.setBounds(50,0,500,50);

        Panel.add(Mess);
        Panel.add(Icon);
        Panel.setLayout(null);
        Panel.setBounds(30,Y*50+50,550,50);
        Panel.setBackground(new Color(0x313338));
    }
    public JPanel getPanel() {
        return Panel;
    }
}
