package utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Graph extends JScrollPane {
    Dimension dimension;

    public GraphPanel graphPanel;

    public Graph(Dimension scrollPaneDimension, GraphPanel graphPanel, Border border) {
        dimension = scrollPaneDimension;
        setPreferredSize(dimension);
        setBorder(border);

        this.graphPanel = graphPanel;
        setViewportView(graphPanel);
        getViewport().setViewPosition(new Point(0, (graphPanel.dimension.height - dimension.height) / 2));
    }

    public void addAdjustmentListeners() {
        JScrollBar horizontalBar = getHorizontalScrollBar();
        JScrollBar verticalBar = getVerticalScrollBar();

        horizontalBar.addAdjustmentListener(e ->
                graphPanel.setScrollPosition(
                        horizontalBar.getValue(),
                        verticalBar.getValue()
                )
        );
        verticalBar.addAdjustmentListener(e ->
                graphPanel.setScrollPosition(
                        horizontalBar.getValue(),
                        verticalBar.getValue()
                )
        );
    }
}

class Test {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Graph");
            frame.setSize(500, 500);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            GraphPanel graphPanel = new GraphPanel(
                    new Dimension(1000, 1000),
                    Color.white,
                    Color.cyan
            ) {
                @Override
                protected double calculate(double x) {
                    return (x - 3);
                }
            };
            graphPanel.setTickSpacing(1, 1);
            graphPanel.setScales(40, 30);

            Graph graph = new Graph(
                    frame.getSize(),
                    graphPanel,
                    BorderFactory.createLineBorder(
                            Color.lightGray,
                            4,
                            true
                    )
            );

            frame.add(graph);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}