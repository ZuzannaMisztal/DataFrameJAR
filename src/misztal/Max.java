package misztal;

import java.util.ArrayList;

public class Max implements Applayable {
    @Override
    public DataFrame apply(DataFrame group) {
        ArrayList<String> cNames = new ArrayList<>();
        ArrayList<Class<? extends Value>> tNames = new ArrayList<>();
        for (int i = 0; i < group.getColumnNames().size(); ++i) {
            if (group.getType_names().get(i) != StringValue.class) {
                cNames.add(group.getColumnNames().get(i));
                tNames.add(group.getType_names().get(i));
            }
        }
        DataFrame toReturn = new DataFrame(cNames, tNames);
        for (String name : cNames) {
            toReturn.get(name).add(group.get(name).getMax());
        }
        return toReturn;
    }
}
