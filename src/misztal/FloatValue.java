package misztal;

public class FloatValue extends Value {
    private Float v;

    public FloatValue(Float v) {
        this.v = v;
    }

    public FloatValue(String str) {
        v = Float.parseFloat(str);
    }

    @Override
    public String toString() {
        return v.toString();
    }

    @Override
    public Value add(Value value) {
        Float i = Float.parseFloat(value.toString()) + v;
        return new FloatValue(i);
    }

    @Override
    public Value sub(Value value) {
        Float i = v - Float.parseFloat(value.toString());
        return new FloatValue(i);
    }

    @Override
    public Value mul(Value value) {
        Float i = v * Float.parseFloat(value.toString());
        return new FloatValue(i);
    }

    @Override
    public Value div(Value value) {
        Float i = v / Float.parseFloat(value.toString());
        return new FloatValue(i);
    }

    @Override
    public Value pow(Value value) {
        double x = v;
        Double d = Math.pow(x, Double.parseDouble(value.toString()));
        Float f = d.floatValue();
        return new FloatValue(f);
    }

    @Override
    public Value sqrt() {
        return new DoubleValue(Math.sqrt(v));
    }

    @Override
    public boolean eq(Value value) {
        return v.equals(Float.parseFloat(value.toString()));
    }

    @Override
    public boolean lte(Value value) {
        return v < Float.parseFloat(value.toString());
    }

    @Override
    public boolean gte(Value value) {
        return v > Float.parseFloat(value.toString());
    }

    @Override
    public boolean neq(Value value) {
        return !eq(value);
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass().equals(this.getClass())) {
            Float i = Float.parseFloat(other.toString());
            return v.equals(i);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return v.hashCode();
    }

    @Override
    public FloatValue create(String value) {
        return new FloatValue(Float.parseFloat(value));
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new FloatValue(v);
    }
}
