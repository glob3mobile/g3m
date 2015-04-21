//
//  StarsParser.hpp
//  G3MiOSDemo
//
//  Created by Jose Miguel SN on 21/4/15.
//
//

#import <Foundation/Foundation.h>

#include <vector>

#include "StarDomeRenderer.hpp"

@interface StarsParser : NSObject{

}

+ (std::vector<Constellation>) parse:(NSString*) csv;

@end
