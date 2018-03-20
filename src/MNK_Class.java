import java.util.ArrayList;

public class MNK_Class {
    private ArrayList<Point> points;
    private ArrayList<Double> listX;
    private ArrayList<Double> listY;
    private double aLine;
    private double bLine;
    private double aParabola;
    private double bParabola;
    private double cParabola;

    public MNK_Class(ArrayList<Point> points){
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

    public ArrayList<Double> getListY() {
        return listY;
    }

    public void setListX(ArrayList<Double> listX) {
        this.listX = listX;
    }

    public void setListY(ArrayList<Double> listY) {
        this.listY = listY;
    }

    public double getaLine() {
        return aLine;
    }

    public void setaLine(double aLine) {
        this.aLine = aLine;
    }

    public double getbLine() {
        return bLine;
    }

    public void setbLine(double bLine) {
        this.bLine = bLine;
    }

    public double getbParabola() {
        return bParabola;
    }

    public void setbParabola(double bParabola) {
        this.bParabola = bParabola;
    }

    public double getaParabola() {
        return aParabola;
    }

    public void setaParabola(double aParabola) {
        this.aParabola = aParabola;
    }

    public double getcParabola() {
        return cParabola;
    }

    public void setcParabola(double cParabola) {
        this.cParabola = cParabola;
    }

    public ArrayList<Point> calculatePointsUsingOrdinaryLeastSquaresByLine(){
        ArrayList<Point> points = new ArrayList<>();
        int n = listX.size();
        double sumOfListX = calculateSum(listX);
        double sumOfListY = calculateSum(listY);
        double sumOfSquaredListX = calculateSum(multiplyItemsFromTheSameLists(2, listX));
        double sumOfMultipliedListXListY = calculateSum(multiplyItemsFromTwoLists(listX, listY));
        Matrix mainMatrix = new Matrix(sumOfListX, n, sumOfSquaredListX, sumOfListX);
        double mainDeterminant = mainMatrix.getDeterminant();
        aLine = calculateCoefficient(mainDeterminant, new Matrix(sumOfListY, n, sumOfMultipliedListXListY, sumOfListX));
        bLine = calculateCoefficient(mainDeterminant, new Matrix(sumOfListX, sumOfListY, sumOfSquaredListX, sumOfMultipliedListXListY));
        for (int x = -500; x <= 500; x += 1){
            points.add(new Point(x, calculateYUsingOrdinaryLeastSquaresByLine(x)));
        }
        return points;
    }

    public ArrayList<Point> calculatePointsUsingOrdinaryLeastSquaresByParabola(){
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
        aParabola = calculateCoefficient(mainDeterminant, matrixA);
        Matrix matrixB = new Matrix(sumOfSquaredListX, sumOfListY, n, sumOfCubedListX, sumOfMultipliedListXListY, sumOfListX, sumOfListXInFourthDegree, sumOfMultipliedSquaredListXListY, sumOfSquaredListX);
        bParabola = calculateCoefficient(mainDeterminant, matrixB);
        Matrix matrixC = new Matrix(sumOfSquaredListX, sumOfListX, sumOfListY, sumOfCubedListX, sumOfSquaredListX, sumOfMultipliedListXListY, sumOfListXInFourthDegree, sumOfCubedListX, sumOfMultipliedSquaredListXListY);
        cParabola = calculateCoefficient(mainDeterminant, matrixC);
        for (int x = -500; x <= 500; x += 1){
            points.add(new Point(x, calculateYUsingOrdinaryLeastSquaresByParabola(x)));
        }
        return points;
    }

    public double calculateYUsingOrdinaryLeastSquaresByLine(double x){
        return aLine * x + bLine;
    }

    public double calculateYUsingOrdinaryLeastSquaresByParabola(double x){
        return aParabola * Math.pow(x, 2) + bParabola * x + cParabola;
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
