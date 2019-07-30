package aoizora.runner;

import aoizora.annotation.After;
import aoizora.annotation.Before;
import aoizora.annotation.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Runner {

    private final Class testClass;
    private Set<Method> tests = new HashSet<>();
    private Map<String, Method> x = new HashMap<>();

    public Runner(Class<?> test) {
        this.testClass = test;
        parseTests(this.testClass);
    }

    private void parseTests(Class<?> testClass) {
        for (Method method: testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                tests.add(method);
            }
            if (method.isAnnotationPresent(Before.class)) {
                if (x.containsKey("before"))
                    throw new RuntimeException("More than one @Before handler defined");
                x.put("before", method);
            }
            if (method.isAnnotationPresent(After.class)) {
                if (x.containsKey("after"))
                    throw new RuntimeException("More than one @After handler defined");
                x.put("after", method);
            }
        }
    }

    public void run() {
        try {
            for (Method method : tests) {
                Object object = testClass.newInstance();
                runBefore(object);
                runTest(object, method);
                runAfter(object);
            }
        } catch(InstantiationException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean runBefore(Object object) {
        try {
            if (x.get("before") != null) {
                x.get("before").invoke(object);
                return true;
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private void runAfter(Object object) {
        try {
            if (x.get("after") != null)
                x.get("after").invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.out.println(e.getMessage());
        }
    }

    private void runTest(Object object, Method method) {
        try {
            method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.out.println(e.getMessage());
        } catch (Exception ignore) {}
    }
}
