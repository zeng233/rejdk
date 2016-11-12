package mytest.java.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MyLogManager {
	// 初始化LogManager
	static {
		// 读取配置文件
		ClassLoader cl = MyLogManager.class.getClassLoader();
		InputStream inputStream = null;
		if (cl != null) {
			inputStream = cl.getResourceAsStream("log.properties");
		} else {
			inputStream = ClassLoader.getSystemResourceAsStream("log.properties");
		}
		LogManager logManager = LogManager.getLogManager();
		try {
			// 重新初始化日志属性并重新读取日志配置。
			logManager.readConfiguration(inputStream);
		} catch (SecurityException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/**
	 * 获取日志对象
	 * 
	 * @param clazz
	 * @return
	 */
	public static Logger getLogger(Class clazz) {
		Logger logger = Logger.getLogger(clazz.getName());
		return logger;
	}
	
	public static void main(String[] args) {
		MyLogManager.getLogger(MyLogManager.class).log(Level.INFO, "test");
	}
}
