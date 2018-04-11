import java.util.ArrayList;

public class LagrangePolynomial extends Interpolation{

    public LagrangePolynomial(ArrayList<Point> points){
        super(points);
    }

    public double executeInterpolation(double x, ArrayList<Double> listX, ArrayList<Double> listY){
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
}