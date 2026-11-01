import javax.swing.*;
import java.awt.*;

public class CheckBoxPanel extends JPanel {
    final Dimension dimension = new Dimension(190, 200);
    Color tempColor = Color.decode("#EEEEEE");

    JCheckBox atVector;
    JCheckBox acVector;
    JCheckBox posVector;
    JCheckBox vVector;
    JCheckBox thetaGraph;
    JCheckBox omegaGraph;
    JCheckBox alphaGraph;

    InfoPanel infoPanel;
    SimPanel simPanel;
    public void addCheckBoxes() {
        atVector = new JCheckBox("Tangential Acceleration");
        atVector.setFocusable(false);
        atVector.addActionListener(e -> {
            simPanel.showAt = atVector.isSelected();
            simPanel.repaint();
        });

        acVector = new JCheckBox("Centripetal Acceleration");
        acVector.setFocusable(false);
        acVector.addActionListener(e -> {
            simPanel.showAc = acVector.isSelected();
            simPanel.repaint();
        });

        vVector = new JCheckBox("Velocity");
        vVector.setFocusable(false);
        vVector.addActionListener(e -> {
            simPanel.showV = vVector.isSelected();
            simPanel.repaint();
        });

        posVector = new JCheckBox("Position");
        posVector.setFocusable(false);
        posVector.addActionListener(e -> {
            simPanel.showPos = posVector.isSelected();
            simPanel.repaint();
        });

        thetaGraph = new JCheckBox("Angle-Time Graph");
        thetaGraph.setFocusable(false);
        thetaGraph.addActionListener(e -> {
            infoPanel.remove(infoPanel.angleTimeGraph);
            infoPanel.remove(infoPanel.omegaTimeGraph);
            infoPanel.remove(infoPanel.alphaTimeGraph);
            if(thetaGraph.isSelected()) {
                infoPanel.add(infoPanel.angleTimeGraph);
                omegaGraph.setSelected(false);
                alphaGraph.setSelected(false);
            }

            infoPanel.revalidate();
            infoPanel.repaint();
        });

        omegaGraph = new JCheckBox("Omega-Time Graph");
        omegaGraph.setFocusable(false);
        omegaGraph.addActionListener(e -> {
            infoPanel.remove(infoPanel.angleTimeGraph);
            infoPanel.remove(infoPanel.omegaTimeGraph);
            infoPanel.remove(infoPanel.alphaTimeGraph);
            if(omegaGraph.isSelected()) {
                infoPanel.add(infoPanel.omegaTimeGraph);
                alphaGraph.setSelected(false);
                thetaGraph.setSelected(false);
            }

            infoPanel.revalidate();
            infoPanel.repaint();
        });

        alphaGraph = new JCheckBox("Alpha-Time Graph");
        alphaGraph.setFocusable(false);
        alphaGraph.addActionListener(e -> {
            infoPanel.remove(infoPanel.angleTimeGraph);
            infoPanel.remove(infoPanel.omegaTimeGraph);
            infoPanel.remove(infoPanel.alphaTimeGraph);
            if(alphaGraph.isSelected()) {
                infoPanel.add(infoPanel.alphaTimeGraph);
                thetaGraph.setSelected(false);
                omegaGraph.setSelected(false);
            }

            infoPanel.revalidate();
            infoPanel.repaint();
        });

        add(atVector);
        add(acVector);
        add(vVector);
        add(posVector);
        add(thetaGraph);
        add(omegaGraph);
        add(alphaGraph);
    }
    public void reset() {
        atVector.setSelected(false);
        acVector.setSelected(false);
        posVector.setSelected(false);
        vVector.setSelected(false);
    }
    CheckBoxPanel(SimPanel simPanel, InfoPanel infoPanel) {
        setBackground(tempColor);
        setPreferredSize(dimension);
        setLayout(new FlowLayout(FlowLayout.LEADING));

        this.simPanel = simPanel;
        this.infoPanel = infoPanel;

        addCheckBoxes();
    }
}