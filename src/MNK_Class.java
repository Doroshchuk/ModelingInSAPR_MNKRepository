import java.util.ArrayList;

public class MNK_Class {
    private ArrayList<Double> listX;
    private ArrayList<Double> listY;

    public MNK_Class(ArrayList<Double> listX, ArrayList<Double> listY){
        this.listX = listX;
        this.listY = listY;
    }

    public ArrayList<Point> calculatePointsUsingOrdinaryLeastSquaresByLine(ArrayList<Double> listX, ArrayList<Double> listY){
        ArrayList<Point> points = new ArrayList<>();
        int n = listX.size();
        double sumOfListX = calculateSum(listX);
        double sumOfListY = calculateSum(listY);
        double sumOfSquaredListX = calculateSum(multiplyItemsFromTheSameLists(2, listX));
        double sumOfMultipliedListXListY = calculateSum(multiplyItemsFromTwoLists(listX, listY));
        Matrix mainMatrix = new Matrix(sumOfListX, n, sumOfSquaredListX, sumOfListX);
        double mainDeterminant = mainMatrix.getDeterminant();
        double a = calculateCoefficient(mainDeterminant, new Matrix(sumOfListY, n, sumOfMultipliedListXListY, sumOfListX));
        double b = calculateCoefficient(mainDeterminant, new Matrix(sumOfListX, sumOfListY, sumOfSquaredListX, sumOfMultipliedListXListY));
        for (int x = -50; x <= 50; x += 1){
            double y = a * x + b;
            points.add(new Point(x, y));
        }
        return points;
    }

    public ArrayList<Point> calculatePointsUsingOrdinaryLeastSquaresByParabola(ArrayList<Double> listX, ArrayList<Double> listY){
        ArrayList<Point> points = new ArrayList<>();
        int n = listX.size();
        double sumOfListX = calculateSum(listX);
        double sumOfListY = calculateSum(listY);
        double sumOfSquaredListX = calculateSum(multiplyItemsFromTheSameLists(2, listX));
        double sumOfCubedListX = calculateSum(multiplyItemsFromTheSameLists(3, listX));
        double sumOfListXInFourthDegree = calculateSum(multiplyItemsFromTheSameLists(4, listX));
        double sumOfMultipliedListXListY = calculateSum(multiplyItemsFromTwoLists(listX, listY));
        double sumOfMultipliedSquaredListXListY = calculateSum(multiplyItemsFromTwoLists(multiplyItemsFromTheSameLists(2, listX), listY));
        Matrix mainMatrix = new Matrix(sumOfSquaredListX, sumOfListX, n, sumOfCubedListX, sumOfSquaredListX, sumOfListX, sumOfListXInFourthDegree, sumOfCubedListX, sumOfSquaredListX);
        double mainDeterminant = mainMatrix.getDeterminant();
        Matrix matrixA = new Matrix(sumOfListY, sumOfListX, n, sumOfMultipliedListXListY, sumOfSquaredListX, sumOfListX, sumOfMultipliedSquaredListXListY, sumOfCubedListX, sumOfSquaredListX);
        double a = calculateCoefficient(mainDeterminant, matrixA);
        Matrix matrixB = new Matrix(sumOfSquaredListX, sumOfListY, n, sumOfCubedListX, sumOfMultipliedListXListY, sumOfListX, sumOfListXInFourthDegree, sumOfMultipliedSquaredListXListY, sumOfSquaredListX);
        double b = calculateCoefficient(mainDeterminant, matrixB);
        Matrix matrixC = new Matrix(sumOfSquaredListX, sumOfListX, sumOfListY, sumOfCubedListX, sumOfSquaredListX, sumOfMultipliedListXListY, sumOfListXInFourthDegree, sumOfCubedListX, sumOfMultipliedSquaredListXListY);
        double c = calculateCoefficient(mainDeterminant, matrixC);
        for (int x = -50; x <= 50; x += 1){
            double y = a * Math.pow(x, 2) + b * x + c;
            points.add(new Point(x, y));
        }
        return points;
    }

    private double calculateCoefficient(double mainDeterminant, Matrix coefMatrix){
        return coefMatrix.getDeterminant() / mainDeterminant;
    }

    private double calculateSum(ArrayList<Double> list){
        double sum = 0;
        for (Double element : list) {
            sum += element;
        }
        return  sum;
    }

    private ArrayList<Double> multiplyItemsFromTwoLists(ArrayList<Double> listX, ArrayList<Double> listY){
        ArrayList<Double> resultList = new ArrayList<>();
        for (int i = 0; i < listX.size(); i++){
            resultList.add(listX.get(i) * listY.get(i));
        }
        return resultList;
    }

    private ArrayList<Double> multiplyItemsFromTheSameLists(int power, ArrayList<Double> list){
        ArrayList<Double> resultList = new ArrayList<>();
        for (double element: list){
            resultList.add(Math.pow(element, power));
        }
        return  resultList;
    }
}
