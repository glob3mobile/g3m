//
//  XMLDocument_iOS.mm
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/3/16.
//
//

#include "XMLDocument_iOS.hpp"

#include <G3MiOSSDK/ErrorHandling.hpp>

XMLDocument_iOS::XMLDocument_iOS(const std::string& xmlTextForRootNode){
  
  _doc = xmlParseMemory(xmlTextForRootNode.c_str(), xmlTextForRootNode.size());
  
  if (_doc == NULL) {
    THROW_EXCEPTION("Error processing XML.");
  }
  
  _xpathCtx = xmlXPathNewContext(_doc);
  if(_xpathCtx == NULL) {
    THROW_EXCEPTION("Error: unable to create new XPath context\n");
    xmlFreeDoc(_doc);
  }
  
}

std::vector<double> XMLDocument_iOS::evaluateXPathAndGetTextContentAsNumberArray(const std::string& xpath,
                                                                const std::string& separator){

  
}

std::vector<double> XMLDocument_iOS::getTextContentAsNumberArray(const std::string& separator){

}

std::string XMLDocument_iOS::evaluateXPathAndGetTextContentAsText(const std::string& xpath){

}

int XMLDocument_iOS::evaluateXPathAndGetTextContentAsInteger(const std::string& xpath){

}

double XMLDocument_iOS::evaluateXPathAndGetNumberValueAsDouble(const std::string& xpath){

}

std::vector<IXMLDocument*> XMLDocument_iOS::evaluateXPathAsXMLDocuments(const std::string& xpath){
  
  

}

///////
//
//#import <libxml/tree.h>
//#import <libxml/parser.h>
//#import <libxml/HTMLparser.h>
//#import <libxml/xpath.h>
//#import <libxml/xpathInternals.h>
//
//NSDictionary *DictionaryForNode(xmlNodePtr currentNode, NSMutableDictionary *parentResult)
//{
//  NSMutableDictionary *resultForNode = [NSMutableDictionary dictionary];
//  
//  if (currentNode->name)
//  {
//    NSString *currentNodeContent =
//    [NSString stringWithCString:(const char *)currentNode->name encoding:NSUTF8StringEncoding];
//    [resultForNode setObject:currentNodeContent forKey:@"nodeName"];
//  }
//  
//  if (currentNode->content && currentNode->type != XML_DOCUMENT_TYPE_NODE)
//  {
//    NSString *currentNodeContent =
//    [NSString stringWithCString:(const char *)currentNode->content encoding:NSUTF8StringEncoding];
//    
//    if ([[resultForNode objectForKey:@"nodeName"] isEqual:@"text"] && parentResult)
//    {
//      currentNodeContent = [currentNodeContent
//                            stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
//      
//      NSString *existingContent = [parentResult objectForKey:@"nodeContent"];
//      NSString *newContent;
//      if (existingContent)
//      {
//        newContent = [existingContent stringByAppendingString:currentNodeContent];
//      }
//      else
//      {
//        newContent = currentNodeContent;
//      }
//      
//      [parentResult setObject:newContent forKey:@"nodeContent"];
//      return nil;
//    }
//    
//    [resultForNode setObject:currentNodeContent forKey:@"nodeContent"];
//  }
//  
//  xmlAttr *attribute = currentNode->properties;
//  if (attribute)
//  {
//    NSMutableArray *attributeArray = [NSMutableArray array];
//    while (attribute)
//    {
//      NSMutableDictionary *attributeDictionary = [NSMutableDictionary dictionary];
//      NSString *attributeName =
//      [NSString stringWithCString:(const char *)attribute->name encoding:NSUTF8StringEncoding];
//      if (attributeName)
//      {
//        [attributeDictionary setObject:attributeName forKey:@"attributeName"];
//      }
//      
//      if (attribute->children)
//      {
//        NSDictionary *childDictionary = DictionaryForNode(attribute->children, attributeDictionary);
//        if (childDictionary)
//        {
//          [attributeDictionary setObject:childDictionary forKey:@"attributeContent"];
//        }
//      }
//      
//      if ([attributeDictionary count] > 0)
//      {
//        [attributeArray addObject:attributeDictionary];
//      }
//      attribute = attribute->next;
//    }
//    
//    if ([attributeArray count] > 0)
//    {
//      [resultForNode setObject:attributeArray forKey:@"nodeAttributeArray"];
//    }
//  }
//  
//  xmlNodePtr childNode = currentNode->children;
//  if (childNode)
//  {
//    NSMutableArray *childContentArray = [NSMutableArray array];
//    while (childNode)
//    {
//      NSDictionary *childDictionary = DictionaryForNode(childNode, resultForNode);
//      if (childDictionary)
//      {
//        [childContentArray addObject:childDictionary];
//      }
//      childNode = childNode->next;
//    }
//    if ([childContentArray count] > 0)
//    {
//      [resultForNode setObject:childContentArray forKey:@"nodeChildArray"];
//    }
//  }
//  
//  return resultForNode;
//}
//
//NSArray *PerformXPathQuery(xmlDocPtr doc, NSString *query)
//{
//  xmlXPathContextPtr xpathCtx;
//  xmlXPathObjectPtr xpathObj;
//  
//  /* Create xpath evaluation context */
//  xpathCtx = xmlXPathNewContext(doc);
//  if(xpathCtx == NULL)
//  {
//    NSLog(@"Unable to create XPath context.");
//    return nil;
//  }
//  
//  /* Evaluate xpath expression */
//  xpathObj = xmlXPathEvalExpression((xmlChar *)[query cStringUsingEncoding:NSUTF8StringEncoding], xpathCtx);
//  if(xpathObj == NULL) {
//    NSLog(@"Unable to evaluate XPath.");
//    return nil;
//  }
//  
//  xmlNodeSetPtr nodes = xpathObj->nodesetval;
//  if (!nodes)
//  {
//    NSLog(@"Nodes was nil.");
//    return nil;
//  }
//  
//  NSMutableArray *resultNodes = [NSMutableArray array];
//  for (NSInteger i = 0; i < nodes->nodeNr; i++)
//  {
//    NSDictionary *nodeDictionary = DictionaryForNode(nodes->nodeTab[i], nil);
//    if (nodeDictionary)
//    {
//      [resultNodes addObject:nodeDictionary];
//    }
//  }
//  
//  /* Cleanup */
//  xmlXPathFreeObject(xpathObj);
//  xmlXPathFreeContext(xpathCtx);
//  
//  return resultNodes;
//}
//
//NSArray *PerformHTMLXPathQuery(NSData *document, NSString *query)
//{
//  xmlDocPtr doc;
//  
//  /* Load XML document */
//  doc = htmlReadMemory([document bytes], [document length], "", NULL, HTML_PARSE_NOWARNING | HTML_PARSE_NOERROR);
//  
//  if (doc == NULL)
//  {
//    NSLog(@"Unable to parse.");
//    return nil;
//  }
//  
//  NSArray *result = PerformXPathQuery(doc, query);
//  xmlFreeDoc(doc);
//  
//  return result;
//}
//
//NSArray *PerformXMLXPathQuery(NSData *document, NSString *query)
//{
//  xmlDocPtr doc;
//  
//  /* Load XML document */
//  doc = xmlReadMemory([document bytes], [document length], "", NULL, XML_PARSE_RECOVER);
//  
//  if (doc == NULL)
//  {
//    NSLog(@"Unable to parse.");
//    return nil;
//  }
//  
//  NSArray *result = PerformXPathQuery(doc, query);
//  xmlFreeDoc(doc); 
//  
//  return result;
//}

