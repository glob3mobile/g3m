//
//  MarkTouchListener.hpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 05/12/12.
//

#ifndef G3MiOSSDK_MarkTouchListener
#define G3MiOSSDK_MarkTouchListener

class Mark;

class MarkTouchListener {
public:
  virtual ~MarkTouchListener() {
  }

  virtual bool touchedMark(Mark* mark) = 0;
};

#endif
