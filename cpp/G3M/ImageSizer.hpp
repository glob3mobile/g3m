//
//  ImageSizer.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/11/19.
//

#ifndef ImageSizer_hpp
#define ImageSizer_hpp


class ImageSizer {
public:
  
  virtual ~ImageSizer() {
    
  }
  
  virtual int calculate() = 0;
  
  virtual ImageSizer* copy() const = 0;
  
};

#endif
