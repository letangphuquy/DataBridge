package View;


import java.awt.event.*;
import java.util.HashMap;

import javax.swing.*;

import Model.FileLink;
import Model.Group;
import Model.Message;
import Model.NormalMessage;
import Model.Recipient;
import Model.User;
import Rules.GUI;

public class ChatView extends JFrame {
    MenuPanel menu;
    ListPanel dialougeList;
    ChatPanel chatPanel;
    HoverItem[] users = new HoverItem[100];

    private int numUsers = 0;

    private int prevIdx = -1;
    public void changeDialouge(int idx) {
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

    private static final String[] ANIMALS = new String[]{"crocodile", "eagle", "fox", "lion", "monkey"};
    private static HashMap<Recipient, Integer> userIDs = new HashMap<>();

    public void addDialouge(Recipient sender) {
        var avatar = new ImageIcon("images\\" + ANIMALS[numUsers % 5] + " 32.png");
        String name = (sender instanceof User) ? ((User) sender).getUsername() : ((Group) sender).getName();
        var newUser = new HoverItem(avatar, name, numUsers + 1);
        dialougeList.addItem(newUser);
        newUser.setChildren(new ChatPanel());
        final int idx = numUsers;
        newUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                changeDialouge(idx);
            }
        });
        userIDs.put(sender, numUsers);
        users[numUsers++] = newUser;
    }

    public void addMessage(Recipient dialouge, Message message) {
        int idx = userIDs.get(dialouge);
        var userChat = (ChatPanel) users[idx].getChildren();
        String content = (message instanceof NormalMessage) ? ((NormalMessage) message).getContent() : "File " + ((FileLink) message).getFileID();
        Sentence sentence = new Sentence(users[idx].getAvatar(),content, 0);
        userChat.addMessage(sentence);
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
        chatPanel = new ChatPanel();
        
        /*
        messages[0] = new Sentence(new ImageIcon("images\\crocodile 32.png"), "What's up Monkey", 1);
        messages[1] = new Sentence(new ImageIcon("images\\crocodile 32.png"), "Are you free today?", 2);
        messages[2] = new Sentence(new ImageIcon("images\\monkey 32.png"), "I'm fine. What's wrong?", 3);
        for (int i = 3; i < numMessages; i++) {
            messages[i] = new Sentence(new ImageIcon("images\\crocodile 32.png"), "This is my talkshow " + i, i+1);
        }

        chatPanel = (ChatPanel) users[0].getChildren();
        for (int i = 0; i < numMessages; i++) 
            chatPanel.addMessage(messages[i]);
         */
        
        add(menu);
        add(dialougeList);
        add(chatPanel);
        // debug(chatPanel);
    }
    /*
    private void debug(Container parent) {
        System.out.println("Debug for " + parent);
        for (Component c : parent.getComponents()) {
            System.out.println("\tChild: " + c);
        }
    }
     */
}
