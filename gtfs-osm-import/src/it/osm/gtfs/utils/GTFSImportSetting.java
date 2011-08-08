package it.osm.gtfs.utils;

import it.osm.gtfs.plugins.DefaultPlugin;
import it.osm.gtfs.plugins.GTFSPlugin;

import java.io.File;
import java.util.Properties;

public class GTFSImportSetting {
	public static final String GTFS_STOP_FILE_NAME = "stops.txt";
	public static final String GTFS_STOP_TIME_FILE_NAME = "stop_times.txt";
	public static final String GTFS_ROUTES_FILE_NAME = "routes.txt";
	public static final String GTFS_SHAPES_FILE_NAME = "shapes.txt";
	public static final String GTFS_TRIPS_FILE_NAME = "trips.txt";
	public static final String OSM_RELATIONS_FILE_NAME = "relations.osm";
	public static final String OSM_STOP_FILE_NAME = "stops.osm";
	public static final String OUTPUT_PARED_WITHOUT_GTFS = "gtfs_import_pared_without_gtfsid.osm";
	public static final String OUTPUT_UNPARED_IN_GTFS = "gtfs_import_unpared_in_gtfs.osm";

	private final Properties properties;

	private GTFSImportSetting() {
		properties = new Properties();
		try {
			properties.load(getClass().getClassLoader().getResourceAsStream("gtfs-import.properties"));
		} catch (Exception e) {
			throw new IllegalArgumentException("An error occurred while reading setting: " + e.getMessage());
		}
	}

	private static class SettingHolder { 
		private final static GTFSImportSetting INSTANCE = new GTFSImportSetting();
	}

	public static GTFSImportSetting getInstance() {
		return SettingHolder.INSTANCE;
	}

	private String gtfsPath = null;
	public String getGTFSPath() {
		if (gtfsPath == null){
			synchronized (this) {
				gtfsPath = properties.getProperty("gtfs-path");
				if (gtfsPath == null)
					throw new IllegalArgumentException("Please set a valid gtfs-path.");
				if (!gtfsPath.endsWith(File.separator))
					gtfsPath = gtfsPath + File.separator;
				if (!new File(gtfsPath).isDirectory())
					throw new IllegalArgumentException("Please set a valid gtfs-path.");
			}
		}
		return gtfsPath;
	}

	private String osmPath = null;
	public String getOSMPath() {
		if (osmPath == null){
			synchronized (this) {
				osmPath = properties.getProperty("osm-path");
				if (osmPath == null)
					throw new IllegalArgumentException("Please set a valid osm-path.");
				if (!osmPath.endsWith(File.separator))
					osmPath = osmPath + File.separator;
				if (!new File(osmPath).isDirectory())
					throw new IllegalArgumentException("Please set a valid osm-path.");
			}
		}
		return osmPath;
	}

	private String outputPath = null;
	public String getOutputPath() {
		if (outputPath == null){
			synchronized (this) {
				outputPath = properties.getProperty("output-path");
				if (outputPath == null)
					throw new IllegalArgumentException("Please set a valid output-path.");
				if (!outputPath.endsWith(File.separator))
					outputPath = outputPath + File.separator;
				if (!new File(outputPath).isDirectory())
					throw new IllegalArgumentException("Please set a valid output-path.");
			}
		}
		return outputPath;
	}
	
	private String osmosisPath = null;
	public String getOsmosisPath() {
		if (osmosisPath == null){
			synchronized (this) {
				osmosisPath = properties.getProperty("osmosis-path");
				if (osmosisPath == null)
					throw new IllegalArgumentException("Please set a valid osmosis-path.");
				if (!osmosisPath.endsWith(File.separator))
					osmosisPath = osmosisPath + File.separator;
				if (!new File(osmosisPath).isDirectory())
					throw new IllegalArgumentException("Please set a valid osmosis-path.");
			}
		}
		return osmosisPath;
	}

	private GTFSPlugin plugin = null;
	public GTFSPlugin getPlugin(){
		if (plugin == null){
			synchronized (this) {
				String pluginName = properties.getProperty("plugin");
				if (pluginName == null){
					plugin = new DefaultPlugin();
				}else{
					try{
						Class<?> pluginClass  = Class.forName(pluginName);
						boolean validPlugin = false;
						for (Class<?> c : pluginClass.getInterfaces()) {
					        if (c.equals(GTFSPlugin.class)) {
					            validPlugin = true;
					        }
					    }
						if (validPlugin)
							plugin = (GTFSPlugin) pluginClass.newInstance();
						else
							throw new IllegalArgumentException("The specified plugin is not found or not valid");
					}catch (Exception e) {
						throw new IllegalArgumentException("The specified plugin is not found or not valid");
					}
				}
			}
		}
		return plugin;
	}
	
	private String operator = null;
	public String getOperator() {
		if (operator == null){
			synchronized (this) {
				operator = properties.getProperty("operator");
				if (operator == null)
					throw new IllegalArgumentException("Please set a valid operator.");
			}
		}
		return operator;
	}
	
	private String network = null;
	public String getNetwork() {
		if (network == null){
			synchronized (this) {
				network = properties.getProperty("network");
				if (network == null)
					throw new IllegalArgumentException("Please set a valid network.");
			}
		}
		return network;
	}
	
	private String revisitedKey = null;
	public String getRevisitedKey() {
		if (revisitedKey == null){
			synchronized (this) {
				revisitedKey = properties.getProperty("revisitedkey");
				if (revisitedKey == null)
					throw new IllegalArgumentException("Please set a valid operator.");
			}
		}
		return revisitedKey;
	}
	
	
}
