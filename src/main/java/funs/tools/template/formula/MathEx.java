package funs.tools.template.formula;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MathEx {

	public static final Logger LOGGER = LoggerFactory.getLogger(MathEx.class);

	/**
	 * 转Int
	 * 
	 * @param number
	 * @return
	 */
	public static Integer toInt(final Number number) {
		return number.intValue();
	}

	/**
	 * 转Long
	 * 
	 * @param number
	 * @return
	 */
	public static Long toLong(final Number number) {
		return number.longValue();
	}

	/**
	 * 转Double
	 * 
	 * @param number
	 * @return
	 */
	public static Double toDouble(final Number number) {
		return number.doubleValue();
	}

	/**
	 * 转Float
	 * 
	 * @param number
	 * @return
	 */
	public static Float toFloat(final Number number) {
		return number.floatValue();
	}

	/**
	 * 转Byte
	 * 
	 * @param number
	 * @return
	 */
	public static Byte toByte(final Number number) {
		return number.byteValue();
	}

	/**
	 * a 的 b 次幂
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static int toPow(int a, int b) {
		return (int) Math.pow(a, b);
	}

	/**
	 * 开平方根
	 * 
	 * @param a
	 * @return
	 */
	public static int toSqrt(int a) {
		return (int) Math.sqrt(a);
	}

	/**
	 * 获取 0 到 number - 1 的随机数
	 * 
	 * @param number
	 * @return
	 */
	public static int rand(final int number) {
		return ThreadLocalRandom.current().nextInt(number);
	}

	private static class RandomItem implements Comparable<RandomItem> {

		private Object object;

		private int value;

		public RandomItem(Object object, int value) {
			this.object = object;
			this.value = value;
		}

		public Object getObject() {
			return this.object;
		}

		public int getValue() {
			return this.value;
		}

		@Override
		public int compareTo(RandomItem o) {
			return (this.value - o.getValue()) * -1;
		}

		@Override
		public String toString() {
			return this.object + ":" + this.value;
		}
	}

	/**
	 * 抽签 (权重)
	 * 参数: [权重1, 返回值1, 权重2, 返回值2, .... 权重n, 返回值n] 如[1,"a",2,"b"]
	 * 在 权重为1的"a" 和 权重为2的"b" 的范围内进行抽签
	 * 返回抽中的"a"或"b"
	 * 
	 * @param randomMap
	 *            抽签参数
	 * @return
	 */
	public static Object lot(List<Object> randomItemList) {
		List<RandomItem> itemList = new ArrayList<RandomItem>();
		int number = 0;
		for (int index = 0; index < randomItemList.size(); index = index + 2) {
			Integer value = (Integer) randomItemList.get(index);
			Object object = randomItemList.get(index + 1);
			number += value;
			itemList.add(new RandomItem(object, number));
		}
		int value = ThreadLocalRandom.current().nextInt(number);
		for (RandomItem item : itemList) {
			if (value < item.getValue())
				return item.getObject();
		}
		return null;
	}

	/**
	 * 随机获取对象
	 * 参数: [100:"a", 200:"b"]
	 * 抽中"a"的范围为 0-99
	 * 抽中"b"的范围为 100-199
	 * 返回抽中的"a"或"b" 若无对象抽中返回 null
	 * 
	 * @param randomMap
	 *            抽签参数
	 * @param number
	 *            随机范围
	 * @param randomMap
	 *            随机内容
	 * @return
	 */
	public static Object rand(final int number, List<Object> randomItemList) {
		return rand(number, randomItemList, null);
	}

	/**
	 * 随机获取对象
	 * 参数: [100:"a", 200:"b"]
	 * 抽中"a"的范围为 0-99
	 * 抽中"b"的范围为 100-199
	 * 返回抽中的"a"或"b" 若无对象抽中返回 defaultObject
	 * 
	 * @param randomMap
	 *            抽签参数
	 * @param number
	 *            随机范围
	 * @param randomMap
	 *            随机内容
	 * @param defaultObject
	 *            随机不到对象 返回的默认值
	 * @return
	 */
	public static Object rand(final int number, List<Object> randomItemList, Object defaultObject) {
		List<RandomItem> itemList = new ArrayList<RandomItem>();
		for (int index = 0; index < randomItemList.size(); index = index + 2) {
			Integer value = (Integer) randomItemList.get(index);
			Object object = randomItemList.get(index + 1);
			itemList.add(new RandomItem(object, value));
		}
		Collections.sort(itemList);
		int value = ThreadLocalRandom.current().nextInt(number);
		for (RandomItem item : itemList) {
			if (value < item.getValue()) {
				return item.getObject();
			}
		}
		return defaultObject;
	}

	/**
	 * 随机获取对象(存在Key重复问题)
	 * 参数: [100:"a", 200:"b"]
	 * 抽中"a"的范围为 0-99
	 * 抽中"b"的范围为 100-199
	 * 返回抽中的"a"或"b" 若无对象抽中返回 null
	 * 
	 * @param randomMap
	 *            抽签参数
	 * @param number
	 *            随机范围
	 * @param randomMap
	 *            随机内容
	 * @return
	 */
	public static Object rand(final int number, Map<Integer, Object> randomMap) {
		return rand(number, randomMap, null);
	}

	/**
	 * 随机获取对象(存在Key重复问题)
	 * 参数: [100:"a", 200:"b"]
	 * 抽中"a"的范围为 0-99
	 * 抽中"b"的范围为 100-199
	 * 返回抽中的"a"或"b" 若无对象抽中返回 defaultObject
	 * 
	 * @param randomMap
	 *            抽签参数
	 * @param number
	 *            随机范围
	 * @param randomMap
	 *            随机内容
	 * @param defaultObject
	 *            随机不到对象 返回的默认值
	 * @return
	 */
	public static Object rand(final int number, Map<Integer, Object> randomMap, Object defaultObject) {
		int value = ThreadLocalRandom.current().nextInt(number);
		SortedMap<Integer, Object> sortedMap = new TreeMap<Integer, Object>(randomMap);
		for (Entry<Integer, Object> entry : sortedMap.entrySet()) {
			if (value < entry.getKey()) {
				return entry.getValue();
			}
		}
		return defaultObject;
	}

	/**
	 * 随机 from 到 to 范围的随机数
	 * 
	 * @param randomMap
	 * @return
	 */
	public static int rand(final int from, final int to) {
		if (to < from)
			throw new IllegalArgumentException("to : " + to + " < " + "from : " + from);
		return MathEx.rand((to - from) + 1) + from;
	}

	/**
	 * 限制time次出num个数量的随机器
	 * @param time 监控次数
	 * @param num 监控次数出现的num
	 * @param prob 随机概率
	 * @param currentTime 当前次数
	 * @param currentNum 当前出现数量
	 * @return 是否出现
	 */
	public static boolean randLimited(int time, int num, int prob, int currentTime, int currentNum) {
		return randLimited(time, num, prob, 0, currentTime, currentNum);
	}

	/**
	 * 限制time次出num个数量的随机器
	 * @param time 监控次数
	 * @param num 监控次数出现的num
	 * @param prob 随机概率列表
	 * @param currentTime 当前次数
	 * @param currentNum 当前出现数量
	 * @return 是否出现
	 */
	public static boolean randLimited(int time, int num, Map<Integer, Integer> timesProbsMap, int currentTime, int currentNum) {
		return randLimited(time, num, timesProbsMap, 0, currentTime, currentNum);
	}

	/**
	 * 限制time次出num个数量的随机器
	 * @param time 监控次数
	 * @param num 监控次数出现的num
	 * @param probs n次的概率列表 {3:30, 9:20, 20:10000} 没伦监控第n次概率
	 * @param extra 额外次数 够监控次数后的额外次数
	 * @param currentTime 当前次数
	 * @param currentNum 当前出现数量
	 * @return 是否出现
	 */
	public static boolean randLimited(int time, int num, Map<Integer, Integer> timesProbsMap, int extra, int currentTime, int currentNum) {
		int certainly = checkCertainly(time, num, extra, currentTime, currentNum);
		boolean drop = false;
		if (certainly == 0) {
			int moreTime = currentTime % time;
			Integer prob = null;
			SortedMap<Integer, Integer> sortedMap = null;
			if (timesProbsMap instanceof SortedMap)
				sortedMap = (SortedMap<Integer, Integer>) timesProbsMap;
			else
				sortedMap = new TreeMap<Integer, Integer>(timesProbsMap);
			for (Entry<Integer, Integer> entry : sortedMap.entrySet()) {
				if (moreTime < entry.getKey()) {
					prob = entry.getValue();
					break;
				}
			}
			if (prob == null)
				prob = sortedMap.lastKey();
			int randValue = rand(0, 10000);
			drop = randValue < prob;
		} else {
			drop = certainly > 0;
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("随机结果 : " + drop);
		return drop;
	}

	/**
	 * 限制time次出num个数量的随机器
	 * @param time 监控次数
	 * @param num 监控次数出现的num
	 * @param probs n次的概率列表 [30, 20, 10] 没伦监控第n次概率
	 * @param extra 额外次数 够监控次数后的额外次数
	 * @param currentTime 当前次数
	 * @param currentNum 当前出现数量
	 * @return 是否出现
	 */
	private static int checkCertainly(int time, int num, int extra, int currentTime, int currentNum) {
		int validNum = ((currentTime / time) + 1) * num;
		int moreTime = currentTime % time;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("当前已获取 = {}; 当前次数 ={}; 允许掉落数量 = {}; 周期当前次数 = {};",
					currentNum, currentTime, validNum, moreTime);
		}
		if (currentNum < validNum) {
			if (moreTime >= (time - num)) {
				return 1;
			}
		} else if (currentNum >= validNum + extra) {
			return -1;
		}
		return 0;
	}

	/**
	 * 限制time次出num个数量的随机器
	 * @param time 监控次数
	 * @param num 监控次数出现的num
	 * @param prob 随机概率
	 * @param extra 额外次数 够监控次数后的额外次数
	 * @param currentTime 当前次数
	 * @param currentNum 当前出现数量
	 * @return 是否出现
	 */
	public static boolean randLimited(int time, int num, int prob, int extra, int currentTime, int currentNum) {
		int certainly = checkCertainly(time, num, extra, currentTime, currentNum);
		boolean drop = false;
		if (certainly == 0) {
			int randValue = rand(0, 10000);
			drop = randValue < prob;
		} else {
			drop = certainly > 0;
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("随机结果 : " + drop);
		return drop;
	}

	public static void main(String[] args) {
		final int timePerRound = 50;
		final int allTime = 100;
		int currentTime = 0;
		int currentNum = 0;
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(25, 500);
		map.put(40, 3000);
		map.put(50, 9000);
		for (int index = 0; index < allTime; index++) {
			boolean ok = false;
			if (randLimited(timePerRound, 10, map, 0, currentTime, currentNum)) {
				currentNum++;
				ok = true;
			}
			System.out.println(currentTime + " 次 ==> " + (ok ? 'O' : 'X') + " = " + currentNum);
			if (index % timePerRound == (timePerRound - 1))
				System.out.println(currentTime + " 临界值 " + " = " + currentNum);
			currentTime++;
		}
	}

	/**
	 * 获取时间对象
	 * 
	 * @return
	 */
	@Deprecated
	public static DateTime currentDateTime() {
		return new DateTime();
	}

	/**
	 * 获取时间对象
	 * 
	 * @return
	 */
	public static DateTime now() {
		return new DateTime();
	}

	/**
	 * 随机字符串
	 * @param src
	 * @param length
	 * @return
	 */
	public static String randKey(String src, int length) {
		StringBuilder builder = new StringBuilder(length);
		ThreadLocalRandom random = ThreadLocalRandom.current();
		for (int index = 0; index < length; index++) {
			int srcIndex = random.nextInt(src.length());
			builder.append(src.charAt(srcIndex));
		}
		return builder.toString();
	}

	public static int clamp(int value, int minValue, int maxValue) {
		return Math.min(Math.max(minValue, value), maxValue);
	}

}
