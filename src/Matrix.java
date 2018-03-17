public class Matrix {
    private double[][] matrix;
    private double determinant;

    public Matrix(double[][] matrix){
        this.matrix = matrix;
    }

    public Matrix(double item1, double item2, double item3, double item4){
        matrix = new double[2][2];
        matrix[0][0] = item1;
        matrix[0][1] = item2;
        matrix[1][0] = item3;
        matrix[1][1] = item4;
    }

    public Matrix(double item1, double item2, double item3, double item4, double item5, double item6, double item7, double item8, double item9){
        matrix = new double[3][3];
        matrix[0][0] = item1;
        matrix[0][1] = item2;
        matrix[0][2] = item3;
        matrix[1][0] = item4;
        matrix[1][1] = item5;
        matrix[1][2] = item6;
        matrix[2][0] = item7;
        matrix[2][1] = item8;
        matrix[2][2] = item9;
    }

    public double getDeterminant(){
        return calculateDeterminant(matrix);
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    private double calculateDeterminant(double[][] matrix) {
        double calcResult = 0.0;
        if (matrix.length == 2) {
            calcResult = matrix[0][0] * matrix[1][1] - matrix[1][0] * matrix[0][1];
        } else {
            int coeff = 1;
            for (int i = 0; i < matrix.length; i++) {
                if (i % 2 == 1) {
                    coeff = -1;
                } else {
                    coeff = 1;
                }
                //собственно разложение:
                calcResult += coeff * matrix[0][i] * this.calculateDeterminant(this.getMinor(matrix, 0, i));
            }
        }
        //возвращаем ответ
        return calcResult;
    }

    //функция, к-я возвращает нужный нам минор. На входе - определитель, из к-го надо достать минор и номера строк-столбцов, к-е надо вычеркнуть.
    private double[][] getMinor(double[][] matrix, int row, int column) {
        int minorLength = matrix.length - 1;
        double[][] minor = new double[minorLength][minorLength];
        int dI = 0;//эти переменные для того, чтобы "пропускать" ненужные строку и столбец
        int dJ = 0;
        for (int i = 0; i <= minorLength; i++) {
            dJ = 0;
            for (int j = 0; j <= minorLength; j++) {
                if (i == row) {
                    dI = 1;
                } else {
                    if (j == column) {
                        dJ = 1;
                    } else {
                        minor[i - dI][j - dJ] = matrix[i][j];
                    }
                }
            }
        }
        return minor;
    }
}
