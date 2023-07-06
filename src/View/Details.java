import java.awt.*;

import javax.swing.*;

class Details {
    private JPanel Panel = new JPanel();

    private JLabel Icon;
    private JLabel Name;
    private JLabel Date;
    private JLabel Size;

    Details(ImageIcon icon, String name, String date, String size, int y){
        Icon = new JLabel(icon);
            Icon.setBounds(10,0,32,50);

        Name = new JLabel(name);
            Name.setBounds(50,0,350,50);
            Name.setForeground(new Color(0x949ba4));
            Name.setFont(new Font("Arial",Font.PLAIN,24));

        Date = new JLabel(date);
            Date.setBounds(400,0,150,50);
            Date.setForeground(new Color(0x949ba4));
            Date.setFont(new Font("Arial",Font.PLAIN,24));

        Size = new JLabel(size);
            Size.setBounds(550,0,150,50);
            Size.setForeground(new Color(0x949ba4));
            Size.setFont(new Font("Arial",Font.PLAIN,24));

        Panel.setBounds(10,y*60,700,50);
        Panel.setBackground(new Color(0x313338));
        Panel.setLayout(null);
        Panel.add(Icon);
        Panel.add(Name);
        Panel.add(Date);
        Panel.add(Size);
    }

    public JPanel getPanel() {
        return Panel;
    }
}