public class Point {
    private double x;
    private double y;

    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object object) {
        boolean sameSame = false;

        if (object != null && object instanceof Point) {
            sameSame = this.x == ((Point) object).x && this.y == ((Point) object).y;
        }

        return sameSame;
    }
}
