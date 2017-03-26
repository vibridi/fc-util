package com.vibridi.fcutil.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.vibridi.fcutil.Main;

public enum AppContext {
	instance;
	
	public static final String VERSION_NUMBER = Main.class.getPackage().getImplementationVersion();
	public static final String NOT_ASSIGNED = "NonAssegnati.xlsx";
	public static final String CONTENDED = "GiocatoriContesi.xlsx";
	
	private Properties props;
	
	private AppContext() {
		props = new Properties();
		InputStream in = Main.class.getResourceAsStream("context.properties");
		try {
			props.load(in);
		} catch(IOException e) {
			throw new RuntimeException("Cannot load properties");
		}
	}
	
	public String getString(String key) {
		return props.getProperty(key);
	}
	
	public int getInt(String key) {
		return Integer.parseInt(props.getProperty(key));
	}
	
	public long getLong(String key) {
		return Long.parseLong(props.getProperty(key));
	}
	
	public double getDouble(String key) {
		return Double.parseDouble(props.getProperty(key));
	}
}
