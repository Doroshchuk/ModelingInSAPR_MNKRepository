import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.*;

public class Plot extends JPanel {
    private Graphics2D graphics;
    private double unitVectorSize;
    private double zoom;
    private Point center;
    private LinkedHashMap<Point, Point> points; // real point, point after zoom
    private Point selectedPoint;
    private boolean executingPieceLinearInterpolation;
    private boolean executingInterpolationByLagrangePolynomial;
    private ArrayList<Point> pointsUsingPieceLinearInterpolation;
    private ArrayList<Point> pointsUsingInterpolationByLagrangePolynomial;
    private Point minValue;
    private Point maxValue;
    private boolean transformationOfPlot;
    private ArrayList<Line> axisLines;
    private ArrayList<Line> gridLines;
    public static int width = 1000;
    public static int height = 720;

    public ArrayList<Line> getAxisLines() {
        return axisLines;
    }

    public void setAxisLines(ArrayList<Line> axisLines) {
        this.axisLines = axisLines;
    }

    public ArrayList<Line> getGridLines() {
        return gridLines;
    }

    public void setGridLines(ArrayList<Line> gridLines) {
        this.gridLines = gridLines;
    }

    public boolean isTransformationOfPlot() {
        return transformationOfPlot;
    }

    public void setTransformationOfPlot(boolean transformationOfPlot) {
        this.transformationOfPlot = transformationOfPlot;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
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

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
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
        if (!transformationOfPlot) {
            drawLinesForCoordinateGrid();
            drawAxis();
        } else {
            drawTransformedLines(axisLines);
            drawTransformedLines(gridLines);
        }
        if (!points.isEmpty())
            drawVertexes(points);
        if (executingPieceLinearInterpolation) {
            drawPlot(pointsUsingPieceLinearInterpolation, TypeOfLine.LINE);
        }
        if (executingInterpolationByLagrangePolynomial) {
            drawPlot(pointsUsingInterpolationByLagrangePolynomial, TypeOfLine.PARABOLA);
        }
    }

    public Plot() {
        unitVectorSize = 20;
        zoom = 1;
        center = new Point(0, 0);
        points = new LinkedHashMap<>();
        minValue = new Point(- 1000 + 20, - 720 + 20);
        maxValue = new Point(1000 - 20, 720 - 20);
        axisLines = new ArrayList<>();
        gridLines = new ArrayList<>();
    }

    public void drawTransformedLines(ArrayList<Line> lines){
        int linesSize = lines.size();
        for (int i = 0; i < linesSize; i++) {
            drawLine(lines.get(i).getStartPoint(), lines.get(i).getEndPoint(), lines.get(i).getTypeOfLine());
        }
    }

    private void drawLinesForCoordinateGrid() {
        for (int i = 0; i < width; i += unitVectorSize) {
            //vertical lines
            drawLine(new Point(center.getX() - width / 2 + i, center.getY() + height), new Point(center.getX() - width / 2 + i, center.getY() - height), TypeOfLine.COORDINATEGridLine);
        }
        for (int i = 0; i < height; i += unitVectorSize) {
            //horizontal lines
            drawLine(new Point(center.getX() - width, center.getY() - height / 2 + i), new Point(center.getX() + width, center.getY() - height / 2 + i), TypeOfLine.COORDINATEGridLine);
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
        drawLine(new Point(center.getX(), center.getY() - height / 2), new Point(center.getX(), center.getY() + height / 2), TypeOfLine.AXISLine);
        drawLine(new Point(center.getX() - 10, center.getY() + height / 2 - 10), new Point(center.getX(), center.getY() + height / 2), TypeOfLine.AXISLine);
        drawLine(new Point(center.getX() + 10, center.getY() + height / 2 - 10), new Point(center.getX(), center.getY() + height / 2), TypeOfLine.AXISLine);

        drawLine(new Point(center.getX() - width / 2, center.getY()), new Point(center.getX() + width / 2, center.getY()), TypeOfLine.AXISLine);
        drawLine(new Point(center.getX() + width / 2 - 10, center.getY() - 10), new Point(center.getX() + width / 2, center.getY()), TypeOfLine.AXISLine);
        drawLine(new Point(center.getX() + width / 2 - 10, center.getY() + 10), new Point(center.getX() + width / 2, center.getY()), TypeOfLine.AXISLine);
        graphics.setFont(new Font("TimesRoman", Font.BOLD, 13));
        graphics.drawString("y", (int) center.getX() + 20, (int) (center.getY() - height / 2 + 15));
        graphics.drawString("x", (int) (center.getX() + width / 2 - 30), (int)center.getY() - 15);
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
        Line currentLine = new Line(point1, point2, typeOfLine);
        switch (typeOfLine) {
            case COORDINATEGridLine:
                if (!gridLines.contains(currentLine))
                    gridLines.add(new Line(point1, point2, typeOfLine));
                graphics.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
                graphics.setColor(new Color(0f, 0f, 0f, .4f));
                break;
            case AXISLine:
                if (!axisLines.contains(currentLine))
                    axisLines.add(new Line(point1, point2, typeOfLine));
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
            unitVectorSizeX = maxValue.getX() / ((getWidth() / 2 - 20) / zoom);
        else unitVectorSizeX = Math.abs(minValue.getX()) / ((getWidth() / 2 - 20) / zoom);
        if (maxValue.getY() > Math.abs(minValue.getY()))
            unitVectorSizeY = maxValue.getY() / ((getHeight() / 2 - 20) / zoom);
        else unitVectorSizeY = Math.abs(minValue.getY()) / ((getHeight() / 2 - 20) / zoom);
        if (unitVectorSizeX > unitVectorSizeY){
            zoom = zoom / unitVectorSizeX;
        } else {
            zoom = zoom / unitVectorSizeY;
        }
        center = performAffineTransformation(center, new Point(zoom, center.getY()), new Point(center.getX(), zoom), center);
        for (Point realPoint : points.keySet()){
            Point pointAfterZoom = performAffineTransformation(center, new Point(zoom, center.getY()), new Point(center.getX(), zoom), realPoint);
            points.put(realPoint, pointAfterZoom);
            System.out.println("pointAfterZoom " + pointAfterZoom.getX() + ", " + pointAfterZoom.getY());
        }
    }

    public void zoom(float coef){
        zoom = zoom * coef;
        transformationOfPlot = true;
        for (Point realPoint : points.keySet()){
            Point pointAfterZoom = performAffineTransformation(center, new Point(coef, center.getY()), new Point(center.getX(), coef), realPoint);
            points.put(realPoint, pointAfterZoom);
            System.out.println("pointAfterZoom " + pointAfterZoom.getX() + ", " + pointAfterZoom.getY());
        }
//        zoom = performAffineTransformation(center, new Point(coef, center.getY()), new Point(center.getX(), coef), zoom);
        gridLines = manipulateAffinnesTransformation(gridLines, center, new Point(coef, center.getY()), new Point(center.getX(), coef));
        axisLines = manipulateAffinnesTransformation(axisLines, center, new Point(coef, center.getY()), new Point(center.getX(), coef));
        center = performAffineTransformation(center, new Point(coef, center.getY()), new Point(center.getX(), coef), center);
    }

    public ArrayList<Line> manipulateAffinnesTransformation(ArrayList<Line> initialLines, Point r0, Point r_x, Point r_y){
        ArrayList<Line> transformedLines = new ArrayList<>();
        for (int i = 0; i < initialLines.size(); i++) {
            Point startPoint = performAffineTransformation(r0, r_x, r_y, initialLines.get(i).getStartPoint());
            Point endPoint = performAffineTransformation(r0, r_x, r_y, initialLines.get(i).getEndPoint());
            transformedLines.add(new Line(startPoint, endPoint, initialLines.get(i).getTypeOfLine()));
        }
        return transformedLines;
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
            Point point1AfterZoom = performAffineTransformation(center, new Point(zoom, center.getY()), new Point(center.getX(), zoom), points.get(i));
            Point point2AfterZoom = performAffineTransformation(center, new Point(zoom, center.getY()), new Point(center.getX(), zoom), points.get(i + 1));
            drawLine(point1AfterZoom, point2AfterZoom, typeOfLine);
        }
    }
}
