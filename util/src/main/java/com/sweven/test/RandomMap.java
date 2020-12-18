package com.sweven.test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Sweven
 * @date 2020/12/15 -- 11:43
 * Email: sweventears@163.com
 */
public class RandomMap {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        while (!"exit".equals(line)) {
            int size = 0;
            try {
                size = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.err.println("input error");
                line = scanner.nextLine();
                continue;
            }
            RandomMap aa = new RandomMap(size, size);
            long l = LayoutSimple.getmicTime();
            List<LayoutConfig> configs = null;
            try {
                configs = aa.layout();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.err.println("re-create");
                continue;
            }
            System.out.println("耗时：" + (LayoutSimple.getmicTime() - l) + "mic");
            String[][] map = new String[size][size];
            for (int a = 0; a < map.length; a++) {
                Arrays.fill(map[a], "---");
            }
            for (LayoutConfig config : configs) {
                for (int i = config.getX(); i < config.getX() + config.getXSpec(); i++) {
                    for (int j = config.getY(); j < config.getY() + config.getYSpec(); j++) {
                        map[j][i] = config.getName();
                    }
                }
            }
            for (String[] col : map) {
                for (String v : col) {
                    System.out.print(v + "\t");
                }
                System.out.println();
            }
            line = scanner.nextLine();
        }
    }

    private int width = 4;
    private int height = 4;
    private Set<String> set;

    public RandomMap() {
        set = new HashSet<>();
    }

    public RandomMap(int width, int height) {
        this.width = width;
        this.height = height;
        set = new HashSet<>();
    }

    public List<LayoutConfig> layout() throws Exception {
        Set<LayoutConfig> locates = new HashSet<>();
        int size = size();
        int index = 1;
        while (size < width * height) {
            Rect rect = getLocate(-1, -1);
            LayoutConfig locate =
                    new LayoutConfig(
                            index < 100 ? index < 10 ? "00" + index : "0" + index : "" + index,
                            rect.x, rect.y, rect.xSpec, rect.ySpec);
            locates.add(locate);
            index++;
            size = size();
        }
        return Arrays.asList(locates.toArray(new LayoutConfig[0]));
    }

    private Rect getLocate(int exceptX, int exceptY) throws Exception {
        Rect rect = create(exceptX, exceptY);
        Set<String> map = new HashSet<>();
        for (int i = 0; i < rect.xSpec; i++) {
            for (int j = 0; j < rect.ySpec; j++) {
                map.add((rect.x + i) + "," + (rect.y + j));
            }
        }
        // 查找是否存在重复的单元
        for (String a : map) {
            String[] split = a.split(",", 2);
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);
            // 重复的位置
            if (has(x, y)) {
                return getLocate(x, y);
            }
        }
        set.addAll(map);
        return new Rect(rect.x, rect.y, rect.xSpec, rect.ySpec);
    }

    private Rect create(int exceptX, int exceptY) throws Exception {

        int x = random(0, width - 1, exceptX);
        int y = random(0, height - 1, exceptY);
        if (has(x, y)) {
            return create(x, y);
        }
        int xSpec = random(1, width - x);
        int ySpec = random(1, height - y);
        return new Rect(x, y, xSpec, ySpec);
    }

    private int random(int min, int max, int... except) {
        if (min == max) return min;
        int result = (int) (Math.random() * (max + 1 - min)) + min;
        if (except == null) {
            return result;
        } else {
            boolean b = false;
            for (int i : except) {
                if (i == result) {
                    b = true;
                    break;
                }
            }
            return b ? random(min, max, except) : result;
        }
    }

    private int size() {
        return set.size();
    }

    private boolean has(int x, int y) {
        return set.contains(x + "," + y);
    }

    private boolean add(int x, int y) {
        return set.add(x + "," + y);
    }

    private class Rect {
        int x;
        int y;
        int xSpec;
        int ySpec;

        public Rect(int x, int y, int xSpec, int ySpec) {
            this.x = x;
            this.y = y;
            this.xSpec = xSpec;
            this.ySpec = ySpec;
        }

        public Rect() {

        }
    }
}
