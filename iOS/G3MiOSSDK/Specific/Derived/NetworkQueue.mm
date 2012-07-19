//
//  NetworkPetition.mm
//  Glob3 Mobile
//
//  Created by Agustín Trujillo Pino on 16/05/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#import "NetworkQueue.hpp"


#include "IDownloadListener.hpp"

@implementation NetworkQueue

-(id)init: (void*) dl
{	
	_networkQueue = nil;
  _listener = dl; //Instance that should be notified
  
	return self;
}

-(void)setListener: (void*) dl
{
  _listener = dl; //Instance that should be notified
}

- (void) makeAsyncPetition: (const char *) url
{
    // activate network queue
    if (_networkQueue == nil) _networkQueue = [[NSOperationQueue alloc] init];
    [_networkQueue setMaxConcurrentOperationCount:1000];
    
    NSString *myurl = [NSString stringWithUTF8String:url];
    DataDownload* currentOperation = [[DataDownload alloc] initWithURL:[NSURL URLWithString:myurl]];
    [currentOperation addObserver:self forKeyPath:@"isFinished" options:NSKeyValueObservingOptionNew context:NULL];
    [_networkQueue addOperation: currentOperation];
}


- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context
{
	DataDownload* op = (DataDownload *)object;
	[op removeObserver:self forKeyPath:@"isFinished"];
  
	if( ![op error] ) {
    int length = [[op downloadData] length];
    unsigned char *bytes = new unsigned char[ length ] ;
    [[op downloadData] getBytes:bytes length: length];
    ByteBuffer bb(bytes, [[op downloadData] length]);  //CREATING BYTEBUFFER
    std::string resp = (char*)bb.getData();
    //printf("\nData: %s;\n", resp.c_str());
    Response r([[op getURL] cStringUsingEncoding:NSUTF8StringEncoding] , &bb);
    ((IDownloadListener*)_listener)->onDownload(r);
	} else {
    ByteBuffer bb(NULL, 0);
    Response r([[op getURL] cStringUsingEncoding:NSUTF8StringEncoding], &bb);
    ((IDownloadListener*)_listener)->onError(r);
  }
}


@end
