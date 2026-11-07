import physics.Physics;
import utils.SimColors;
import utils.Slider;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

public class SliderPanel extends JPanel {

    private final Physics physics;
    private final SimPanel simPanel;
    private final InfoPanel infoPanel;
    private Slider omegaSlider, alphaSlider, pathRadiusSlider, timeScaleSlider;

    private final int padding = 5;

    private void setSliders() {
        Dimension sliderDimension = new Dimension(180, 50);

        omegaSlider = new Slider("Angular Velocity(ω₀)", -50, 50, 0, 20, SimColors.omega);
        omegaSlider.setPreferredSize(sliderDimension);
        omegaSlider.slider.setUI(getBasicSliderUI(omegaSlider.slider, SimColors.omega.darker()));
        omegaSlider.slider.addChangeListener(e -> {
            double value = omegaSlider.slider.getValue() / omegaSlider.scale;
            omegaSlider.sliderLabel.setText(omegaSlider.label + ": " + String.format("%.2f", value));
            physics.omega = -value;
            physics.initOmega = -value;

            omegaSlider.repaint();
            physics.updateVectors();
            simPanel.repaint();
            infoPanel.update();
        });

        alphaSlider = new Slider("Angular Acc.(α)", -50, 50, 0, 100, SimColors.alpha);
        alphaSlider.setPreferredSize(sliderDimension);
        alphaSlider.slider.setUI(getBasicSliderUI(alphaSlider.slider, SimColors.alpha.darker()));
        alphaSlider.slider.addChangeListener(e -> {
            double value = alphaSlider.slider.getValue() / alphaSlider.scale;
            alphaSlider.sliderLabel.setText(alphaSlider.label + ": " + String.format("%.2f", value));
            physics.alpha = -value;

            alphaSlider.repaint();
            physics.updateVectors();
            simPanel.repaint();
            infoPanel.update();
        });

        pathRadiusSlider = new Slider("Path Radius(R)", 30, 100, 50, 10.0, SimColors.pos);
        pathRadiusSlider.setPreferredSize(sliderDimension);
        pathRadiusSlider.slider.setUI(getBasicSliderUI(pathRadiusSlider.slider, SimColors.pos.darker()));
        pathRadiusSlider.slider.addChangeListener(e -> {
            double value = pathRadiusSlider.slider.getValue() / pathRadiusSlider.scale;
            pathRadiusSlider.sliderLabel.setText(pathRadiusSlider.label + ": " + String.format("%.2f", value));
            physics.pathRadius = value;

            pathRadiusSlider.repaint();
            physics.updatePosition();
            physics.updateVectors();
            simPanel.repaint();
            infoPanel.update();
        });

        timeScaleSlider = new Slider("Time Scale", 20, 150, 100, 100, SimColors.timeScale);
        timeScaleSlider.setPreferredSize(sliderDimension);
        timeScaleSlider.slider.setUI(getBasicSliderUI(timeScaleSlider.slider, SimColors.timeScale.darker()));
        timeScaleSlider.slider.addChangeListener(e -> {
            double value = timeScaleSlider.slider.getValue() / timeScaleSlider.scale;
            timeScaleSlider.sliderLabel.setText(timeScaleSlider.label + ": " + String.format("%.2f", value));
            physics.updateTimeScale(value);

            timeScaleSlider.repaint();
            simPanel.repaint();
            infoPanel.update();
        });
    }

    private BasicSliderUI getBasicSliderUI(JSlider jSlider, Color thumbColor) {
        return new BasicSliderUI(jSlider) {
            @Override
            public void paintTrack(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(SimColors.sliderTrackColor);

                g2.fillRoundRect(
                        trackRect.x,
                        trackRect.y + trackRect.height / 2 - 3,
                        trackRect.width,
                        6,
                        6, 6
                );

                g2.dispose();
            }

            @Override
            public void paintThumb(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(thumbColor);

                g2.fillRoundRect(
                        thumbRect.x,
                        thumbRect.y,
                        thumbRect.width,
                        thumbRect.height,
                        12,
                        12
                );

                g2.dispose();
            }
        };
    }

    public void reset() {
        omegaSlider.slider.setValue(0);
        alphaSlider.slider.setValue(0);
        pathRadiusSlider.slider.setValue(50);
        timeScaleSlider.slider.setValue(100);
    }

    SliderPanel(Physics physics, SimPanel simPanel, InfoPanel infoPanel) {
        this.physics = physics;
        this.simPanel = simPanel;
        this.infoPanel = infoPanel;

        Dimension dimension = new Dimension(190, 230);
        setPreferredSize(dimension);
        setBackground(SimColors.sliderPanelBg);
        setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        setLayout(new GridLayout(4, 1, padding, padding));

        setSliders();

        add(omegaSlider);
        add(alphaSlider);
        add(pathRadiusSlider);
        add(timeScaleSlider);
    }

}