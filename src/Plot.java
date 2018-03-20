import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Plot extends JPanel {
    private Graphics2D graphics;
    private double unitVectorSizeX;
    private double unitVectorSizeY;
    private double x0;
    private double y0;
    private ArrayList<Point> points;
    private ArrayList<Line> axisLines;
    private ArrayList<Line> gridLines;
    private boolean choice;
    private Point selectedPoint;
    private boolean executingMNKByLine;
    private boolean executingMNKByParabola;
    private ArrayList<Point> pointsUsingOrdinaryLeastSquaresByLine;
    private ArrayList<Point> pointsUsingOrdinaryLeastSquaresByParabola;

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

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }

    public double getUnitVectorSizeX() {
        return unitVectorSizeX;
    }

    public void setUnitVectorSizeX(double unitVectorSizeX) {
        this.unitVectorSizeX = unitVectorSizeX;
    }

    public double getUnitVectorSizeY() {
        return unitVectorSizeY;
    }

    public void setUnitVectorSizeY(double unitVectorSizeY) {
        this.unitVectorSizeY = unitVectorSizeY;
    }

    public double getX0() {
        return x0;
    }

    public void setX0(double x0) {
        this.x0 = x0;
    }

    public double getY0() {
        return y0;
    }

    public void setY0(double y0) {
        this.y0 = y0;
    }

    public boolean isChoice() {
        return choice;
    }

    public void setChoice(boolean choice) {
        this.choice = choice;
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
        drawLinesForCoordinateGrid();
        graphics.setColor(Color.BLACK);
        drawAxis();
        if (choice) zoom();
        else designateCoordinateAxes();
        if(executingMNKByLine){
            drawPlot(pointsUsingOrdinaryLeastSquaresByLine, TypeOfLine.LINE);
            executingMNKByLine = false;
        }
        if(executingMNKByParabola){
            drawPlot(pointsUsingOrdinaryLeastSquaresByParabola, TypeOfLine.PARABOLA);
            executingMNKByParabola = false;
        }
        if (!points.isEmpty())
            drawVertexes(points);
    }

    public Plot() {
        unitVectorSizeX = 20;
        unitVectorSizeY = 20;
        x0 = 0;
        y0 = 0;
        points = new ArrayList<>();
        axisLines = new ArrayList<>();
        gridLines = new ArrayList<>();
    }

    private void drawLinesForCoordinateGrid() {
        for (int i = 0; i < getWidth(); i += unitVectorSizeY) {
            //vertical lines
            drawLine(new Point(x0 - getWidth() / 2 + i, y0 + getHeight()), new Point(x0 - getWidth() / 2 + i, y0 - getHeight()), TypeOfLine.COORDINATEGridLine);
        }
        for (int i = 0; i < getHeight(); i += unitVectorSizeX) {
            //horizontal lines
            drawLine(new Point(x0 - getWidth(), y0 - getHeight() / 2 + i), new Point(x0 + getWidth(), y0 - getHeight() / 2 + i), TypeOfLine.COORDINATEGridLine);
        }
    }

    private void designateCoordinateAxes(){
        graphics.setFont(new Font("TimesRoman", Font.PLAIN, 11));
        for (int y = - getHeight() / 2; y < getHeight() / 2; y += unitVectorSizeY){
            drawLine(new Point(-4, y), new Point(4, y), TypeOfLine.AXISLine);
            graphics.drawString("" + y, 4, y);
        }
        for (int x = - getWidth() / 2; x < getWidth() / 2; x += unitVectorSizeX){
            drawLine(new Point(x, -4), new Point(x, 4), TypeOfLine.AXISLine);
            graphics.drawString("" + x, x, 10);
        }
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

    public void addPoint(Point newPoint){
        points.add(newPoint);
    }

    private void drawVertexes(ArrayList<Point> points) {
        int size = 4;
        for (Point point : points) {
            if (point.equals(selectedPoint))
                graphics.setColor(Color.RED);
            else graphics.setColor(Color.BLACK);
            graphics.fillOval((int) (x0 + point.getX() - size / 2), (int) (y0 - point.getY() - size / 2), size, size);
            graphics.setColor(Color.BLACK);
            graphics.drawString(Integer.toString(points.indexOf(point)), (int) (x0 + point.getX() + size), (int) (y0 - point.getY()));
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

    public void zoom(){
        double maxX = findMaxValue("x", points);
        double maxY = findMaxValue("y", points);
        double minX = findMinValue("x", points);
        double minY = findMinValue("y", points);

        Rescale rescaleX = new Rescale(minX, maxX, - getWidth() / 2 + 20, getWidth() / 2 - 20);
        Rescale rescaleY = new Rescale(minY, maxY, - getHeight() / 2 + 20, getHeight() / 2 - 20);

        for (Point point: points){
            point.setX(rescaleX.rescale(point.getX()));
            point.setY(rescaleY.rescale(point.getY()));
        }
//        for (double sign = (int) minY, h = - getHeight() / 2; sign <= maxY && h < getHeight() / 2; sign += rescaleY.rescale(unitVectorSizeY), h += unitVectorSizeY){
//            graphics.drawString("" + (int) sign, 4, (int) - h);
//            System.out.println(sign + "    " + h);
//        }
//        for (int x = - getWidth() / 2; x < getWidth() / 2; x += unitVectorSizeX){
//            graphics.drawString("" + rescaleX.rescale(x), x, 10);
//        }
    }
//
//    public void performAffineTransformation(Point r0, Point r_x, Point r_y){
//        x0 = r0.getX() + r_x.getX() * x0 + r_x.getY() * y0;
//        y0 = r0.getY() + r_y.getX() * x0 + r_y.getY() * y0;
//        for (Point point: points){
//            point.setX(r0.getX() + r_x.getX() * point.getX() + r_x.getY() * point.getY());
//            point.setY(r0.getY() + r_y.getX() * point.getX() + r_y.getY() * point.getY());
//        }
//        axisLines = manipulateAffinnesTransformation(axisLines, r0, r_x, r_y);
//        gridLines = manipulateAffinnesTransformation(gridLines, r0, r_x, r_y);
//    }
//
//    public ArrayList<Line> manipulateAffinnesTransformation(ArrayList<Line> initialLines, Point r0, Point r_x, Point r_y){
//        ArrayList<Line> transformedLines = new ArrayList<>();
//        for (Line initialLine : initialLines) {
//            double x_st = r0.getX() + r_x.getX() * initialLine.getStartPoint().getX() + r_x.getY() * initialLine.getStartPoint().getY();
//            double y_st = r0.getY() + r_y.getX() * initialLine.getStartPoint().getX() + r_y.getY() * initialLine.getStartPoint().getY();
//            double x_en = r0.getX() + r_x.getX() * initialLine.getEndPoint().getX() + r_x.getY() * initialLine.getEndPoint().getY();
//            double y_en = r0.getY() + r_y.getX() * initialLine.getEndPoint().getX() + r_y.getY() * initialLine.getEndPoint().getY();
//            transformedLines.add(new Line(new Point(x_st, y_st), new Point(x_en, y_en), initialLine.getTypeOfLine()));
//        }
//        return transformedLines;
//    }
//
//    public void executeZoom(){
//        double maxX = findMaxValue("x", points);
//        double maxY = findMaxValue("y", points);
//        double coefByX = maxX / (getWidth() / 2);
//        double coefByY = maxY / (getHeight() / 2);
//        performAffineTransformation(new Point(x0, y0), new Point(coefByX, y0), new Point(x0, coefByY));
//    }

//    private double scaleBetween(double value, double minAllowed, double maxAllowed, double min, double max){
//        return (maxAllowed - minAllowed) * (value - min) / (max - min) + minAllowed;
//    }
//
//    public void zoom(){
//        double maxX = findMaxValue("x", points);
//        double maxY = findMaxValue("y", points);
//        double minX = findMinValue("x", points);
//        double minY = findMinValue("y", points);
//
//        for (Point point: points){
//            point.setX(scaleBetween(point.getX(), - getWidth() / 2, getWidth() / 2, minX, maxX));
//            point.setY(scaleBetween(point.getY(), - getHeight() / 2, getHeight() / 2, minY, maxY));
//        }
//    }

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
        points.remove(selectedPoint);
    }

    public void drawPlot(ArrayList<Point> points, TypeOfLine typeOfLine){
        for (int i = 0; i < points.size() - 1; i++) {
            drawLine(points.get(i), points.get(i + 1), typeOfLine);
        }
    }
}
