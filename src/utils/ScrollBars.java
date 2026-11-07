package utils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class ScrollBars {
    private final JScrollBar verticalBar;
    private final JScrollBar horizontalBar;
    public BasicScrollBarUI horizontalBarUI;
    public BasicScrollBarUI verticalBarUI;

    public ScrollBars() {
        verticalBar = new JScrollBar(JScrollBar.VERTICAL);
        verticalBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = Color.darkGray;
                thumbDarkShadowColor = Color.darkGray;
                thumbHighlightColor = Color.darkGray;
                thumbLightShadowColor = Color.darkGray;
                trackColor = Color.black;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.darkGray);

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
        });
        verticalBar.setPreferredSize(new Dimension(10, 0));

        horizontalBar = new JScrollBar(JScrollBar.HORIZONTAL);
        horizontalBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = Color.darkGray;
                thumbDarkShadowColor = Color.darkGray;
                thumbHighlightColor = Color.darkGray;
                thumbLightShadowColor = Color.darkGray;
                trackColor = Color.black;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.darkGray);

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
        });
        horizontalBar.setPreferredSize(new Dimension(0, 10));
    }

    public void changeBarsUI() {
        verticalBar.setUI(verticalBarUI);
        horizontalBar.setUI(horizontalBarUI);
    }

    public JScrollBar getHorizontalBar() {
        return horizontalBar;
    }

    public JScrollBar getVerticalBar() {
        return verticalBar;
    }
}