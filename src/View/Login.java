import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
public class Login extends JFrame {
    JTextField Username;
    JTextField Password;

    Login(){
        JLabel Wel = new JLabel("Welcome!");
            Wel.setBounds(175, 20, 400, 32);
            Wel.setFont(new Font("Comfortaa",Font.BOLD,32));
            Wel.setForeground(new Color(0xFFFFFF));

        JLabel Pleasure = new JLabel("Pleasure to meet you again!");
            Pleasure.setBounds(100, 60, 400, 32);
            Pleasure.setFont(new Font("Comfortaa",Font.PLAIN,24));
            Pleasure.setForeground(new Color(0xFFFFFF));

        JLabel User = new JLabel("USERNAME");
            User.setBounds(50, 100, 400, 24);
            User.setFont(new Font("Comfortaa",Font.BOLD,14));
            User.setForeground(new Color(0xFFFFFF));

        JLabel Pass = new JLabel("PASSWORD");
            Pass.setBounds(50, 200, 400, 24);
            Pass.setFont(new Font("Comfortaa",Font.BOLD,14));
            Pass.setForeground(new Color(0xFFFFFF));

        //User & Pass
        Text Username = new Text(130);
        Text Password = new Text(230);
        
        JLabel Log = new JLabel("LOGIN");
            Log.setFont(new Font("Comfortaa",Font.BOLD,24));
            Log.setForeground(new Color(0xFFFFFF));
            Log.setBounds(160, 8, 80, 24);

        JPanel Log3 = new JPanel();
            Log3.setBackground(new Color(0x3c78d8));
            Log3.setBounds(50, 330, 400, 40);
            Log3.setLayout(null);
            Log3.add(Log);
            Log3.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Perform action when label is clicked
                    // System.out.println(Username.getText());
                    // System.out.println(Password.getText());
                    // if (Username.getString().equals("Asuragenie") && Password.getString().equals("Dung031004"))
                    {
                        new Chat();
                        //this.setVisible(true);
                    }
                }
            });
        
        JPanel Log2 = new JPanel();
            Log2.setBackground(new Color(0x2b2d31));
            Log2.setBounds(150, 150, 700, 400);
            Log2.setLayout(null);
            Log2.add(Wel);
            Log2.add(Pleasure);
            Log2.add(User);
            Log2.add(Pass);
            Log2.add(Username.getText());
            Log2.add(Password.getText());
            Log2.add(Log3);


        ImageIcon BG1 = new ImageIcon("Icon//ZBG.jpg");
        java.awt.Image BG2 = BG1.getImage();
        java.awt.Image BG3 = BG2.getScaledInstance(1000, 750, java.awt.Image.SCALE_SMOOTH);
        ImageIcon BG4 = new ImageIcon(BG3);
        JLabel Log1 = new JLabel(BG4);
            Log1.setBounds(0, 0, 1000, 750);

        this.setTitle("DataBridge");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(1000, 750);
        this.setResizable(false);
        this.setVisible(true);

        this.add(Log2);
        this.add(Log1);
    }
}