package View;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import Rules.GUI;

public class Chat extends JFrame {
    MenuPanel menu;
    ListPanel dialougeList;
    MainPanel conversation;
    HoverItem[] users = new HoverItem[100];
    Sentence[] messages = new Sentence[100];

    Chat() {
        menu = new MenuPanel();
        menu.select(2);

        dialougeList = new ListPanel();
        users[0] = new HoverItem(new ImageIcon("images\\crocodile 32.png"), "Crocodile", 1);
        users[1] = new HoverItem(new ImageIcon("images\\eagle 32.png"), "Eagle", 2);
        users[2] = new HoverItem(new ImageIcon("images\\fox 32.png"), "Fox", 3);
        users[3] = new HoverItem(new ImageIcon("images\\lion 32.png"), "Lion", 4);

        users[0].setColor(new Color(0xedf2fa), new Color(0xedf2fa));

        dialougeList.addItem(users[0]);
        dialougeList.addItem(users[1]);
        dialougeList.addItem(users[2]);
        dialougeList.addItem(users[3]);

        conversation = new MainPanel();

        JPanel textInputer = new JPanel();
            textInputer.setBackground(new Color(0x2b2d31));
            textInputer.setBounds(10, 650, 660, 40);
            textInputer.setLayout(null);
        
        Color textInputColor = new Color(0x2b2d31);
        JTextField chatTextfield = new JTextField();
            chatTextfield.setFont(new Font("Comfortaa",Font.PLAIN,24));
            chatTextfield.setBounds(10, 0, 600, 40);
            chatTextfield.setBackground(textInputColor);
            chatTextfield.setForeground(new Color(0xFFFFFF));
            chatTextfield.setCaretColor(new Color(0xFFFFFF));
            chatTextfield.setBorder(null);

        JButton sendButton = new JButton(new ImageIcon("images\\send 32.png"));
            sendButton.setBounds(610, 5, 32, 32);
            sendButton.setBackground(textInputColor);
            sendButton.setBorder(null);

        textInputer.add(chatTextfield);
        textInputer.add(sendButton);
        conversation.add(textInputer);

        messages[0] = new Sentence(new ImageIcon("images\\crocodile 32.png"), "What's up Monkey", 1);
        messages[1] = new Sentence(new ImageIcon("images\\crocodile 32.png"), "Are you free today?", 2);
        messages[2] = new Sentence(new ImageIcon("images\\monkey 32.png"), "I'm fine, is something wrong?", 3);
        // conversation.add(messages[0].getPanel());
        // conversation.add(messages[1].getPanel());
        // conversation.add(messages[2].getPanel());
        for (int i = 0; i < 3; i++) {
            conversation.add(messages[i].getPanel());
            // conversation.add(messages[i]);
            //conversation.repaint();
        }
        
        JLabel Up = new JLabel(new ImageIcon("images\\up 32.png"));
            Up.setBounds(630, 550, 32, 32);
            Up.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for (int j = 0; j < 3; j++){
                        messages[j].setY(messages[j].getY() + 1);
                        //conversation.repaint();
                    }
                }
            });

        JLabel Down = new JLabel(new ImageIcon("images\\down 32.png"));
            Down.setBounds(630, 600, 32, 32);
            Down.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for (int j = 0; j < 3; j++){
                        messages[j].setY(messages[j].getY()-1);
                        //conversation.repaint();
                    }
                }
            });

        conversation.add(Up);
        conversation.add(Down);
        
        add(menu);
        add(dialougeList);
        add(conversation);
        
        // Frame
        setTitle("DataBridge");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(GUI.WIDTH, GUI.HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

    }
}
