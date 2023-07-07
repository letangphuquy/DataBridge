package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.Border;

import Rules.GUI;

public class ChatView extends JFrame {
    MenuPanel menu;
    ListPanel dialougeList;
    ChatPanel chatPanel;
    JScrollPane contentScroller; JPanel messageList;
    HoverItem[] users = new HoverItem[100];
    Sentence[] messages = new Sentence[100];

    private final Dimension chatSize = new Dimension(700, GUI.HEIGHT);
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
        // contentScroller = new JScrollPane(chatPanel,  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // remove(contentScroller);
        // add(contentScroller);
        add(chatPanel);
        repaint();
    }

    public ChatView() {
        setTitle("DataBridge");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(GUI.WIDTH, GUI.HEIGHT);
        // setResizable(false);
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
            users[i].setChildren(new ChatPanel(null));
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
        messageList = new JPanel();
        messageList.setLayout(null);
        // messageList.setLayout(new BoxLayout(messageList, BoxLayout.Y_AXIS));
        var messageSize = new Dimension(700, numMessages * 50 + 100);
        // messageList.setPreferredSize(new Dimension(700, 500));
        messageList.setPreferredSize(messageSize);
        messageList.setSize(messageSize);
        // messageList.setBackground(new Color(0x2b2d31));
        // messageList.setBackground(Color.BLUE); // DEBUG
        for (int i = 0; i < numMessages; i++) {
            messageList.add(messages[i].getPanel());
        }
        // /*
        JLabel Up = new JLabel(new ImageIcon("images\\up 32.png"));
            Up.setBounds(630, 550, 32, 32);
            Up.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for (int j = 0; j < numMessages; j++){
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
                    for (int j = 0; j < numMessages; j++){
                        messages[j].setY(messages[j].getY() - 1);
                        //conversation.repaint();
                    }
                }
            });
        chatPanel.add(Up);
        chatPanel.add(Down);
        // */
        
        /*
        contentScroller = new JScrollPane(messageList,  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentScroller.setPreferredSize(chatSize);
        contentScroller.getVerticalScrollBar().setUnitIncrement(20);
        chatPanel.add(contentScroller, BorderLayout.CENTER);
         */
        chatPanel.add(messageList, BorderLayout.CENTER);

        add(menu);
        add(dialougeList);
        add(chatPanel);
        // setContentPane(contentScroller);
        // debug(contentScroller);
        // changeDialouge(0);
        debug(chatPanel);
        revalidate();
        repaint();
    }

    private void debug(Container parent) {
        System.out.println("Debug for " + parent);
        for (Component c : parent.getComponents()) {
            System.out.println("\tChild: " + c);
        }
    }
}
