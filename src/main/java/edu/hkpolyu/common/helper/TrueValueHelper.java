package edu.hkpolyu.common.helper;

import java.util.Collection;
import java.util.Map;
import java.lang.reflect.Array;

public class TrueValueHelper {

	public static boolean isTrue(Object obj, boolean noZeroValue, boolean noEmptyValue) {
		if (null == obj || obj.equals(false)) return false;
		if (noZeroValue && obj.equals(0)) return false;
		if (obj instanceof String) {
			String str = (String)obj;
			if (str.equalsIgnoreCase("false")) return false;
			if (noZeroValue) {
				try {
					if (Double.parseDouble(str) == 0.0) return false;
				} catch (NumberFormatException ex) {
				}
			}
		}
		if (noEmptyValue) {
			if (obj instanceof String && ((String)obj).length() == 0) return false;
			if (obj.getClass().isArray()) {
				if (Array.getLength(obj) == 0) return false;
			} else if (obj instanceof Collection) {
				return !((Collection)obj).isEmpty();
			} else if (obj instanceof Map) {
				return !((Map)obj).isEmpty();
			}
		}
		return true;
	}
	
	public static boolean isTrue(Map<String, Object> map, String key,
			boolean noZeroValue, boolean noEmptyValue) {
		if (null == map || !map.containsKey(key)) return false;
		return isTrue(map.get(key), noZeroValue, noEmptyValue);
	}
	
	public static boolean isTrue(Map<String, Object> map, String key) {
		return isTrue(map, key, false, false);
	}
}