//
//  MarkFilter.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/27/16.
//
//

#ifndef MarkFilter_hpp
#define MarkFilter_hpp

class Mark;


class MarkFilter {
public:
  virtual ~MarkFilter() {
  }

  virtual bool test(const Mark* mark) const = 0;

};

#endif 
