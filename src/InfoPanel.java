import physics.Physics;
import utils.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class InfoPanel extends JPanel {
    final int infoPanelWidth = 250;
    final int padding = 40;

    Graph angleTimeGraph;
    Graph omegaTimeGraph;
    Graph alphaTimeGraph;

    private JScrollPane magScrollPanel;

    private JPanel magPanel;
    private JPanel magHeaderPanel;
    private JPanel magQuantsPanel;
        private JLabel thetaInRadiansLabel;
        private JLabel thetaInRadiansValue;

        private JLabel thetaInDegreesLabel;
        private JLabel thetaInDegreesValue;

        private JLabel omegaLabel;
        private JLabel omegaValue;

        private JLabel alphaLabel;
        private JLabel alphaValue;

        private JLabel acLabel;
        private JLabel acValue;

        private JLabel atLabel;
        private JLabel atValue;

        private JLabel posLabel;
        private JLabel posValue;

        private JLabel vLabel;
        private JLabel vValue;

        private JLabel timeScaleLabel;
        private JLabel timeScaleValue;

    private Physics physics;

    public InfoPanel(Physics physics) {
        setPreferredSize(new Dimension(infoPanelWidth, 0));
        setBackground(Color.black);
        setLayout(new FlowLayout(FlowLayout.CENTER, padding, padding / 2));

        this.physics = physics;

        setMagPanel();
        setMagScrollPanel();
        setGraphs();
    }

    void setMagPanel() {
        magPanel = new JPanel();

        int magPanelHeight = 275;
        magPanel.setPreferredSize(new Dimension(infoPanelWidth - padding / 2, magPanelHeight));
        magPanel.setBackground(Color.black);
        magPanel.setLayout(new BorderLayout(0, 5));

        setMagHeaderPanel();
        setMagQuantsPanel();
    }

    void setMagScrollPanel() {
        magScrollPanel = new JScrollPane(
                magPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        int magScrollPanelHeight = 200;
        magScrollPanel.setPreferredSize(new Dimension(infoPanelWidth - padding / 2 + 10, magScrollPanelHeight));
        magScrollPanel.setBackground(Color.black);
        magScrollPanel.setBorder(BorderFactory.createLineBorder(SimColors.magPanelBorder, 5, true));

        ScrollBars scrollBars = new ScrollBars();

        magScrollPanel.setVerticalScrollBar(scrollBars.getVerticalBar());
        magScrollPanel.setHorizontalScrollBar(scrollBars.getHorizontalBar());

        add(magScrollPanel);
    }

    void setMagHeaderPanel() {
        magHeaderPanel = new JPanel();
        magHeaderPanel.setPreferredSize(new Dimension(0, 40));
        magHeaderPanel.setBackground(Color.black);

        JLabel header = new JLabel("Magnitude Panel");
        header.setVerticalAlignment(JLabel.TOP);
        header.setHorizontalAlignment(JLabel.CENTER);
        header.setFont(SimFonts.magPanelHeader);
        header.setForeground(SimColors.magPanelHeader);
        magHeaderPanel.add(header);

        magPanel.add(magHeaderPanel, BorderLayout.NORTH);
    }

    void setMagQuantsPanel() {
        magQuantsPanel = new JPanel(new GridBagLayout());
        magQuantsPanel.setBackground(Color.black);
        GridBagConstraints gdc = new GridBagConstraints();
        gdc.insets = new Insets(5, 5,5, 5);

        thetaInRadiansLabel = quantLabel("θ(in radians): ", SimColors.theta, gdc,0);
        magQuantsPanel.add(thetaInRadiansLabel, gdc);
        thetaInRadiansValue = quantValue((physics.theta), " ", "radians", SimColors.theta, gdc,0);
        magQuantsPanel.add(thetaInRadiansValue, gdc);

        thetaInDegreesLabel = quantLabel("θ(in degrees): ", SimColors.theta, gdc,1);
        magQuantsPanel.add(thetaInDegreesLabel, gdc);
        thetaInDegreesValue = quantValue((Math.toDegrees(physics.theta)), "", "°", SimColors.theta, gdc,1);
        magQuantsPanel.add(thetaInDegreesValue, gdc);

        omegaLabel = quantLabel("ω: ", SimColors.omega, gdc,2);
        magQuantsPanel.add(omegaLabel, gdc);
        omegaValue = quantValue((physics.initOmega), " ", "rad/s", SimColors.omega, gdc,2);
        magQuantsPanel.add(omegaValue, gdc);

        alphaLabel = quantLabel("α: ", SimColors.alpha, gdc,3);
        magQuantsPanel.add(alphaLabel, gdc);
        alphaValue = quantValue((physics.initOmega), " ", "rad/s²", SimColors.alpha, gdc,3);
        magQuantsPanel.add(alphaValue, gdc);

        acLabel = quantLabel("Centripetal Acc.: ", SimColors.ac, gdc,4);
        magQuantsPanel.add(acLabel, gdc);
        acValue = quantValue((physics.pathRadius * physics.omega * physics.omega), " ", "m/s²", SimColors.ac, gdc,4);
        magQuantsPanel.add(acValue, gdc);

        atLabel = quantLabel("Tangential Acc.: ", SimColors.at, gdc,5);
        magQuantsPanel.add(atLabel, gdc);
        atValue = quantValue((physics.pathRadius * physics.alpha), " ", "m/s²", SimColors.at, gdc,5);
        magQuantsPanel.add(atValue, gdc);

        posLabel = quantLabel("Ball Position: ", SimColors.pos, gdc,6);
        magQuantsPanel.add(posLabel, gdc);
        posValue = quantValue((physics.pathRadius), " ", "m", SimColors.pos, gdc,6);
        magQuantsPanel.add(posValue, gdc);

        vLabel = quantLabel("Velocity: ", SimColors.velocity, gdc,7);
        magQuantsPanel.add(vLabel, gdc);
        vValue = quantValue((physics.pathRadius * physics.omega), " ", "m/s", SimColors.velocity, gdc,7);
        magQuantsPanel.add(vValue, gdc);

        timeScaleLabel = quantLabel("Time Scale: ", SimColors.timeScale, gdc, 8);
        magQuantsPanel.add(timeScaleLabel, gdc);
        timeScaleValue = quantValue((physics.timeScale), "", "x", SimColors.timeScale, gdc, 8);
        magQuantsPanel.add(timeScaleValue, gdc);

        addSpacer(9);

        magPanel.add(magQuantsPanel);
    }

    public void update() {
        acValue.setText(String.format("%.2f", Math.abs(physics.pathRadius * physics.omega * physics.omega)) + " m/s²");
        atValue.setText(String.format("%.2f", Math.abs(physics.pathRadius * physics.alpha)) + " m/s²");
        posValue.setText(String.format("%.2f", Math.abs(physics.pathRadius)) + " m");
        vValue.setText(String.format("%.2f", Math.abs(physics.pathRadius * physics.omega)) + " m/s");
        thetaInRadiansValue.setText(String.format("%.2f", Math.abs(physics.theta)) + " radians");
        thetaInDegreesValue.setText(String.format("%.2f", Math.abs(Math.toDegrees(physics.theta))) + "°");
        omegaValue.setText(String.format("%.2f", Math.abs(physics.omega)) + " rad/s");
        timeScaleValue.setText(String.format("%.2f", physics.timeScale) + "x");

        repaint();
    }

    JLabel quantLabel(String label, Color color, GridBagConstraints gdc, int y) {
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(SimFonts.magPanelQuants);
        jLabel.setForeground(color);

        gdc.gridx = 0;
        gdc.gridy = y;
        gdc.anchor = GridBagConstraints.WEST;

        return jLabel;
    }

    JLabel quantValue(double value, String separator, String unit, Color color, GridBagConstraints gdc, int y) {
        JLabel jLabel = new JLabel(String.format("%.2f", Math.abs(value)) + separator + unit);
        jLabel.setFont(SimFonts.magPanelQuants);
        jLabel.setForeground(color);

        gdc.gridx = 1;
        gdc.gridy = y;
        gdc.anchor = GridBagConstraints.EAST;

        return jLabel;
    }

    void addSpacer(int y) {
        GridBagConstraints spacer = new GridBagConstraints();
        spacer.gridx = 0;
        spacer.gridy = y;
        spacer.gridwidth = GridBagConstraints.REMAINDER;
        spacer.weighty = 1.0;
        spacer.fill = GridBagConstraints.VERTICAL;
        magQuantsPanel.add(Box.createVerticalBox(), spacer);
    }

    public void setGraphs() {
        GraphPanel angleTimeGraphPanel = new GraphPanel(
                new Dimension(1000, 1000),
                Color.white,
                SimColors.theta
        ) {
            @Override
            protected double calculate(double t) {
                return (physics.initTheta + (-physics.initOmega) * t + 0.5 * (-physics.alpha) * t * t);
            }
        };
        angleTimeGraphPanel.setScales(50, 40);
        angleTimeGraphPanel.setTickSpacing(1, Math.PI / 4);
        angleTimeGraphPanel.setIcon(
                Latex.convert("t", 15),
                Latex.convert("\\theta", 15)
        );
        angleTimeGraph = createGraph(angleTimeGraphPanel);

        GraphPanel omegaTimeGraphPanel = new GraphPanel(
                new Dimension(1000, 1000),
                Color.white,
                SimColors.omega
        ) {
            @Override
            protected double calculate(double t) {
                return (-physics.initOmega - physics.alpha * t);
            }
        };
        omegaTimeGraphPanel.setScales(50, 40);
        omegaTimeGraphPanel.setTickSpacing(1, Math.PI / 4);
        omegaTimeGraphPanel.setIcon(
                Latex.convert("t", 15),
                Latex.convert("\\omega", 15)
        );
        omegaTimeGraph = createGraph(omegaTimeGraphPanel);

        GraphPanel alphaTimeGraphPanel = new GraphPanel(
                new Dimension(1000, 1000),
                Color.white,
                SimColors.alpha
        ) {
            @Override
            protected double calculate(double t) {
                return (-physics.alpha);
            }
        };
        alphaTimeGraphPanel.setScales(50, 100);
        alphaTimeGraphPanel.setTickSpacing(1, 0.25);
        alphaTimeGraphPanel.setIcon(
                Latex.convert("t", 15),
                Latex.convert("\\alpha", 15)
        );
        alphaTimeGraph = createGraph(alphaTimeGraphPanel);
    }

    private Graph createGraph(GraphPanel graphPanel) {
        ScrollBars scrollBars = new ScrollBars();
        changeBarUI(scrollBars);

        int graphHeight = 400;
        Graph graph = new Graph(
                new Dimension(infoPanelWidth - padding / 2, graphHeight),
                graphPanel,
                BorderFactory.createLineBorder(SimColors.magPanelBorder, 5, true)
        );
        graph.setBackground(Color.black);
        graph.setHorizontalScrollBar(scrollBars.getHorizontalBar());
        graph.setVerticalScrollBar(scrollBars.getVerticalBar());
        graph.addAdjustmentListeners();

        return graph;
    }

    private void changeBarUI(ScrollBars scrollBars) {
        scrollBars.horizontalBarUI = createScrollUI();
        scrollBars.verticalBarUI = createScrollUI();
        scrollBars.changeBarsUI();
    }

    private BasicScrollBarUI createScrollUI() {
        return new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = Color.lightGray;
                thumbDarkShadowColor = Color.lightGray;
                thumbHighlightColor = Color.lightGray;
                thumbLightShadowColor = Color.lightGray;
                trackColor = Color.darkGray;
            }
            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2 = (Graphics2D)g;

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.lightGray);

                g2.fillRoundRect(
                        thumbBounds.x + 2,
                        thumbBounds.y + 2,
                        thumbBounds.width - 4,
                        thumbBounds.height - 4,
                        5, 5
                );
            }
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
        };
    }
}