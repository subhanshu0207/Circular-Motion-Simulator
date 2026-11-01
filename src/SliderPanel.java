import physics.Physics;
import utils.*;
import javax.swing.*;
import java.awt.*;

public class SliderPanel extends JPanel {
    Physics physics;
    SimPanel simPanel;
    InfoPanel infoPanel;

    Color tempColor = Color.lightGray;

    Dimension dimension = new Dimension(190, 230);
    Dimension sliderDimension = new Dimension(180, 50);

    Slider omegaSlider, alphaSlider, pathRadiusSlider, timeScaleSlider;

    public void addSliders() {
        omegaSlider = new Slider("ω₀", -50, 50, 0, 20);
        omegaSlider.setPreferredSize(sliderDimension);
        omegaSlider.slider.addChangeListener(e -> {
            double value = omegaSlider.slider.getValue() / omegaSlider.scale;
            omegaSlider.sliderLabel.setText(omegaSlider.label + ": " + value);
            physics.omega = -value;
            physics.initOmega = -value;

            omegaSlider.repaint();
            physics.updateVectors();
            simPanel.repaint();
            infoPanel.update();
        });

        alphaSlider = new Slider("α", -50, 50, 0, 100);
        alphaSlider.setPreferredSize(sliderDimension);
        alphaSlider.slider.addChangeListener(e -> {
            double value = alphaSlider.slider.getValue() / alphaSlider.scale;
            alphaSlider.sliderLabel.setText(alphaSlider.label + ": " + value);
            physics.alpha = -value;

            alphaSlider.repaint();
            physics.updateVectors();
            simPanel.repaint();
            infoPanel.update();
        });

        pathRadiusSlider = new Slider("R", 30, 100, 50, 10.0);
        pathRadiusSlider.setPreferredSize(sliderDimension);
        pathRadiusSlider.slider.addChangeListener(e -> {
            double value = pathRadiusSlider.slider.getValue() / pathRadiusSlider.scale;
            pathRadiusSlider.sliderLabel.setText(pathRadiusSlider.label + ": " + String.format("%.2f",value));
            physics.pathRadius = value;

            pathRadiusSlider.repaint();
            physics.updatePosition();
            physics.updateVectors();
            simPanel.repaint();
            infoPanel.update();
        });

        timeScaleSlider = new Slider("Time Scale", 20, 150, 100, 100);
        timeScaleSlider.setPreferredSize(sliderDimension);
        timeScaleSlider.slider.addChangeListener(e -> {
            double value = timeScaleSlider.slider.getValue() / timeScaleSlider.scale;
            timeScaleSlider.sliderLabel.setText(timeScaleSlider.label + ": " + String.format("%.2f", value));
            physics.updateTimeScale(value);

            timeScaleSlider.repaint();
            simPanel.repaint();
            infoPanel.update();
        });

        add(omegaSlider);
        add(alphaSlider);
        add(pathRadiusSlider);
        add(timeScaleSlider);
    }

    public void reset() {
        omegaSlider.slider.setValue(0);
        alphaSlider.slider.setValue(0);
        pathRadiusSlider.slider.setValue(50);
    }

    SliderPanel(Physics physics, SimPanel simPanel, InfoPanel infoPanel) {
        this.physics = physics;
        this.simPanel = simPanel;
        this.infoPanel = infoPanel;

        setPreferredSize(dimension);
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        addSliders();
    }

}