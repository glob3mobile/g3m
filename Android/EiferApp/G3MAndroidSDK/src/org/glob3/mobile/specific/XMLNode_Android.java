

package org.glob3.mobile.specific;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.glob3.mobile.generated.IXMLNode;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class XMLNode_Android
extends
IXMLNode {

   //   String        _xmlText;
   Element _node;


   public XMLNode_Android(final String xmlText) {
      //      _xmlText = xmlText;
      try {
         _node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(xmlText.getBytes())).getDocumentElement();
      }
      catch (final Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         _node = null;
      }
   }


   public XMLNode_Android(final Element node) {
      _node = node;
   }


   @Override
   public ArrayList<IXMLNode> evaluateXPathAsXMLNodes(final String xpath) {
      // TODO Auto-generated method stub

      final XPathFactory factory = XPathFactory.newInstance();
      final XPath xPath = factory.newXPath();
      try {
         final NodeList list = (NodeList) xPath.evaluate(xpath, _node, XPathConstants.NODESET);

         final ArrayList<IXMLNode> nodeList = new ArrayList<IXMLNode>();

         for (int i = 0; i < list.getLength(); i++) {
            final Node node = list.item(i);
            nodeList.add(new XMLNode_Android((Element) node));
         }
         return nodeList;
      }
      catch (final XPathExpressionException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }


      return null;
   }


   @Override
   public String getTextContent() {
      return _node.getTextContent();
   }


   @Override
   public String getAttribute(final String attributeName) {
      final NamedNodeMap nnm = _node.getAttributes();
      for (int i = 0; i < nnm.getLength(); i++) {
         final String name = nnm.item(i).getNodeName();
         if ((name == attributeName) || name.endsWith(":" + attributeName)) {
            return nnm.item(i).getNodeValue();
         }
      }

      return null;
   }

}
