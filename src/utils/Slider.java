package utils;
import javax.swing.*;
import java.awt.*;

public class Slider extends JPanel {
    public String label;
    public JSlider slider;
    public JLabel sliderLabel;
    public double scale;
    public Slider(String label, int min, int max, int initial, double scale) {
        this.label = label;
        this.scale = scale;
        sliderLabel = new JLabel(label + ": " + initial / scale);

        slider = new JSlider(min, max, initial);
        slider.setPreferredSize(new Dimension(180, 20));

        setLayout(new FlowLayout(FlowLayout.CENTER, 50, 5));
        slider.setOpaque(false);
        add(sliderLabel);
        add(slider);
    }
}