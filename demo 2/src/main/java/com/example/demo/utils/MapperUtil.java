package com.example.demo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class MapperUtil {
    private static ObjectMapper mapper = createMapper();

   private static ObjectMapper createMapper() {
        mapper = new ObjectMapper();
        return mapper;
   }

   public static String toJson(Object obj) {
       try {
           return mapper.writeValueAsString(obj);
       } catch (IOException e) {
           log.error(e.getMessage(),e);
           return null;
       }
   }

   public static Map<String, Object> fromJson(String json) {
       try {

           String value = mapper.writeValueAsString(json);
           value = value.substring(2, value.length()-2);
           String[] keyValuePairs = value.split(",");
           log.info("KeyValuePairs {} and value {}" , keyValuePairs, value);

           Map<String,Object> map = new HashMap<>();

           for(String pair : keyValuePairs)
           {
               String[] entry = pair.split("=");

               map.put(entry[0].trim(), Integer.parseInt(entry[1].trim()));
           }

           log.info("Map {}", map);

           return map;
       } catch (IOException e) {
           log.error(e.getMessage(), e);
           return null;
       }
   }

   public static Map<String , Object> parse(String json) throws JsonProcessingException {

       String input = mapper.writeValueAsString(json);
       input = input.substring(1, input.length() - 1);

       HashMap<String, Object> map = parseStringToMap(input);

       // Print the result
       log.info("Parsed HashMap:{} " , map);
       return map;
   }
    public static HashMap<String, Object> parseStringToMap(String input) {
        HashMap<String, Object> resultMap = new HashMap<>();

        // Match key-value pairs using regex
        Pattern pairPattern = Pattern.compile("(\\w+)=(\\[.*?\\]|\\d+)");
        Matcher matcher = pairPattern.matcher(input);

        while (matcher.find()) {
            String key = matcher.group(1); // Key
            String value = matcher.group(2); // Value

            if (value.startsWith("[") && value.endsWith("]")) {
                // If the value is an array of objects, parse it
                value = value.substring(1, value.length() - 1); // Remove square brackets
                List<Map<String, Integer>> list = new ArrayList<>();

                Pattern objectPattern = Pattern.compile("\\{(.*?)\\}");
                Matcher objectMatcher = objectPattern.matcher(value);

                while (objectMatcher.find()) {
                    String objectContent = objectMatcher.group(1);
                    Map<String, Integer> map = new HashMap<>();

                    for (String pair : objectContent.split(",\\s*")) {
                        String[] keyValue = pair.split("=");
                        map.put(keyValue[0], Integer.parseInt(keyValue[1]));
                    }
                    list.add(map);
                }

                resultMap.put(key, list);
            } else {
                // Parse simple key-value pair as Integer
                resultMap.put(key, Integer.parseInt(value));
            }
        }

        return resultMap;
    }


 }
