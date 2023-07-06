import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;

public class Drive extends JFrame implements ActionListener {
    Menu Pan1;
    List Pan2;
    Info Pan3;
    JButton Plus;
    int k=4;

    Mess[] TN = new Mess[100];

    Drive() {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        // Pan1 - Left
        Pan1 = new Menu();
        Pan1.select(2);

        // Pan2 - Center
        Pan2 = new List();

        Pan2.add(new Partners(new ImageIcon("Icon\\home 32.png"), "Home", 1).getPanel());
        Pan2.add(new Partners(new ImageIcon("Icon\\star 32.png"), "Stared", 2).getPanel());
        Pan2.add(new Partners(new ImageIcon("Icon\\trash 32.png"), "Trash", 3).getPanel());

        Plus = new JButton("Add", new ImageIcon("Icon\\add 32.png"));
            Plus.setBounds(100,30,170,50);
            Plus.setFont(new Font("Arial",Font.PLAIN,32));
            Plus.addActionListener(this);
            Plus.setBorder(null);
            Plus.setForeground(new Color(0x949ba4));
            Plus.setBackground(new Color(0xeeeeee));
            Plus.setHorizontalTextPosition(JButton.LEFT);

        Pan2.add(Plus);

        // Pan3 - Right
        JPanel Line = new JPanel();
            Line.setBounds(10,110,660,5);
            Line.setBackground(new Color(0x949ba4));

        Pan3 = new Info();

            Pan3.add(new Details(null, "Name", "Date", "Size", 1).getPanel());
            Pan3.add(Line);
            Pan3.add(new Details(new ImageIcon("Icon\\doc 32.png"), "DOC", "19/6", "1.5 mb", 2).getPanel());
            Pan3.add(new Details(new ImageIcon("Icon\\sheet 32.png"), "SHEET", "18/6", "2.3 mb", 3).getPanel());
            Pan3.add(new Details(new ImageIcon("Icon\\slide 32.png"), "SLIDE", "17/6", "3.7 mb", 4).getPanel());
            Pan3.add(new Details(new ImageIcon("Icon\\picture 32.png"), "PIC", "16/6", "5.0 mb", 5).getPanel());
        
        // Frame
        setTitle("DataBridge");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(1000, 750);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);

        add(Plus);
        add(Pan1.getPanel());
        add(Pan2.getPanel());
        add(Pan3.getPanel());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==Plus){
            Pan2.add(new Partners(new ImageIcon("Icon\\folder 32.png"), "Folder", k).getPanel());
            k+=1;
            revalidate();
            repaint();
        }
    }
}
