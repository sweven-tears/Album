package com.sweven.event;

public interface EventListener<T> {
	void method();
	
	@SuppressWarnings("unchecked")
	void method(T... t);
}
