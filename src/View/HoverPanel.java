package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HoverPanel extends JPanel {
    private Color normalColor;
    private Color hoverColor;
    
    public void setHoverColor(Color hoverColor) {
        this.hoverColor = hoverColor;
    }

    public void setNormalColor(Color normalColor) {
        this.normalColor = normalColor;
        setBackground(normalColor);
    }

    

    public HoverPanel() {
        normalColor =  Color.WHITE;
        hoverColor = Color.LIGHT_GRAY;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalColor);
            }
        });

        setBackground(normalColor);
    }
}
