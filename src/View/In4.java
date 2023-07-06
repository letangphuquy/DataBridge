package View;

import java.awt.*;
import javax.swing.*;

class In4 {
    private JPanel Panel = new JPanel();
    private JLabel Info1;
    private JLabel Info2;
    private int Y;

    In4(String info1, String info2, int y){
        Y=y;

        Info1 = new JLabel(info1);
        Info1.setForeground(new Color(0x949ba4));
        Info1.setFont(new Font("Arial",Font.PLAIN,24));
        Info1.setBounds(200,0,200,50);

        Info2 = new JLabel(info2);
        Info2.setForeground(new Color(0x949ba4));
        Info2.setFont(new Font("Arial",Font.PLAIN,24));
        Info2.setBounds(400,0,300,50);

        Panel.add(Info1);
        Panel.add(Info2);
        Panel.setLayout(null);
        Panel.setBounds(0,Y*50+50,700,50);
        Panel.setBackground(new Color(0x313338));
    }
    public JPanel getPanel() {
        return Panel;
    }
}
