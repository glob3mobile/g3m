//
//  MarkTouchListener.hpp
//  G3M
//
//  Created by Eduardo de la Montaña on 05/12/12.
//

#ifndef G3M_MarkTouchListener
#define G3M_MarkTouchListener

class Mark;

class MarkTouchListener {
public:
  virtual ~MarkTouchListener() {
  }

  virtual bool touchedMark(Mark* mark) = 0;
};

#endif
