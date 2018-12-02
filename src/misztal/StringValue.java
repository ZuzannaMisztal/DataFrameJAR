package misztal;

public class StringValue extends Value {
    private String value;

    public StringValue(String str) {
        value = str;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public Value add(Value value) {
        return new StringValue(this.value.concat(value.toString()));
    }

    @Override
    public Value sub(Value value) {
        return new StringValue(this.value.replaceAll(value.toString(), ""));
    }

    @Override
    public Value mul(Value value) {
        return null;
    }

    @Override
    public Value div(Value value) {
        return null;
    }

    @Override
    public Value pow(Value value) {
        return null;
    }

    @Override
    public Value sqrt() {
        return null;
    }

    @Override
    public boolean eq(Value value) {
        return this.value.equals(value.toString());
    }

    @Override
    public boolean lte(Value value) {
        return false;
    }

    @Override
    public boolean gte(Value value) {
        return false;
    }

    @Override
    public boolean neq(Value value) {
        return !eq(value);
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass().equals(this.getClass())) {
            return value.equals(other.toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public Value create(String value) {
        return new StringValue(value);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new StringValue(value);
    }
}
