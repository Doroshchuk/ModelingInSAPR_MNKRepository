import java.util.ArrayList;

public class PieceLinearInterpolation extends Interpolation{

    public PieceLinearInterpolation(ArrayList<Point> points){
        super(points);
    }

    public double executeInterpolation(double x, ArrayList<Double> listX, ArrayList<Double> listY){
//        int i = 0;
//        while (x > listX.get(i)){
//            i++;
//        }
//        i--;
//        double x1 = listX.get(i);
//        double x2 = listX.get(i + 1);
//        double y1 = listY.get(i);
//        double y2 = listY.get(i + 1);
//        double a = (y2 - y1) / (x2 - x1);
//        double b = y1 - a * x1;
//        return (a * x  + b);
        ArrayList<Double> listK = new ArrayList<>();
        for (int i = 0; i < listX.size() - 1; i++){
            listK.add((listY.get(i + 1) - listY.get(i)) / (listX.get(i + 1) - listX.get(i)));
        }
        ArrayList<Double> listB = new ArrayList<>();
        for (int i = 0; i < listK.size(); i++){
            listB.add(listY.get(i) - listK.get(i) * listX.get(i));
        }
        int i = 0;
        if (x <= listX.get(0))
            return listY.get(0);
        else if (x >= listX.get(listX.size() - 1))
            return listY.get(listY.size() - 1);

        // . в точках?
        for (i = 0; i < listX.size() - 1; ++i)
            if (x == listX.get(i))
                return listY.get(i);

        // . между точками?
        for (i = 0; i < listX.size() - 1; ++i)
            if (x >= listX.get(i) && x <= listX.get(i + 1))
                return listK.get(i) * x + listB.get(i);
        return 0;
    }
}
