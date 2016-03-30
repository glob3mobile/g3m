//
//  G3MCityGMLDemoScene.hpp
//  G3MApp
//
//  Created by Jose Miguel SN on 29/3/16.
//  Copyright © 2016 Igo Software SL. All rights reserved.
//

#ifndef G3MCityGMLDemoScene_hpp
#define G3MCityGMLDemoScene_hpp

#include "G3MDemoScene.hpp"
#include "G3MDemoModel.hpp"

class G3MCityGMLDemoScene : public G3MDemoScene {
private:
  long long _requestId;
  
  
protected:
  void rawActivate(const G3MContext* context);
  
  void rawSelectOption(const std::string& option,
                       int optionIndex);
  
public:
  
  G3MCityGMLDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "CityGML", "", -1),
  _requestId(-1)
  {
  }
  
  void deactivate(const G3MContext* context);
  
  
};

#endif /* G3MCityGMLDemoScene_hpp */
