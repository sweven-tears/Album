package com.sweven.sqlite.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rows {
    private List<Map<String, Object>> maps;

    public Rows(List<Map<String, Object>> maps) {
        this.maps = maps;
    }

    public int size() {
        return maps == null ? 0 : maps.size();
    }

    public Map<String, Object> get(int position) {
        Map<String, Object> map = new HashMap<>();
        try {
            map = maps.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    private Map<String, Object> currentRow;
    private int currentIndex = -1;

    //------------------------map while遍历方式----------------------//

    public boolean next() {
        if (size() - currentIndex > 1) {
            currentRow = get(currentIndex);
            currentIndex++;
            return true;
        }
        return false;
    }

    public String getString(String name) {
        return String.valueOf(currentRow.get(name));
    }

    public Short getShort(String name) {
        return (Short) currentRow.get(name);
    }

    public Long getLong(String name) {
        return (Long) currentRow.get(name);
    }

    public Integer getInt(String name) {
        return (Integer) currentRow.get(name);
    }

    public Float getFloat(String name) {
        return (Float) currentRow.get(name);
    }

    public Double getDouble(String name) {
        return (Double) currentRow.get(name);
    }

    public byte[] getBlob(String name) {
        String value = String.valueOf(currentRow.get(name));
        byte[] bytes = new byte[0];
        try {
            bytes = new byte[value.length()];
            for (int i = 0; i < value.length(); i++) {
                bytes[i] = (byte) value.charAt(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    //----------------list for遍历获取-----------------//

    public String getString(int position, String name) {
        return String.valueOf(get(position).get(name));
    }

    public Short getShort(int position, String name) {
        return Short.valueOf(getString(position, name));
    }

    public Long getLong(int position, String name) {
        return Long.getLong(getString(position, name));
    }

    public Integer getInt(int position, String name) {
        return Integer.valueOf(getString(position, name));
    }

    public Float getFloat(int position, String name) {
        return Float.parseFloat(getString(position, name));
    }

    public Double getDouble(int position, String name) {
        return Double.parseDouble(getString(position, name));
    }

    public byte[] getBlob(int position, String name) {
        String value = getString(position, name);
        byte[] bytes = new byte[0];
        try {
            bytes = new byte[value.length()];
            for (int i = 0; i < value.length(); i++) {
                bytes[i] = (byte) value.charAt(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
