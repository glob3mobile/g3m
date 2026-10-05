//
//  IImageFactory.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//

#ifndef __G3M__IImageFactory__
#define __G3M__IImageFactory__

class G3MContext;
class IImageFactoryListener;
class ChangedListener;

class IImageFactory {
protected:

public:
#ifdef C_CODE
  virtual ~IImageFactory();
#endif
#ifdef JAVA_CODE
  void dispose();
#endif

  virtual bool isMutable() const = 0;

  virtual void create(const G3MContext* context,
                     IImageFactoryListener* listener,
                     bool deleteListener) = 0;

  virtual void setChangeListener(ChangedListener* listener) = 0;

};

#endif
