package ru.javalab.chat.context;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ApplicationContextReflectionBased implements ApplicationContext {
    private HashMap<String, Object> componentMap;

    public ApplicationContextReflectionBased() {
        componentMap = new HashMap<>();
    }

    @Override
    public <T> T getComponent(Class<T> componentType, String name) {
        if (!componentMap.containsKey(name)) {
            createComponent(componentType);
        }
        return (T) componentMap.get(name);
    }

    public <T> void createComponent(Class<T> componentType) {
        if (componentMap.get(componentType.getName()) != null) {
            return;
        }
        Field[] fields = componentType.getDeclaredFields();
        for (Field f : fields) {
            Class[] interfaces = f.getType().getInterfaces();
            for (Class i : interfaces) {
                if (i.getName().equals(Component.class.getName())) {
                    createComponent(f.getType());
                }
            }
        }

        injectDependency(componentType);
    }

    private <T> void injectDependency(Class<T> componentType) {
        try {
            Object o = componentType.getDeclaredConstructor().newInstance();
            componentMap.put(((Component) o).getName(), o);
            for (Field f : o.getClass().getDeclaredFields()) {
                Class[] interfaces = f.getType().getInterfaces();
                for (Class i : interfaces) {
                    if (i.getName().equals(Component.class.getName())) {
                        f.setAccessible(true);
                        f.set(o, findComponent(f));
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Component findComponent(Field f) {
        String componentClassName = f.getType().toString().substring(6, f.getType().toString().length());
        return (Component) componentMap.entrySet().stream()
                .filter(c -> c.getValue().getClass().getName().equals(componentClassName))
                .limit(1).collect(Collectors.toList()).get(0).getValue();
    }
}
