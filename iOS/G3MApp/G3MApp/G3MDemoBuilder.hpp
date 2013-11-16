//
//  G3MDemoBuilder.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/14/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MDemoBuilder__
#define __G3MApp__G3MDemoBuilder__

class IG3MBuilder;
class LayerSet;
class G3MDemoModel;
class G3MDemoListener;


class G3MDemoBuilder {
private:
  G3MDemoModel* _model;

  LayerSet* createLayerSet();

protected:
  G3MDemoBuilder(G3MDemoListener* listener);

  void build();

  virtual IG3MBuilder* getG3MBuilder() = 0;

public:
  virtual ~G3MDemoBuilder();

  G3MDemoModel* getModel();

};

#endif
