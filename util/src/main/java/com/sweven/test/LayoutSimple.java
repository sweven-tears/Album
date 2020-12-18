package com.sweven.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by Sweven on 2020/10/21.
 * Email:sweventears@Foxmail.com
 */
public class LayoutSimple {
    private int width = 4;
    private int height = 4;
    private boolean[][] map = new boolean[width][height];
    private Set<String> set = new HashSet<>();
    public static List<String> resources;
    public static Map<String, String> mapping = new HashMap<>();

    public static void init() {
        mapping.clear();
        resources = new ArrayList<>();
        resources.add("石头");
        resources.add("燧石");
        resources.add("空地");
        resources.add("杂草");
        resources.add("水稻");
        resources.add("野花");
        resources.add("果实");
        resources.add("松树");
        resources.add("柏杨");
        resources.add("石桌");
        resources.add("火苗");
        resources.add("矿石");
        resources.add("铁矿");
        resources.add("煤块");
        resources.add("岩浆");
        resources.add("脚印");
        resources.add("水洼");
        resources.add("泥沼");
        resources.add("树枝");
        resources.add("杂草");
        resources.add("小麦");
        resources.add("石凳");
        resources.add("野鸡");
        resources.add("山贼");
        resources.add("树桩");
    }

    public static String get(String index) {
        if (mapping.containsKey(index)) {
            return mapping.get(index);
        }
        int size = resources.size();
        if (size == 0) {
            return "开发";
        }
        int i = (int) (Math.random() * size);
        String s = resources.get(i);
        resources.remove(i);
        mapping.put(index, s);
        return s;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        while (!"exit".equals(line)) {
            int size = Integer.parseInt(line);
            LayoutSimple layoutSimple = new LayoutSimple(size, size);
            long l = getmicTime();
            List<LayoutConfig> configs = layoutSimple.get();
            long h = getmicTime() - l;
            System.out.println("耗时：" + h + "mic");
            String[][] map = new String[size][size];
            for (String[] strings : map) {
                Arrays.fill(strings, "---");
            }
            init();
            for (LayoutConfig config : configs) {
                for (int i = config.getX(); i < config.getX() + config.getXSpec(); i++) {
                    for (int j = config.getY(); j < config.getY() + config.getYSpec(); j++) {
                        map[j][i] = get(config.getName());
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

    public static Long getmicTime() {
        long cutime = System.currentTimeMillis() * 1000; // 微秒
        long nanoTime = System.nanoTime(); // 纳秒
        return cutime + (nanoTime - nanoTime / 1000000 * 1000000) / 1000;
    }

    public LayoutSimple() {
    }

    public LayoutSimple(int width, int height) {
        this.width = width;
        this.height = height;
        map = new boolean[width][height];
    }

    public List<LayoutConfig> get() {
        Set<LayoutConfig> locates = new HashSet<>();
        int count = 0;
        int x = 0;
        int y = 0;
        int maxYSpec = 4;
        int index = 0;
        while (count < width * height) {
            int xSpec = random(1, width - x);
            int ySpec = Math.min(maxYSpec, random(1, height - y));
            Rect rect = new Rect(x, y, xSpec, ySpec);
            String name = index < 100 ? index < 10 ? ("00" + index) : ("0" + index) : ("" + index);
            locates.add(new LayoutConfig(name, x, y, xSpec, ySpec));
            index++;
            count = addRect(rect);
            if (count == width * height) {
                continue;
            }

            int nextY = y + ySpec;
            if (nextY < height) {
                while (y < height && map[x][nextY]) {
                    nextY++;
                    if (nextY == height) {
                        break;
                    }
                }
                if (nextY < height) {
                    int maxHeight = 0;
                    for (int i = nextY; i < height; i++) {
                        if (!map[x][i]) {
                            maxHeight++;
                        } else {
                            break;
                        }
                    }
                    y = nextY;
                    maxYSpec = maxHeight;
                    continue;
                }
            }
            while (nextY == height) {
                x = x + 1;
                for (int i = 0; i < height; i++) {
                    if (!map[x][i]) {
                        nextY = nextY == height ? i : nextY;
                    } else {
                        if (nextY != height) {
                            maxYSpec = i - nextY;
                            break;
                        }
                    }
                }
            }
            y = nextY;
        }
        return Arrays.asList(locates.toArray(new LayoutConfig[0]));
    }

    private int addRect(Rect rect) {
        set.add(rect.x + "," + rect.y);
        for (int i = 0; i < rect.xSpec; i++) {
            for (int j = 0; j < rect.ySpec; j++) {
                set.add((rect.x + i) + "," + (rect.y + j));
                map[rect.x + i][rect.y + j] = true;
            }
        }
        return set.size();
    }

    private Rect pick(int x, int y, int xSpec, int ySpec) {
        for (int i = x; i < x + xSpec; i++) {
            for (int j = y; j < y + ySpec; j++) {
                if (map[i][j]) {

                }
            }
        }
        return null;
    }

    private int random(int min, int max, int... except) {
        if (min == max) {
            return min;
        }
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

    private int randomRange(int... range) {
        int length = range.length;
        int result = (int) (Math.random() * length);
        return range[result];
    }

    private static class Rect {
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

        @Override
        public String toString() {
            return "Rect{" + x +
                    "," + y +
                    "," + xSpec +
                    "," + ySpec +
                    '}';
        }
    }
}
