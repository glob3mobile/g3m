//
//  AbsoluteImageSizer.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/11/19.
//

#ifndef AbsoluteImageSizer_hpp
#define AbsoluteImageSizer_hpp

#include "ImageSizer.hpp"


class AbsoluteImageSizer : public ImageSizer {
private:
  const int _size;
  
public:
  
  AbsoluteImageSizer(int size);
  
  ~AbsoluteImageSizer();
  
  int calculate();
  
  AbsoluteImageSizer* copy() const;
  
};

#endif
