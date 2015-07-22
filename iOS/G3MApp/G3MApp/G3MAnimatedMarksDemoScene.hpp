//
//  G3MAnimatedMarksDemoScene.h
//  G3MApp
//
//  Created by Jose Miguel SN on 20/4/15.
//  Copyright (c) 2015 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MAnimatedMarksDemoScene__
#define __G3MApp__G3MAnimatedMarksDemoScene__


#include "G3MDemoScene.hpp"

class Mark;

class G3MAnimatedMarksDemoScene : public G3MDemoScene {

protected:
  void rawActivate(const G3MContext* context);
  
  void rawSelectOption(const std::string& option,
                       int optionIndex);
  
public:
  
  G3MAnimatedMarksDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Animated Marks", "", -1)
  {
  }
  
  void deactivate(const G3MContext* context);
  
};

#endif /* defined(__G3MApp__G3MAnimatedMarksDemoScene__) */
