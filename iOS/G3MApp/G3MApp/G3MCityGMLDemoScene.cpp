//
//  G3MCityGMLDemoScene.cpp
//  G3MApp
//
//  Created by Jose Miguel SN on 29/3/16.
//  Copyright © 2016 Igo Software SL. All rights reserved.
//

#include "G3MCityGMLDemoScene.hpp"

#include <G3MiOSSDK/IFactory.hpp>
#include <G3MiOSSDK/IXMLDocument.hpp>

void G3MCityGMLDemoScene::rawActivate(const G3MContext* context) {
  
  IFactory::instance()->createXMLDocumentFromXML("<note>  <to>Tove</to> <from>Jani</from> <heading>Reminder</heading> <body>Don't forget me this weekend!</body> </note>");

}

void G3MCityGMLDemoScene::deactivate(const G3MContext* context) {

  G3MDemoScene::deactivate(context);
}

void G3MCityGMLDemoScene::rawSelectOption(const std::string& option,
                                        int optionIndex) {
  
}