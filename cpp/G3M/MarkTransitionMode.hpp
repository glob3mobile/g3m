//
//  MarkTransitionMode.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/8/26.
//

#ifndef MarkTransitionMode_hpp
#define MarkTransitionMode_hpp

// how an outfit comes in and goes away when a decluttering MarksRenderer changes it
enum MarkTransitionMode {
  SCALE,
  ALPHA,
  SCALE_AND_ALPHA,
  WIDTH,          // scales the width only, around the anchor: a label at the side folds into its icon
  WIDTH_AND_ALPHA,
  HEIGHT,         // the same for a label above or below its icon
  HEIGHT_AND_ALPHA
};

#endif
