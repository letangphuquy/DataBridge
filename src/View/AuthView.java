package View;

import java.awt.*;

import javax.swing.*;

import Rules.GUI;

public class AuthView extends JFrame {
    private static final int PANEL_WIDTH = 500;
    private static final int PANEL_HEIGHT = 500;

    private Font paragraphFont = new Font("Comfortaa",Font.BOLD,14);
    private Font subheadingFont = new Font("Comfortaa",Font.PLAIN,24);
    private Font headingFont = new Font("Comfortaa", Font.BOLD, 32);

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
            welcomeLabel.setFont(headingFont);
            welcomeLabel.setForeground(new Color(0xFFFFFF));

        JLabel greetLabel = new JLabel("Pleasure to meet you again!", SwingConstants.CENTER);
            greetLabel.setBounds(0, 60, PANEL_WIDTH, 32);
            greetLabel.setFont(subheadingFont);
            greetLabel.setForeground(new Color(0xFFFFFF));

        final int INPUT_WIDTH = 400;
        JLabel usernameLabel = new JLabel("USERNAME");
            usernameLabel.setBounds(50, 100, INPUT_WIDTH, 24);
            usernameLabel.setFont(paragraphFont);
            usernameLabel.setForeground(new Color(0xFFFFFF));

        JLabel passwordLabel = new JLabel("PASSWORD");
            passwordLabel.setBounds(50, 200, INPUT_WIDTH, 24);
            passwordLabel.setFont(paragraphFont);
            passwordLabel.setForeground(new Color(0xFFFFFF));

        //User & Pass
        setupTextfield(usernameTextfield, 130, INPUT_WIDTH);
        setupTextfield(passwordTextfield, 230, INPUT_WIDTH);
        
        final int BUTTON_WIDTH = 250;
        final int BUTTON_X = (PANEL_WIDTH - BUTTON_WIDTH) / 2;
        loginButton = new JButton("LOGIN");
            loginButton.setFont(new Font("Comfortaa",Font.BOLD,24));
            loginButton.setForeground(new Color(0xFFFFFF));
            loginButton.setBackground(new Color(0x3c78d8));
            loginButton.setBounds(BUTTON_X, 330, BUTTON_WIDTH, 40);

        JLabel orLabel = new JLabel("OR", SwingConstants.CENTER);
            orLabel.setBounds(BUTTON_X, 365, BUTTON_WIDTH, 40);
            orLabel.setFont(paragraphFont);
            orLabel.setForeground(new Color(0xFFFFFF));

        registerButton = new JButton("REGISTER");
            registerButton.setForeground(Color.WHITE);
            registerButton.setFont(new Font("Dialog", Font.BOLD, 24));
            registerButton.setBackground(new Color(60, 120, 216));
            registerButton.setBounds(BUTTON_X, 405, BUTTON_WIDTH, 40);
        
        JPanel loginPanel = new JPanel();
            loginPanel.setBackground(new Color(0x2b2d31));
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
            new ImageIcon(GUI.IMAGE_ROOT + "BG.jpg").getImage().getScaledInstance(GUI.WIDTH, GUI.HEIGHT, Image.SCALE_SMOOTH)
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
        textfield.setFont(new Font("Comfortaa",Font.PLAIN,16));
        textfield.setBounds(50, y, width, 40);
        textfield.setBackground(new Color(0x1e1f22));
        textfield.setForeground(new Color(0xFFFFFF));
        textfield.setCaretColor(new Color(0xFFFFFF));
        textfield.setBorder(null);
    }
}