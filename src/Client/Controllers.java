package Client;

import java.awt.event.*;

import javax.swing.JOptionPane;

import View.AuthView;
import View.ChatView;
import View.DriveView;
import View.MenuPanel;
import View.ProfileView;

public class Controllers {
    private static final Client instance = Client.instance;

    public static class MenuController {
        private MenuController() {}
        
        public static void addMenuController(MenuPanel menu) {
            menu.driveButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    instance.changeFrame(new DriveView());
                }
            });
            menu.chatButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    instance.changeFrame(new ChatView());
                }
            });
            menu.profileButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    instance.changeFrame(new ProfileView());
                }
            });
        }
    }

    public static class AuthController {
        private AuthController() {}
        private static final AuthView gui = (AuthView) instance.currentFrame;

        private static boolean checkValid() {
            String username = gui.getUsername();
            String password = gui.getPassword();
            if (username.length() > 0 && password.length() > 0) return true;
            JOptionPane.showMessageDialog(null, "Please enter username and password", "Input required", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        public static void initialize() {
            gui.loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    try {
                        if (checkValid() && Authenticator.login(gui.getUsername(), gui.getPassword())) {
                            System.out.println("Logged in successfully");
                            instance.changeFrame(new ChatView());
                            ChatController.initialize();
                        }
                    } catch (Exception e) {}
                }
            });
            gui.registerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    try {
                        if (checkValid() && Authenticator.register(gui.getUsername(), gui.getPassword())) {
                            System.out.println("Registered and logged in successfully");
                            instance.changeFrame(new ChatView());
                            ChatController.initialize();
                        }
                    } catch (Exception e) {}
                }
            });
        }
    }

    public static class ChatController {
        private ChatController() {}
        // private static final ChatView gui = (ChatView) instance.currentFrame;

        public static void initialize() {
            
        }
    }

    public static class GeneralController {
        private GeneralController() {}
        public static WindowAdapter closeWindow = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                instance.terminate();
            }
        };

        public static void loadDataIntoGUI() {

        }
    }
}
