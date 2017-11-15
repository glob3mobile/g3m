package org.glob3.mobile.client;

import com.google.gwt.core.client.JsArray;

public class NativeUtils {
	
	  public static native void generateG3MCalls()/*-{
		if (!$wnd.G3M) {
			$wnd.G3M = {};
		}

		$wnd.G3M.activateBuildings = $entry(function(active) {
			@org.glob3.mobile.client.G3MWebGLTestingApplication::activateBuildings(Z)(active);
		});
		$wnd.G3M.activatePipes = $entry(function(active) {
			@org.glob3.mobile.client.G3MWebGLTestingApplication::activatePipes(Z)(active);
		});
		$wnd.G3M.setProximity = $entry(function(proximity) {
			@org.glob3.mobile.client.G3MWebGLTestingApplication::setProximity(D)(proximity);
		});
		$wnd.G3M.setAlphaMethod = $entry(function(method) {
			@org.glob3.mobile.client.G3MWebGLTestingApplication::setAlphaMethod(I)(method);
		});
		//$wnd.G3M.changeHole = $entry(function(method) {
		//	@org.glob3.mobile.client.G3MWebGLTestingApplication::changeHole()();
		//});
	  }-*/;
	
	  public static native void callNativeAlert(String alertMsg)/*-{
	  	alert(alertMsg);
	  }-*/;
	  
	  public static native void callNativeLog(String logMsg)/*-{
	  	console.log(logMsg);
	  }-*/;
	  
	  public static native void activateMenu()/*-{
	  	//Nothing done yet!
	  }-*/;
	  
	  public static native JsArray splitString(String s,String symbol)/*-{
	  	return s.split(symbol);
	  }-*/;
	  
	  public static native void assignHtmlToElementId (String s, String id)/*-{
	  	document.getElementById(id).innerHTML = s;
	  }-*/;
	  
	  public static native void changeDisplayStatus (String status, String id)/*-{
	  	var x = document.getElementById(id);
	  	x.style.display = status;
	  }-*/;
}
