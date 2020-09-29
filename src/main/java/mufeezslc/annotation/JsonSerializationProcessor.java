package mufeezslc.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

// Annotation processor
public class JsonSerializationProcessor {

    public String convertToJson(Object o){
        try {
            checkIfSerializable(o);
            formateNames(o);
            return convertToString(o);
        } catch (Exception e){
            throw new JsonSerializationException(e.getMessage());
        }
    }

    private void checkIfSerializable(Object o){
        if(Objects.isNull(o)){
            throw new JsonSerializationException("provided object is null");
        }
        Class<Person> clazz = (Class<Person>) o.getClass();
        if(! clazz.isAnnotationPresent(JsonSerializable.class)){
            throw new JsonSerializationException("class is not serializable");
        }
    }

    private void formateNames(Object o) throws InvocationTargetException, IllegalAccessException {
        Class<?> clazz = o.getClass();
        // use declared methods instead of getMethods as the later is only for public methods
        Method[] methods = clazz.getDeclaredMethods();
        for(Method method : methods){
            if(method.isAnnotationPresent(FormatNames.class)){
                method.setAccessible(true);
                method.invoke(o);
            }
        }
    }

    private String convertToString(Object o) throws IllegalAccessException {
        Class<?> clazz = o.getClass();
        Map<String, String> values = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            if(field.isAnnotationPresent(JsonElement.class)){
                field.setAccessible(true);
                values.put(getKey(field), (String) field.get(o));
            }
        }

        String jsonString = values.entrySet().stream()
                .map(entry -> "\"" + entry.getKey() + "\":" + "\"" + entry.getValue() + "\"")
                .collect(Collectors.joining(","));
        return "{" + jsonString + "}";
    }

    private String getKey(Field field){
        String key = field.getAnnotation(JsonElement.class).key();
        return key.isEmpty() ? field.getName() : key;
    }
}
