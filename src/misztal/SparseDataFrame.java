package misztal;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class SparseDataFrame extends DataFrame {
    private ArrayList<SparseColumn> sparseDataFrame;

    public SparseDataFrame(ArrayList<String> column_names_, ArrayList<Class<? extends Value>> type_names_, ArrayList<SparseColumn> sparseDataFrame) {
        super(column_names_, type_names_);
        this.sparseDataFrame = sparseDataFrame;
    }

    public SparseDataFrame(DataFrame dataFrame, Value hide) {
        super(dataFrame.columnNames, dataFrame.type_names);
        sparseDataFrame = new ArrayList<>();
        for (Column column : dataFrame.dataFrame) {
            SparseColumn sparseColumn = new SparseColumn(column, hide);
            sparseDataFrame.add(sparseColumn);
        }
    }

    public SparseDataFrame(ArrayList<String> column_names_, ArrayList<Class<? extends Value>> type_names_, Value hide) {
        super(column_names_, type_names_);
        sparseDataFrame = new ArrayList<>();
        for (int i = 0; i < column_names_.size(); ++i) {
            sparseDataFrame.add(new SparseColumn(column_names_.get(i), type_names_.get(i), hide));
        }
    }


    /**
     * konwertuje sparseDataFrame na dataframe.misztal.DataFrame
     *
     * @return dataframe.misztal.DataFrame
     */
    public DataFrame toDanse() {
        DataFrame ramka = new DataFrame(columnNames, type_names);
        int i = 0;
        for (SparseColumn sparseColumn : sparseDataFrame) {
            sparseColumn.toDanse(ramka.dataFrame.get(i));
            ++i;
        }
        return ramka;
    }

    public SparseDataFrame(String file, ArrayList<Class<? extends Value>> types, boolean header, Value hiden) throws IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        super(types, file, true);

        if (!header) {
            throw new IllegalArgumentException();
        }

        FileInputStream fstream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        sparseDataFrame = new ArrayList<>();
        for (int i = 0; i < columnNames.size(); ++i) {
            sparseDataFrame.add(new SparseColumn(columnNames.get(i), type_names.get(i), hiden));
        }

        String strline;
        br.readLine();
        while ((strline = br.readLine()) != null) {
            ArrayList<Value> row = new ArrayList<>();
            String[] str = strline.split(",");
            for (int i = 0; i < str.length; ++i) {
                row.add(types.get(i).getConstructor(String.class).newInstance(str[i]));
            }
            addRow(row);
        }
        br.close();
    }

    public void addRow(ArrayList<Value> r) {
        int i = 0;
        for (Value value : r) {
            sparseDataFrame.get(i).addValue(value);
            ++i;
        }
    }

    public int size() {
        return sparseDataFrame.get(0).getSize() * sparseDataFrame.size();
    }


    public SparseColumn get(String colname) {
        for (SparseColumn column : sparseDataFrame) {
            if (column.getName().equals(colname)) {
                return column;
            }
        }
        return null;
    }

    /**
     * @param from
     * @param to
     * @return
     */
    public SparseDataFrame iloc(int from, int to) {
        SparseDataFrame ramka = new SparseDataFrame(columnNames, type_names, sparseDataFrame.get(0).getHiden());
        int i = 0; //iterator "wszerz" po kolumnach nowej dataFrame
        for (SparseColumn sparseColumn : this.sparseDataFrame) {
            int j = 0; //iterator "w dół" po wartościach w kolumnach starej dataFrame
            while (j < sparseColumn.getSparsedColumn().size() && sparseColumn.getSparsedColumn().get(j).getIndex() < from) {
                ++j;
            }
            while (j < sparseColumn.getSparsedColumn().size() && sparseColumn.getSparsedColumn().get(j).getIndex() < to) {
                ramka.sparseDataFrame.get(i).add(new COOValue(sparseColumn.getSparsedColumn().get(j).getIndex() - from, sparseColumn.getSparsedColumn().get(j).getValue()));
                ++j;
            }
            ++i;
        }
        return ramka;
    }

    public SparseDataFrame iloc(int i) {
        return iloc(i, i + 1);
    }


    @Override
    public String toString() {
        return "dataframe.misztal.SparseDataFrame=" + sparseDataFrame + ", columnNamesToGroupBy=" + columnNames + ", typeNames="/*+Arrays.toString(type_names)*/;
    }


}
