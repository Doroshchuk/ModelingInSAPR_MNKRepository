import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Plot extends JPanel {
    private Graphics2D graphics;
    private double unitVectorSize;
    private double x0;
    private double y0;

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 720);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        // set the canvas origin (0,0) to center canvas
        // All coordinates to the left of center canvas are negative
        // All coordinates below center canvas are negative
        this.graphics = (Graphics2D) graphics;
        this.graphics.translate(getWidth() / 2, getHeight() / 2);
    }

    public Plot() {
        unitVectorSize = 20;
        x0 = 0;
        y0 = 0;
    }

    private void drawLinesForCoordinateGrid() {
        for (int i = 0; i < getWidth(); i += unitVectorSize) {
            //vertical lines
            drawLine(new Point(x0 - getWidth() / 2 + i, y0 + getHeight()), new Point(x0 - getWidth() / 2 + i, y0 - getHeight()), TypeOfLine.COORDINATEGridLine);
        }
        for (int i = 0; i < getHeight(); i += unitVectorSize) {
            //horizontal lines
            drawLine(new Point(x0 - getWidth(), y0 - getHeight() / 2 + i), new Point(x0 + getWidth(), y0 - getHeight() / 2 + i), TypeOfLine.COORDINATEGridLine);
        }
        drawLine(new Point(unitVectorSize, -2), new Point(unitVectorSize, 2), TypeOfLine.COORDINATEGridLine);
        drawLine(new Point(-2, unitVectorSize), new Point(2, unitVectorSize), TypeOfLine.COORDINATEGridLine);
    }

    private void drawAxis() {
        //x and y lines
        drawLine(new Point(0, -getHeight() / 2), new Point(0, getHeight() / 2), TypeOfLine.AXISLine);
        drawLine(new Point(-10, getHeight() / 2 - 10), new Point(0, getHeight() / 2), TypeOfLine.AXISLine);
        drawLine(new Point(10, getHeight() / 2 - 10), new Point(0, getHeight() / 2), TypeOfLine.AXISLine);

        drawLine(new Point(-getWidth() / 2, 0), new Point(getWidth() / 2, 0), TypeOfLine.AXISLine);
        drawLine(new Point(getWidth() / 2 - 10, -10), new Point(getWidth() / 2, 0), TypeOfLine.AXISLine);
        drawLine(new Point(getWidth() / 2 - 10, 10), new Point(getWidth() / 2, 0), TypeOfLine.AXISLine);
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("TimesRoman", Font.PLAIN, 11));
        graphics.drawString("20", (int) unitVectorSize - 12, 10);
        graphics.drawString("20", 4, (int) -unitVectorSize);
        graphics.setFont(new Font("TimesRoman", Font.BOLD, 15));
    }

    private void drawLine(Point point1, Point point2, TypeOfLine typeOfLine) {
        switch (typeOfLine) {
            case COORDINATEGridLine:
                graphics.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
                graphics.setColor(new Color(0f, 0f, 0f, .4f));
                break;
            case AXISLine:
                graphics.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
                graphics.setColor(new Color(0f, 0f, 0f, .6f));
                break;
            case CURVELine:
                graphics.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
                graphics.setColor(new Color(0f, 0f, 0f, 1f));
                break;
            case DIMENSIONLine:
                graphics.setColor(new Color(0f, 0f, 1f, 1f));
                graphics.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
                break;
        }
        graphics.draw(new Line2D.Double(point1.getX(), -point1.getY(), point2.getX(), -point2.getY()));
    }
}
