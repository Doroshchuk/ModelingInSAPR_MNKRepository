import java.util.ArrayList;

public class PieceLinearInterpolation extends Interpolation{

    public PieceLinearInterpolation(ArrayList<Point> points){
        super(points);
    }

    public double executeInterpolation(double x, ArrayList<Double> listX, ArrayList<Double> listY){
        int i = 0;
        while (x > listX.get(i)){
            i++;
        }
        i--;
        double x1 = listX.get(i);
        double x2 = listX.get(i + 1);
        double y1 = listY.get(i);
        double y2 = listY.get(i + 1);
        double a = (y2 - y1) / (x2 - x1);
        double b = y1 - a * x1;
        return (a * x  + b);
    }
}
