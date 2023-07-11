package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import Rules.GUI;

public class ChatPanel extends MainPanel {
    private static final int WIDTH = 650;

    public JButton sendButton;
    public JTextField chatTextfield;
    private MessagePanel messageList;

    public JTextField searchField;
    public JButton searchButton;

    public ChatPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(WIDTH, GUI.HEIGHT));
        setBackground(GUI.COLOR_MAIN);
        
        Color textInputColor = GUI.COLOR_LIST.darker();
        JPanel textInputer = new JPanel();
        textInputer.setLayout(null);
        Dimension textInputerSize = new Dimension(650, 82); // weird shifting down, so I pushed it up
        textInputer.setPreferredSize(textInputerSize);
        textInputer.setSize(textInputerSize);
        textInputer.setBackground(GUI.COLOR_MAIN);
        
        chatTextfield = new JTextField();
        chatTextfield.setFont(GUI.P_SANS);
        chatTextfield.setBounds(10, 0, 630, 40);
        chatTextfield.setBackground(textInputColor);
        chatTextfield.setForeground(new Color(0xFFFFFF));
        chatTextfield.setCaretColor(new Color(0xFFFFFF));
        chatTextfield.setBorder(null);

        sendButton = new JButton(new ImageIcon("images\\send 32.png"));
        sendButton.setBounds(640, 0, 32, 40);
        sendButton.setBackground(textInputColor);
        sendButton.setBorder(null);

        textInputer.add(chatTextfield);
        textInputer.add(sendButton);

        messageList = new MessagePanel();
        var scroller = new JScrollPane(messageList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setPreferredSize(new Dimension(WIDTH, GUI.HEIGHT));
        scroller.getVerticalScrollBar().setUnitIncrement(20);
        // scroller.setBackground(GUI.COLOR_MAIN);
        scroller.setBorder(null);

        var searchPanel = new JPanel();
        searchPanel.setLayout(null);

        searchField = new JTextField();
        searchField.setToolTipText("Search for messages");
        searchField.setFont(GUI.P_MONO);
        searchField.setBounds(10, 30, 630, 40);
        searchField.setBackground(textInputColor);
        searchField.setForeground(new Color(0xFFFFFF));
        searchField.setCaretColor(new Color(0xFFFFFF));
        searchField.setBorder(null);

        searchButton = new JButton(new ImageIcon("images\\search 32.png"));
        searchButton.setBounds(640, 30, 32, 40);
        searchButton.setBackground(textInputColor);
        searchButton.setBorder(null);
        
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.setPreferredSize(new Dimension(650, 100));
        searchPanel.setBackground(GUI.COLOR_MAIN);
        searchPanel.setBorder(BorderFactory.createDashedBorder(GUI.COLOR_LIST, 3, 2, 1, false));

        assert (getLayout() instanceof BorderLayout);

        add(textInputer, BorderLayout.SOUTH);
        add(scroller, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);
    }

    class MessagePanel extends JPanel {
        int numMessages = 0;
        MessagePanel() {
            setLayout(null);
            setBackground(GUI.COLOR_MAIN);
            resize();
        }
        private void resize() {
            setPreferredSize(new Dimension(650, numMessages * 50 + 100));
        }
        void enplace() {
            ++numMessages;
            resize();
        }
    }

    public void addMessage(Sentence sentence) {
        sentence.setY(messageList.numMessages);
        messageList.add(sentence.getPanel());
        messageList.enplace();
    }
}
