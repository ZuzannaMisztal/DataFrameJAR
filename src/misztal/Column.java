package misztal;

import java.util.ArrayList;

public class Column {
    protected String name;
    protected Class<? extends Value> type;
    protected ArrayList<Value> actual_column;

    @Override
    public String toString() {
        return "dataframe.misztal.Column \"" + name + "\"" + actual_column;
    }

    public Column(String n, Class<? extends Value> t) {
        name = n;
        type = t;
        actual_column = new ArrayList<>();
    }

    /**
     * Dodaje zadany element do kolumny. Sprawdza czy element jest danego typu, jeżeli nie jest wyrzuca wyjątek
     *
     * @param value
     */
    public void add(Value value) {
        if (type.isInstance(value)) {
            actual_column.add(value);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @return rozmiar kolumny
     */
    public int size() {
        return actual_column.size();
    }

    /**
     * @return nazwa kolumny
     */
    public String getName() {
        return name;
    }

    /**
     * @return typ danych w kolumnie
     */
    public Class<? extends Value> getType() {
        return type;
    }

    /**
     * @return zawartość kolumny
     */
    public ArrayList<Value> getActual_column() {
        return actual_column;
    }

    /**
     * @param i index elementu do pobrania
     * @return element o indeksie i
     */
    public Value getElement(int i) {
        return actual_column.get(i);
    }

    public Value getMax() {
        Value toReturn = actual_column.get(0);
        for (Value value : actual_column) {
            if (value.gte(toReturn)) {
                toReturn = value;
            }
        }
        return toReturn;
    }

    public Value getMin() {
        Value toReturn = actual_column.get(0);
        for (Value value : actual_column) {
            if (value.lte(toReturn)) {
                toReturn = value;
            }
        }
        return toReturn;
    }

    public Value getMean() {
        return getSum().div(new IntegerValue(actual_column.size()));
    }

    public Value getVar() {
        Value mean = getMean();
        Value toReturn = actual_column.get(0).sub(mean).pow(new IntegerValue(2));
        for (int i = 1; i < actual_column.size(); ++i) {
            toReturn = toReturn.add(actual_column.get(i).sub(mean).pow(new IntegerValue(2)));
        }
        toReturn = toReturn.div(new IntegerValue(actual_column.size()));
        return toReturn;
    }

    public Value getStd() {
        Value toReturn = getVar();
        return toReturn.sqrt();
    }

    public Value getSum() {
        Value toReturn = actual_column.get(0);
        for (int i = 1; i < actual_column.size(); ++i) {
            toReturn = toReturn.add(actual_column.get(i));
        }
        return toReturn;
    }

    public Column addValueToColumn(Value value) {
        for (int i = 0; i < actual_column.size(); ++i) {
            actual_column.set(i, actual_column.get(i).add(value));
        }
        return this;
    }

    public Column multiplyColumnByValue(Value value) {
        for (int i = 0; i < actual_column.size(); ++i) {
            actual_column.set(i, actual_column.get(i).mul(value));
        }
        return this;
    }

    public Column subtractValueFromColumn(Value value) {
        for (int i = 0; i < actual_column.size(); ++i) {
            actual_column.set(i, actual_column.get(i).sub(value));
        }
        return this;
    }

    public Column divideColumnByValue(Value value) {
        for (int i = 0; i < actual_column.size(); ++i) {
            actual_column.set(i, actual_column.get(i).div(value));
        }
        return this;
    }
}