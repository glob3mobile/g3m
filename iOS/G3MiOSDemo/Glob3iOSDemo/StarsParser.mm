//
//  StarsParser.m
//  G3MiOSDemo
//
//  Created by Jose Miguel SN on 21/4/15.
//
//

#import "StarsParser.hpp"

#include "Angle.hpp"

@implementation StarsParser

+(double) getNumFromArray:(NSArray*) array index:(int) i{
  NSString* string = [array objectAtIndex:i];
  string = [string stringByReplacingOccurrencesOfString:@"," withString:@"."];
  return [string doubleValue];
}

+(int) starIndex:(std::vector<Star>*) stars forName:(NSString*) name{
  for (int k = 0; k < stars->size(); k++) {
    NSString* n = [[NSString alloc] initWithUTF8String:stars->at(k).getName().c_str()];
    if ([n containsString:name] || [name containsString:n]){
      return k;
    }
  }
  return -1;
}

+(std::vector<int>) parseLinks:(NSString*) csv forConstellation:(NSString*) name withStars:(std::vector<Star>*) stars{
  
  std::vector<int> links;
  
  NSArray* lines = [csv componentsSeparatedByString:@"\r"];
  
  for(unsigned int i = 0; i < [lines count]; i++){
    NSString* line = [lines objectAtIndex:i];
    
    if ([line rangeOfString:name options:NSCaseInsensitiveSearch].location != NSNotFound){
      
      for(int j = i+1; j < [lines count]; j++){
        line = [lines objectAtIndex:j];
        
        if ([line containsString:@";;"]){
          return links;
        }
        
        NSArray* starNames = [line componentsSeparatedByString:@";"];
        NSString* s1 = [starNames objectAtIndex:0];
        NSString* s2 = [starNames objectAtIndex:1];
        
        
        int n1 = [self starIndex:stars forName:s1];
        int n2 = [self starIndex:stars forName:s2];
        
        
        NSLog(@"%@ -> %@, %d -> %d", s1, s2, n1, n2);
        
        links.push_back(n1);
        links.push_back(n2);

        
      }
      
    }
  }
  
  
  return links;
  
}

+ (std::vector<Constellation>) parse:(NSString*) csv withLinks:(NSString*) csvLinks{
  
  std::vector<Constellation> constellations;
  
  NSArray* lines = [csv componentsSeparatedByString:@"\r"];
  
  NSString* constellationName = nil;
  std::vector<Star> stars;
  
  for(unsigned int i = 0; i < [lines count]; i++){
    NSString* line = [lines objectAtIndex:i];
    
    NSArray* comp = [line componentsSeparatedByString:@";"];
    if ([line rangeOfString:@";;"].location != NSNotFound){ //CONSTELATION
      
      
      if (constellationName != nil && stars.size() > 0){
        
        std::vector<int> links = [self parseLinks:csvLinks forConstellation:constellationName withStars:&stars];
        
        //PUSHING CONSTELATION
        Constellation constellation([constellationName UTF8String],
                                    Color::red().wheelStep([lines count], i),
                                    stars,
                                    links);
        
        constellations.push_back(constellation);
        
        stars.clear();
      }
      
      //New Constellation
      constellationName = [comp objectAtIndex:0];
      NSLog(@"CONSTELLATION %@", [comp objectAtIndex:0]);
    } else{ //STAR
      NSString* name = [comp objectAtIndex:1];
      
      Angle ascension = Angle::fromClockHoursMinutesSeconds([self getNumFromArray:comp index:2],
                                                            [self getNumFromArray:comp index:3],
                                                            [self getNumFromArray:comp index:4]);
      
      Angle declination = Angle::fromDegreesMinutesSeconds([self getNumFromArray:comp index:5],
                                                           [self getNumFromArray:comp index:6],
                                                           [self getNumFromArray:comp index:7]);
      
      Star star([name UTF8String], ascension._degrees, declination._degrees, Color::white());
      
      stars.push_back(star);
      
      
      NSLog(@"STAR %@ %f %f", name, ascension._degrees, declination._degrees);
      
    }
    
  }
  
  return constellations;
  
}

@end
