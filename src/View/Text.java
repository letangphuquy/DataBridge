import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Text {
    private JTextField text;
    Text(int y){
        text = new JTextField();
        text.setFont(new Font("Comfortaa",Font.PLAIN,16));
        text.setBounds(50, y, 400, 40);
        text.setBackground(new Color(0x1e1f22));
        text.setForeground(new Color(0xFFFFFF));
        text.setCaretColor(new Color(0xFFFFFF));
        text.setBorder(null);
    }

    public String getString() {
        return text.getText();
    }

    public JTextField getText() {
        return text;
    }
}
