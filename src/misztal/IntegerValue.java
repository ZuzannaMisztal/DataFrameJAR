package misztal;

public class IntegerValue extends Value {
    private Integer v;

    public IntegerValue(Integer value) {
        v = value;
    }

    public IntegerValue(String str) {
        v = Integer.parseInt(str);
    }

    public String toString() {
        return v.toString();
    }

    public Value add(Value value) {
        Integer i = v + Integer.parseInt(value.toString());
        return new IntegerValue(i);
    }

    public Value sub(Value value) {
        Integer i = v - Integer.parseInt(value.toString());
        return new IntegerValue(i);
    }

    public Value mul(Value value) {
        Integer i = v * Integer.parseInt(value.toString());
        return new IntegerValue(i);
    }

    public Value div(Value value) {
        if (Integer.parseInt((value.toString())) != 0) {
            Double i = v / Double.parseDouble(value.toString());
            return new DoubleValue(i);
        } else {
            return null;
        }
    }

    public Value pow(Value value) {
        Double d = Math.pow(v, Integer.parseInt(value.toString()));
        Integer i = d.intValue();
        return new IntegerValue(i);
    }

    @Override
    public Value sqrt() {
        return new DoubleValue(Math.sqrt(v));
    }

    public boolean eq(Value value) {
        Integer i = Integer.parseInt(value.toString());
        return v.equals(i);
    }

    @Override
    public boolean lte(Value value) {
        Integer i = Integer.parseInt(value.toString());
        return v < i;
    }

    @Override
    public boolean gte(Value value) {
        Integer i = Integer.parseInt(value.toString());
        return v > i;
    }

    @Override
    public boolean neq(Value value) {
        return !eq(value);
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass().equals(this.getClass())) {
            Integer i = Integer.parseInt(other.toString());
            return v.equals(i);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return v.hashCode();
    }

    @Override
    public IntegerValue create(String value) {
        Integer i = Integer.parseInt(value);
        return new IntegerValue(i);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new IntegerValue(v);
    }
}
