package org.net9.redbud.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class DepartmentUtils {
	private static final String configFile = "department";

	private HashMap<Short, String> departmentMap;

	private HashMap<String, Short> codeMap;

	private static DepartmentUtils instance;

	private DepartmentUtils() throws IOException {
		departmentMap = new HashMap<Short, String>();
		codeMap = new HashMap<String, Short>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				getClass().getResourceAsStream(configFile), "UTF-8"));
		String line;
		while ((line = reader.readLine()) != null) {
			String[] ss = line.split(" +");
			short code = Short.parseShort(ss[0]);
			String deptName = ss[1];
			departmentMap.put(code, deptName);
			codeMap.put(deptName, code);
		}
	}

	public static DepartmentUtils getInstance() {
		if (instance == null) {
			try {
				instance = new DepartmentUtils();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	public String getDepartment(short code) {
		return departmentMap.get(code);
	}

	public short getDeptNum(String deptName) {
		return codeMap.get(deptName);
	}

	public static void main(String[] args) {
		for (String s : DepartmentUtils.getInstance().departmentMap.values()) {
			System.out.println(s);
		}
	}
}
