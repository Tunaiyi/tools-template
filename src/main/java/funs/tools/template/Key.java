package funs.tools.template;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

public class Key {

	private static final Map<String, Key> keyMap = new ConcurrentHashMap<>();

	private static final String PROFILE_START = "[";
	private static final String PROFILE_END = "]";
	private static final char PROFILE_SEPARATOR = ',';
	private static final char SEPARATOR = '.';

	private static final char PLURALITY_SEPARATOR = '$';

	private boolean plurality;

	private String source;

	private int index;// 基数为1

	private boolean composite;

	private List<String> profiles = Collections.emptyList();

	/**
	 * 主键
	 */
	private String mainKey;

	/**
	 * 副键
	 */
	private Key viceKey;

	public static final Key EMPTY_KEY = new Key("");

	private Key(String key) {
		this.source = key;
		if (key.startsWith(PROFILE_START)) {
			int profileEndIndex = key.indexOf(PROFILE_END);
			if (profileEndIndex < 0)
				throw new IllegalArgumentException(key + " 格式错误");
			String profilesWord = key.substring(1, profileEndIndex);
			this.profiles = Arrays.asList(StringUtils.split(profilesWord, PROFILE_SEPARATOR));
			key = key.substring(profileEndIndex + 1);
		}
		int index = key.indexOf(SEPARATOR);
		if (index > 0) {
			this.composite = true;
			this.mainKey = key.substring(0, index);
			this.viceKey = getKey(key.substring(index + 1));
		} else {
			this.composite = false;
			this.mainKey = key;
		}
		index = this.mainKey.indexOf(PLURALITY_SEPARATOR);
		if (index > 0) {
			this.plurality = true;
			this.index = Integer.parseInt(mainKey.substring(index + 1));
			this.mainKey = mainKey.substring(0, index);
		} else {
			this.plurality = false;
		}
	}

	/**
	 * 是否为复数
	 * 
	 * @return
	 */
	public boolean isPlurality() {
		return plurality;
	}

	public int getIndex() {
		return this.index;
	}

	/**
	 * 是否是复合键
	 * 
	 * @return
	 */
	public boolean isComposite() {
		return composite;
	}

	public String getMainKey() {
		return this.mainKey;
	}

	public boolean hasViceKey() {
		return this.viceKey != null;
	}

	public Key getViceKey() {
		return this.viceKey;
	}
	
	public String getSource() {
		return source;
	}

	public boolean isProfiles() {
		return profiles != null && !profiles.isEmpty();
	}
	
	public List<String> getProfiles() {
		return Collections.unmodifiableList(profiles);
	}

	public static Key getKey(String val) {
		Key key = keyMap.get(val);
		if (key == null) {
			key = new Key(val);
			keyMap.put(val, key);
		}
		return key;
	}

	public boolean isEmpty() {
		return this == EMPTY_KEY;
	}

	@Override
	public String toString() {
		return "Key [source=" + source + ", composite=" + composite + ", mainKey=" + mainKey + ", viceKey=" + viceKey
				+ "]";
	}

}
