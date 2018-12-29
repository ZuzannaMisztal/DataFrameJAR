package misztal;

import java.util.ArrayList;

public class Column {
    protected String name;
    protected Class<? extends Value> type;
    protected ArrayList<Value> actualColumn;

    @Override
    public String toString() {
        return "dataframe.misztal.Column \"" + name + "\"" + actualColumn;
    }

    public Column(String n, Class<? extends Value> t) {
        name = n;
        type = t;
        actualColumn = new ArrayList<>();
    }

    public ArrayList<Value> distinct(){
        ArrayList<Value> toReturn=new ArrayList<>();
        for (Value value : actualColumn){
            if (!toReturn.contains(value)){
                toReturn.add(value);
            }
        }
        return toReturn;
    }

    /**
     * Dodaje zadany element do kolumny. Sprawdza czy element jest danego typu, jeżeli nie jest wyrzuca wyjątek
     * @param value
     */
    public void add(Value value) {
        if (type.isInstance(value)) {
            actualColumn.add(value);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @return rozmiar kolumny
     */
    public int size() {
        return actualColumn.size();
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
    public ArrayList<Value> getActualColumn() {
        return actualColumn;
    }

    /**
     * @param i index elementu do pobrania
     * @return element o indeksie i
     */
    public Value getElement(int i) {
        return actualColumn.get(i);
    }

    public Value getMax() {
        Value toReturn = actualColumn.get(0);
        for (Value value : actualColumn) {
            if (value.gte(toReturn)) {
                toReturn = value;
            }
        }
        return toReturn;
    }

    public Value getMin() {
        Value toReturn = actualColumn.get(0);
        for (Value value : actualColumn) {
            if (value.lte(toReturn)) {
                toReturn = value;
            }
        }
        return toReturn;
    }

    public Value getMean() {
        return getSum().div(new IntegerValue(actualColumn.size()));
    }

    public Value getVar() {
        Value mean = getMean();
        Value toReturn = actualColumn.get(0).sub(mean).pow(new IntegerValue(2));
        for (int i = 1; i < actualColumn.size(); ++i) {
            toReturn = toReturn.add(actualColumn.get(i).sub(mean).pow(new IntegerValue(2)));
        }
        toReturn = toReturn.div(new IntegerValue(actualColumn.size()));
        return toReturn;
    }

    public Value getStd() {
        Value toReturn = getVar();
        return toReturn.sqrt();
    }

    public Value getSum() {
        Value toReturn = actualColumn.get(0);
        for (int i = 1; i < actualColumn.size(); ++i) {
            toReturn = toReturn.add(actualColumn.get(i));
        }
        return toReturn;
    }

    public Column addValueToColumn(Value value) {
        for (int i = 0; i < actualColumn.size(); ++i) {
            actualColumn.set(i, actualColumn.get(i).add(value));
        }
        return this;
    }

    public Column multiplyColumnByValue(Value value) {
        for (int i = 0; i < actualColumn.size(); ++i) {
            actualColumn.set(i, actualColumn.get(i).mul(value));
        }
        return this;
    }

    public Column subtractValueFromColumn(Value value) {
        for (int i = 0; i < actualColumn.size(); ++i) {
            actualColumn.set(i, actualColumn.get(i).sub(value));
        }
        return this;
    }

    public Column divideColumnByValue(Value value) {
        for (int i = 0; i < actualColumn.size(); ++i) {
            actualColumn.set(i, actualColumn.get(i).div(value));
        }
        return this;
    }
}