package View;


import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import Rules.GUI;

public class ChatView extends JFrame {
    MenuPanel menu;
    ListPanel dialougeList;
    ChatPanel chatPanel;
    HoverItem[] users = new HoverItem[100];
    Sentence[] messages = new Sentence[100];

    private int numUsers = 4;
    private int numMessages = 20;

    private int prevIdx = -1;
    private void changeDialouge(int idx) {
        if (prevIdx != -1)
            users[prevIdx].swapColor();
        users[idx].swapColor();
        prevIdx = idx;
        remove(chatPanel);
        chatPanel = (ChatPanel) users[idx].getChildren();
        add(chatPanel);
        revalidate();
        repaint();
    }

    public ChatView() {
        setTitle("DataBridge");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(GUI.WIDTH, GUI.HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        menu = new MenuPanel();
        menu.select(2);

        dialougeList = new ListPanel();
        users[0] = new HoverItem(new ImageIcon("images\\crocodile 32.png"), "Crocodile", 1);
        users[1] = new HoverItem(new ImageIcon("images\\eagle 32.png"), "Eagle", 2);
        users[2] = new HoverItem(new ImageIcon("images\\fox 32.png"), "Fox", 3);
        users[3] = new HoverItem(new ImageIcon("images\\lion 32.png"), "Lion", 4);

        for (int i = 0; i < numUsers; i++) {
            dialougeList.addItem(users[i]);
            users[i].setChildren(new ChatPanel());
            final int idx = i;
            users[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    changeDialouge(idx);
                }
            });
        }
        
        messages[0] = new Sentence(new ImageIcon("images\\crocodile 32.png"), "What's up Monkey", 1);
        messages[1] = new Sentence(new ImageIcon("images\\crocodile 32.png"), "Are you free today?", 2);
        messages[2] = new Sentence(new ImageIcon("images\\monkey 32.png"), "I'm fine. What's wrong?", 3);
        for (int i = 3; i < numMessages; i++) {
            messages[i] = new Sentence(new ImageIcon("images\\crocodile 32.png"), "This is my talkshow " + i, i+1);
        }

        chatPanel = (ChatPanel) users[0].getChildren();
        for (int i = 0; i < numMessages; i++) 
            chatPanel.addMessage(messages[i]);
        
        add(menu);
        add(dialougeList);
        add(chatPanel);
        // debug(chatPanel);
        changeDialouge(0);
    }

    private void debug(Container parent) {
        System.out.println("Debug for " + parent);
        for (Component c : parent.getComponents()) {
            System.out.println("\tChild: " + c);
        }
    }
}
