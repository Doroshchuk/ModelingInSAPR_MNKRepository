import java.util.ArrayList;

public class PieceLinearInterpolation extends Interpolation{

    public PieceLinearInterpolation(ArrayList<Point> points){
        super(points);
    }

    public double executeInterpolation(double x, ArrayList<Double> listX, ArrayList<Double> listY){
        double y = 0;
        for (int i = 1; i < listX.size(); i++){
            y = listY.get(i - 1) + (x - listX.get(i - 1)) * ((listY.get(i) - listY.get(i - 1)) / (listX.get(i) - listX.get(i - 1)));
        }
       return y;
    }
}
