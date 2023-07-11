package View;

import java.awt.*;
import javax.swing.*;

import Rules.GUI;

//WHAT THE ****?
// /*
class Sentence {
    private HoverItem Panel = new HoverItem();
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

    Sentence(ImageIcon icon, String msg, int y){

        setColor(new Color(0x313338), new Color(0x575b64));
        Y=y;

        Icon = new JLabel(icon);
        Icon.setBounds(0,0,32,50);

        Mess = new JLabel(msg);
        Mess.setForeground(new Color(0xFFFFFF));
        Mess.setFont(GUI.P_SANS);
        Mess.setBounds(50,0,500,50);

        Panel.setLayout(null);
        Panel.add(Mess);
        Panel.add(Icon);
        Panel.setBounds(30,Y*50+50,550,50);
        Panel.setBackground(new Color(0x313338));
    }
    public JPanel getPanel() {
        return Panel;
    }
}
// */

/*
class Sentence extends HoverItem {
    private int y;

    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
        setBounds(30, y*50 + 50,550,50);
    }

    Sentence(ImageIcon iconBg, String msg, int y) {
        super();
        setColor(new Color(0x313338), new Color(0x575b64));

        icon = new JLabel(iconBg);
        icon.setBounds(0,0,32,50);

        content = new JLabel(msg);
        content.setForeground(new Color(0xFFFFFF));
        content.setFont(new Font("Comfortaa",Font.PLAIN,24));
        content.setBounds(50,0,500,50);
        
        setBackground(normalColor);
        setLayout(null);
        add(content);
        add(icon);
        setY(y);
        setVisible(true);
        System.out.println("Fuck, sentence created");
    }

    public JPanel getPanel() {
        return this;
    }
 }
 
 */
