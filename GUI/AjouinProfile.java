package GUI;

import datatype.Ajouin;
import datatype.EAjouinIdentity;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class AjouinProfile extends JPanel {
    private final int WIDTH = 200;
    private final int HEIGHT = 250;
    private String identity;
    private Font identityFont;
    private Color identityColor;

    public AjouinProfile(Ajouin ajouin) {
        super();

        this.identity = ajouin.getIdentity().toString();
        this.identityColor = ajouin.getIdentity() == EAjouinIdentity.PROFESSOR ? new Color(0xc3c3fd) : new Color(0xebebfe);
        this.identityFont = new Font(null, Font.BOLD, 25);

        setSize(WIDTH, HEIGHT);
        setLayout(new GridLayout(2, 1));
        setBackground(new Color(200, 200, 200));
        setBorder(BorderFactory.createEmptyBorder(130, 0, 50, 0));

        JLabel nameLabel = new JLabel(ajouin.getName());
        JLabel depLabel = new JLabel(ajouin.getMajor().toString());
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        depLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(nameLabel);
        add(depLabel);

        Font defaultFont = nameLabel.getFont();
        nameLabel.setFont(new Font(defaultFont.getFontName(), Font.BOLD, 20));
        depLabel.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, 15));

        setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        int radius = 36;
        int centerX = WIDTH / 2;
        int centerY = HEIGHT / 3;
        Ellipse2D circle = new Ellipse2D.Double();
        circle.setFrameFromCenter(centerX, centerY, centerX + radius, centerY + radius);

        Color defaultColor = g2.getColor();
        g2.setColor(identityColor);
        g2.fill(circle);
        g2.setColor(defaultColor);
        g.setFont(identityFont);
        g.drawString(this.identity, (int) (centerX - radius * 0.6), centerY + 11);
    }


}
