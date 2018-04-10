import java.util.ArrayList;

public class LagrangePolynomial {
    private ArrayList<Point> points;
    private ArrayList<Double> listX;
    private ArrayList<Double> listY;

    public LagrangePolynomial(ArrayList<Point> points){
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

    private double executeInterpoliationUsingLagrangePolynomial(double x){
        double result = 0;
        int size = listX.size();
        for(int i = 0; i < size; i++) {
            double basicsPol = 1;
            for(int j = 0; j < size; j++) {
                if (j != i) {
                    basicsPol *= (x - listX.get(j))/(listX.get(i) - listX.get(j));
                }
            }
            result += basicsPol * listY.get(i);
        }
        return result;
    }

    public ArrayList<Point> calculatePoints(){
        ArrayList<Point> resultPoints = new ArrayList<>();
        for(int i = -500; i <= 500; i++){
            resultPoints.add(new Point(i, executeInterpoliationUsingLagrangePolynomial(i)));
        }
        return resultPoints;
    }
}