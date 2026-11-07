import physics.Physics;
import utils.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class ControlPanel extends JPanel {
    Color bgColor = new Color(50, 50, 50);
    Color buttonColor = new Color(40, 40, 40);
    Color buttonHoverColor = new Color(55, 55, 55);
    Color buttonFontColor = new Color(240, 240, 240);

    Font buttonFont = new Font("Monospaced", Font.BOLD, 12);

    private CustomButton startButton, pauseButton, resetButton, exitButton;
    private Physics physics;
    private Timer timer;
    private SimPanel simPanel;
    private SliderPanel sliderPanel;
    private CheckBoxPanel checkBoxPanel;

    final int LENGTH = 200;

    private void addButtons() {
        startButton = new CustomButton("START", buttonFont, buttonColor, buttonHoverColor, buttonColor.darker(), buttonFontColor);
        pauseButton = new CustomButton("PAUSE", buttonFont, buttonColor, buttonHoverColor, buttonColor.darker(), buttonFontColor);
        resetButton = new CustomButton("RESET", buttonFont, buttonColor, buttonHoverColor, buttonColor.darker(), buttonFontColor);
        exitButton = new CustomButton("EXIT", buttonFont, buttonColor, buttonHoverColor, buttonColor.darker(), buttonFontColor);

        startButton.addActionListener(e -> {
            physics.time.resume();
            timer.start();
            startButton.setEnabled(false);
            pauseButton.setEnabled(true);
        });

        pauseButton.setEnabled(false);
        pauseButton.addActionListener(e -> {
            timer.stop();
            physics.time.pause();
            pauseButton.setEnabled(false);
            startButton.setEnabled(true);
        });

        resetButton.addActionListener(e -> {
            physics.reset();
            physics.time.reset();
            timer.stop();
            sliderPanel.reset();
            simPanel.repaint();

            startButton.setEnabled(true);
            pauseButton.setEnabled(false);
        });

        exitButton.addActionListener(e -> {
            timer.stop();
            System.exit(0);
        });

        add(startButton);
        add(pauseButton);
        add(resetButton);
        add(exitButton);
    }

    ControlPanel(Physics physics, Timer timer, SimPanel simPanel, InfoPanel infoPanel) {
        this.physics = physics;
        this.timer = timer;
        this.simPanel = simPanel;
        sliderPanel = new SliderPanel(physics, simPanel, infoPanel);
        checkBoxPanel = new CheckBoxPanel(simPanel, infoPanel);

        setPreferredSize(new Dimension(LENGTH, 0));
        setBackground(bgColor);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        addButtons();
        add(sliderPanel);
        add(checkBoxPanel);
    }

}