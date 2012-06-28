//
//  NetworkPetition.h
//  Glob3 Mobile
//
//  Created by Agustín Trujillo Pino on 16/05/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#include "DataDownload.hpp"


@interface NetworkPetition : NSObject {
@private	
	DataDownload* currentOperation;
	NSOperationQueue* networkQueue;
	NSData *downloadData;	
  bool dataArrived;
  bool dataOK;
  
  void * _listener; 
}

-(id)init: (void*) dl;

- (void) makeAsyncPetition: (const char *) url;
- (void *) getData;
- (unsigned int) getDataLength;
- (bool) isDataArrived;
- (bool) isDataOK;
//- (void) ReleaseData;

@end

