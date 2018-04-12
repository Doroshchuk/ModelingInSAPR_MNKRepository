import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.*;

public class Plot extends JPanel {
    private Graphics2D graphics;
    private double unitVectorSize;
    private double unitVectorSizeForZoom;
    private Point center;
    private LinkedHashMap<Point, Point> points; // real point, point after zoom
    private boolean zoom;
    private Point selectedPoint;
    private boolean executingPieceLinearInterpolation;
    private boolean executingInterpolationByLagrangePolynomial;
    private ArrayList<Point> pointsUsingPieceLinearInterpolation;
    private ArrayList<Point> pointsUsingInterpolationByLagrangePolynomial;
    private Point minValue;
    private Point maxValue;

    public double getUnitVectorSizeForZoom() {
        return unitVectorSizeForZoom;
    }

    public void setUnitVectorSizeForZoom(double unitVectorSizeForZoom) {
        this.unitVectorSizeForZoom = unitVectorSizeForZoom;
    }

    public Map<Point, Point> getPoints() {
        return points;
    }

    public void setPoints(LinkedHashMap<Point, Point> points) {
        this.points = points;
    }

    public Point getMinValue() {
        return minValue;
    }

    public void setMinValue(Point minValue) {
        this.minValue = minValue;
    }

    public Point getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Point maxValue) {
        this.maxValue = maxValue;
    }

    public ArrayList<Point> getPointsUsingPieceLinearInterpolation() {
        return pointsUsingPieceLinearInterpolation;
    }

    public void setPointsUsingPieceLinearInterpolation(ArrayList<Point> pointsUsingPieceLinearInterpolation) {
        this.pointsUsingPieceLinearInterpolation = pointsUsingPieceLinearInterpolation;
    }

    public ArrayList<Point> getPointsUsingInterpolationByLagrangePolynomial() {
        return pointsUsingInterpolationByLagrangePolynomial;
    }

    public void setPointsUsingInterpolationByLagrangePolynomial(ArrayList<Point> pointsUsingInterpolationByLagrangePolynomial) {
        this.pointsUsingInterpolationByLagrangePolynomial = pointsUsingInterpolationByLagrangePolynomial;
    }

    public boolean isExecutingPieceLinearInterpolation() {
        return executingPieceLinearInterpolation;
    }

    public void setExecutingPieceLinearInterpolation(boolean executingPieceLinearInterpolation) {
        this.executingPieceLinearInterpolation = executingPieceLinearInterpolation;
    }

    public boolean isExecutingInterpolationByLagrangePolynomial() {
        return executingInterpolationByLagrangePolynomial;
    }

    public void setExecutingInterpolationByLagrangePolynomial(boolean executingInterpolationByLagrangePolynomial) {
        this.executingInterpolationByLagrangePolynomial = executingInterpolationByLagrangePolynomial;
    }

    public Point getSelectedPoint() {
        return selectedPoint;
    }

    public void setSelectedPoint(Point selectedPoint) {
        this.selectedPoint = selectedPoint;
    }

    public double getUnitVectorSize() {
        return unitVectorSize;
    }

    public void setUnitVectorSize(double unitVectorSize) {
        this.unitVectorSize = unitVectorSize;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public boolean isZoom() {
        return zoom;
    }

    public void setZoom(boolean zoom) {
        this.zoom = zoom;
    }

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
        graphics.setColor(Color.BLACK);
        if (points.isEmpty()){
            zoom = false;
        }
        drawAxis();
        if (zoom) {
            zoom();
            designateCoordinateAxesAfterZooming();
            drawVertexes(points);
        } else {
            designateCoordinateAxis();
            if (!points.isEmpty())
                drawVertexes(points);
        }
        drawLinesForCoordinateGrid();
        if(executingPieceLinearInterpolation){
            drawPlot(pointsUsingPieceLinearInterpolation, TypeOfLine.LINE);
        }
        if(executingInterpolationByLagrangePolynomial){
            drawPlot(pointsUsingInterpolationByLagrangePolynomial, TypeOfLine.PARABOLA);
        }
    }

    public Plot() {
        unitVectorSize = 20;
        unitVectorSizeForZoom = 1;
        center = new Point(0, 0);
        points = new LinkedHashMap<>();
        minValue = new Point(- 500 + 20, - 340 + 20);
        maxValue = new Point(500 - 20, 340 - 20);
    }

    private void drawLinesForCoordinateGrid() {
        for (int i = 0; i < getWidth(); i += unitVectorSize) {
            //vertical lines
            drawLine(new Point(center.getX() - getWidth() / 2 + i, center.getY() + getHeight()), new Point(center.getX() - getWidth() / 2 + i, center.getY() - getHeight()), TypeOfLine.COORDINATEGridLine);
        }
        for (int i = 0; i < getHeight(); i += unitVectorSize) {
            //horizontal lines
            drawLine(new Point(center.getX() - getWidth(), center.getY() - getHeight() / 2 + i), new Point(center.getX() + getWidth(), center.getY() - getHeight() / 2 + i), TypeOfLine.COORDINATEGridLine);
        }
    }

    private void designateCoordinateAxis(){
        graphics.setFont(new Font("TimesRoman", Font.PLAIN, 11));
        for (int y = getHeight() / 2; y > - getHeight() / 2; y -= unitVectorSize){
            drawLine(new Point(-4, y), new Point(4, y), TypeOfLine.AXISLine);
            graphics.drawString("" + (-y), 4, y);
        }
        for (int x = getWidth() / 2 ; x > - getWidth() / 2; x -= unitVectorSize){
            drawLine(new Point(x, -4), new Point(x, 4), TypeOfLine.AXISLine);
            if (x != 0)
                graphics.drawString("" + x, x, 10);
        }
    }

    public void setRealPoint(Point point){
        LinkedHashMap<Point, Point> oldPoints = (LinkedHashMap<Point, Point>) points.clone();
        points.clear();
        points.put(point, point);
        points.putAll(oldPoints);
    }

    public ArrayList<Point> getRealPoints(){
        ArrayList<Point>realPoints = new ArrayList<>();
        realPoints.addAll(points.keySet());
        return realPoints;
    }

    public ArrayList<Point> getPointsAfterZoom(){
        ArrayList<Point> pointsAfterZoom = new ArrayList<>();
        pointsAfterZoom.addAll(points.values());
        return pointsAfterZoom;
    }

    private void drawAxis() {
        //x and y lines
        drawLine(new Point(0, -getHeight() / 2), new Point(0, getHeight() / 2), TypeOfLine.AXISLine);
        drawLine(new Point(-10, getHeight() / 2 - 10), new Point(0, getHeight() / 2), TypeOfLine.AXISLine);
        drawLine(new Point(10, getHeight() / 2 - 10), new Point(0, getHeight() / 2), TypeOfLine.AXISLine);

        drawLine(new Point(-getWidth() / 2, 0), new Point(getWidth() / 2, 0), TypeOfLine.AXISLine);
        drawLine(new Point(getWidth() / 2 - 10, -10), new Point(getWidth() / 2, 0), TypeOfLine.AXISLine);
        drawLine(new Point(getWidth() / 2 - 10, 10), new Point(getWidth() / 2, 0), TypeOfLine.AXISLine);
        graphics.setFont(new Font("TimesRoman", Font.BOLD, 13));
        graphics.drawString("y", 20, -getHeight() / 2 + 15);
        graphics.drawString("x", (int) (getWidth() / 2 - 30), -15);
    }

    private void drawVertexes(Map<Point, Point> points) {
        int size = 4;
        for (Map.Entry<Point, Point> point : points.entrySet()){
            if (point.getKey().equals(selectedPoint))
                graphics.setColor(Color.RED);
            else graphics.setColor(Color.BLACK);
            graphics.fillOval((int) (center.getX() + point.getValue().getX() - size / 2), (int) (center.getY() - point.getValue().getY() - size / 2), size, size);
            graphics.setColor(Color.BLACK);
        }
    }

    private void drawLine(Point point1, Point point2, TypeOfLine typeOfLine) {
        switch (typeOfLine) {
            case COORDINATEGridLine:
                graphics.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
                graphics.setColor(new Color(0f, 0f, 0f, .4f));
                break;
            case AXISLine:
                graphics.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
                graphics.setColor(new Color(0f, 0f, 0f, .8f));
                break;
            case LINE:
                graphics.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
                graphics.setColor(Color.RED);
                break;
            case PARABOLA:
                graphics.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
                graphics.setColor(Color.GREEN);
                break;
            case DIMENSIONLine:
                graphics.setColor(new Color(0f, 0f, 1f, 1f));
                graphics.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
                break;
        }
        graphics.draw(new Line2D.Double(point1.getX(), -point1.getY(), point2.getX(), -point2.getY()));
    }

    public Point performAffineTransformation(Point r0, Point r_x, Point r_y, Point point){
        return new Point(r0.getX() + r_x.getX() * point.getX() + r_x.getY() * point.getY(), r0.getY() + r_y.getX() * point.getX() + r_y.getY() * point.getY());
    }

    public void zoom(){
        maxValue.setX(findMaxValue("x", getRealPoints()));
        maxValue.setY(findMaxValue("y", getRealPoints()));
        minValue.setX(findMinValue("x", getRealPoints()));
        minValue.setY(findMinValue("y", getRealPoints()));
        double unitVectorSizeX = 0;
        double unitVectorSizeY = 0;
        if (maxValue.getX() > Math.abs(minValue.getX()))
            unitVectorSizeX = maxValue.getX() / ((getWidth() / 2 - 20) / unitVectorSizeForZoom);
        else unitVectorSizeX = Math.abs(minValue.getX()) / ((getWidth() / 2 - 20) / unitVectorSizeForZoom);
        if (maxValue.getY() > Math.abs(minValue.getY()))
            unitVectorSizeY = maxValue.getY() / ((getHeight() / 2 - 20) / unitVectorSizeForZoom);
        else unitVectorSizeY = Math.abs(minValue.getY()) / ((getHeight() / 2 - 20) / unitVectorSizeForZoom);
        if (unitVectorSizeX > unitVectorSizeY){
            unitVectorSizeForZoom = unitVectorSizeForZoom / unitVectorSizeX;
        } else {
            unitVectorSizeForZoom = unitVectorSizeForZoom / unitVectorSizeY;
        }
        center = performAffineTransformation(center, new Point(unitVectorSizeForZoom, center.getY()), new Point(center.getX(), unitVectorSizeForZoom), center);
        for (Point realPoint : points.keySet()){
            Point pointAfterZoom = performAffineTransformation(center, new Point(unitVectorSizeForZoom, center.getY()), new Point(center.getX(), unitVectorSizeForZoom), realPoint);
            points.put(realPoint, pointAfterZoom);
            System.out.println("pointAfterZoom " + pointAfterZoom.getX() + ", " + pointAfterZoom.getY());
        }
    }

    private void designateCoordinateAxesAfterZooming(){
        graphics.setFont(new Font("TimesRoman", Font.PLAIN, 11));
        double h;
        double hY = maxValue.getY() / (getHeight() / 2 - 20);
        double hX = maxValue.getX() / (getWidth() / 2 - 20);
        if (hY > hX)
            h = hY;
        else h = hX;
        for (int y = getHeight() / 2; y > - getHeight() / 2; y -= unitVectorSize){
            drawLine(new Point(-4, y), new Point(4, y), TypeOfLine.AXISLine);
            graphics.drawString("" + String.format("%.2f", h * y), 4, -y);
        }
        int i = -1;
        for (int x = getWidth() / 2; x > - getWidth() / 2; x -= unitVectorSize){
            drawLine(new Point(x, -4), new Point(x, 4), TypeOfLine.AXISLine);
            if (x != 0)
                graphics.drawString("" + String.format("%.2f", h * x), x, 10 * i);
            i *= -1;
        }
    }

    private double findMaxValue(String field, ArrayList<Point> points){
        double max = 0;
        for (Point point: points){
            switch (field){
                case "x":
                    if(point.getX() > max)
                        max = point.getX();
                    break;
                case "y":
                    if(point.getY() > max)
                        max = point.getY();
                    break;
            }
        }
        return max;
    }

    private double findMinValue(String field, ArrayList<Point> points) {
        double minX = points.get(0).getX();
        double minY = points.get(0).getY();
        double min = 0;
        for (int i = 1; i < points.size(); i++) {
            switch (field) {
                case "x":
                    if (points.get(i).getX() < minX)
                        minX = points.get(i).getX();
                    break;
                case "y":
                    if (points.get(i).getY() < minY)
                        minY = points.get(i).getY();
                    break;
            }
        }
        switch (field) {
            case "x":
                if (points.size() == 1)
                    min = - minX;
                else min = minX;
                break;
            case "y":
                if (points.size() == 1)
                    min = - minY;
                else min = minY;
                break;
        }
        return min;
    }

    public double getDistance(Point p1, Point p2) {
        double a = p1.getX() - p2.getX();
        double b = p1.getY() - p2.getY();
        return Math.sqrt(a * a + b * b);
    }

    public void removeSelectedPoint(){
        for(Iterator<Map.Entry<Point,Point>> it = points.entrySet().iterator(); it.hasNext();){
            Map.Entry<Point,Point> entry = it.next();
            if (entry.getKey().equals(selectedPoint)) {
                it.remove();
            }
        }
    }

    public void drawPlot(ArrayList<Point> points, TypeOfLine typeOfLine){
        for (int i = 0; i < points.size() - 1; i++) {
            Point point1AfterZoom = performAffineTransformation(center, new Point(unitVectorSizeForZoom, center.getY()), new Point(center.getX(), unitVectorSizeForZoom), points.get(i));
            Point point2AfterZoom = performAffineTransformation(center, new Point(unitVectorSizeForZoom, center.getY()), new Point(center.getX(), unitVectorSizeForZoom), points.get(i + 1));
            drawLine(point1AfterZoom, point2AfterZoom, typeOfLine);
        }
    }
}
