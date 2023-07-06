package View;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;

public class Profile extends JFrame {
    Menu Pan1;
    List Pan2;
    Info Pan3;
    Mess[] TN = new Mess[100];

    Profile() {
        // Pan1 - Left
        Pan1 = new Menu();
        Pan1.select(3);

        // Pan2 - Center
        Pan2 = new List();

        Pan2.add(new Partners("Information", 1).getPanel());
        Pan2.add(new Partners("Username", 2).getPanel());
        Pan2.add(new Partners("Password", 3).getPanel());
        Pan2.add(new Partners("Setting", 4).getPanel());
        
        // Pan3 - Right
        Pan3 = new Info();
        JLabel Avt = new JLabel(new ImageIcon("Icon\\monkey 128.png"));
            Avt.setBounds(36,100,128,128);

        Pan3.add(Avt);

        Pan3.add(new In4("Full name","Monkey",1).getPanel());
        Pan3.add(new In4("Birthday","25-10-2019",2).getPanel());
        Pan3.add(new In4("Phone number","0763751XXX",3).getPanel());
        Pan3.add(new In4("Email address","SuperMK@gmail.com",4).getPanel());
        Pan3.add(new In4("Country/Region","Viet Nam",5).getPanel());
        Pan3.add(new In4("City","Da Nang",6).getPanel());
        Pan3.add(new In4("Address","1XX Pham Quang Anh",7).getPanel());

        // Frame
        setTitle("DataBridge");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(1000, 750);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);

        add(Pan1.getPanel());
        add(Pan2.getPanel());
        add(Pan3.getPanel());
    }
}
