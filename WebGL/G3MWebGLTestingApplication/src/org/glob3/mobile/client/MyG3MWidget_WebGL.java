package org.glob3.mobile.client;

import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.specific.G3MWidget_WebGL;


public class MyG3MWidget_WebGL extends G3MWidget_WebGL {
	
	private MarksRenderer _marksRenderer;
	private String _urlString = "";

	   public MyG3MWidget_WebGL(final MarksRenderer marksRenderer) {
		   super();
		   _marksRenderer = marksRenderer;
		   exportMyJSFunctions();
	   }
		   
	   private native void exportMyJSFunctions() /*-{
			var that = this;
			if (!$wnd.G3M) {
				$wnd.G3M = {};
			}

			// here we set all my function headings
			$wnd.G3M.doble = $entry(function(numero) {
				return that.@org.glob3.mobile.client.MyG3MWidget_WebGL::doble(I)(numero);
			});
	   }-*/;

	   private int doble(final int numero) {
	      return 2*numero;
	   }
	   
}
