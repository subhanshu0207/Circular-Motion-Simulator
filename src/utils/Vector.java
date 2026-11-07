package utils;

import org.scilab.forge.jlatexmath.TeXIcon;

import java.awt.*;
import java.awt.geom.Line2D;

public class Vector {
    public double magnitude;
    public double angle;   //in radians

    public static void drawVector(
            Graphics2D g2,
            Component panel,
            Color color,
            TeXIcon label,
            double x, double y,
            Vector vector
    ) {
        if (vector.magnitude != 0) {
            double angle = vector.angle;
            double x1 = x;
            double y1 = y;
            double x2 = x1 + vector.magnitude * Math.cos(angle);
            double y2 = y1 + vector.magnitude * Math.sin(angle);

            g2.setColor(color);
            g2.setStroke(new BasicStroke(
                    2.5f,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND
            ));

            g2.draw(new Line2D.Double(x1, y1, x2, y2));

            double arrowHeadLength = 5;
            double arrowAngle = Math.toRadians(45);

            double angle1 = angle + Math.PI - arrowAngle;
            double angle2 = angle + Math.PI + arrowAngle;

            double x3 = x2 + arrowHeadLength * Math.cos(angle1);
            double y3 = y2 + arrowHeadLength * Math.sin(angle1);

            double x4 = x2 + arrowHeadLength * Math.cos(angle2);
            double y4 = y2 + arrowHeadLength * Math.sin(angle2);

            g2.draw(new Line2D.Double(x2, y2, x3, y3));
            g2.draw(new Line2D.Double(x2, y2, x4, y4));

            double offset = 12;

            double labelCenterX = x2 - offset * Math.sin(angle);
            double labelCenterY = y2 + offset * Math.cos(angle);

            int labelX = (int) (labelCenterX
                    - label.getIconWidth() / 2.0);

            int labelY = (int) (labelCenterY
                    - label.getIconHeight() / 2.0);

            label.setForeground(g2.getColor());
            label.paintIcon(panel, g2, labelX, labelY);
        }
    }

    public static void drawVector(
            Graphics2D g2,
            Color color,
            Stroke stroke,
            double x1, double y1,
            double x2, double y2,
            double arrowHeadLength
    ) {
        g2.setColor(color);
        g2.setStroke(stroke);

        g2.draw(new Line2D.Double(x1, y1, x2, y2));

        double arrowAngle = Math.toRadians(45);

        double angle = Math.atan2(y2 - y1, x2 - x1);

        double angle1 = angle + Math.PI - arrowAngle;
        double angle2 = angle + Math.PI + arrowAngle;

        double x3 = x2 + arrowHeadLength * Math.cos(angle1);
        double y3 = y2 + arrowHeadLength * Math.sin(angle1);

        double x4 = x2 + arrowHeadLength * Math.cos(angle2);
        double y4 = y2 + arrowHeadLength * Math.sin(angle2);

        g2.draw(new Line2D.Double(x2, y2, x3, y3));
        g2.draw(new Line2D.Double(x2, y2, x4, y4));
    }

    public Vector(double magnitude, double angle) {
        this.magnitude = magnitude;
        this.angle = angle;
    }
}