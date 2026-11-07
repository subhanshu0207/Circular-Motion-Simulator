import org.scilab.forge.jlatexmath.TeXIcon;
import physics.Physics;
import utils.Latex;
import utils.SimColors;
import utils.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

class SimPanel extends JPanel {
    private boolean showAt, showAc, showPos, showV, showAcc;

    private final Physics physics;

    private final TeXIcon acLabel = Latex.convert("\\vec{a_c}");

    private final TeXIcon atLabel = Latex.convert("\\vec{a_t}");

    private final TeXIcon vLabel = Latex.convert("\\vec{v}");

    private final TeXIcon accLabel = Latex.convert("\\vec{a}");

    private final double ppm = 20;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        double cx = getWidth() / 2.0;
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

    private void drawPath(Graphics2D g2, double cx, double cy) {
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

    private void drawBall(Graphics2D g2, double cx, double cy) {
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

    private void drawPosVector(Graphics2D g2, double cx, double cy) {
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

    private void drawAcVector(Graphics2D g2, double cx, double cy) {
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

    private void drawAtVector(Graphics2D g2, double cx, double cy) {
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

    private void drawVelocityVector(Graphics2D g2, double cx, double cy) {
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

    private void drawAccVector(Graphics2D g2, double cx, double cy) {
        if (showAcc)
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

    public boolean isShowAc() {
        return showAc;
    }

    public void setShowAc(boolean showAc) {
        this.showAc = showAc;
    }

    public boolean isShowAt() {
        return showAt;
    }

    public void setShowAt(boolean showAt) {
        this.showAt = showAt;
    }

    public boolean isShowPos() {
        return showPos;
    }

    public void setShowPos(boolean showPos) {
        this.showPos = showPos;
    }

    public boolean isShowV() {
        return showV;
    }

    public void setShowV(boolean showV) {
        this.showV = showV;
    }

    public boolean isShowAcc() {
        return showAcc;
    }

    public void setShowAcc(boolean showAcc) {
        this.showAcc = showAcc;
    }

    SimPanel(Physics physics) {
        setBackground(SimColors.simPanelBg);

        this.physics = physics;

        showAc = showAt = showPos = showV = false;
    }
}