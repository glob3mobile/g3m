

package org.glob3.mobile.client;

import java.util.ArrayList;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayNumber;


public class XPathResult {

   private final JavaScriptObject _result;


   public XPathResult(final JavaScriptObject jso) {
      _result = jso;
   }


   public native double getAsNumber() /*-{
		return this.@org.glob3.mobile.client.XPathResult::_result.numberValue;
   }-*/;


   public native String getTextContentAsText() /*-{
		var r = this.@org.glob3.mobile.client.XPathResult::_result;
		var thisNode = r.iterateNext();
		return thisNode.textContent;
   }-*/;


   public native int getTextContentAsInteger() /*-{
		var r = this.@org.glob3.mobile.client.XPathResult::_result;
		var thisNode = r.iterateNext();
		return thisNode.textContent;
   }-*/;


   public native boolean getAsBoolean() /*-{
		return this.@org.glob3.mobile.client.XPathResult::_result.booleanValue;
   }-*/;


   public ArrayList<Double> getTextContentAsNumberArray(final String separator) {
      final JsArrayNumber a = jsGetTextContentAsNumberArray(separator);
      final ArrayList<Double> res = new ArrayList<Double>();
      for (int i = 0; i < a.length(); i++) {
         res.add(a.get(i));
      }
      return res;
   }


   public native JsArrayNumber jsGetTextContentAsNumberArray(String separator) /*-{
		var r = this.@org.glob3.mobile.client.XPathResult::_result
				.iterateNext();

		var ts = r.textContent.split(separator);

		var n = [];
		for (i = 0; i < ts.length; i++) {
			n.push(parseFloat(ts[i]));
		}

		return n;

   }-*/;


   public ArrayList<XMLDocument> getAsXMLDocuments() {
      final JsArray array = jsGetAsXMLDocuments();
      //      ILogger.instance().logInfo("SIZE %d", array.length());

      final ArrayList<XMLDocument> result = new ArrayList<XMLDocument>();

      for (int i = 0; i < array.length(); i++) {
         final XMLDocument xml = new XMLDocument(array.get(i));
         result.add(xml);
      }
      return result;
   }


   public native JsArray jsGetAsXMLDocuments() /*-{
		var r = this.@org.glob3.mobile.client.XPathResult::_result;

		//debugger;
		//console.log(r);

		var array = [];

		try {
			var thisNode = r.iterateNext();

			while (thisNode) {
				array.push(thisNode);
				thisNode = r.iterateNext();
			}
		} catch (e) {
			console.log('Error: Document tree modified during iteration ' + e);
		}

		return array;
   }-*/;

}
