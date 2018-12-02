package misztal;

public final class COOValue {
    private final Integer index;
    private final Value value;

    public COOValue(int index, Value value) {
        this.index = index;
        this.value = value;
    }

    public Integer getIndex() {
        return index;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(" + index + "," + value + ")";
    }


}
