package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Rules.GUI;

public class ChatPanel extends MainPanel {
    public JButton sendButton;
    public ChatPanel(JPanel content) {
        // super(new BorderLayout());
        // setLayout(new BorderLayout());
        setLayout(null);
        // setBounds(0, 0, 700, GUI.HEIGHT); // override
        setPreferredSize(new Dimension(700, GUI.HEIGHT));
        
        Color textInputColor = new Color(0x2b2d31);
        JPanel textInputer = new JPanel();
        textInputer.setLayout(null);
        textInputer.setBounds(10, 650, 660, 40);
        textInputer.setBackground(textInputColor);
        
        JTextField chatTextfield = new JTextField();
        chatTextfield.setFont(new Font("Comfortaa",Font.PLAIN,24));
        chatTextfield.setBounds(10, 0, 600, 40);
        chatTextfield.setBackground(textInputColor);
        chatTextfield.setForeground(new Color(0xFFFFFF));
        chatTextfield.setCaretColor(new Color(0xFFFFFF));
        chatTextfield.setBorder(null);

        sendButton = new JButton(new ImageIcon("images\\send 32.png"));
        sendButton.setBounds(610, 5, 32, 32);
        sendButton.setBackground(textInputColor);
        sendButton.setBorder(null);

        textInputer.add(chatTextfield);
        textInputer.add(sendButton);
        if (getLayout() instanceof BorderLayout) {
            add(textInputer, BorderLayout.SOUTH);
            if (content != null) add(content, BorderLayout.CENTER);
        }
        else add(textInputer);
    }
}
