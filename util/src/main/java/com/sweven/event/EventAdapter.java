package com.sweven.event;

public abstract class EventAdapter<T> implements EventListener<T> {

    @Override
    public void method() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void method(T... t) {
    }

}
