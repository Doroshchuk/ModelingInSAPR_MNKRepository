import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Plot extends JPanel {
    private Graphics2D graphics;
    private double unitVectorSize;
    private double unitVectorSizeForZoom;
    private Point center;
    private Map<Point, Point> points; // real point, point after zoom
    private ArrayList<Line> axisLines;
    private ArrayList<Line> gridLines;
    private boolean zoom;
    private Point selectedPoint;
    private boolean executingMNKByLine;
    private boolean executingMNKByParabola;
    private ArrayList<Point> pointsUsingOrdinaryLeastSquaresByLine;
    private ArrayList<Point> pointsUsingOrdinaryLeastSquaresByParabola;
    private Point minValue;
    private Point maxValue;
    private Rescale rescaleX;
    private Rescale rescaleY;

    public double getUnitVectorSizeForZoom() {
        return unitVectorSizeForZoom;
    }

    public void setUnitVectorSizeForZoom(double unitVectorSizeForZoom) {
        this.unitVectorSizeForZoom = unitVectorSizeForZoom;
    }

    public Map<Point, Point> getPoints() {
        return points;
    }

    public void setPoints(Map<Point, Point> points) {
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

    public Rescale getRescaleX() {
        return rescaleX;
    }

    public void setRescaleX(Rescale rescaleX) {
        this.rescaleX = rescaleX;
    }

    public Rescale getRescaleY() {
        return rescaleY;
    }

    public void setRescaleY(Rescale rescaleY) {
        this.rescaleY = rescaleY;
    }

    public ArrayList<Point> getPointsUsingOrdinaryLeastSquaresByLine() {
        return pointsUsingOrdinaryLeastSquaresByLine;
    }

    public void setPointsUsingOrdinaryLeastSquaresByLine(ArrayList<Point> pointsUsingOrdinaryLeastSquaresByLine) {
        this.pointsUsingOrdinaryLeastSquaresByLine = pointsUsingOrdinaryLeastSquaresByLine;
    }

    public ArrayList<Point> getPointsUsingOrdinaryLeastSquaresByParabola() {
        return pointsUsingOrdinaryLeastSquaresByParabola;
    }

    public void setPointsUsingOrdinaryLeastSquaresByParabola(ArrayList<Point> pointsUsingOrdinaryLeastSquaresByParabola) {
        this.pointsUsingOrdinaryLeastSquaresByParabola = pointsUsingOrdinaryLeastSquaresByParabola;
    }

    public boolean isExecutingMNKByLine() {
        return executingMNKByLine;
    }

    public void setExecutingMNKByLine(boolean executingMNKByLine) {
        this.executingMNKByLine = executingMNKByLine;
    }

    public boolean isExecutingMNKByParabola() {
        return executingMNKByParabola;
    }

    public void setExecutingMNKByParabola(boolean executingMNKByParabola) {
        this.executingMNKByParabola = executingMNKByParabola;
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
            designateCoordinateAxes();
            if (!points.isEmpty())
                drawVertexes(points);
        }
        drawLinesForCoordinateGrid();
        if(executingMNKByLine){
            drawPlot(pointsUsingOrdinaryLeastSquaresByLine, TypeOfLine.LINE);
        }
        if(executingMNKByParabola){
            drawPlot(pointsUsingOrdinaryLeastSquaresByParabola, TypeOfLine.PARABOLA);
        }
    }

    public Plot() {
        unitVectorSize = 20;
        unitVectorSizeForZoom = unitVectorSize;
        center = new Point(0, 0);
        points = new HashMap<>();
        axisLines = new ArrayList<>();
        gridLines = new ArrayList<>();
        minValue = new Point(- 1000 / 2 + 20, - 720 / 2 + 20);
        maxValue = new Point(1000 / 2 - 20, 720 / 2 - 20);
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

    private void designateCoordinateAxes(){
        graphics.setFont(new Font("TimesRoman", Font.PLAIN, 11));
        for (int y = - getHeight() / 2; y < getHeight() / 2; y += unitVectorSize){
            drawLine(new Point(-4, y), new Point(4, y), TypeOfLine.AXISLine);
            graphics.drawString("" + -y, 4, y);
        }
        for (int x = - getWidth() / 2; x < getWidth() / 2; x += unitVectorSize){
            drawLine(new Point(x, -4), new Point(x, 4), TypeOfLine.AXISLine);
            graphics.drawString("" + -x, x, 10);
        }
    }

    public void setRealPoint(Point point){
        points.put(point, point);
    }

//    public Point getRealPointByPointAfterZoom(Point pointAfterZoom){
//        for (Map.Entry point : points.entrySet()){
//            if (point.getValue().equals(pointAfterZoom)){
//                return (Point) point.getKey();
//            }
//        }
//        return null;
//    }
//
//    public Point getPointAfterZoomByRealPoint(Point realPoint){
//        for (Map.Entry point : points.entrySet()){
//            if (point.getValue().equals(realPoint)){
//                return (Point) point.getValue();
//            }
//        }
//        return null;
//    }

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
                if (!gridLines.contains(new Line(point1, point2, typeOfLine)))
                    gridLines.add(new Line(point1, point2, typeOfLine));
                break;
            case AXISLine:
                graphics.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
                graphics.setColor(new Color(0f, 0f, 0f, .8f));
                if (!axisLines.contains(new Line(point1, point2, typeOfLine)))
                    axisLines.add(new Line(point1, point2, typeOfLine));
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

//    public void zoom(){
//        maxValue.setX(findMaxValue("x", points));
//        maxValue.setY(findMaxValue("y", points));
//        minValue.setX(findMinValue("x", points));
//        minValue.setY(findMinValue("y", points));
//
//        rescaleX = new Rescale(minValue.getX(), maxValue.getX(), minRange.getX(), maxRange.getX());
//        rescaleY = new Rescale(minValue.getY(), maxValue.getY(), minRange.getY(), maxRange.getY());
//        for (Point point: points){
//            point.setX(rescaleX.rescale(point.getX()));
//            point.setY(rescaleY.rescale(point.getY()));
//            System.out.println("Point:" + point.getX() + "," + point.getY());
//        }
//
////        Rescale signY = new Rescale(minY, maxY, minRange.getY(), maxRange.getY());
//        double unitVectorSizeY = Math.abs(rescaleY.rescale(unitVectorSize));
//        double unitVectorSizeX = Math.abs(rescaleX.rescale(unitVectorSize));
//        System.out.println("unitVectorSizeY " + unitVectorSizeY);
//        System.out.println("unitVectorSizeX " + unitVectorSizeX);
//        if (unitVectorSizeY > unitVectorSizeX){
//            for (double y = getHeight() / 2 - 20, s = maxValue.getY(); y > - getHeight() / 2 + 20 && s > minValue.getY(); y -= unitVectorSize, s -= unitVectorSizeY){
//                drawLine(new Point(-4, Math.round(y)), new Point(4, Math.round(y)), TypeOfLine.AXISLine);
//                graphics.drawString("" + s, 4, Math.round(-y));
//                System.out.println(y + " | " + s);
//            }
//        } else {
//            for (double y = getHeight() / 2 - 20, s = maxValue.getY(); y > - getHeight() / 2 + 20 && s > minValue.getY(); y -= unitVectorSize, s -= unitVectorSizeX){
//                drawLine(new Point(-4, Math.round(y)), new Point(4, Math.round(y)), TypeOfLine.AXISLine);
//                graphics.drawString("" + s, 4, Math.round(-y));
//                System.out.println(y + " | " + s);
//            }
//        }
//
//        this.minRange = new Point(minValue.getX(), minValue.getY());
//        this.maxRange = new Point(maxValue.getX(), maxValue.getY());
////        for (double sign = (int) minY, h = - getHeight() / 2; sign <= maxY && h < getHeight() / 2; sign += rescaleY.rescale(unitVectorSizeY), h += unitVectorSizeY){
////            graphics.drawString("" + (int) sign, 4, (int) - h);
////            System.out.println(sign + "    " + h);
////        }
////        for (int x = - getWidth() / 2; x < getWidth() / 2; x += unitVectorSizeX){
////            graphics.drawString("" + rescaleX.rescale(x), x, 10);
////
//    }

    public Point performAffineTransformation(Point r0, Point r_x, Point r_y, Point point){
        return new Point(r0.getX() + r_x.getX() * point.getX() + r_x.getY() * point.getY(), r0.getY() + r_y.getX() * point.getX() + r_y.getY() * point.getY());
//        axisLines = manipulateAffinnesTransformation(axisLines, r0, r_x, r_y);
//        gridLines = manipulateAffinnesTransformation(gridLines, r0, r_x, r_y);
    }

    public ArrayList<Line> manipulateAffinnesTransformation(ArrayList<Line> initialLines, Point r0, Point r_x, Point r_y){
        ArrayList<Line> transformedLines = new ArrayList<>();
        for (Line initialLine : initialLines) {
            double x_st = r0.getX() + r_x.getX() * initialLine.getStartPoint().getX() + r_x.getY() * initialLine.getStartPoint().getY();
            double y_st = r0.getY() + r_y.getX() * initialLine.getStartPoint().getX() + r_y.getY() * initialLine.getStartPoint().getY();
            double x_en = r0.getX() + r_x.getX() * initialLine.getEndPoint().getX() + r_x.getY() * initialLine.getEndPoint().getY();
            double y_en = r0.getY() + r_y.getX() * initialLine.getEndPoint().getX() + r_y.getY() * initialLine.getEndPoint().getY();
            transformedLines.add(new Line(new Point(x_st, y_st), new Point(x_en, y_en), initialLine.getTypeOfLine()));
        }
        return transformedLines;
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
            System.out.println("unitVectorSizeX " + unitVectorSizeX);
            center = performAffineTransformation(center, new Point(unitVectorSizeForZoom / unitVectorSizeX, center.getY()), new Point(center.getX(), unitVectorSizeForZoom / unitVectorSizeX), center);
            for (Point realPoint : points.keySet()){
                Point pointAfterZoom = performAffineTransformation(center, new Point(unitVectorSizeForZoom / unitVectorSizeX, center.getY()), new Point(center.getX(), unitVectorSizeForZoom / unitVectorSizeX), realPoint);
                points.put(realPoint, pointAfterZoom);
                System.out.println("pointAfterZoom " + pointAfterZoom.getX() + ", " + pointAfterZoom.getY());
            }
            unitVectorSizeForZoom = unitVectorSizeForZoom / unitVectorSizeX;
        } else {
            System.out.println("unitVectorSizeY " + unitVectorSizeY);
            center = performAffineTransformation(center, new Point(unitVectorSizeForZoom / unitVectorSizeY, center.getY()), new Point(center.getX(), unitVectorSizeForZoom / unitVectorSizeY), center);
            for (Point realPoint : points.keySet()){
                System.out.println("realPoint " + realPoint.getX() + ", " + realPoint.getY());
                Point pointAfterZoom = performAffineTransformation(center, new Point(unitVectorSizeForZoom / unitVectorSizeY, center.getY()), new Point(center.getX(), unitVectorSizeForZoom / unitVectorSizeY), realPoint);
                points.put(realPoint, pointAfterZoom);
                System.out.println("pointAfterZoom " + pointAfterZoom.getX() + ", " + pointAfterZoom.getY());
            }
            unitVectorSizeForZoom = unitVectorSizeForZoom / unitVectorSizeY;
        }
    }

    private void designateCoordinateAxesAfterZooming(){
//        graphics.setFont(new Font("TimesRoman", Font.PLAIN, 11));
//        double h = maxValue.getY();
//        System.out.println("maxValue " + h);
//        for (int y = getHeight() / 2 - 20; y > - getHeight() / 2 + 20; y -= unitVectorSize){
//            drawLine(new Point(-4, y), new Point(4, y), TypeOfLine.AXISLine);
//            graphics.drawString("" + Math.round(h), 4, - y);
//            h -= unitVectorSizeForZoom;
//            System.out.println("h " + h);
//        }
//        for (int x = getWidth() / 2; x > - getWidth() / 2; x += unitVectorSize){
//            drawLine(new Point(x, -4), new Point(x, 4), TypeOfLine.AXISLine);
//            graphics.drawString("" + (minValue.getX() + unitVectorSizeForZoom), x, 10);
//        }
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
        for (Point realPoint: points.keySet()) {
            if (realPoint.equals(selectedPoint))
                points.remove(realPoint);
        }
//        points.remove(new Point(selectedPoint.getX(), selectedPoint.getY()));
    }

    public void drawPlot(ArrayList<Point> points, TypeOfLine typeOfLine){
        for (int i = 0; i < points.size() - 1; i++) {
            drawLine(points.get(i), points.get(i + 1), typeOfLine);
        }
    }
}
