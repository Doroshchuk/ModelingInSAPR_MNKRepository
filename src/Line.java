public class Line {
    private Point startPoint;
    private Point endPoint;
    private TypeOfLine typeOfLine;

    public Line(Point startPoint, Point endPoint, TypeOfLine typeOfLine) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.typeOfLine = typeOfLine;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public TypeOfLine getTypeOfLine() {
        return typeOfLine;
    }

    public void setTypeOfLine(TypeOfLine typeOfLine) {
        this.typeOfLine = typeOfLine;
    }

    @Override
    public boolean equals(Object object) {
        boolean sameSame = false;

        if (object != null && object instanceof Line) {
            sameSame = this.startPoint.getX() == ((Line) object).startPoint.getX() && this.startPoint.getY() == ((Line) object).startPoint.getY() &&
                    this.endPoint.getX() == ((Line) object).endPoint.getX() && this.endPoint.getY() == ((Line) object).endPoint.getY() &&
                    this.typeOfLine == ((Line) object).getTypeOfLine();
        }

        return sameSame;
    }
}
