import physics.Physics;
import utils.*;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    
    private CustomButton startButton, pauseButton, resetButton, exitButton;
    private final Physics physics;
    private final Timer timer;
    private final SimPanel simPanel;
    private final SliderPanel sliderPanel;
    private final CheckBoxPanel checkBoxPanel;

    final int LENGTH = 200;

    private void addButtons() {
        startButton = new CustomButton("START", SimFonts.button, SimColors.button, SimColors.buttonHover, SimColors.button.darker(), SimColors.buttonFont);
        pauseButton = new CustomButton("PAUSE", SimFonts.button, SimColors.button, SimColors.buttonHover, SimColors.button.darker(), SimColors.buttonFont);
        resetButton = new CustomButton("RESET", SimFonts.button, SimColors.button, SimColors.buttonHover, SimColors.button.darker(), SimColors.buttonFont);
        exitButton = new CustomButton("EXIT", SimFonts.button, SimColors.button, SimColors.buttonHover, SimColors.button.darker(), SimColors.buttonFont);

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
        setBackground(SimColors.controlPanelBg);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        addButtons();
        add(sliderPanel);
        add(checkBoxPanel);
    }

}