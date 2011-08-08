package it.osm.gtfs.model;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import it.osm.gtfs.output.IElementCreator;
import it.osm.gtfs.utils.GTFSImportSetting;
import it.osm.gtfs.utils.OSMDistanceUtils;
import it.osm.gtfs.utils.OSMXMLUtils;

public class Stop {
	private String gtfsId;
	private String code;
	private Double lat;
	private Double lon;
	private String name;
	private Boolean isRailway;
	public Stop paredWith;
	public Node originalXMLNode;
	
	public Stop(String gtfsId, String code, Double lat, Double lon, String name) {
		super();
		this.gtfsId = gtfsId;
		this.code = code;
		this.lat = lat;
		this.lon = lon;
		this.name = name;
	}
	
	public String getGtfsId() {
		return gtfsId;
	}
	public String getCode() {
		return code;
	}
	public Double getLat() {
		return lat;
	}
	public Double getLon() {
		return lon;
	}
	public String getName() {
		return name;
	}
	public Boolean isRailway(){
		return isRailway;
	}
	public String getOSMId(){
		return (originalXMLNode == null) ? null : originalXMLNode.getAttributes().getNamedItem("id").getNodeValue();
	}
	

	public void setIsRailway(Boolean isRailway){
		this.isRailway = isRailway;
	}
	public void setGtfsId(String gtfsId) {
		this.gtfsId = gtfsId;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Stop [gtfsId=" + gtfsId + ", code=" + code + ", lat=" + lat
				+ ", lon=" + lon + ", name=" + name + 
				((originalXMLNode != null) ? ", osmid=" + getOSMId() : "" )
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gtfsId == null) ? 0 : gtfsId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stop other = (Stop) obj;
		if (gtfsId == null) {
			return false;
		} else if (!gtfsId.equals(other.gtfsId))
			return false;
		return true;
	}

	public boolean seams(Stop os) {
		if (os.getCode() != null && os.getCode().equals(getCode()))
			//if less than 200m far away or already linked with gtfsid
			if (OSMDistanceUtils.distVincenty(getLat(), getLon(), os.getLat(), os.getLon()) < 50 || 
					(os.getGtfsId() != null && getGtfsId() != null && os.getGtfsId().equals(getGtfsId())))
				return true;
			else if (OSMDistanceUtils.distVincenty(getLat(), getLon(), os.getLat(), os.getLon()) < 10000)
				System.err.println("Warning: Same ref with dist > 50 m (and less than 10km) [" + this + " -> " + os +  "]");
		else
			//if less than 5m far away and both don't have gtfsid or have the same (should never happen)
			if (OSMDistanceUtils.distVincenty(getLat(), getLon(), os.getLat(), os.getLon()) < 5 && 
					((os.getGtfsId() == null && getGtfsId() == null) || 
					 (os.getGtfsId() != null && getGtfsId() != null && os.getGtfsId().equals(getGtfsId()))))
				return true;
		return false;
	}
	
	public Element getNewXMLNode(IElementCreator document){
		Element node = document.createElement("node");
		node.setAttribute("id", "-" + getGtfsId());
		node.setAttribute("visible", "true");
		node.setAttribute("lat", getLat().toString());
		node.setAttribute("lon", getLon().toString());
		node.appendChild(OSMXMLUtils.createTagElement(document, "highway", "bus_stop"));
		node.appendChild(OSMXMLUtils.createTagElement(document, "operator", GTFSImportSetting.getInstance().getOperator()));
		node.appendChild(OSMXMLUtils.createTagElement(document, GTFSImportSetting.getInstance().getRevisitedKey(), "no"));
		node.appendChild(OSMXMLUtils.createTagElement(document, "shelter", "unknown"));
		node.appendChild(OSMXMLUtils.createTagElement(document, "bench", "unknown"));
		node.appendChild(OSMXMLUtils.createTagElement(document, "tactile_paving", "unknown"));
		node.appendChild(OSMXMLUtils.createTagElement(document, "name", GTFSImportSetting.getInstance().getPlugin().fixBusStopName(getName())));
		node.appendChild(OSMXMLUtils.createTagElement(document, "ref", getCode()));
		node.appendChild(OSMXMLUtils.createTagElement(document, "gtfs_id", getGtfsId()));
		return node;
	}
	
	
	public static class GTFSStop extends Stop{
		public Stop paredWithRailWay;
		
		public GTFSStop(String gtfsId, String code, Double lat, Double lon, String name) {
			super(gtfsId, code, lat, lon, name);
		}
		
	}
}
