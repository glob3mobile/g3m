//
//  XMLDocument_iOS.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/3/16.
//
//

#ifndef XMLDocument_iOS_hpp
#define XMLDocument_iOS_hpp

#include "IXMLDocument.hpp"

//MUST INCLUDE LIBXML2.2.TDB IN PROJECT
//REMEMBER TO ADD TO G3MIOSSDK PROYECT $(SDKROOT)/usr/include/libxml2 TO HEADER SEARCH PATH

#import <libxml/tree.h>
#import <libxml/parser.h>
#import <libxml/HTMLparser.h>
#import <libxml/xpath.h>
#import <libxml/xpathInternals.h>

class XMLDocument_iOS: public IXMLDocument{
  
  xmlDocPtr _doc;
  bool _docOwner;
  xmlXPathContextPtr _xpathCtx;
  xmlNodePtr _node;
  
  xmlXPathObjectPtr executeXPath(const std::string& xpath);
  
public:
  XMLDocument_iOS(const std::string& xmlTextForRootNode);
  
  XMLDocument_iOS(const xmlDocPtr doc, const xmlNodePtr node, xmlXPathContextPtr xpathCtx);
  
  ~XMLDocument_iOS(){
    if (_docOwner){
      xmlFreeDoc(_doc);
      xmlXPathFreeContext(_xpathCtx);
    }
  }
  
  std::vector<double>* evaluateXPathAndGetTextContentAsNumberArray(const std::string& xpath,
                                                                  const std::string& separator);
  
  std::string* getTextContent();
  
  std::vector<double>* getTextContentAsNumberArray(const std::string& separator);
  
  std::string* evaluateXPathAndGetTextContentAsText(const std::string& xpath);
  
  int evaluateXPathAndGetTextContentAsInteger(const std::string& xpath);
  
  double evaluateXPathAndGetNumberValueAsDouble(const std::string& xpath);
  
  std::vector<IXMLDocument*> evaluateXPathAsXMLDocuments(const std::string& xpath);
  
};

#endif /* XMLDocument_iOS_hpp */
