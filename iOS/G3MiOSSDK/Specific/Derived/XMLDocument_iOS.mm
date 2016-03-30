//
//  XMLDocument_iOS.mm
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/3/16.
//
//

#include "XMLDocument_iOS.hpp"

#include "ErrorHandling.hpp"
#include "ILogger.hpp"

XMLDocument_iOS::XMLDocument_iOS(const std::string& xmlTextForRootNode){
  
  _doc = xmlParseMemory(xmlTextForRootNode.c_str(), (int) xmlTextForRootNode.size());
  _docOwner = true;
  _node = _doc->next;
  
  
  if (_doc == NULL) {
    THROW_EXCEPTION("Error processing XML.");
  }
  
  _xpathCtx = xmlXPathNewContext(_doc);
  if(_xpathCtx == NULL) {
    xmlFreeDoc(_doc);
    THROW_EXCEPTION("Error: unable to create new XPath context\n");
  }
  
}

XMLDocument_iOS::XMLDocument_iOS(const xmlDocPtr doc, const xmlNodePtr node, xmlXPathContextPtr xpathCtx){
  _doc = doc;
  _docOwner = false;
  _xpathCtx = xpathCtx;
  _node = node;
}

std::vector<double>* XMLDocument_iOS::evaluateXPathAndGetTextContentAsNumberArray(const std::string& xpath,
                                                                                 const std::string& separator){
  
  
}

std::vector<double>* XMLDocument_iOS::getTextContentAsNumberArray(const std::string& separator){
  
}

std::string* XMLDocument_iOS::evaluateXPathAndGetTextContentAsText(const std::string& xpath){
  xmlXPathObjectPtr xpathObj = executeXPath(xpath);
  if (xpathObj == NULL){
    return NULL;
  }
  if (xpathObj->nodesetval->nodeNr != 1){
    return NULL;
  }
  xmlNodePtr node = xpathObj->nodesetval->nodeTab[0];
  if (node->type != XML_TEXT_NODE){
    return NULL;
  }
    
  xmlXPathFreeObject(xpathObj);
  return NULL;
}

std::string* XMLDocument_iOS::getTextContent(){
  
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

int XMLDocument_iOS::evaluateXPathAndGetTextContentAsInteger(const std::string& xpath){
  
  
  
}

double XMLDocument_iOS::evaluateXPathAndGetNumberValueAsDouble(const std::string& xpath){
  
}

xmlXPathObjectPtr XMLDocument_iOS::executeXPath(const std::string& xpath){
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

std::vector<IXMLDocument*> XMLDocument_iOS::evaluateXPathAsXMLDocuments(const std::string& xpath){
  
  xmlXPathObjectPtr xpathObj = executeXPath(xpath);
  
  std::vector<IXMLDocument*> docs;
  
  ILogger::instance()->logInfo("Found %d items", xpathObj->nodesetval->nodeNr);
  
  for (int i = 0; i < xpathObj->nodesetval->nodeNr; i++) {
    xmlNodePtr node = xpathObj->nodesetval->nodeTab[i];
    XMLDocument_iOS* newDoc = new XMLDocument_iOS(_doc, node, _xpathCtx);
    docs.push_back(newDoc);
  }
  
  xmlXPathFreeObject(xpathObj);
  
  return docs;
}
