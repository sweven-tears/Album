package com.sweven.sqlite.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Rows {
    private List<Map<String, String>> maps;

    public Rows(List<Map<String, String>> maps) {
        this.maps = maps;
    }

    public int size() {
        return maps.size();
    }

    public Map<String, String> get(int position) {
        Map<String, String> map = new HashMap<>();
        try {
            map = maps.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public String getString(int position, String name) {
        return maps.get(position).get(name);
    }

    public Short getShort(int position, String name) {
        return Short.parseShort(get(position).get(name));
    }

    public Long getLong(int position, String name) {
        return Long.parseLong(Objects.requireNonNull(get(position).get(name)));
    }

    public Integer getInt(int position, String name) {
        return Integer.parseInt(Objects.requireNonNull(get(position).get(name)));
    }

    public Float getFloat(int position, String name) {
        return Float.parseFloat(Objects.requireNonNull(get(position).get(name)));
    }

    public Double getDouble(int position, String name) {
        return Double.parseDouble(Objects.requireNonNull(get(position).get(name)));
    }

    public byte[] getBlob(int position, String name) {
        String value = get(position).get(name);
        byte[] bytes = new byte[0];
        try {
            assert value != null;
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
