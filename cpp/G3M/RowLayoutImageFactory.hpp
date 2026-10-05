//
//  RowLayoutImageFactory.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/20/19.
//

#ifndef RowLayoutImageFactory_hpp
#define RowLayoutImageFactory_hpp

#include "LayoutImageFactory.hpp"


class RowLayoutImageFactory : public LayoutImageFactory {
private:
  const int _childrenSeparation;

protected:
  ~RowLayoutImageFactory() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void doLayout(const G3MContext* context,
                IImageFactoryListener* listener,
                bool deleteListener,
                const std::vector<ChildResult*>& results);

public:

  RowLayoutImageFactory(const std::vector<IImageFactory*>& children,
                        const ImageBackground*             background         = NULL,
                        const int                          childrenSeparation = 0);

  RowLayoutImageFactory(IImageFactory*         child0,
                        IImageFactory*         child1,
                        const ImageBackground* background         = NULL,
                        const int              childrenSeparation = 0);

  RowLayoutImageFactory(IImageFactory*         child0,
                        const ImageBackground* background         = NULL,
                        const int              childrenSeparation = 0);

};


#endif
