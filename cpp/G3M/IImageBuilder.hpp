//
//  IImageBuilder.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//

#ifndef __G3M__IImageBuilder__
#define __G3M__IImageBuilder__

class G3MContext;
class IImageBuilderListener;
class ChangedListener;

class IImageBuilder {
protected:

public:
#ifdef C_CODE
  virtual ~IImageBuilder();
#endif
#ifdef JAVA_CODE
  void dispose();
#endif

  virtual bool isMutable() const = 0;

  virtual void build(const G3MContext* context,
                     IImageBuilderListener* listener,
                     bool deleteListener) = 0;

  virtual void setChangeListener(ChangedListener* listener) = 0;

};

#endif
