import org.scilab.forge.jlatexmath.TeXIcon;
import physics.Physics;
import utils.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

class SimPanel extends JPanel {
    Physics physics;

    Color bgColor = SimColors.simPanel;

    boolean showAt, showAc, showPos, showV;

    private final TeXIcon acLabel = Latex.convert("\\vec{a_c}");

    private final TeXIcon atLabel = Latex.convert("\\vec{a_t}");

    private final TeXIcon vLabel = Latex.convert("\\vec{v}");

    private final TeXIcon accLabel = Latex.convert("\\vec{a}");

    final int ppm = 20;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        double offset = 0;
        double cx = getWidth() / 2.0 + offset;
        double cy = getHeight() / 2.0;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawPath(g2, cx, cy);
        drawPosVector(g2, cx, cy);
        drawBall(g2, cx, cy);
        drawAcVector(g2, cx, cy);
        drawAtVector(g2, cx, cy);
        drawAccVector(g2, cx, cy);
        drawVelocityVector(g2, cx, cy);
    }

    void drawPath(Graphics2D g2, double cx, double cy) {
        g2.setColor(SimColors.path);

        float[] dashPattern = {5, 10};
        g2.setStroke(new BasicStroke(
                3,
                BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_BEVEL,
                10.0f,
                dashPattern,
                0.0f
        ));

        //Drawing Path
        g2.draw(
                new Ellipse2D.Double(
                        cx - ppm * physics.pathRadius,
                        cy - ppm * physics.pathRadius,
                        2 * ppm * physics.pathRadius,
                        2 * ppm * physics.pathRadius
                )
        );
    }

    void drawBall(Graphics2D g2, double cx, double cy) {
        double ballX = cx + ppm * (physics.x - physics.ballRadius);
        double ballY = cy + ppm * (physics.y - physics.ballRadius);
        g2.setColor(SimColors.ball);
        g2.fill(new Ellipse2D.Double(
                ballX,
                ballY,
                ppm * 2 * physics.ballRadius,
                ppm * 2 * physics.ballRadius
        ));
    }

    void drawPosVector(Graphics2D g2, double cx, double cy) {
        if (showPos)
            //Drawing Position Vector
            Vector.drawVector(
                    g2,
                    SimColors.pos,
                    new BasicStroke(
                            3.0f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    ),
                    cx, cy,
                    cx + ppm * physics.x,
                    cy + ppm * physics.y,
                    5
            );
    }

    void drawAcVector(Graphics2D g2, double cx, double cy) {
        if (showAc)
            //Drawing Centripetal Acceleration Vector
            Vector.drawVector(
                    g2,
                    this,
                    SimColors.ac,
                    acLabel,
                    cx + ppm * physics.x,
                    cy + ppm * physics.y,
                    physics.ac
            );
    }

    void drawAtVector(Graphics2D g2, double cx, double cy) {
        if (showAt)
            //Drawing Tangential Acceleration Vector
            Vector.drawVector(
                    g2,
                    this,
                    SimColors.at,
                    atLabel,
                    cx + ppm * physics.x,
                    cy + ppm * physics.y,
                    physics.at
            );
    }

    void drawVelocityVector(Graphics2D g2, double cx, double cy) {
        if (showV) {
            double offset = 20;

            //Drawing Velocity Vector
            Vector.drawVector(
                    g2,
                    this,
                    SimColors.velocity,
                    vLabel,
                    cx + ppm * physics.x + offset * Math.cos(physics.theta),
                    cy + ppm * physics.y + offset * Math.sin(physics.theta),
                    physics.v
            );
        }
    }

    void drawAccVector(Graphics2D g2, double cx, double cy) {
        Vector.drawVector(
                g2,
                this,
                SimColors.acc,
                accLabel,
                cx + ppm * physics.x,
                cy + ppm * physics.y,
                physics.acc
        );
    }

    SimPanel(Physics physics) {
        setBackground(bgColor);

        this.physics = physics;

        showAc = showAt = showPos = showV = false;
    }
}