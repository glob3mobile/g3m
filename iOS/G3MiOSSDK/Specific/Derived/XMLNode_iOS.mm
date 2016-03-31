//
//  XMLNode_iOS.mm
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/3/16.
//
//

#include "XMLNode_iOS.hpp"

#include "ErrorHandling.hpp"
#include "ILogger.hpp"

XMLNode_iOS::XMLNode_iOS(const std::string& xmlTextForRootNode){
  
  _doc = xmlParseMemory(xmlTextForRootNode.c_str(), (int) xmlTextForRootNode.size());
  if (_doc == NULL) {
    THROW_EXCEPTION("Error processing XML.");
  }
  
  _docOwner = true;
  _node = _doc->next;
  
  _xpathCtx = xmlXPathNewContext(_doc);
  if(_xpathCtx == NULL) {
    xmlFreeDoc(_doc);
    THROW_EXCEPTION("Error: unable to create new XPath context\n");
  }
  
}

XMLNode_iOS::XMLNode_iOS(const xmlDocPtr doc, const xmlNodePtr node, xmlXPathContextPtr xpathCtx){
  _doc = doc;
  _docOwner = false;
  _xpathCtx = xpathCtx;
  _node = node;
}

std::string* XMLNode_iOS::getTextContent(){
  
  xmlNodePtr node = _node->children;
  while (node != NULL) {
    if (node->type == XML_TEXT_NODE)
    {
      return new std::string((char*)node->content);
    }
    node = node->next;
  }
  
  return NULL;
}

std::string* XMLNode_iOS::getAttribute(const std::string& attributeName){
  
  xmlAttrPtr att = _node->properties;
  while (att != NULL){
    if (attributeName.compare((char*)att->name) == 0){
      if (att->children[0].type == XML_TEXT_NODE){
        return new std::string((char*) att->children[0].content);
      }
    }
    att = att->next;
  }
  return NULL;
}

xmlXPathObjectPtr XMLNode_iOS::executeXPath(const std::string& xpath){
  xmlChar xPathExpr[200];
  strcpy((char*) xPathExpr, xpath.c_str());
  
  
  //NO NAMESPACES ARE PREDECLARED ON XPATH
  //  xmlXPathRegisterNs(_xpathCtx, BAD_CAST " ", BAD_CAST "http://www.opengis.net/citygml/2.0");
  //  xmlXPathRegisterNs(_xpathCtx, BAD_CAST "bldg", BAD_CAST "http://www.opengis.net/citygml/building/2.0");
  
  if (_node != NULL){
    _xpathCtx->node = _node;
  }
  
  xmlXPathObjectPtr xpathObj = xmlXPathEvalExpression(xPathExpr, _xpathCtx);
  if(xpathObj == NULL) {
    ILogger::instance()->logError("Error: unable to evaluate xpath expression \"%s\"\n", xpath.c_str());
    return NULL;
  }
  return xpathObj;
}

std::vector<IXMLNode*> XMLNode_iOS::evaluateXPathAsXMLNodes(const std::string& xpath){
  
  xmlXPathObjectPtr xpathObj = executeXPath(xpath);
  
  std::vector<IXMLNode*> docs;
  
//  ILogger::instance()->logInfo("Found %d items", xpathObj->nodesetval->nodeNr);
  
  for (int i = 0; i < xpathObj->nodesetval->nodeNr; i++) {
    xmlNodePtr node = xpathObj->nodesetval->nodeTab[i];
    XMLNode_iOS* newDoc = new XMLNode_iOS(_doc, node, _xpathCtx);
    docs.push_back(newDoc);
  }
  
  xmlXPathFreeObject(xpathObj);
  
  return docs;
}
