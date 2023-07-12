package Client;

import java.awt.Window;
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
        
        static void changeFrame(String frameName) {
            System.out.println("Changing to the " + frameName + " frame");
            if ("chat".equalsIgnoreCase(frameName)) {
                if (instance.chatFrame == null) 
                    instance.chatFrame = new ChatView();
                instance.changeFrame(instance.chatFrame);
            } else
            if ("drive".equalsIgnoreCase(frameName)) {
                if (instance.driveFrame == null) 
                    instance.driveFrame = new DriveView();
                instance.changeFrame(instance.driveFrame);
            } else
            if ("profile".equalsIgnoreCase(frameName)) {
                if (instance.profileFrame == null) 
                    instance.profileFrame = new ProfileView();
                instance.changeFrame(instance.profileFrame);
            } else {
                System.out.println("Invalid frame name: " + frameName);
            }
        }

        public static void addMenuController(MenuPanel menu) {
            menu.driveButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    changeFrame("drive");
                }
            });
            menu.chatButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    changeFrame("chat");
                }
            });
            menu.profileButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    changeFrame("profile");
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
                            MenuController.changeFrame("chat");
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
                            MenuController.changeFrame("chat");
                            ChatController.initialize();
                        }
                    } catch (Exception e) {}
                }
            });
        }
    }

    public static class ChatController {
        private ChatController() {}
        private static final ChatView gui = (ChatView) instance.currentFrame;

        public static void initialize() {
            for (var conv : Data.conversations.values()) {
                gui.addDialouge(conv);
                for (var message : conv.getMessages()) {
                    gui.addMessage(conv, message);
                }
            }
            gui.changeDialouge(0);
        }
    }

    public static class GeneralController {
        private GeneralController() {}
        public static WindowAdapter closeWindow = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                var frame = (Window) e.getSource();
                frame.setVisible(false);
                frame.dispose();
                instance.terminate();
            }
        };

        public static void loadDataIntoGUI() {

        }
    }
}
