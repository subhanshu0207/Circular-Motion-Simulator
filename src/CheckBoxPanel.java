import utils.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;

public class CheckBoxPanel extends JPanel {
    private final int padding = 7;

    private JPanel vectorCheckBoxPanel;
    private JCheckBox atVector;
    private JCheckBox acVector;
    private JCheckBox accVector;
    private JCheckBox posVector;
    private JCheckBox vVector;

    private JPanel graphCheckBoxPanel;
    private JCheckBox thetaGraph;
    private JCheckBox omegaGraph;
    private JCheckBox alphaGraph;

    private final InfoPanel infoPanel;
    private final SimPanel simPanel;

    public void setVectorCheckBoxPanel() {
        vectorCheckBoxPanel = new JPanel(new GridLayout(6, 1, 0, 5));
        vectorCheckBoxPanel.setPreferredSize(new Dimension(0, 180));
        vectorCheckBoxPanel.setBackground(SimColors.vectorCheckBoxPanel);

        JLabel header = new JLabel("<html><b><u>Vectors</u></b></html>", JLabel.CENTER);
        header.setFont(SimFonts.checkBoxPanelHeader);
        header.setForeground(SimColors.checkBoxHeader);
        vectorCheckBoxPanel.add(header);
    }

    public void setGraphCheckBoxPanel() {
        graphCheckBoxPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        graphCheckBoxPanel.setPreferredSize(new Dimension(0, 120));
        graphCheckBoxPanel.setBackground(SimColors.graphCheckBoxPanel);

        JLabel header = new JLabel("<html><b><u>Graphs</u></b></html>", JLabel.CENTER);
        header.setFont(SimFonts.checkBoxPanelHeader);
        header.setForeground(SimColors.checkBoxHeader);
        graphCheckBoxPanel.add(header);
    }

    private void addCheckBoxes() {

        atVector = getVectorCheckBox("Tangential Acc.", e -> {
            simPanel.setShowAt(atVector.isSelected());
            simPanel.repaint();
        });

        acVector = getVectorCheckBox("Centripetal Acc.", e -> {
            simPanel.setShowAc(acVector.isSelected());
            simPanel.repaint();
        });

        accVector = getVectorCheckBox("Net Acc.", e -> {
            simPanel.setShowAcc(accVector.isSelected());
            simPanel.repaint();
        });

        vVector = getVectorCheckBox("Velocity", e -> {
            simPanel.setShowV(vVector.isSelected());
            simPanel.repaint();
        });

        posVector = getVectorCheckBox("Position", e -> {
            simPanel.setShowPos(posVector.isSelected());
            simPanel.repaint();
        });

        thetaGraph = getGraphCheckBox("Angle-Time Graph", e -> {
            removeGraphs();
            if (thetaGraph.isSelected()) {
                infoPanel.add(infoPanel.angleTimeGraph);
                omegaGraph.setSelected(false);
                alphaGraph.setSelected(false);
            }
            updateInfoPanel();
        });
        thetaGraph.setSelected(true);
        infoPanel.add(infoPanel.angleTimeGraph);

        omegaGraph = getGraphCheckBox("Omega-Time Graph", e -> {
            removeGraphs();
            if (omegaGraph.isSelected()) {
                infoPanel.add(infoPanel.omegaTimeGraph);
                alphaGraph.setSelected(false);
                thetaGraph.setSelected(false);
            }
            updateInfoPanel();
        });

        alphaGraph = getGraphCheckBox("Alpha-Time Graph", e -> {
            removeGraphs();
            if (alphaGraph.isSelected()) {
                infoPanel.add(infoPanel.alphaTimeGraph);
                thetaGraph.setSelected(false);
                omegaGraph.setSelected(false);
            }
            updateInfoPanel();
        });

        vectorCheckBoxPanel.add(atVector);
        vectorCheckBoxPanel.add(acVector);
        vectorCheckBoxPanel.add(accVector);
        vectorCheckBoxPanel.add(vVector);
        vectorCheckBoxPanel.add(posVector);

        graphCheckBoxPanel.add(thetaGraph);
        graphCheckBoxPanel.add(omegaGraph);
        graphCheckBoxPanel.add(alphaGraph);
    }

    private JCheckBox getVectorCheckBox(String label, ActionListener e) {
        JCheckBox vectorCheckBox = new JCheckBox(label);
        vectorCheckBox.setFocusable(false);
        vectorCheckBox.addActionListener(e);
        vectorCheckBox.setFont(SimFonts.checkBoxPanelQuants);
        vectorCheckBox.setForeground(SimColors.checkBoxQuants);
        vectorCheckBox.setBackground(SimColors.vectorCheckBoxPanel);

        Image uncheckedImage = new ImageIcon(getClass().getResource("/images/unchecked.png")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        Image checkedImage = new ImageIcon(getClass().getResource("/images/checked.png")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        Image hoverImage = new ImageIcon(getClass().getResource("/images/unchecked-hover.png")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);

        vectorCheckBox.setIcon(new ImageIcon(uncheckedImage));
        vectorCheckBox.setSelectedIcon(new ImageIcon(checkedImage));
        vectorCheckBox.setRolloverEnabled(true);
        vectorCheckBox.setRolloverIcon(new ImageIcon(hoverImage));

        return vectorCheckBox;
    }

    private JCheckBox getGraphCheckBox(String label, ActionListener e) {
        JCheckBox graphCheckBox = new JCheckBox(label);
        graphCheckBox.setFocusable(false);
        graphCheckBox.addActionListener(e);
        graphCheckBox.setFont(SimFonts.checkBoxPanelQuants);
        graphCheckBox.setForeground(SimColors.checkBoxQuants);
        graphCheckBox.setBackground(SimColors.graphCheckBoxPanel);

        Image uncheckedImage = new ImageIcon(getClass().getResource("/images/unchecked.png")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        Image checkedImage = new ImageIcon(getClass().getResource("/images/checked.png")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        Image hoverImage = new ImageIcon(getClass().getResource("/images/unchecked-hover.png")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);

        graphCheckBox.setIcon(new ImageIcon(uncheckedImage));
        graphCheckBox.setSelectedIcon(new ImageIcon(checkedImage));
        graphCheckBox.setRolloverEnabled(true);
        graphCheckBox.setRolloverIcon(new ImageIcon(hoverImage));

        return graphCheckBox;
    }

    private void removeGraphs() {
        infoPanel.remove(infoPanel.angleTimeGraph);
        infoPanel.remove(infoPanel.omegaTimeGraph);
        infoPanel.remove(infoPanel.alphaTimeGraph);
    }

    private void updateInfoPanel() {
        infoPanel.revalidate();
        infoPanel.repaint();
    }

    CheckBoxPanel(SimPanel simPanel, InfoPanel infoPanel) {
        Dimension dimension = new Dimension(190, 300);

        setBackground(SimColors.checkBoxPanelBg);
        setPreferredSize(dimension);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Border paddedBorder = BorderFactory.createEmptyBorder(padding, padding, padding, padding);
        setBorder(paddedBorder);

        this.simPanel = simPanel;
        this.infoPanel = infoPanel;

        setVectorCheckBoxPanel();
        add(vectorCheckBoxPanel);

        add(Box.createVerticalStrut(padding));

        setGraphCheckBoxPanel();
        add(graphCheckBoxPanel);

        addCheckBoxes();
    }
}