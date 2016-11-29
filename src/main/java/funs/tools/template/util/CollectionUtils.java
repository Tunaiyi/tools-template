package funs.tools.template.util;

import java.util.List;

public class CollectionUtils {

	@SuppressWarnings("unchecked")
	public static <T> List<T>[] split(List<T> source, int num) {
		if (source.size() == 0) {
			return new List[0];
		}
		int len = (source.size() + num - 1) / num;
		List<T>[] result = new List[len];
		for (int i = 0; i < len; i++) {
			int last = (i + 1) * num;
			result[i] = source.subList(i * num, last > source.size() ? source.size() : last);
		}
		return result;
	}

}
