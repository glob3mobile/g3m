//
//  MarksFilter.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/27/16.
//
//

#ifndef MarksFilter_hpp
#define MarksFilter_hpp

class Mark;


class MarksFilter {
public:
  virtual ~MarksFilter() {
  }

  virtual bool test(const Mark* mark) const = 0;

};



#endif 
