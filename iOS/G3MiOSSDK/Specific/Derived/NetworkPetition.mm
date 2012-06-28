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
	_networkQueue = nil;
	_currentOperation = nil;
  _listener = dl; //Instance that should be notified
  
	return self;
}


- (void) makeAsyncPetition: (const char *) url
{
    // activate network queue
    if (_networkQueue == nil) _networkQueue = [[NSOperationQueue alloc] init];
    [_networkQueue setMaxConcurrentOperationCount:1];
    
    NSString *myurl = [NSString stringWithUTF8String:url];
    _currentOperation = [[DataDownload alloc] initWithURL:[NSURL URLWithString:myurl]];
    [_currentOperation addObserver:self forKeyPath:@"isFinished" options:NSKeyValueObservingOptionNew context:NULL];
    [_networkQueue addOperation:_currentOperation];
}


- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context
{
	DataDownload* op = (DataDownload *)object;
	[op removeObserver:self forKeyPath:@"isFinished"];
  
	if( ![op error] ) {
    unsigned char *bytes = (unsigned char*)[[op downloadData] bytes];
    
    Response r("", [[op getURL] cStringUsingEncoding:NSUTF8StringEncoding] , bytes, [[op downloadData] length]);
    ((IDownloadListener*)_listener)->onDownload(r);
	} else {
    Response r("", [[op getURL] cStringUsingEncoding:NSUTF8StringEncoding] , NULL, 0);
    ((IDownloadListener*)_listener)->onError(r);
  }
}


@end
