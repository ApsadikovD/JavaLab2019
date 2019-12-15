package ru.javalab.di;

public interface ApplicationContext {
    <T> T getComponent(Class<T> componentType, String name);
}
