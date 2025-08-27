package GUI;

import javax.swing.*;
import java.awt.*;

public class DesktopConFondo extends JDesktopPane {
    private Image imagenFondo;

    public DesktopConFondo(ImageIcon icono) {
        this.imagenFondo = icono.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

