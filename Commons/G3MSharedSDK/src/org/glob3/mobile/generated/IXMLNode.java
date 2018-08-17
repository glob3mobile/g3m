package org.glob3.mobile.generated;//
//  IXMLNode.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/3/16.
//
//

//
//  IXMLNode.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/3/16.
//
//



public abstract class IXMLNode
{
  public void dispose()
  {
  }

  public abstract java.util.ArrayList<IXMLNode> evaluateXPathAsXMLNodes(String xpath);

  public abstract String getTextContent();

  public abstract String getAttribute(String attributeName);

}
