package funs.tools.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

public class TemplateAttribute {

	private Map<String, Map<String, Object>> profileMap = new HashMap<>();

	private Map<String, Object> attributes = new HashMap<>();

	public Map<String, Object> getAttribute(String profile) {
		if (profile == null)
			return attributes;
		if (profileMap.isEmpty())
			return attributes;
		Map<String, Object> profileAtrribute = profileMap.get(profile);
		if (profileAtrribute == null || profileAtrribute.isEmpty())
			return attributes;
		Map<String, Object> map = new HashMap<>();
		if (attributes != null)
			map.putAll(attributes);
		map.putAll(profileAtrribute);
		return map;
	}

	public void filter() {
		Map<String, Map<String, Object>> profileMap = new HashMap<>();
		for (Entry<String, Map<String, Object>> entry : this.profileMap.entrySet()) {
			Map<String, Object> pfMap = filterAttr(entry.getValue());
			if (pfMap != null)
				profileMap.put(entry.getKey(), pfMap);
		}
		this.profileMap = profileMap;
		this.attributes = this.filterAttr(this.attributes);
	}

	private Map<String, Object> filterAttr(Map<String, ?> attributes) {
		return this.filterEmptyMap(this.filterNullList(attributes));
	}

	/**
	 * 递归过滤Map掉的List中的null元素
	 *
	 * @param attributes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> filterNullList(Map<String, ?> attributes) {
		Map<String, Object> newMap = new HashMap<>(attributes);
		for (Entry<String, Object> entry : newMap.entrySet()) {
			if (entry.getValue() == null) {
				continue;
			}
			if (entry.getValue() instanceof List) {
				List<Object> listValue = (List<Object>) entry.getValue();
				List<Object> newList = new ArrayList<>(listValue.size());
				for (Object value : listValue) {
					if (value == null) {
						continue;
					}
					if (value instanceof Map) {
						this.filterNullList((Map<String, Object>) value);
					}
					newList.add(value);
				}
				newMap.put(entry.getKey(), newList);
			}
			if (entry.getValue() instanceof Map) {
				Map<String, Object> mapValue = (Map<String, Object>) entry.getValue();
				this.filterNullList(mapValue);
				newMap.put(entry.getKey(), mapValue);
			}
		}
		return newMap;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> filterEmptyMap(Map<String, Object> attributes) {
		Map<String, Object> newMap = new HashMap<>(attributes);
		for (Entry<String, Object> entry : newMap.entrySet()) {
			if (entry.getValue() == null) {
				continue;
			}
			if (entry.getValue() instanceof List) {
				List<Object> listValue = (List<Object>) entry.getValue();
				List<Object> newList = new ArrayList<>(listValue.size());
				for (Object value : listValue) {
					if (value == null) {
						continue;
					}
					if (value instanceof Map) {
						value = this.filterEmptyMap((Map<String, Object>) value);
					}
					if (value != null) {
						newList.add(value);
					}
				}
				newMap.put(entry.getKey(), newList);
			}
			if (entry.getValue() instanceof Map) {
				Map<String, Object> mapValue = (Map<String, Object>) entry.getValue();
				newMap.put(entry.getKey(), this.filterEmptyMap(mapValue));
			}
		}
		return this.isEmptyMap(newMap) ? null : newMap;
	}

	/**
	 * map的所有元素为null、空数组或者空Map，则map为空
	 *
	 * @param attributes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean isEmptyMap(Map<String, Object> attributes) {
		for (Entry<String, Object> entry : attributes.entrySet()) {
			if (entry.getValue() == null) {
				continue;
			}
			if (!(entry.getValue() instanceof List) && !(entry.getValue() instanceof Map)) {
				return false;
			}
			if (entry.getValue() instanceof List && ((List<?>) entry.getValue()).size() > 0) {
				return false;
			}
			if (entry.getValue() instanceof Map && !this.isEmptyMap((Map<String, Object>) entry.getValue())) {
				return false;
			}
		}
		return true;
	}

	public Map<String, Object> loadOrCreate(String profile) {
		if (profile == null || StringUtils.isBlank(profile))
			return this.attributes;
		Map<String, Object> map = profileMap.get(profile);
		if (map == null)
			profileMap.put(profile, map = new HashMap<>());
		return map;
	}

}
