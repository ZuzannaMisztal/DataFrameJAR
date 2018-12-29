package misztal;

//import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;


import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DataFrame {
    protected ArrayList<Column> dataFrame;
    protected ArrayList<String> columnNames;
    protected ArrayList<Class<? extends Value>> type_names;


    public class GroupedDataFrame implements GroupBy {
        protected HashMap<ArrayList<Value>, DataFrame> dataFrameHashMap;
        protected ArrayList<String> columnNamesToGroupBy;

        public GroupedDataFrame(HashMap<ArrayList<Value>, DataFrame> dataFrameHashMap, ArrayList<String> columnNamesToGroupBy) {
            this.dataFrameHashMap = dataFrameHashMap;
            this.columnNamesToGroupBy = columnNamesToGroupBy;
        }

        public HashMap<ArrayList<Value>, misztal.DataFrame> getDataFrameHashMap() {
            return dataFrameHashMap;
        }

        public ArrayList<String> getColumnNamesToGroupBy() {
            return columnNamesToGroupBy;
        }

        public DataFrame apply(Applayable applayable) {
            boolean first = true;
            DataFrame applied = new DataFrame(new ArrayList<>(), new ArrayList<>());
            ArrayList<Class<? extends Value>> tNamesToGroupedBy = new ArrayList<>();

            for (String name : columnNamesToGroupBy) {
                tNamesToGroupedBy.add(get(name).getType());
            }
            DataFrame groupedBy = new DataFrame(columnNamesToGroupBy, tNamesToGroupedBy);

            for (Map.Entry<ArrayList<Value>, DataFrame> dataFrameEntry : dataFrameHashMap.entrySet()) {
                if (first) {
                    applied = applayable.apply(dataFrameEntry.getValue());
                    first = false;
                } else {
                    applied.addDataFrameHorrizontaly(applayable.apply(dataFrameEntry.getValue()));
                }
                groupedBy.addRow(dataFrameEntry.getKey());
            }
            DataFrame toReturn = new DataFrame(new ArrayList<>(), new ArrayList<>());
            for (String name : columnNames) {
                if (groupedBy.getColumnNames().contains(name)) {
                    toReturn.getDataFrame().add(groupedBy.get(name));
                    toReturn.getColumnNames().add(name);
                    toReturn.getType_names().add(groupedBy.get(name).getType());
                } else if (applied.getColumnNames().contains(name)) {
                    toReturn.getDataFrame().add(applied.get(name));
                    toReturn.getColumnNames().add(name);
                    toReturn.getType_names().add(applied.get(name).getType());
                }
            }
            return toReturn;
        }

        public DataFrame applyStandard(Method method) {
            ArrayList<String> columnNamesToReturn = new ArrayList<>();
            ArrayList<Class<? extends Value>> typeNamesToReturn = new ArrayList<>();
            for (String name : columnNames) {
                Class<? extends Value> clazz = type_names.get(columnNames.indexOf(name));
                if (columnNamesToGroupBy.contains(name)) {
                    columnNamesToReturn.add(name);
                    typeNamesToReturn.add(clazz);
                } else {
                    try {
                        if (method.equals(Column.class.getMethod("getMax")) || method.equals(Column.class.getMethod("getMin"))) {
                            if (clazz != StringValue.class) {
                                columnNamesToReturn.add(name);
                                typeNamesToReturn.add(clazz);
                            }
                        } else {
                            if (clazz != StringValue.class && clazz != DataTimeValue.class) {
                                columnNamesToReturn.add(name);
                                typeNamesToReturn.add(clazz);
                            }
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
            DataFrame toReturn = new DataFrame(columnNamesToReturn, typeNamesToReturn);
            for (Map.Entry<ArrayList<Value>, DataFrame> dataFrameEntry : dataFrameHashMap.entrySet()) {
                ArrayList<Value> row = new ArrayList<>();
                int i = 0;
                for (String name : columnNamesToReturn) {
                    if (columnNamesToGroupBy.contains(name)) {
                        row.add(dataFrameEntry.getKey().get(i));
                        ++i;
                    } else {
                        try {
                            row.add((Value) method.invoke(dataFrameEntry.getValue().get(name)));
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
                toReturn.addRow(row);
            }
            return toReturn;
        }


        @Override
        public DataFrame max() {
            try {
                return applyStandard(Column.class.getMethod("getMax"));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public DataFrame min() {
            try {
                return applyStandard(Column.class.getMethod("getMin"));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public DataFrame mean() {
            try {
                return applyStandard(Column.class.getMethod("getMean"));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public DataFrame std() {
            try {
                return applyStandard(Column.class.getMethod("getStd"));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public DataFrame sum() {
            try {
                return applyStandard(Column.class.getMethod("getSum"));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public DataFrame var() {
            try {
                return applyStandard(Column.class.getMethod("getVar"));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public String toString() {
            String toReturn = "";
            for (Map.Entry i : dataFrameHashMap.entrySet()) {
                toReturn = toReturn + "Grouped by: " + columnNamesToGroupBy + " in " + DataFrame.this.columnNames + "\ndataframe.misztal.Value: " + i.getKey() + " dataframe.misztal.DataFrame: " + i.getValue() + "\n\n";
            }
            return toReturn;
        }
    }

    /**
     * Dzieli dataFrame na kilka mniejszych dataFrame. Kluczem są wartości w zadanych kolumnach - wszystkie rzędy o tym samym kluczu trafiają do wspólnego dataFrame.
     * Powstałe dataFrame nie zawierają kolumn z kluczem - ta informacja jest przechowywana jako klucz w hashMapie GropedDataFrame
     *
     * @param columnNames nazwy kolumn będących kluczem podziału
     * @return Obiekt klasy GroupedDataFrame przechowujący w hashmapie każdą z podzielonych dataframe.misztal.DataFrame
     */
    public GroupedDataFrame groupBy(ArrayList<String> columnNames) {
        HashMap<ArrayList<Value>, DataFrame> grouped = new HashMap<>();
        DataFrame columns = get(new ArrayList<String>(columnNames), false);
        ArrayList<String> columnNamesForGrouped = new ArrayList<>();
        ArrayList<Class<? extends Value>> typeNamesForGrouped = new ArrayList<>();
        for (int j = 0; j < this.columnNames.size(); ++j) {
            if (!columnNames.contains(this.columnNames.get(j))) {
                columnNamesForGrouped.add(this.columnNames.get(j));
                typeNamesForGrouped.add(type_names.get(j));
            }
        }
        for (int i = 0; i < dataFrame.get(0).size(); ++i) {
            ArrayList<Value> key = columns.getRow(i);
            if (grouped.containsKey(key)) {
                DataFrame dataFrame = grouped.get(key);
                dataFrame.addRow(this.getRowWithoutSpecifiedColumns(i, columnNames));
            } else {
                DataFrame dataFrame = new DataFrame(columnNamesForGrouped, typeNamesForGrouped);
                dataFrame.addRow(this.getRowWithoutSpecifiedColumns(i, columnNames));
                grouped.put(key, dataFrame);
            }
        }
        return new GroupedDataFrame(grouped, columnNames);
    }


    /**
     * Konstruktor parametryczny klasy. Tworzy odpowiedznią liczbę pustych kolumn o odpowiadających nazwach i typach danych
     *
     * @param type_names_   ArrayList class. Kolejne pola odpowiadają typom danych jakie przechowują kolejne kolumny
     * @param column_names_ ArrayList string. Kolejne pola odpowiadają nazwom kolejnych kolumn
     */
    public DataFrame(ArrayList<String> column_names_, ArrayList<Class<? extends Value>> type_names_) {
        dataFrame = new ArrayList<>();
        type_names = type_names_;
        createColumns(column_names_);
    }

    public void createColumns(ArrayList<String> column_names_) {
        for (int i = 0; i < column_names_.size(); ++i) {
            dataFrame.add(new Column(column_names_.get(i), type_names.get(i)));
        }
        columnNames = column_names_;
    }

    /**
     * Konstruktor parametryczny klasy. Czyta z pliku, jednak nie wypełnia dataFrame a tylko tworzy puste kolumny o zadanych nazwach
     * @param file   plik do odczytu
     * @param types  typy kolumn
     * @param header musi być true, bo to właśnie nazwy kolumn chcemy odczytać
     */
    public DataFrame(ArrayList<Class<? extends Value>> types, String file, boolean header) {
        if (!header) {
            throw new IllegalArgumentException();
        }
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        type_names = types;
        dataFrame = new ArrayList<>();
        try {
            createColumns(new ArrayList<>(Arrays.asList(br.readLine().split(","))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Konstrunktor klasy. Pobiera dane z pliku file
     * @param header musi być true - oznacza to, że w pliku pierwsza linia zawiera nazwy kolumn. Jeżeli jest false wymagany jest argument String[] columnNamesToGroupBy
     * @param file   plik z którego zostaną pobrane dane
     * @param types  nazwy typów kolumn
     * @throws IOException
     */
    public DataFrame(String file, ArrayList<Class<? extends Value>> types, Boolean header) throws IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (!header) {
            throw new IllegalArgumentException();
        }

        FileInputStream fstream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        type_names = types;
        dataFrame = new ArrayList<>();
        createColumns(new ArrayList<>(Arrays.asList(br.readLine().split(","))));

        String strLine;
        while ((strLine = br.readLine()) != null) {
            String[] str = strLine.split(",");
            ArrayList<Value> row = new ArrayList<>();
            for (int i = 0; i < str.length; ++i) {
                row.add(types.get(i).getConstructor(String.class).newInstance(str[i]));
            }
            this.addRow(row);
        }
        br.close();
    }

    /**
     * Konkstruktor klasy. Pobiera dane z pliku file
     * @param file
     * @param types
     * @param header        jeśli true - argument columnNamesToGroupBy jest ignorowany i nazwy kolumn są pobierane z pliku
     * @param column_names_
     * @throws IOException
     */
    public DataFrame(String file, ArrayList<Class<? extends Value>> types, Boolean header, ArrayList<String> column_names_) throws IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        FileInputStream fstream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        type_names = types;
        dataFrame = new ArrayList<>();

        if (!header) {
            createColumns(column_names_);
        } else {
            createColumns(new ArrayList<>(Arrays.asList(br.readLine().split(","))));
        }

        String strLine;
        while ((strLine = br.readLine()) != null) {
            String[] str = strLine.split(",");
            ArrayList<Value> row = new ArrayList<>();
            for (int i = 0; i < str.length; ++i) {
                row.add(types.get(i).getConstructor(String.class).newInstance(str[i]));
            }
            this.addRow(row);
        }
        br.close();
    }

    public DataFrame(File file) throws IOException {
        FileInputStream fstream= null;
        try {
            fstream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        dataFrame=new ArrayList<>();
        ArrayList<String> firstLineOfFile=null;
        try {
            firstLineOfFile = new ArrayList<>(Arrays.asList(br.readLine().split(",")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean secondLine = true;
        String strLine;
        while ((strLine=br.readLine()) != null) {
            String[] str = strLine.split(",");
            if (secondLine){
                type_names=findTypes(str);
                if (firstLineOfFile!=null) {
                    createColumns(firstLineOfFile);
                }
                secondLine=false;
            }
            ArrayList<Value> row = new ArrayList<>();
            for (int i=0; i<str.length; ++i) {
                try {
                    row.add(type_names.get(i).getConstructor(String.class).newInstance(str[i]));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            this.addRow(row);
        }
        br.close();
    }

    public DataFrame(File file, ArrayList<String> columnNames) throws IOException {
        FileInputStream fstream= null;
        try {
            fstream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        dataFrame=new ArrayList<>();

        boolean secondLine = true;
        String strLine;
        while ((strLine=br.readLine()) != null) {
            String[] str = strLine.split(",");
            if (secondLine){
                type_names=findTypes(str);
                createColumns(columnNames);
                secondLine=false;
            }
            ArrayList<Value> row = new ArrayList<>();
            for (int i=0; i<str.length; ++i) {
                try {
                    row.add(type_names.get(i).getConstructor(String.class).newInstance(str[i]));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            this.addRow(row);
        }
        br.close();
    }


    public ArrayList<Class<? extends Value>> findTypes(String[] secondLineOfFile){
        ArrayList<Class<? extends Value>> types= new ArrayList<>();
        for (String toValue:Arrays.asList(secondLineOfFile)){
            if (toValue.matches("\\d{4}-\\d{2}-\\d{2}")){
                types.add(DataTimeValue.class);
            } else if (toValue.matches("-?\\d*")){
                types.add(IntegerValue.class);
            } else if (toValue.matches("-?\\d+\\.\\d+")){
                types.add(DoubleValue.class);
            } else {
                types.add(StringValue.class);
            }
        }
        return types;
    }

    public void setColumnNames(ArrayList<String> columnNames) {
        this.columnNames = columnNames;
    }

    public void setType_names(ArrayList<Class<? extends Value>> type_names) {
        this.type_names = type_names;
    }

    /**
     * Dodaje rząd r do isniejącej dataFrame. Wewnątrz funkcji sprawdzana jest poprawność typów.
     * Jeśli typ jest niepoprawny wyrzuca wyjątek IllegalArgumentException
     *
     * @param r
     */
    public void addRow(ArrayList<Value> r) {
        int i = 0;
        for (Value value : r) {
            dataFrame.get(i).add(value);
            ++i;
        }
    }

    public ArrayList<Value> getRow(int i) {
        ArrayList<Value> row = new ArrayList<>();
        for (Column column : dataFrame) {
            row.add(column.getActualColumn().get(i));
        }
        return row;
    }

    public ArrayList<Value> getRowWithoutSpecifiedColumns(int rowIndex, ArrayList<String> columnNames) {
        ArrayList<Value> row = new ArrayList<>();
        for (Column column : dataFrame) {
            if (!columnNames.contains(column.getName())) {
                row.add(column.getActualColumn().get(rowIndex));
            }
        }
        return row;
    }

    /**
     * @return ilość komórek dataFrame
     */
    public int size() {
        return numberOfRows() * dataFrame.size();
    }

    /**
     * Wśród dataframe.misztal.Column przechowywanych w dataFrame znajduje tę, która przechowuje nazwę zadaną w parametrze i zwraca ją.
     * Jeśli nie ma takiej kolumny zwraca null
     *
     * @param colname
     * @return objekt klasy dataframe.misztal.Column
     */
    public Column get(String colname) {
        for (Column column : dataFrame) {
            if (column.getName().equals(colname)) {
                return column;
            }
        }
        return null;
    }

    public ArrayList<String> getColumnNames() {
        return columnNames;
    }

    public ArrayList<Class<? extends Value>> getType_names() {
        return type_names;
    }

    public ArrayList<Column> getDataFrame() {
        return dataFrame;
    }

    /**
     * Tworzy nową dataFrame złożoną z wierszy starej dataFrame o zadanym zakresie indeksów. Nazwy kolumn i typy danych w kolumnach zostają zachowane
     *
     * @param from
     * @param to
     * @return dataFrame
     */
    public DataFrame iloc(int from, int to) {
        DataFrame ramka = new DataFrame(columnNames, type_names);
        int j = 0;
        for (Column column : ramka.dataFrame) {
            for (int k = from; k < to; ++k) {
                column.add(this.dataFrame.get(j).getElement(k));
            }
            ++j;
        }
        return ramka;
    }


    /**
     * zwraca wiersz o zadanym indeksie w postaci dataFrame
     *
     * @param i
     * @return dataFrame
     */
    public DataFrame iloc(int i) {
        return iloc(i, i + 1);
    }

    public DataFrame get(ArrayList<String> cols, boolean copy) {
        DataFrame ramka;
        if (copy) { //deep one
            ArrayList<Class<? extends Value>> types = new ArrayList<>();
            ArrayList<Column> frame = new ArrayList<>();
            for (int b = 0; b < cols.size(); ++b) {
                for (int c = 0; c < columnNames.size(); ++c) {
                    if (cols.get(b).equals(columnNames.get(c))) {
                        types.add(b, type_names.get(c));
                        frame.add(dataFrame.get(c));
                    }
                }
            }
            ramka = new DataFrame(cols, types);
            ramka.dataFrame = frame;
        } else { //shallow one
            ramka = this.get(cols, true).iloc(0, dataFrame.get(0).size());
        }
        return ramka;
    }

    public DataFrame addDataFrameHorrizontaly(DataFrame other) {
        if (this.columnNames.equals(other.getColumnNames()) && this.type_names.equals(other.getType_names())) {
            for (int i = 0; i < other.getDataFrame().get(0).size(); ++i) {
                this.addRow(other.getRow(i));
            }
            return this;
        }
        return null;
    }

    @Override
    public String toString() {
        return dataFrame +
                ", type_names=" + type_names
                + '}';
    }

    public int numberOfRows(){
        return dataFrame.get(0).size();
    }
}
