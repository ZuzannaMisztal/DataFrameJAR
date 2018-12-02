package misztal;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] argv) throws IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        ArrayList<Class<? extends Value>> types = new ArrayList<>();
        types.add(StringValue.class);
        types.add(DataTimeValue.class);
        types.add(DoubleValue.class);
        types.add(DoubleValue.class);
//        String[] colnames= new String[] {"x","y", "z"};
        DataFrame ramka = new DataFrame("C:\\Users\\zuzan\\Downloads\\groupby.csv", types, true);


        String[] names = {"id"};

        System.out.println(ramka.iloc(0, 4));
        System.out.println(ramka.iloc(0, 4).get("total").multiplyColumnByValue(new IntegerValue(2)));

    }

}
