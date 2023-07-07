package View;

import javax.swing.*;

public class ProfileView extends JFrame {
    MenuPanel Pan1;
    ListPanel Pan2;
    MainPanel Pan3;
    Sentence[] TN = new Sentence[100];

    public ProfileView() {
        // Pan1 - Left
        Pan1 = new MenuPanel();
        Pan1.select(3);

        // Pan2 - Center
        Pan2 = new ListPanel();

        Pan2.addUser(new HoverItem("Information", 1));
        Pan2.addUser(new HoverItem("Username", 2));
        Pan2.addUser(new HoverItem("Password", 3));
        Pan2.addUser(new HoverItem("Setting", 4));
        
        // Pan3 - Right
        Pan3 = new MainPanel();
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

        add(Pan1);
        add(Pan2);
        add(Pan3);
    }
}
