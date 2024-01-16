package net.argus.prompteur.net;

import java.util.ArrayList;
import java.util.List;

import net.argus.cjson.CJSONBuilder;
import net.argus.cjson.value.CJSONArray;
import net.argus.cjson.value.CJSONInteger;
import net.argus.cjson.value.CJSONObject;
import net.argus.cjson.value.CJSONString;
import net.argus.cjson.value.CJSONValue;
import net.argus.prompteur.Page;

public class PackagePrefab {
	
	public static String getConnectionPackage(List<Page> pages, int offY) {
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
		
		return builder.getMainObject().toString();
	}

}
