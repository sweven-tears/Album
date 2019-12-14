package com.sweven.event;

public class EventDispatch {

	private String name;
	@SuppressWarnings("rawtypes")
	private EventAdapter event;

	@SuppressWarnings("rawtypes")
	public EventDispatch(String name, EventAdapter event) {
		this.name = name;
		this.event = event;
	}

	@SuppressWarnings("unchecked")
	public <T> void start(T... t) {
		if (EventManager.getInstance().exist(name)) {
			if (t == null || t.length==0) {
				event.method();
			} else {
				event.method(t);
			}
		}
	}

}
