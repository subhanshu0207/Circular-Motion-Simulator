package utils;
import javax.swing.*;
import java.awt.*;

public class Slider extends JPanel {
    public String label;
    public JSlider slider;
    public JLabel sliderLabel;
    public double scale;

    public Slider(String label, int min, int max, int initial, double scale, Color fontColor) {
        this.label = label;
        this.scale = scale;
        sliderLabel = new JLabel(label + ": " + String.format("%.2f", initial / scale));
        sliderLabel.setForeground(fontColor);
        sliderLabel.setFont(new Font("Monospaced", Font.BOLD, 11));

        slider = new JSlider(min, max, initial);
        slider.setPreferredSize(new Dimension(180, 20));
        slider.setFocusable(false);
        slider.setOpaque(false);

        setLayout(new FlowLayout(FlowLayout.CENTER, 50, 5));
        setBackground(new Color(20,20, 20));
        add(sliderLabel);
        add(slider);
    }

}