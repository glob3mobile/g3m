//
//  WebMercatorProjection.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/14/16.
//
//

#ifndef WebMercatorProjection_hpp
#define WebMercatorProjection_hpp

#include "Projection.hpp"

class WebMercatorProjection : public Projection {
private:
  static WebMercatorProjection* INSTANCE;


protected:
  virtual ~WebMercatorProjection();

  WebMercatorProjection();


public:

  static WebMercatorProjection* instance();

  const std::string getEPSG() const;
  
};

#endif
