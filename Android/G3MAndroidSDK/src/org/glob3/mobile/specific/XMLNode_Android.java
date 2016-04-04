

package org.glob3.mobile.specific;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.glob3.mobile.generated.IXMLNode;
import org.xml.sax.InputSource;


public class XMLNode_Android
extends
IXMLNode {

   String _xmlText;


   public XMLNode_Android(final String xmlText) {
      _xmlText = xmlText;
   }


   @Override
   public ArrayList<IXMLNode> evaluateXPathAsXMLNodes(final String xpath) {
      // TODO Auto-generated method stub

      final XPathFactory factory = XPathFactory.newInstance();
      final XPath xPath = factory.newXPath();
      try {
         xPath.evaluate(xpath, new InputSource(new StringReader(_xmlText)));
      }
      catch (final XPathExpressionException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }


      return null;
   }


   @Override
   public String getTextContent() {
      // TODO Auto-generated method stub
      return null;
   }


   @Override
   public String getAttribute(final String attributeName) {
      // TODO Auto-generated method stub
      return null;
   }

}
