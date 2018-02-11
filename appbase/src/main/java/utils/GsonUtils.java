package com.wyw.ljtds.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonUtils {
	/**
	 * 将服务端返回的json字符转换为javabean
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T Json2Bean(String json,Class<T> clazz){
		Gson gson=new Gson();
		T t=gson.fromJson(json,clazz);
		return t;
	}

	/**
	 * 实体类转json
	 * @param clazz
	 * @param <T>
     * @return
     */
	public static<T> String Bean2Json(Class<T> clazz){
		GsonBuilder builder = new GsonBuilder();
		// 不转换没有 @Expose 注解的字段
		builder.excludeFieldsWithoutExposeAnnotation();
		Gson gson = builder.create();
		return gson.toJson(clazz);
	}

	/**
	 * jison转集合
	 * @param json
	 * @param clazz
	 * @param <T>
     * @return
     */
	public static <T> ArrayList<T> Json2ArrayList(String json, Class<T> clazz)
	{
		Type type = new TypeToken<ArrayList<JsonObject>>()
		{}.getType();
		ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);

		ArrayList<T> arrayList = new ArrayList<>();
		for (JsonObject jsonObject : jsonObjects)
		{
			arrayList.add(new Gson().fromJson(jsonObject, clazz));
		}
		return arrayList;
	}

	/**
	 * 实体类转string格式json
	 * @param clazz
     * @return
     */
	public static String Bean2Json(Object clazz){
		Gson gs = new Gson();

		return gs.toJson(clazz);
	}

	/**
	 * list转string格式json
	 * @param list
	 * @return
     */
	public static String List2Json(List list){
		Gson gs = new Gson();

		return gs.toJson(list);//把List转为JSON格式的字符串
	}

}