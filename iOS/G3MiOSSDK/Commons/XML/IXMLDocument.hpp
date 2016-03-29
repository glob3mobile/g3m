//
//  IXMLDocument.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/3/16.
//
//

#ifndef IXMLDocument_hpp
#define IXMLDocument_hpp

#include <string>
#include <vector>

class IXMLDocument{
public:
  virtual ~IXMLDocument(){}
  
  virtual std::vector<double> evaluateXPathAndGetTextContentAsNumberArray(const std::string& xpath,
                                                                          const std::string& separator) = 0;
  
  virtual std::vector<double> getTextContentAsNumberArray(const std::string& separator) = 0;
  
  virtual std::string evaluateXPathAndGetTextContentAsText(const std::string& xpath) = 0;
  
  virtual int evaluateXPathAndGetTextContentAsInteger(const std::string& xpath) = 0;
  
  virtual double evaluateXPathAndGetNumberValueAsDouble(const std::string& xpath) = 0;
  
  virtual std::vector<IXMLDocument*> evaluateXPathAsXMLDocuments(const std::string& xpath) = 0;
  
};

#endif /* IXMLDocument_hpp */
