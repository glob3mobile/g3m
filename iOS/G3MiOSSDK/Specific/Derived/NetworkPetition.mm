//
//  NetworkPetition.mm
//  Glob3 Mobile
//
//  Created by Agustín Trujillo Pino on 16/05/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#import "NetworkPetition.hpp"


#include "IDownloadListener.hpp"

@implementation NetworkPetition

-(id)init: (void*) dl
{	
	networkQueue = nil;
	currentOperation = nil;
  dataArrived = true;
  
  _listener = dl; //Instanced that should be notified
  
	return self;
}


- (void) makeAsyncPetition: (const char *) url
{
    // activate network queue
    networkQueue = [[NSOperationQueue alloc] init];
    [networkQueue setMaxConcurrentOperationCount:1];	
    if( currentOperation) [self releaseAsyncMemory];
    
    NSString *myurl = [NSString stringWithUTF8String:url];
    currentOperation = [[DataDownload alloc] initWithURL:[NSURL URLWithString:myurl]];
    [currentOperation addObserver:self forKeyPath:@"isFinished" options:NSKeyValueObservingOptionNew context:NULL];
    [networkQueue addOperation:currentOperation];
    dataArrived = false;
}


- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context
{
	DataDownload* op = (DataDownload *)object;
	[op removeObserver:self forKeyPath:@"isFinished"];
  dataArrived = true;
  
	if( ![op error] ) {
    downloadData = [op downloadData];
    dataOK = true;
    
    Response r;
    ((IDownloadListener*)_listener)->onDownload(r);
	} else {
    dataOK = false;
    
    Response r;
    ((IDownloadListener*)_listener)->onError(r);
  }
}


- (void) releaseAsyncMemory
{	
    if (!networkQueue)
        printf ("networkQueue NULL!!\n");

	int opCount = [[networkQueue operations] count];	
	
    // Release curren operation
    if (currentOperation) {
        if (opCount>0) [currentOperation removeObserver:self forKeyPath:@"isFinished"];
        [currentOperation cancel];
        currentOperation = nil;
    }
    
	[networkQueue cancelAllOperations];
}


- (void) dealloc 
{	
	// free async memory
	if (networkQueue) [self releaseAsyncMemory];
}


- (bool) isDataArrived
{
    return dataArrived;
}


- (bool) isDataOK
{
    return dataOK;
}


-(void*) getData
{
//	return (void *) CFDataGetBytePtr((CFDataRef) downloadData);
	return (void *) [downloadData bytes];
}


-(unsigned int) getDataLength
{
    return [downloadData length];
}


@end
