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