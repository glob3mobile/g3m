//
//  XMLNode_iOS.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/3/16.
//
//

#ifndef XMLNode_iOS_hpp
#define XMLNode_iOS_hpp

#include "IXMLNode.hpp"

//MUST INCLUDE LIBXML2.2.TDB IN PROJECT
//REMEMBER TO ADD TO G3MIOSSDK PROYECT $(SDKROOT)/usr/include/libxml2 TO HEADER SEARCH PATH

#import <libxml/tree.h>
#import <libxml/parser.h>
#import <libxml/HTMLparser.h>
#import <libxml/xpath.h>
#import <libxml/xpathInternals.h>

class XMLNode_iOS: public IXMLNode{
  
  xmlDocPtr _doc;
  bool _docOwner;
  xmlXPathContextPtr _xpathCtx;
  xmlNodePtr _node;
  
  xmlXPathObjectPtr executeXPath(const std::string& xpath);
  
public:
  XMLNode_iOS(const std::string& xmlTextForRootNode);
  
  XMLNode_iOS(const xmlDocPtr doc, const xmlNodePtr node, xmlXPathContextPtr xpathCtx);
  
  ~XMLNode_iOS(){
    if (_docOwner){
      xmlFreeDoc(_doc);
      xmlXPathFreeContext(_xpathCtx);
    }
    //xmlFreeNode(_node);
  }
  
  std::string* getAttribute(const std::string& attributeName);
  
  std::string* getTextContent();
  
  std::vector<IXMLNode*> evaluateXPathAsXMLNodes(const std::string& xpath);
  
};

#endif /* XMLNode_iOS_hpp */
