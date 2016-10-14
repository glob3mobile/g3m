//
//  G3MMarksDemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/18/13.
//

#ifndef __G3MApp__G3MMarksDemoScene__
#define __G3MApp__G3MMarksDemoScene__

#include "G3MDemoScene.hpp"

class Mark;

class G3MMarksDemoScene : public G3MDemoScene {
private:
  long long _requestID;

protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex);

public:

  G3MMarksDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Marks", "", -1),
  _requestID(-1)
  {
  }

  void deactivate(const G3MContext* context);


  void addMark(Mark* mark);

};

#endif
