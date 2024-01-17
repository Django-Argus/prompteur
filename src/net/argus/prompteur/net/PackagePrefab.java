package net.argus.prompteur.net;

import java.util.ArrayList;
import java.util.List;

import net.argus.cjson.CJSONBuilder;
import net.argus.cjson.value.CJSONArray;
import net.argus.cjson.value.CJSONBoolean;
import net.argus.cjson.value.CJSONInteger;
import net.argus.cjson.value.CJSONObject;
import net.argus.cjson.value.CJSONString;
import net.argus.cjson.value.CJSONValue;
import net.argus.prompteur.Page;
import net.argus.prompteur.net.event.EventNetworkSystem;

public class PackagePrefab {
	
	public static String getConnectionPackage(List<Page> pages, int offY, boolean playing, int speed, int direction) {
		CJSONBuilder builder = new CJSONBuilder();
		
		List<CJSONValue> vals = new ArrayList<CJSONValue>();
		
		for(Page page : pages) {
			CJSONObject obj = new CJSONObject();
			
			obj.addItem("name", new CJSONString(page.getName()));
			obj.addItem("text", new CJSONString(page.getText()));
			vals.add(obj);
		}
		
		CJSONArray array = new CJSONArray(vals);
		
		builder.addValue(".", "pages", array);
		builder.addValue(".", "offy", new CJSONInteger(offY));
		builder.addValue(".", "speed", new CJSONInteger(speed));
		builder.addValue(".", "playing", new CJSONBoolean(playing));
		builder.addValue(".", "direction", new CJSONInteger(direction));
		
		return builder.getMainObject().toString();
	}
	
	public static String getChangeDirectionPackage(int direction) {
		CJSONBuilder builder = new CJSONBuilder();
				
		builder.addValue(".", "type", new CJSONInteger(EventNetworkSystem.CHANGE_DIRECTION));
		builder.addValue(".", "direction", new CJSONInteger(direction));
		
		return builder.getMainObject().toString();
	}
	
	public static String getStartStopPackage(boolean play) {
		CJSONBuilder builder = new CJSONBuilder();
				
		builder.addValue(".", "type", new CJSONInteger(EventNetworkSystem.START_STOP));
		builder.addValue(".", "play", new CJSONBoolean(play));
		
		return builder.getMainObject().toString();
	}
	
	public static String getResetPackage() {
		CJSONBuilder builder = new CJSONBuilder();
				
		builder.addValue(".", "type", new CJSONInteger(EventNetworkSystem.RESET));
		
		return builder.getMainObject().toString();
	}
	
	public static String getResetSpeedPackage() {
		CJSONBuilder builder = new CJSONBuilder();
				
		builder.addValue(".", "type", new CJSONInteger(EventNetworkSystem.RESET_SPEED));
		
		return builder.getMainObject().toString();
	}
	
	public static String getChangeSpeedPackage(int speed) {
		CJSONBuilder builder = new CJSONBuilder();
				
		builder.addValue(".", "type", new CJSONInteger(EventNetworkSystem.CHANGE_SPEED));
		builder.addValue(".", "speed", new CJSONInteger(speed));
		
		return builder.getMainObject().toString();
	}
	
	
	@SuppressWarnings("deprecation")
	public static String getOffYPackage(int offY) {
		CJSONBuilder builder = new CJSONBuilder();
				
		builder.addValue(".", "type", new CJSONInteger(EventNetworkSystem.SEND_OFF_Y));
		builder.addValue(".", "offy", new CJSONInteger(offY));
		
		return builder.getMainObject().toString();
	}
}
