package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class CustomButton extends JButton {

    Color normalBg;
    Color hoverBg;
    Color pressBg;

    public CustomButton(String text, Font font, Color normalBg, Color hoverBg, Color pressBg, Color fontColor) {
        super(text);

        this.normalBg = normalBg;
        this.hoverBg = hoverBg;
        this.pressBg = pressBg;

        setFont(font);

        setForeground(fontColor);

        setPreferredSize(new Dimension(80, 30));

        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        // Enable antialiasing
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );

        int width = getWidth();
        int height = getHeight();

        // Background color based on button state
        Color background;

        ButtonModel model = getModel();

        if (model.isPressed()) {
            background = pressBg;
        }
        else if (model.isRollover()) {
            background = hoverBg;
        }
        else if (!model.isEnabled()) {
            background = normalBg.darker();
        }
        else {
            background = normalBg;
        }

        // Draw background
        g2.setColor(background);

        g2.fill(new RoundRectangle2D.Float(
                1, 1,
                width - 2, height - 2,
                10, 10
        ));

        // Draw border
        g2.setColor(hoverBg);

        g2.setStroke(new BasicStroke(1.2f));

        g2.draw(new RoundRectangle2D.Float(
                1, 1,
                width - 2, height - 2,
                10, 10
        ));

        // Draw centred text
        g2.setFont(getFont());
        if(!model.isEnabled())
            g2.setColor(getForeground().darker());
        else
            g2.setColor(getForeground());

        FontMetrics fm = g2.getFontMetrics();

        int textX = (width - fm.stringWidth(getText())) / 2;

        int textY = (height - fm.getHeight()) / 2
                + fm.getAscent();

        g2.drawString(getText(), textX, textY);

        g2.dispose();
    }
}
