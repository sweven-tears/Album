package com.sweven.event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventManager {
    private static EventManager instance;

    /**
     * addEvent添加的event name 和 事件检查线程
     */
    private Map<String, EventDispatch> dispatches;
    /**
     * removeEvent移除的event name， 标记加为黑名单，之后不再响应， 需要移除黑名单用readdEvent
     */
    private Set<String> blackNames;

    public static EventManager getInstance() {
        if (instance == null) {
            synchronized (EventManager.class) {
                if (instance == null) {
                    instance = new EventManager();
                }
            }
        }
        return instance;
    }

    public EventManager() {
        dispatches = new HashMap<>();
        blackNames = new HashSet<>();
    }

    /**
     * 添加event，当其他地方通过sendEvent时响应 重复添加event，将只执行
     *
     * @param <T>   参数类型
     * @param name  event name
     * @param event 需要执行回调方法响应
     */
    public <T> void addEvent(String name, EventAdapter<T> event) {
        if (!dispatches.containsKey(name)) {
            EventDispatch dispatch = new EventDispatch(name, event);
            dispatch.start();
            dispatches.put(name, dispatch);
        } else {
            addEvent("_" + name, event);
        }
    }

    /**
     * 添加event，当其他地方通过sendEvent时响应 重复添加event，将只执行
     *
     * @param <T>   参数类型
     * @param name  event name
     * @param event 需要执行回调方法响应
     */
    public <T> void addEvent(String name, Object event) throws Exception {
        if (name.startsWith("_")) {
            throw new Exception("Event names cannot begin with '_'");
        }
        if (!dispatches.containsKey(name)) {
            EventDispatch dispatch = new EventDispatch(name, new EventAdapter<T>() {
                @Override
                public void method() {
                }
            });
            dispatch.start();
            dispatches.put(name, dispatch);
        } else {
            addEvent("_" + name, event);
        }
    }

    /**
     * 发送event，在添加addEvent的地方响应事件
     *
     * @param <T>  参数类型
     * @param name event name
     * @param t    参数数组
     */
    @SuppressWarnings("unchecked")
    public <T> void sendEvent(String name, T... t) {
        if (!blackNames.contains(name)) {
            Set<String> mapSet = dispatches.keySet();
            for (String set : mapSet) {
                if (set.endsWith(name) && !set.replace(name, "").matches("/_*/")) {
                    dispatches.get(set).start(t);
                }
            }
        }
    }

    /**
     * 暂时性移除event，即将event添加到黑名单中，下一次添加也不再响应
     * 如果需要继续响应该event，用{readdEvent(name,t)}重新加入事件当中
     *
     * @param name
     */
    public void removeEvent(String name) {
        blackNames.add(name);
    }

    /**
     * 销毁event。无法恢复
     *
     * @param name
     */
    public void destoryEvent(String name) {
        dispatches.remove(name);
    }

    /**
     * @param <T>  参数类型
     * @param name event name
     * @param t    参数数组
     */
    @SuppressWarnings("unchecked")
    public <T> void readdEvent(String name, T... t) {
        blackNames.remove(name);
        sendEvent(name, t);
    }

    /**
     * @return event count
     */
    public int count() {
        return dispatches.size();
    }

    /**
     * even 是否在派发列表中
     *
     * @param name event name
     * @return
     */
    public boolean exist(String name) {
        return dispatches.keySet().contains(name);
    }

    public void destory() {
        dispatches.clear();
        blackNames.clear();
    }

}
