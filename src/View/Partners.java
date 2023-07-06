import java.awt.*;

import javax.swing.*;

class Partners {
    private HoverPanel Panel = new HoverPanel();
    private JLabel Icon;
    private JLabel Name;
    
    public void setColor(Color normalColor, Color hoverColor) {
        Panel.setNormalColor(normalColor);
        Panel.setHoverColor (hoverColor);
    }

    Partners(ImageIcon icon, String name, int y){

        Panel.setNormalColor(new Color(0x2b2d31));
        Panel.setHoverColor (new Color(0xbdc5cf));

        Icon = new JLabel(icon);
        Icon.setBounds(30,0,32,50);

        Name = new JLabel(name);
        Name.setForeground(new Color(0x949ba4));
        Name.setFont(new Font("Comfortaa",Font.PLAIN,24));
        Name.setBounds(70,0,130,50);

        Panel.add(Name);
        Panel.add(Icon);
        Panel.setLayout(null);
        Panel.setBounds(0,y*50+50,230,50);
    }

    Partners(String name, int y){
        Panel.setNormalColor(new Color(0x2b2d31));
        Panel.setHoverColor (new Color(0xbdc5cf));

        Name = new JLabel(name);
        Name.setForeground(new Color(0x949ba4));
        Name.setFont(new Font("Comfortaa",Font.PLAIN,24));
        Name.setBounds(50,0,130,50);

        Panel.add(Name);
        Panel.setLayout(null);
        Panel.setBounds(0,y*50+50,230,50);
    }

    public JPanel getPanel() {
        return Panel;
    }
}
