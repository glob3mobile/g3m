//
//  GTask.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/16/12.
//
//

#ifndef __G3M__GTask__
#define __G3M__GTask__

class G3MContext;


class GTask {
public:
  
  virtual ~GTask() { }

  
  virtual void run(const G3MContext* context) = 0;
};

#endif
