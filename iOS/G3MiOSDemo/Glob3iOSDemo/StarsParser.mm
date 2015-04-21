//
//  StarsParser.m
//  G3MiOSDemo
//
//  Created by Jose Miguel SN on 21/4/15.
//
//

#import "StarsParser.hpp"

@implementation StarsParser

+(double) getNumFromArray:(NSArray*) array index:(int) i{
  NSString* string = [array objectAtIndex:i];
  string = [string stringByReplacingOccurrencesOfString:@"," withString:@"."];
  return [string doubleValue];
}


+ (std::vector<Constellation>) parse:(NSString*) csv{
  
  std::vector<Constellation> constellations;
  
  NSArray* lines = [csv componentsSeparatedByString:@"\r"];
  
  NSString* constellationName = nil;
  std::vector<Star> stars;
  
  for(unsigned int i = 0; i < [lines count]; i++){
    NSString* line = [lines objectAtIndex:i];
    
    NSArray* comp = [line componentsSeparatedByString:@";"];
    if ([line rangeOfString:@";;"].location != NSNotFound){ //CONSTELATION
      
      
      if (constellationName != nil && stars.size() > 0){
        //PUSHING CONSTELATION
        Constellation constellation([constellationName UTF8String],
                                    Color::red().wheelStep([lines count], i),
                                    stars,
                                    std::vector<int>());
        
        constellations.push_back(constellation);
        
        stars.clear();
      }
      
      //New Constellation
      constellationName = [comp objectAtIndex:0];
      NSLog(@"CONSTELLATION %@", [comp objectAtIndex:0]);
    } else{ //STAR
      NSString* name = [comp objectAtIndex:0];
      
      Angle ascension = Angle::fromClockHoursMinutesSeconds([self getNumFromArray:comp index:2],
                                                            [self getNumFromArray:comp index:3],
                                                            [self getNumFromArray:comp index:4]);
      
      Angle declination = Angle::fromDegreesMinutesSeconds([self getNumFromArray:comp index:5],
                                                           [self getNumFromArray:comp index:6],
                                                           [self getNumFromArray:comp index:7]);
      
      stars.push_back(Star(ascension._degrees, declination._degrees, Color::white()));
      
      NSLog(@"STAR %@ %f %f", name, ascension._degrees, declination._degrees);
      
    }
    
  }
  
  return constellations;
  
}

@end
