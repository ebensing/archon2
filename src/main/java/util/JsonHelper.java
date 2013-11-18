package util;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * User: EJ
 * Date: 11/17/13
 * Time: 10:02 PM
 */
public class JsonHelper {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T parseJson(String str, Class<T> tClass) {
        try {
            return objectMapper.readValue(str, tClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String stringify(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
