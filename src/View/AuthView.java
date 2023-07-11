package View;

import java.awt.*;

import javax.swing.*;

import Rules.GUI;

public class AuthView extends JFrame {
    private static final int PANEL_WIDTH = 500;
    private static final int PANEL_HEIGHT = 500;

    private JTextField usernameTextfield = new JTextField();
    private JPasswordField passwordTextfield = new JPasswordField();
    public JButton loginButton;
    public JButton registerButton;

    public String getUsername() {
        return usernameTextfield.getText();
    }

    public String getPassword() {
        return String.valueOf(passwordTextfield.getPassword());
    }

    public AuthView() {
        JLabel welcomeLabel = new JLabel("Welcome", SwingConstants.CENTER);
            welcomeLabel.setBounds(0, 20, PANEL_WIDTH, 32);
            welcomeLabel.setFont(GUI.H1_FONT);
            welcomeLabel.setForeground(Color.WHITE);

        JLabel greetLabel = new JLabel("Pleasure to meet you again!", SwingConstants.CENTER);
            greetLabel.setBounds(0, 60, PANEL_WIDTH, 32);
            greetLabel.setForeground(Color.WHITE);
            Font greetFont = GUI.H2_FONT.deriveFont(Font.PLAIN);
            greetLabel.setFont(greetFont);

        final int INPUT_WIDTH = 400;
        JLabel usernameLabel = new JLabel("USERNAME");
            usernameLabel.setBounds(50, 125, INPUT_WIDTH, 24);
            usernameLabel.setFont(GUI.H3_FONT);
            usernameLabel.setForeground(Color.WHITE);

        JLabel passwordLabel = new JLabel("PASSWORD");
            passwordLabel.setBounds(50, 225, INPUT_WIDTH, 24);
            passwordLabel.setFont(GUI.H3_FONT);
            passwordLabel.setForeground(Color.WHITE);

        //User & Pass
        setupTextfield(usernameTextfield, 155, INPUT_WIDTH);
        setupTextfield(passwordTextfield, 255, INPUT_WIDTH);
        
        final int BUTTON_WIDTH = 250;
        final int BUTTON_X = (PANEL_WIDTH - BUTTON_WIDTH) / 2;
        Color buttonColor = new Color(0x3c78d8);
        loginButton = new JButton("LOGIN");
            loginButton.setFont(GUI.H2_FONT);
            loginButton.setForeground(Color.WHITE);
            loginButton.setBackground(buttonColor);
            loginButton.setBounds(BUTTON_X, 330, BUTTON_WIDTH, 40);

        JLabel orLabel = new JLabel("OR", SwingConstants.CENTER);
            orLabel.setBounds(BUTTON_X, 365, BUTTON_WIDTH, 40);
            orLabel.setFont(GUI.H3_FONT);
            orLabel.setForeground(Color.WHITE);

        registerButton = new JButton("REGISTER");
            registerButton.setForeground(Color.WHITE);
            registerButton.setFont(GUI.H2_FONT);
            registerButton.setBackground(buttonColor);
            registerButton.setBounds(BUTTON_X, 405, BUTTON_WIDTH, 40);
        
        JPanel loginPanel = new JPanel();
            loginPanel.setBackground(GUI.COLOR_LIST);
            loginPanel.setBounds((GUI.WIDTH - PANEL_WIDTH) / 2, (GUI.HEIGHT - PANEL_HEIGHT) / 2, PANEL_WIDTH, PANEL_HEIGHT);
            loginPanel.setLayout(null);
            loginPanel.add(welcomeLabel);
            loginPanel.add(greetLabel);
            loginPanel.add(usernameLabel);
            loginPanel.add(passwordLabel);
            loginPanel.add(usernameTextfield);
            loginPanel.add(passwordTextfield);
            loginPanel.add(loginButton);
            loginPanel.add(orLabel);
            loginPanel.add(registerButton);
            loginPanel.setVisible(true);
    
        // Error: Background overlaps important components
        ImageIcon backgroundImage = new ImageIcon(
            new ImageIcon("images\\BG.jpg").getImage().getScaledInstance(GUI.WIDTH, GUI.HEIGHT, Image.SCALE_SMOOTH)
        );
        JPanel backgroundPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage.getImage(),0, 0, null);
            }
        };
        setContentPane(backgroundPanel);

        getContentPane().add(loginPanel);

        setTitle("DataBridge");
        getContentPane().setLayout(null);
        setSize(GUI.WIDTH, GUI.HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupTextfield(JTextField textfield, int y, int width) {
        textfield.setFont(GUI.P_MONO);
        textfield.setBounds(50, y, width, 40);
        textfield.setBackground(GUI.COLOR_MENU);
        textfield.setForeground(Color.WHITE);
        textfield.setCaretColor(Color.WHITE);
        textfield.setBorder(null);
    }
}