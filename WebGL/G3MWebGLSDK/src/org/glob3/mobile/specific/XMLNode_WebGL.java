

package org.glob3.mobile.specific;

import java.util.ArrayList;

import org.glob3.mobile.generated.IXMLNode;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;


public class XMLNode_WebGL
extends
IXMLNode {

   private final JavaScriptObject _doc;
   private final JavaScriptObject _resolver;
   private final JavaScriptObject _node;


   public XMLNode_WebGL(final String doc) {
      _doc = jsParse(doc);
      _resolver = null;
      _node = jsGetNode();
   }


   private native JavaScriptObject jsParse(final String doc) /*-{
		var xml = (new window.DOMParser()).parseFromString(doc, "text/xml");
		return xml;
   }-*/;


   private native JavaScriptObject jsGetNode() /*-{
		var xml = this.@org.glob3.mobile.specific.XMLNode_WebGL::_doc;
		return xml.childNodes[0];
   }-*/;


   public XMLNode_WebGL(final JavaScriptObject doc,
                        final JavaScriptObject resolver,
                        final JavaScriptObject node) {
      _doc = doc;
      _resolver = resolver;
      _node = node;
   }


   @Override
   public ArrayList<IXMLNode> evaluateXPathAsXMLNodes(final String xpath) {
      final JavaScriptObject res = xpathToJSO(xpath);
      final JsArray<JavaScriptObject> a = jsGetAsNodes(res);

      final ArrayList<IXMLNode> out = new ArrayList<IXMLNode>();
      for (int i = 0; i < a.length(); i++) {
         out.add(new XMLNode_WebGL(_doc, jsGetResolver(), a.get(i)));
      }
      return out;
   }


   public native JavaScriptObject xpathToJSO(final String xpath) /*-{

		var xml = this.@org.glob3.mobile.specific.XMLNode_WebGL::_doc;
		var node = this.@org.glob3.mobile.specific.XMLNode_WebGL::_node;
		var nsResolver = this.@org.glob3.mobile.specific.XMLNode_WebGL::jsGetResolver()();

		//REMEMBER TO ERASE XMLNS= ATTRIBUTE!!!!!!!!!!!!
		try {
			var xpathResult = xml.evaluate(xpath, node, nsResolver,
					XPathResult.ANY_TYPE, null);
		} catch (e) {
			debugger;
			console.log(e);
		}

		return xpathResult;
   }-*/;


   public native JsArray<JavaScriptObject> jsGetAsNodes(JavaScriptObject xpathResult) /*-{
		var thisNode = xpathResult.iterateNext();
		var array = [];
		while (thisNode != null) {
			array.push(thisNode);
			thisNode = xpathResult.iterateNext();
		}

		//		for (i = 0; i < array.length; i++) {
		////			var doc = document.implementation.createDocument('', '');
		////			doc.appendChild(array[i]);
		////			array[i] = doc;
		//                        array[i] = 
		//		}

		return array;
   }-*/;


   private native JavaScriptObject jsGetResolver() /*-{
		if (this.@org.glob3.mobile.specific.XMLNode_WebGL::_resolver == null) {

			var xml = this.@org.glob3.mobile.specific.XMLNode_WebGL::_doc;
			var docNSResolver = xml.createNSResolver(xml.documentElement);
			var defaultNS = xml.documentElement.getAttribute('xmlns');

			console.log('DNS: ' + defaultNS);

			this.@org.glob3.mobile.specific.XMLNode_WebGL::_resolver =

			function nsResolver(prefix) {
				var uri = docNSResolver.lookupNamespaceURI(prefix);
				if (uri != null) {
					return uri;
				}

				console.log('Unrecognized NS ' + prefix + '-> '
						+ docNSResolver.lookupNamespaceURI(prefix));

				return defaultNS;
			};

		}

		return this.@org.glob3.mobile.specific.XMLNode_WebGL::_resolver;
   }-*/;


   @Override
   public native String getTextContent() /*-{
		return this.@org.glob3.mobile.specific.XMLNode_WebGL::_node.textContent
   }-*/;


   @Override
   public native String getAttribute(final String attributeName)/*-{

		var node = this.@org.glob3.mobile.specific.XMLNode_WebGL::_node;

		//Checking sufix
		String.prototype.endsWith = function(suffix) {
			return this.indexOf(suffix, this.length - suffix.length) !== -1;
		};

		for (i = 0; i < node.attributes.length; i++) {
			var name = node.attributes[i].name;
			if (name.endsWith(":" + attributeName) || name == attributeName) {
				return node.attributes[i].value;
			}
		}

		return null;
   }-*/;

}
