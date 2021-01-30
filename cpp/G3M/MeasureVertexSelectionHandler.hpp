//
//  MeasureVertexSelectionHandler.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/30/21.
//

#ifndef MeasureVertexSelectionHandler_hpp
#define MeasureVertexSelectionHandler_hpp

class Measure;
class Geodetic3D;


class MeasureVertexSelectionHandler {
public:

  virtual ~MeasureVertexSelectionHandler() {

  }

  virtual void onVextexSelection(Measure* measure,
                                 const Geodetic3D* geodetic,
                                 int selectedIndex) = 0;

};

#endif
