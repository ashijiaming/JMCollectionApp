package jm.com.collection.net;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonReader;

public class GsonUtil {

    private static Gson gson = null;
    private static GsonBuilder gsonBuilder=null;
    static {
	if (gson == null) {
	    gson = new Gson();
	}
	if(gsonBuilder==null){
	    gsonBuilder=new GsonBuilder();
	}
//	gsonBuilder.registerTypeAdapter(String.class, new StringConverter());
//	gson=gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    private GsonUtil() {
    }

    /**
     * 将对象转换成json格式
     * 
     * @param ts
     * @return
     */
    public static String parseJson(Object ts) {
	String jsonStr = null;
	if (gson != null) {
	    try {
		jsonStr = gson.toJson(ts);
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
	return jsonStr;
    }

    /**
     * 将List<T>对象转换成json格式
     * 
     * @param list
     * @return
     */
    public static <T> String parseJson(List<T> list) {
	String jsonStr = null;
	if (gson != null) {
	    try {
		jsonStr = gson.toJson(list);
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
	return jsonStr;
    }

    /**
     * 将Map<String, Object>对象转换成json格式
     * 
     * @param map
     * @return
     */
    public static String parseJson(Map<String, Object> map) {
	String jsonStr = null;
	if (gson != null) {
	    try {
		jsonStr = gson.toJson(map);
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
	return jsonStr;
    }

    /**
     * 将T[]数組转换成json格式
     * 
     * @param arr
     * @return
     */
    public static <T> String parseJson(T[] arr) {
	String jsonStr = null;
	if (gson != null) {
	    try {
		jsonStr = gson.toJson(arr);
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
	return jsonStr;
    }

    /**
     *  将对象转换成json格式
     * @param ts
     * @param dataFormat 日期格式
     * @return
     */
    public static String parseJson(Object ts,String dataFormat) {
	String jsonStr = null;
	if (gson != null) {
	    try {
		gson=gsonBuilder.setDateFormat(dataFormat).create();
		jsonStr = gson.toJson(ts);
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
	return jsonStr;
    }

    /**
     * 将List<T>对象转换成json格式
     * 
     * @param list
     * @param dataFormat 日期格式
     * @return
     */
    public static <T> String parseJson(List<T> list,String dataFormat) {
	String jsonStr = null;
	if (gson != null) {
	    try {
		gson=gsonBuilder.setDateFormat(dataFormat).create();
		jsonStr = gson.toJson(list);
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
	return jsonStr;
    }

    /**
     * 将Map<String, Object>对象转换成json格式
     * 
     * @param map
     * @param dataFormat 日期格式
     * @return
     */
    public static String parseJson(Map<String, Object> map,String dataFormat) {
	String jsonStr = null;
	if (gson != null) {
	    try {
		gson=gsonBuilder.setDateFormat(dataFormat).create();
		jsonStr = gson.toJson(map);
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
	return jsonStr;
    }

    /**
     * 将T[]数組转换成json格式
     * 
     * @param arr
     * @param dataFormat 日期格式
     * @return
     */
    public static <T> String parseJson(T[] arr,String dataFormat) {
	String jsonStr = null;
	if (gson != null) {
	    try {
		gson=gsonBuilder.setDateFormat(dataFormat).create();
		jsonStr = gson.toJson(arr);
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
	return jsonStr;
    }
    
    /**
     * 将对象转换成json格式(并自定义日期格式)
     * 
     * @param ts
     * @return
     */
    public static String parseJsonDateSerializer(Object ts,
	    final String dateformat) {
	String jsonStr = null;
	gson = new GsonBuilder()
		.registerTypeHierarchyAdapter(Date.class,
			new JsonSerializer<Date>() {
			    public JsonElement serialize(Date src,
				    Type typeOfSrc,
				    JsonSerializationContext context) {
				SimpleDateFormat format = new SimpleDateFormat(
					dateformat);
				return new JsonPrimitive(format.format(src));
			    }
			}).setDateFormat(dateformat).create();
	if (gson != null) {
	    jsonStr = gson.toJson(ts);
	}
	return jsonStr;
    }
    
    /**
     * 
     * @param jsonString
     * @param cls
     * @return
     */
    public static <T> T[] getArrayJson(String jsonString, Class<T[]> cls) {
	T[] t = null;
	try {
	    t = gson.fromJson(jsonString, cls);
	} catch (Exception e) {
	}
	return t;
    }
    
    /**
     * 
     * @param jsonString
     * @param cls
     * @param dataFormat 日期格式
     * @return
     */
    public static <T> T[] getArrayJson(String jsonString, Class<T[]> cls,String dataFormat) {
	T[] t = null;
	try {
	    gson=gsonBuilder.setDateFormat(dataFormat).create();
	    t = gson.fromJson(jsonString, cls);
	} catch (Exception e) {
	}
	return t;
    }
    
    /**
     * 
     * @param jsonString
     * @param cls
     * @return
     */
    public static <T> T getObjectJson(String jsonString, Class<T> cls) {
	T t = null;
	try {
	    t = gson.fromJson(jsonString, cls);
	} catch (Exception e) {
	}
	return t;
    }

    
    /**
     * 
     * @param jsonString
     * @param cls
     * @param dataFormat 日期格式
     * @return
     */
    public static <T> T getObjectJson(String jsonString, Class<T> cls,String dataFormat) {
	T t = null;
	try {
	    gson=gsonBuilder.setDateFormat(dataFormat).create();
	    t = gson.fromJson(jsonString, cls);
	} catch (Exception e) {
	}
	return t;
    }
    
    /**
     * 
     * @param fileName  例如: D:\\LdMedi\\apk\\app.json
     * @param cls
     * @return
     */
    public static <T> T getJsonReaderJson(String fileName, Class<T> cls) {
	T t = null;
	try {
	    JsonReader jsonReader=new JsonReader(new FileReader(fileName));
	    t = gson.fromJson(jsonReader, cls);
	} catch (Exception e) {
	}
	return t;
    }
    
    /**
     * 
     * @param fileName  例如: D:\\LdMedi\\apk\\app.json
     * @param cls
     * @param dataFormat 日期格式
     * @return
     */
    public static <T> T getJsonReaderJson(String fileName, Class<T> cls,String dataFormat) {
	T t = null;
	try {
	    gson=gsonBuilder.setDateFormat(dataFormat).create();
	    JsonReader jsonReader=new JsonReader(new FileReader(fileName));
	    t = gson.fromJson(jsonReader, cls);
	} catch (Exception e) {
	}
	return t;
    }
    
    /**
     * 
     * @param jsonString
     * @return
     */
    public static Map<String, String> getJsonString(String jsonString) {
	Map<String, String> map = new HashMap<String, String>();

	try {
	    JsonObject jsonObject = new JsonParser().parse(jsonString)
		    .getAsJsonObject();
	    for (Entry<String, JsonElement> elem : jsonObject.entrySet()) {
		String key = elem.getKey();
		String value = elem.getValue().toString();
		map.put(key, value);
	    }

	} catch (Exception e) {
	}
	return map;
    }

    /**
     * 
     * @param jsonString
     * @param dataFormat 日期格式
     * @return
     */
    public static Map<String, String> getJsonString(String jsonString,String dataFormat) {
	Map<String, String> map = new HashMap<String, String>();

	try {
	    gson=gsonBuilder.setDateFormat(dataFormat).create();
	    JsonObject jsonObject = new JsonParser().parse(jsonString)
		    .getAsJsonObject();
	    for (Entry<String, JsonElement> elem : jsonObject.entrySet()) {
		String key = elem.getKey();
		String value = elem.getValue().toString();
		map.put(key, value);
	    }

	} catch (Exception e) {
	}
	return map;
    }

    
    /**
     * 
     * @param jsonString
     * @param cls
     * @return
     */
    public static <T> List<T> getListJson(String jsonString, Class<T> cls) {
	List<T> list = new ArrayList<T>();
	try {
	    JsonArray array = new JsonParser().parse(jsonString)
		    .getAsJsonArray();
	    for (final JsonElement elem : array) {
		list.add(gson.fromJson(elem, cls));
	    }

	} catch (Exception e) {
	}
	return list;
    }

    /**
     * 
     * @param jsonString
     * @param cls
     * @param dataFormat 日期格式
     * @return
     */
    public static <T> List<T> getListJson(String jsonString, Class<T> cls,String dataFormat) {
	List<T> list = new ArrayList<T>();
	try {
	    gson=gsonBuilder.setDateFormat(dataFormat).create();
	    JsonArray array = new JsonParser().parse(jsonString)
		    .getAsJsonArray();
	    for (final JsonElement elem : array) {
		list.add(gson.fromJson(elem, cls));
	    }

	} catch (Exception e) {
	}
	return list;
    }

    
    /**
     * 
     * @param fileName 例如:  D:\\LdMedi\\apk\\app.json
     * @param cls
     * @return
     */
    public static <T> List<T> getJsonReaderListJson(String fileName, Class<T> cls) {
	List<T> list = new ArrayList<T>();
	try {
	    JsonReader jsonReader=new JsonReader(new FileReader(fileName));
	    JsonArray array = new JsonParser().parse(jsonReader)
		    .getAsJsonArray();
	    for (final JsonElement elem : array) {
		list.add(gson.fromJson(elem, cls));
	    }

	} catch (Exception e) {
	}
	return list;
    }
    
    /**
     * 
     * @param fileName 例如:  D:\\LdMedi\\apk\\app.json
     * @param cls
     * @param dataFormat 日期格式
     * @return
     */
    public static <T> List<T> getJsonReaderListJson(String fileName, Class<T> cls,String dataFormat) {
	List<T> list = new ArrayList<T>();
	try {
	    gson=gsonBuilder.setDateFormat(dataFormat).create();
	    JsonReader jsonReader=new JsonReader(new FileReader(fileName));
	    JsonArray array = new JsonParser().parse(jsonReader)
		    .getAsJsonArray();
	    for (final JsonElement elem : array) {
		list.add(gson.fromJson(elem, cls));
	    }

	} catch (Exception e) {
	}
	return list;
    }
    
    /**
     * 
     * @param jsonString
     * @param cls
     * @return
     */
    public static <T> Map<String, List<T>> getMapJson(String jsonString,  Class<T> cls) {
	Map<String, List<T>> map = new HashMap<String, List<T>>();
	try {

	    JsonObject jsonObject = new JsonParser().parse(jsonString)
		    .getAsJsonObject();
	    for (Entry<String, JsonElement> elem : jsonObject.entrySet()) {
		String key = elem.getKey();
		String value = elem.getValue().toString();
		JsonArray array = new JsonParser().parse(value)
			.getAsJsonArray();
		List<T> list = new ArrayList<T>();
		for (JsonElement arrElement : array) {
		    list.add(gson.fromJson(arrElement, cls));
		}
		map.put(key, list);
	    }
	} catch (Exception e) {
	}
	return map;
    }
    
    /**
     * 
     * @param jsonString
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static <T> List<Map<String, Object>> getMapObjectJson(String jsonString) {
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	
	try {

	    JsonObject jsonObject = new JsonParser().parse(jsonString)
		    .getAsJsonObject();
	    for (Entry<String, JsonElement> elem : jsonObject.entrySet()) {
		String key = elem.getKey();
		Object value = elem.getValue();
		JsonArray array = new JsonParser().parse(value.toString())
			.getAsJsonArray();
		List<Map> listmap = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		for (JsonElement arrElement : array) {
		    listmap.add(gson.fromJson(arrElement, HashMap.class));
		}
		if (!map.containsKey(key)) {
		    map.put(key, listmap);
		}
		list.add(map);
	    }
	} catch (Exception e) {
	}
	return list;
    }
    
    /**
     * 
     * @param jsonString
     * @param cls
     * @param dataFormat 日期格式
     * @return
     */
    public static <T> Map<String, List<T>> getMapJson(String jsonString, Class<T> cls,String dataFormat) {
	Map<String, List<T>> map = new HashMap<String, List<T>>();
	try {
	    gson=gsonBuilder.setDateFormat(dataFormat).create();
	    JsonObject jsonObject = new JsonParser().parse(jsonString)
		    .getAsJsonObject();
	    for (Entry<String, JsonElement> elem : jsonObject.entrySet()) {
		String key = elem.getKey();
		String value = elem.getValue().toString();
		JsonArray array = new JsonParser().parse(value)
			.getAsJsonArray();
		List<T> list = new ArrayList<T>();
		for (JsonElement arrElement : array) {
		    list.add(gson.fromJson(arrElement, cls));
		}
		map.put(key, list);
	    }
	} catch (Exception e) {
	}
	return map;
    } 
}
