//
//  IXMLNode.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/3/16.
//
//

#ifndef IXMLNode_hpp
#define IXMLNode_hpp

#include <string>
#include <vector>

class IXMLNode{
public:
  virtual ~IXMLNode(){}
  
  virtual std::vector<IXMLNode*> evaluateXPathAsXMLNodes(const std::string& xpath) = 0;
  
  virtual std::string* getTextContent() = 0;
  
  virtual std::string* getAttribute(const std::string& attributeName) = 0;
  
};

#endif /* IXMLNode_hpp */
