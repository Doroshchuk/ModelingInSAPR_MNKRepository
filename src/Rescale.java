public class Rescale {
    private final double minNewRange, maxNewRange, minOldRange, maxOldRange;

    public Rescale(double minNewRange, double maxNewRange, double minOldRange, double maxOldRange) {
        this.minNewRange = minNewRange;
        this.maxNewRange = maxNewRange;
        this.minOldRange = minOldRange;
        this.maxOldRange = maxOldRange;
    }

    public double getMinNewRange() {
        return minNewRange;
    }

    public double getMaxNewRange() {
        return maxNewRange;
    }

    public double getMinOldRange() {
        return minOldRange;
    }

    public double getMaxOldRange() {
        return maxOldRange;
    }

        private double interpolate(double x) {
        return minOldRange * (1 - x) + maxOldRange * x;
    }

    private double uninterpolate(double x) {
        double b = (maxNewRange - minNewRange) != 0 ? maxNewRange - minNewRange : 1 / maxNewRange;
        return (x - minNewRange) / b;
    }

    public double rescale(double x) {
        return interpolate(uninterpolate(x));
    }
}
