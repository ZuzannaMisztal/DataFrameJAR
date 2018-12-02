package misztal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataTimeValue extends Value {
    private Date v;

    public DataTimeValue(Date data) {
        this.v = data;
    }

    public DataTimeValue(String str) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            v = formatter.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(v);
    }

    @Override
    public Value add(Value value) {
        return null;
    }

    @Override
    public Value sub(Value value) {
        return null;
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
        if (value instanceof DataTimeValue) {
            return (v.compareTo(new DataTimeValue(value.toString()).v) == 0);
        }
        return false;
    }

    @Override
    public boolean lte(Value value) {
        if (value instanceof DataTimeValue) {
            return v.compareTo(new DataTimeValue(value.toString()).v) < 0;
        }
        return false;
    }

    @Override
    public boolean gte(Value value) {
        if (value instanceof DataTimeValue) {
            return v.compareTo(new DataTimeValue(value.toString()).v) > 0;
        }
        return false;
    }

    @Override
    public boolean neq(Value value) {
        return !eq(value);
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass().equals(this.getClass())) {
            return eq(Value.class.cast(other));
        }
        return false;
    }

    public Date getV() {
        return v;
    }

    @Override
    public int hashCode() {
        return v.hashCode();
    }

    @Override
    public Value create(String value) {
        return new DataTimeValue(value);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new DataTimeValue(v);
    }
}
