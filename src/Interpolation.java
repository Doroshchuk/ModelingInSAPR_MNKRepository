import java.util.ArrayList;

public abstract class Interpolation {
    private ArrayList<Point> points;
    private ArrayList<Double> listX;
    private ArrayList<Double> listY;

    public Interpolation(ArrayList<Point> points){
        this.points = points;
        this.listX = new ArrayList<>();
        this.listY = new ArrayList<>();
        for (Point point: points){
            listX.add(point.getX());
            listY.add(point.getY());
        }
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }

    public ArrayList<Double> getListX() {
        return listX;
    }

    public void setListX(ArrayList<Double> listX) {
        this.listX = listX;
    }

    public ArrayList<Double> getListY() {
        return listY;
    }

    public void setListY(ArrayList<Double> listY) {
        this.listY = listY;
    }

    public abstract double executeInterpolation(double x, ArrayList<Double> listX, ArrayList<Double> listY);

    public ArrayList<Point> calculatePoints(double minValue, double maxValue){
        ArrayList<Point> resultPoints = new ArrayList<>();
        for(double i = minValue; i <= maxValue; i += 0.01){
            resultPoints.add(new Point(i, executeInterpolation(i, listX, listY)));
        }
        return resultPoints;
    }
}
