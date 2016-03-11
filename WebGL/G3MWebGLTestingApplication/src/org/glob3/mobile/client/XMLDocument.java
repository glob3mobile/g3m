

package org.glob3.mobile.client;

import com.google.gwt.core.client.JavaScriptObject;


public class XMLDocument {
   private final JavaScriptObject _xml;


   public XMLDocument(final String doc) {
      _xml = jsParse(doc);
   }


   public XMLDocument(final JavaScriptObject xml) {
      _xml = xml;
   }


   private native JavaScriptObject jsParse(final String doc) /*-{
		var xml = (new window.DOMParser()).parseFromString(doc, "text/xml");
		return xml;
   }-*/;


   public XPathResult xpath(final String xpath) {
      return new XPathResult(xpathToJSO(xpath));
   }


   public native String getTextContent() /*-{
		return this.@org.glob3.mobile.client.XMLDocument::_xml.textContent;
   }-*/;


   public native JavaScriptObject xpathToJSO(final String xpath) /*-{

		var xml = this.@org.glob3.mobile.client.XMLDocument::_xml;

		//Resolver for CityGML
		function nsResolver(prefix) {
			switch (prefix) {
			case 'xhtml':
				return 'http://www.w3.org/1999/xhtml';
			case 'mathml':
				return 'http://www.w3.org/1998/Math/MathML';
			case 'gml':
				return 'http://www.opengis.net/gml';
			case 'bldg':
				return 'http://www.opengis.net/citygml/building/1.0';
			default:
				return 'http://www.opengis.net/citygml/1.0';
			}
		}

		//REMEMBER TO ERASE XMLNS= ATTRIBUTE!!!!!!!!!!!!
		try {
			var xpathResult = xml.evaluate(xpath, xml, nsResolver,
					XPathResult.ANY_TYPE, null);
		} catch (e) {
			debugger;
			console.log(e);
		}

		return xpathResult;
   }-*/;

}
