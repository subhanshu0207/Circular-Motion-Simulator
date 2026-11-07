package utils;

import org.scilab.forge.jlatexmath.TeXIcon;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class GraphPanel extends JPanel {
    Dimension dimension;

    Color bgColor;
    Color graphColor;

    double xTickSpacing = 5;
    double yTickSpacing = 5;

    double scaleX = 7;
    double scaleY = 7;

    TeXIcon xIcon = Latex.convert("x", 15);
    TeXIcon yIcon = Latex.convert("f(x)", 15);

    int scrollX = 0;
    int scrollY = 0;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double offsetX = (80 / 2000.0) * getWidth();
        double offsetY = (20 / 2000.0) * getHeight();

        double originX = offsetX;
        double originY = getHeight() / 2.0;

        g2.setColor(graphColor);
        g2.setStroke(new BasicStroke(1.8f));
        plotGraph(g2, originX, originY, offsetX);

        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(1f));
        drawAxes(g2, originX, originY, offsetX, offsetY);

        g2.setColor(new Color(0, 0, 0, 92));
        g2.setStroke(new BasicStroke(0.9f));
        drawTicks(g2, originX, originY, offsetX, offsetY);

        drawIcons(g2, originX, originY, offsetX);
    }

    private void drawAxes(Graphics2D g2, double originX, double originY, double offsetX, double offsetY) {
        //X-axis
        Vector.drawVector(
                g2,
                g2.getColor(),
                g2.getStroke(),
                originX,
                originY,
                (getWidth() - offsetX),
                originY,
                8
        );

        //Upward Y-axis
        Vector.drawVector(
                g2,
                g2.getColor(),
                g2.getStroke(),
                originX,
                originY,
                originX,
                offsetY,
                8
        );

        //Downward Y-axis
        Vector.drawVector(
                g2,
                g2.getColor(),
                g2.getStroke(),
                originX,
                originY,
                originX,
                (getHeight() - offsetY),
                8
        );
    }

    protected double calculate(double x) {
        return 0;
    }

    private void plotGraph(Graphics2D g2, double originX, double originY, double offsetX) {
        Path2D path = new Path2D.Double();
        boolean firstPoint = true;

        for (double x = originX; x <= dimension.width - offsetX; x++) {

            double y = originY - scaleY * calculate(((x - originX) / scaleX));

            if (firstPoint) {
                path.moveTo(x, y);
                firstPoint = false;
            } else
                path.lineTo(x, y);
        }

        g2.draw(path);
    }

    private void drawTicks(Graphics2D g2, double originX, double originY, double offsetX, double offsetY) {
        g2.setFont(SimFonts.tick);
        FontMetrics fm = g2.getFontMetrics();

        int lapsX = (int) (((getWidth() - 2 * offsetX) / xTickSpacing) / scaleX);
        int lapsHalfY = (int) (((getHeight() / 2.0 - offsetY) / yTickSpacing) / scaleY);

        double lengthOfTick = 7;

        //X ticks
        for (int i = 1; i <= lapsX; i++) {
            double tickX = originX + i * xTickSpacing * scaleX;

            g2.draw(
                    new Line2D.Double(
                            tickX, (originY - lengthOfTick / 2),
                            tickX, (originY + lengthOfTick / 2)
                    )
            );

            String label = String.format("%.2f", i * xTickSpacing);
            g2.drawString(
                    label,
                    (float) (tickX - fm.stringWidth(label) / 2.0), (float) (originY + lengthOfTick / 2 + fm.getHeight())
            );
        }

        //Y ticks(down)
        for (int i = 1; i <= lapsHalfY; i++) {
            double tickY = originY + i * yTickSpacing * scaleY;

            g2.draw(
                    new Line2D.Double(
                            (originX - lengthOfTick / 2), tickY,
                            (originX + lengthOfTick / 2), tickY
                    )
            );

            String label = "-" + String.format("%.2f", i * yTickSpacing);
            g2.drawString(
                    label,
                    (float) (originX - lengthOfTick / 2 - fm.stringWidth(label) - 2), (float) (tickY + (fm.getAscent() - fm.getDescent()) / 2.0)
            );
        }

        //Y ticks(up)
        for (int i = 1; i <= lapsHalfY; i++) {
            double tickY = originY - i * yTickSpacing * scaleY;

            g2.draw(
                    new Line2D.Double(
                            (originX - lengthOfTick / 2), tickY,
                            (originX + lengthOfTick / 2), tickY
                    )
            );

            String label = String.format("%.2f", i * yTickSpacing);
            g2.drawString(
                    label,
                    (float) (originX - lengthOfTick / 2 - fm.stringWidth(label) - 2), (float) (tickY + (fm.getAscent() - fm.getDescent()) / 2.0)
            );
        }
    }

    private void drawIcons(Graphics2D g2, double originX, double originY, double offsetX) {

        Container parent = getParent();
        int parentWidth = parent.getWidth();

        int x = (parentWidth - xIcon.getIconWidth() + scrollX);

        int y = (int) (originY - xIcon.getIconHeight());

        if (x <= (getWidth() - offsetX))
            xIcon.paintIcon(this, g2, x, y);
        else
            xIcon.paintIcon(this, g2, getWidth() - (int) offsetX, y);

        int x2 = (int) (originX + 7);

        int y2 = scrollY + 5;

        yIcon.paintIcon(this, g2, x2, y2);
    }

    public void setScrollPosition(int x, int y) {
        scrollX = x;
        scrollY = y;
        repaint();
    }

    public void setScales(double scaleX, double scaleY) {
        if (scaleX > 0 && scaleY > 0) {
            this.scaleX = scaleX;
            this.scaleY = scaleY;
        }
    }

    public void setTickSpacing(double xTickSpacing, double yTickSpacing) {
        if (xTickSpacing > 0 && yTickSpacing > 0) {
            this.xTickSpacing = xTickSpacing;
            this.yTickSpacing = yTickSpacing;
        }
    }

    public void setIcon(TeXIcon xIcon, TeXIcon yIcon) {
        this.xIcon = xIcon;
        this.yIcon = yIcon;
    }

    public GraphPanel(Dimension dimension, Color bgColor, Color graphColor) {
        this.dimension = dimension;
        this.bgColor = bgColor;
        this.graphColor = graphColor;

        setPreferredSize(dimension);
        setBackground(bgColor);
    }
}