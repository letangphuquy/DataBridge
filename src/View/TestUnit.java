package View;

import javax.swing.*;

import Rules.GUI;

import java.awt.*;

public class TestUnit extends JFrame {
    MenuPanel menu;
    ListPanel dialogueList;
    ChatPanel chatPanel;
    JScrollPane contentScrollPane;
    JPanel messageList;
    HoverItem[] users = new HoverItem[100];
    Sentence[] messages = new Sentence[100];

    private final Dimension chatSize = new Dimension(700, GUI.HEIGHT);
    private int numUsers = 4;
    private int numMessages = 20;

    private int prevIdx = -1;

    private void changeDialogue(int idx) {
        if (prevIdx != -1)
            users[prevIdx].swapColor();
        users[idx].swapColor();
        prevIdx = idx;
        // chatPanel.setChatContent(users[idx].getChildren());
        revalidate();
        repaint();
    }

    public TestUnit() {
        setTitle("DataBridge");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(GUI.WIDTH, GUI.HEIGHT);
        setLocationRelativeTo(null);
        setVisible(true);

        menu = new MenuPanel();
        menu.select(2);

        dialogueList = new ListPanel();
        users[0] = new HoverItem(new ImageIcon("images\\crocodile 32.png"), "Crocodile", 1);
        users[1] = new HoverItem(new ImageIcon("images\\eagle 32.png"), "Eagle", 2);
        users[2] = new HoverItem(new ImageIcon("images\\fox 32.png"), "Fox", 3);
        users[3] = new HoverItem(new ImageIcon("images\\lion 32.png"), "Lion", 4);

        for (int i = 0; i < numUsers; i++) {
            dialogueList.addItem(users[i]);
            users[i].setChildren(new ChatPanel(null));
            final int idx = i;
            // users[i].addMouseListener(new MouseAdapter() {
            //     @Override
            //     public void mouseClicked(MouseEvent e) {
            //         changeDialogue(idx);
            //     }
            // });
        }

        messages[0] = new Sentence(new ImageIcon("images\\crocodile 32.png"), "What's up Monkey", 1);
        messages[1] = new Sentence(new ImageIcon("images\\crocodile 32.png"), "Are you free today?", 2);
        messages[2] = new Sentence(new ImageIcon("images\\monkey 32.png"), "I'm fine. What's wrong?", 3);
        for (int i = 3; i < numMessages; i++) {
            messages[i] = new Sentence(new ImageIcon("images\\crocodile 32.png"), "This is my talk show " + i, i + 1);
        }

        chatPanel = (ChatPanel) users[0].getChildren();
        messageList = new JPanel();
        messageList.setLayout(new BoxLayout(messageList, BoxLayout.Y_AXIS));
        messageList.setBackground(new Color(0x2b2d31));
        for (int i = 0; i < numMessages; i++) {
            messageList.add(messages[i].getPanel());
        }

        contentScrollPane = new JScrollPane(messageList);
        contentScrollPane.setPreferredSize(chatSize);
        contentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        contentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        chatPanel.add(contentScrollPane, BorderLayout.CENTER);

        add(menu, BorderLayout.NORTH);
        add(dialogueList, BorderLayout.WEST);
        add(chatPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        new TestUnit();
    }
}
