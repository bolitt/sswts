package org.net9.redbud.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class RegionUtils {

	// 配置文件的名字，最好跟该类放到同一目录下，用UTF-8编码
	private static final String configFile = "nationlist";

	// 保存映射关系的map
	HashMap<String, String> regionMap;

	// singleton的实例
	private static RegionUtils instance;

	private RegionUtils() throws IOException {
		regionMap = new HashMap<String, String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				getClass().getResourceAsStream(configFile), "UTF-8"));
		String line;
		while ((line = reader.readLine()) != null) {
			String[] ss = line.split(",");
			regionMap.put(ss[1], ss[0]);
		}
	}

	public static RegionUtils getInstance() {
		if (instance == null) {
			try {
				instance = new RegionUtils();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	public String getFullRegion(String summary) {
		return regionMap.get(summary);
	}

	public String getSummaryRegion(String full) {
		return full.substring(0, 2);
	}
}
