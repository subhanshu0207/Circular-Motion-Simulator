import physics.Physics;
import utils.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class InfoPanel extends JPanel {
    final int WIDTH = 250;
    final int HEIGHT = 500;
    final int padding = 40;

    Graph angleTimeGraph;
    Graph omegaTimeGraph;
    Graph alphaTimeGraph;

    private JScrollPane infoScrollPanel;

    private JPanel infoPanel;
        private JPanel infoHeaderPanel;
        private JPanel infoQuantsPanel;
            private JLabel acLabel;
            private JLabel acValue;

            private JLabel atLabel;
            private JLabel atValue;

            private JLabel posLabel;
            private JLabel posValue;

            private JLabel vLabel;
            private JLabel vValue;

            private JLabel thetaInRadiansLabel;
            private JLabel thetaInRadiansValue;

            private JLabel thetaInDegreesLabel;
            private JLabel thetaInDegreesValue;

            private JLabel omegaLabel;
            private JLabel omegaValue;

    Physics physics;

    public InfoPanel(Physics physics) {
        setPreferredSize(new Dimension(WIDTH, 0));
        setBackground(Color.black);
        setLayout(new FlowLayout(FlowLayout.CENTER, padding, padding / 2));

        this.physics = physics;

        setInfoPanel();
        setInfoScrollPanel();
        setGraphs();
    }

    void setInfoScrollPanel() {
        infoScrollPanel = new JScrollPane(
                infoPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        infoScrollPanel.setPreferredSize(new Dimension(WIDTH - padding / 2 + 10, 200));
        infoScrollPanel.setBackground(Color.black);
        infoScrollPanel.setBorder(BorderFactory.createLineBorder(SimColors.infoPanelBorder, 5, true));

        ScrollBars scrollBars = new ScrollBars();

        infoScrollPanel.setVerticalScrollBar(scrollBars.getVerticalBar());
        infoScrollPanel.setHorizontalScrollBar(scrollBars.getHorizontalBar());

        add(infoScrollPanel);
    }

    void setInfoPanel() {
        infoPanel = new JPanel();

        infoPanel.setPreferredSize(new Dimension(WIDTH - padding / 2, HEIGHT));
        infoPanel.setBackground(Color.black);
        infoPanel.setLayout(new BorderLayout(0, 5));

        setInfoHeaderPanel();
        setInfoQuantsPanel();
    }

    void setInfoHeaderPanel() {
        infoHeaderPanel = new JPanel();
        infoHeaderPanel.setPreferredSize(new Dimension(0, 40));
        infoHeaderPanel.setBackground(Color.black);

        JLabel header = new JLabel("Magnitude Panel");
        //header.setBorder(BorderFactory.createLineBorder(Color.red, 2));
        header.setVerticalAlignment(JLabel.TOP);
        header.setHorizontalAlignment(JLabel.CENTER);
        header.setFont(SimFonts.infoPanelHeader);
        header.setForeground(SimColors.infoPanelHeader);
        infoHeaderPanel.add(header);

        infoPanel.add(infoHeaderPanel, BorderLayout.NORTH);
    }

    void setInfoQuantsPanel() {
        infoQuantsPanel = new JPanel(new GridBagLayout());
        infoQuantsPanel.setBackground(Color.black);
        GridBagConstraints gdc = new GridBagConstraints();
        gdc.insets = new Insets(5, 5,5, 5);

        thetaInRadiansLabel = quantLabel("θ: ", SimColors.theta, gdc, 0, 0);
        infoQuantsPanel.add(thetaInRadiansLabel, gdc);
        thetaInRadiansValue = quantValue((physics.theta), "radians", SimColors.theta, gdc, 1, 0);
        infoQuantsPanel.add(thetaInRadiansValue, gdc);

        thetaInDegreesLabel = quantLabel("θ: ", SimColors.theta, gdc, 0, 1);
        infoQuantsPanel.add(thetaInDegreesLabel, gdc);
        thetaInDegreesValue = quantValue((Math.toDegrees(physics.theta)), "°", SimColors.theta, gdc, 1, 1);
        infoQuantsPanel.add(thetaInDegreesValue, gdc);

        omegaLabel = quantLabel("ω: ", SimColors.omega, gdc, 0, 2);
        infoQuantsPanel.add(omegaLabel, gdc);
        omegaValue = quantValue(physics.initOmega, "rad/s", SimColors.omega, gdc, 1, 2);
        infoQuantsPanel.add(omegaValue, gdc);

        acLabel = quantLabel("Centripetal Acc.: ", SimColors.ac, gdc, 0, 3);
        infoQuantsPanel.add(acLabel, gdc);
        acValue = quantValue((physics.pathRadius * physics.omega * physics.omega), "m/s²", SimColors.ac, gdc, 1, 3);
        infoQuantsPanel.add(acValue, gdc);

        atLabel = quantLabel("Tangential Acc.: ", SimColors.at, gdc, 0, 4);
        infoQuantsPanel.add(atLabel, gdc);
        atValue = quantValue((physics.pathRadius * physics.alpha), "m/s²", SimColors.at, gdc, 1, 4);
        infoQuantsPanel.add(atValue, gdc);

        posLabel = quantLabel("Ball Position: ", SimColors.pos, gdc, 0, 5);
        infoQuantsPanel.add(posLabel, gdc);
        posValue = quantValue(physics.pathRadius, "m", SimColors.pos, gdc, 1, 5);
        infoQuantsPanel.add(posValue, gdc);

        vLabel = quantLabel("Velocity: ", SimColors.velocity, gdc, 0, 6);
        infoQuantsPanel.add(vLabel, gdc);
        vValue = quantValue((physics.pathRadius * physics.omega), "m/s", SimColors.velocity, gdc, 1, 6);
        infoQuantsPanel.add(vValue, gdc);

        addSpacer(7);

        infoPanel.add(infoQuantsPanel);
    }

    public void update() {
        acValue.setText(String.format("%.2f", Math.abs(physics.pathRadius * physics.omega * physics.omega)) + " m/s²");
        atValue.setText(String.format("%.2f", Math.abs(physics.pathRadius * physics.alpha)) + " m/s²");
        posValue.setText(String.format("%.2f", Math.abs(physics.pathRadius)) + " m");
        vValue.setText(String.format("%.2f", Math.abs(physics.pathRadius * physics.omega)) + " m/s");
        thetaInRadiansValue.setText(String.format("%.2f", Math.abs(physics.theta)) + " radians");
        thetaInDegreesValue.setText(String.format("%.2f", Math.abs(Math.toDegrees(physics.theta))) + " °");
        omegaValue.setText(String.format("%.2f", Math.abs(physics.omega)) + " rad/s");

        repaint();
    }

    JLabel quantLabel(String label, Color color, GridBagConstraints gdc, int x, int y) {
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(SimFonts.infoPanelQuants);
        jLabel.setForeground(color);

        gdc.gridx = x;
        gdc.gridy = y;
        gdc.anchor = GridBagConstraints.WEST;

        return jLabel;
    }

    JLabel quantValue(double value, String unit, Color color, GridBagConstraints gdc, int x, int y) {
        JLabel jLabel = new JLabel(Math.abs(value) + " " + unit);
        jLabel.setFont(SimFonts.infoPanelQuants);
        jLabel.setForeground(color);

        gdc.gridx = x;
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
        infoQuantsPanel.add(Box.createVerticalBox(), spacer);
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

        Graph graph = new Graph(
                new Dimension(WIDTH - padding / 2, 300),
                graphPanel,
                BorderFactory.createLineBorder(SimColors.infoPanelBorder, 5, true)
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
