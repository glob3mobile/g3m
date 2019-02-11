//
//  ImageSizer.hpp
//  G3MiOSSDK
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

};

#endif
