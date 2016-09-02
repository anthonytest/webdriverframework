package com.webdriver.baselibs;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * @author Anthony
 *
 */
public class MyLog {

	public static Logger qaLog = Logger.getLogger("测试日志");

	public static Logger getLogger() {
		final Throwable t = new Throwable();
		final StackTraceElement methodCaller = t.getStackTrace()[1];
		final Logger logger = Logger.getLogger(methodCaller.getClassName());
		return logger;
	}

	public static void main(String[] args) throws IOException, SQLException {
		BasicConfigurator.configure();
		System.out.println("test");
		qaLog.trace("Trace Message!");
		qaLog.debug("Debug Message!");
		qaLog.info("Info Message!");
		qaLog.warn("Warn Message!");
		qaLog.error("Error Message!");
		qaLog.fatal("Fatal Message!");
	}
}
