//
//  RowLayoutImageBuilder.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/20/19.
//

#ifndef RowLayoutImageBuilder_hpp
#define RowLayoutImageBuilder_hpp

#include "LayoutImageBuilder.hpp"


class RowLayoutImageBuilder : public LayoutImageBuilder {
private:
  const int _childrenSeparation;

protected:
  ~RowLayoutImageBuilder() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void doLayout(const G3MContext* context,
                IImageBuilderListener* listener,
                bool deleteListener,
                const std::vector<ChildResult*>& results);

public:

  RowLayoutImageBuilder(const std::vector<IImageBuilder*>& children,
                        const ImageBackground*             background         = NULL,
                        const int                          childrenSeparation = 0);

  RowLayoutImageBuilder(IImageBuilder*         child0,
                        IImageBuilder*         child1,
                        const ImageBackground* background         = NULL,
                        const int              childrenSeparation = 0);

  RowLayoutImageBuilder(IImageBuilder*         child0,
                        const ImageBackground* background         = NULL,
                        const int              childrenSeparation = 0);

};


#endif
