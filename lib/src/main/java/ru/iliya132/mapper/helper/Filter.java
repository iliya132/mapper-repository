package ru.iliya132.mapper.helper;

import java.util.*;

public class Filter {
    private Map<String, Object> params = new HashMap<>();
    private Map<String, List<Object>> repeatedParams = new HashMap<>();

    public Filter() {
    }

    public Filter(Map<String, Object> params) {
        this.params = params;
    }

    public void addParameter(String name, Object parameter) {
        if (!params.containsKey(name)) {
            params.put(name, parameter);
        } else {
            putRepeated(name, parameter);
        }
    }

    public void addRepeatedParameters(Map<String, List<Object>> params){
        repeatedParams.putAll(params);
    }

    private void putRepeated(String name, Object parameter) {
        if (repeatedParams.containsKey(name)) {
            repeatedParams.get(name).add(parameter);
        } else {
            repeatedParams.put(name, List.of(parameter));
        }
    }

    public String getWhere() {
        StringBuilder builder = new StringBuilder();
        builder.append("1=1");
        params.keySet().forEach(key ->
                builder.append(" AND ")
                        .append(key)
                        .append(" = :")
                        .append(key)
                        .append(" "));
        repeatedParams.keySet().forEach(key ->
                builder.append(" AND ")
                        .append(key)
                        .append(" IN (:")
                        .append(key)
                        .append(") "));

        return builder.toString();
    }

    public Map<String, Object> getParams() {
        Map<String, Object> result = new HashMap<>(params);
        result.putAll(repeatedParams);
        return result;
    }
}
