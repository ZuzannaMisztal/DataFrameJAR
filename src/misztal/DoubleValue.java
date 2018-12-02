package misztal;

public class DoubleValue extends Value {
    private Double value;


    public DoubleValue(Double value) {
        this.value = value;
    }

    public DoubleValue(String str) {
        value = Double.parseDouble(str);
    }


    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public Value add(Value value) {
        return new DoubleValue(this.value + Double.parseDouble(value.toString()));
    }

    @Override
    public Value sub(Value value) {
        return new DoubleValue(this.value - Double.parseDouble(value.toString()));
    }

    @Override
    public Value mul(Value value) {
        return new DoubleValue(this.value * Double.parseDouble(value.toString()));
    }

    @Override
    public Value div(Value value) {
        if (Double.parseDouble(value.toString()) != 0) {
            return new DoubleValue(this.value / Double.parseDouble(value.toString()));
        }
        return null;
    }

    @Override
    public Value pow(Value value) {
        return new DoubleValue(Math.pow(this.value, Double.parseDouble(value.toString())));
    }

    @Override
    public Value sqrt() {
        return new DoubleValue(Math.sqrt(value));
    }

    @Override
    public boolean eq(Value value) {
        return this.value.equals(Double.parseDouble(value.toString()));
    }

    @Override
    public boolean lte(Value value) {
        return this.value < Double.parseDouble(value.toString());
    }

    @Override
    public boolean gte(Value value) {
        return this.value > Double.parseDouble(value.toString());
    }

    @Override
    public boolean neq(Value value) {
        return !eq(value);
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass().equals(this.getClass())) {
            return value.equals(Double.parseDouble(other.toString()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public DoubleValue create(String value) {
        return new DoubleValue(Double.parseDouble(value));
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new DoubleValue(value);
    }
}

