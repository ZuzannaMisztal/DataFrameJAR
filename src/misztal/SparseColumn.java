package misztal;

import java.util.ArrayList;

public class SparseColumn extends Column {
    private ArrayList<COOValue> sparsedColumn;
    private Value hiden;
    private int size;


    public SparseColumn(Column column, Value hide) {
        super(column.getName(), column.getType());
        hiden = hide;
        sparsedColumn = new ArrayList<>();
        int i = 0;
        for (Value value : column.getActualColumn()) {
            if (!(value.equals(hiden))) {
                COOValue columnValue = new COOValue(i, value);
                sparsedColumn.add(columnValue);
            }
            ++i;
        }
        this.size = column.size();
    }

    public SparseColumn(String n, Class<? extends Value> t, Value hiden) {
        super(n, t);
        this.hiden = hiden;
        size = 0;
        sparsedColumn = new ArrayList<>();
    }

    public Column toDanse(Column column) {
        int j = 0;
        Integer i = 0;
        for (i = 0; i < size; ++i) {
            if (j < sparsedColumn.size() && i.equals(sparsedColumn.get(j).getIndex())) {
                column.actualColumn.add(sparsedColumn.get(j).getValue());
                ++j;
            } else {
                column.actualColumn.add(hiden);
            }
        }
        return column;
    }


    public int getSize() {
        return size;
    }

    public Value getHiden() {
        return hiden;
    }

    public ArrayList<COOValue> getSparsedColumn() {
        return sparsedColumn;
    }


    /**
     * @param cooValue
     */
    public void add(COOValue cooValue) {
        sparsedColumn.add(cooValue);
    }

    public void addValue(Value value) {
        if (!value.equals(hiden)) {
            COOValue cooValue = new COOValue(size, value);
            sparsedColumn.add(cooValue);
        }
        size += 1;
    }

    @Override
    public String toString() {
        return "SCol" + sparsedColumn;
    }
}
