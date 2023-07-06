package View;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Chat extends JFrame {
    Menu Pan1;
    List Pan2;
    Info Pan3;
    Partners[] User = new Partners[100];
    Mess[] TN = new Mess[100];

    Chat() {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        // Pan1 - Left
        Pan1 = new Menu();
        Pan1.select(1);

        // Pan2 - Center
        Pan2 = new List();

        User[0] = new Partners(new ImageIcon("Icon\\crocodile 32.png"), "Crocodile", 1);
        User[1] = new Partners(new ImageIcon("Icon\\eagle 32.png"), "Eagle", 2);
        User[2] = new Partners(new ImageIcon("Icon\\fox 32.png"), "Fox", 3);
        User[3] = new Partners(new ImageIcon("Icon\\lion 32.png"), "Lion", 4);

        User[0].setColor(new Color(0xedf2fa), new Color(0xedf2fa));

        Pan2.add(User[0]);
        Pan2.add(User[1]);
        Pan2.add(User[2]);
        Pan2.add(User[3]);

        // Pan3 - Right
        Pan3 = new Info();

        JPanel Blank = new JPanel();
            Blank.setBounds(10, 690, 660, 40); 
            Blank.setBackground(new Color(0x313338));

        Pan3.add(Blank);

        JPanel Texter = new JPanel();
            Texter.setBackground(new Color(0x2b2d31));
            Texter.setBounds(10, 650, 660, 40);
            Texter.setLayout(null);
        
        JTextField Text = new JTextField();
        Text.setFont(new Font("Comfortaa",Font.PLAIN,24));
        Text.setBounds(10, 0, 600, 40);
        Text.setBackground(new Color(0x2b2d31));
        Text.setForeground(new Color(0xFFFFFF));
        Text.setCaretColor(new Color(0xFFFFFF));
        Text.setBorder(null);

        JLabel Send = new JLabel(new ImageIcon("Icon\\send 32.png"));
            Send.setBounds(610, 5, 32, 32);

        Texter.add(Text);
        Texter.add(Send);
        Pan3.add(Texter);

        // Pan3.add(new Mess(new ImageIcon("Icon\\crocodile 32.png"), "What's up Monkey", 1));
        // Pan3.add(new Mess(new ImageIcon("Icon\\crocodile 32.png"), "Are you free today?", 2));
        // Pan3.add(new Mess(new ImageIcon("Icon\\monkey 32.png"), "I'm fine, is something wrong?", 3));
        
        TN[0]=new Mess(new ImageIcon("Icon\\crocodile 32.png"), "What's up Monkey", 1);
        TN[1]=new Mess(new ImageIcon("Icon\\crocodile 32.png"), "Are you free today?", 2);
        TN[2]=new Mess(new ImageIcon("Icon\\monkey 32.png"), "I'm fine, is something wrong?", 3);
        
        Pan3.add(TN[0]);
        Pan3.add(TN[1]);
        Pan3.add(TN[2]);

        JLabel Up = new JLabel(new ImageIcon("Icon\\up 32.png"));
            Up.setBounds(630, 550, 32, 32);
            Up.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for (int j=0;j<3;j++){
                        TN[j].setY(TN[j].getY()+1);
                    }
                }
            });

        JLabel Down = new JLabel(new ImageIcon("Icon\\down 32.png"));
            Down.setBounds(630, 600, 32, 32);
            Down.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for (int j=0;j<3;j++){
                        TN[j].setY(TN[j].getY()-1);
                    }
                }
            });

        Pan3.add(Up);
        Pan3.add(Down);

        // Frame
        setTitle("DataBridge");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(1000, 750);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);

        add(Pan1.getPanel());
        add(Pan2.getPanel());
        add(Pan3.getPanel());

        // Thread listener = new Thread(() -> {
        //     try {
        //         connect();
        //     } catch (IOException e) {
        //         // TODO Auto-generated catch block
        //         e.printStackTrace();
        //     }
        // }, "Listener");
        // listener.setPriority(Thread.MIN_PRIORITY);
        // listener.start();
    }

    // void connect() throws IOException {
    //     String sentence_from_client;
    //     String sentence_to_client;
    //     try (// Tạo socket server, chờ tại cổng '6543'
    //             ServerSocket welcomeSocket = new ServerSocket(6543)) {
    //         System.out.println("Server started. Listening on port: 6543");

    //         while (true) {

    //             // chờ yêu cầu từ client
    //             Socket connectionSocket = welcomeSocket.accept();
    //             System.out.println("CLIENT CONNECT!!!");
    //             // Tạo input stream, nối tới Socket
    //             BufferedReader inFromClient = new BufferedReader(
    //                     new InputStreamReader(connectionSocket.getInputStream()));
    //             // Tạo outputStream, nối tới socket
    //             DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

    //             int i = 1;
    //             while (true) {

    //                 // Đọc thông tin từ socket
    //                 sentence_from_client = inFromClient.readLine();

    //                 if (sentence_from_client.equalsIgnoreCase("End")) {
    //                     System.out.println("CLIENT DISCONNECT!!!");
    //                     break;
    //                 }

    //                 System.out.println("FROM CLIENT: " + sentence_from_client);

    //                 TN[i] = new Mess(sentence_from_client, 9);
    //                 Pan3.add(TN[i]);

    //                 for (int j=1;j<i;j++){
    //                     TN[j].setY(TN[j].getY()-1);
    //                 }

    //                 revalidate();
    //                 repaint();
    //                 i += 1;

    //                 sentence_to_client = sentence_from_client + " (Server accepted!)" + '\n';

    //                 // ghi dữ liệu ra socket
    //                 outToClient.writeBytes(sentence_to_client);

    //                 // return;
    //             }
    //         }
    //     }
    // }
}
