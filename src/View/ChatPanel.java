package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Rules.GUI;

public class ChatPanel extends MainPanel {
    public JButton sendButton;
    public ChatPanel(Component content) {
        setLayout(new BorderLayout());
        // setLayout(null);
        setPreferredSize(new Dimension(650, GUI.HEIGHT));
        setBackground(GUI.COLOR_MAIN);
        
        Color textInputColor = GUI.COLOR_LIST.darker();
        JPanel textInputer = new JPanel();
        textInputer.setLayout(null);
        Dimension textInputerSize = new Dimension(650, 82); // weird shifting down, so I pushed it up
        textInputer.setPreferredSize(textInputerSize);
        textInputer.setSize(textInputerSize);
        textInputer.setBackground(GUI.COLOR_MAIN);
        
        JTextField chatTextfield = new JTextField();
        chatTextfield.setFont(new Font("Comfortaa",Font.PLAIN,16));
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
        if (getLayout() instanceof BorderLayout) {
            // if (content != null)
            //     System.out.println("The chat panel is using BorderLayout");
            add(textInputer, BorderLayout.SOUTH);
            if (content != null) {
                content.setBackground(GUI.COLOR_MAIN);
                add(content, BorderLayout.CENTER);
            }
            JPanel north = new JPanel();
            north.setBackground(Color.RED);
            north.setPreferredSize(new Dimension(700, 100));
            add(north, BorderLayout.NORTH);
        }
        else add(textInputer);
    }
}
