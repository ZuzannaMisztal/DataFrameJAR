package misztal;

import java.lang.reflect.InvocationTargetException;

public interface GroupBy {
    DataFrame max() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    DataFrame min() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    DataFrame mean() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    DataFrame std() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    DataFrame sum() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    DataFrame var() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}
