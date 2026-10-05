//
//  ColumnLayoutImageFactory.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//

#ifndef __G3M__ColumnLayoutImageFactory__
#define __G3M__ColumnLayoutImageFactory__

#include "LayoutImageFactory.hpp"


class ColumnLayoutImageFactory : public LayoutImageFactory {
private:
  const int _childrenSeparation;
  
protected:
  ~ColumnLayoutImageFactory() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }
  
  void doLayout(const G3MContext* context,
                IImageFactoryListener* listener,
                bool deleteListener,
                const std::vector<ChildResult*>& results);
  
public:
  
  ColumnLayoutImageFactory(const std::vector<IImageFactory*>& children,
                           const ImageBackground*             background         = NULL,
                           const int                          childrenSeparation = 0);
  
  ColumnLayoutImageFactory(IImageFactory*         child0,
                           IImageFactory*         child1,
                           const ImageBackground* background         = NULL,
                           const int              childrenSeparation = 0);
  
  ColumnLayoutImageFactory(IImageFactory*         child0,
                           const ImageBackground* background         = NULL,
                           const int              childrenSeparation = 0);
  
};

#endif
