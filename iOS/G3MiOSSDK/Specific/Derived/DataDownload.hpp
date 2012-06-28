//
//  PictureDownloadOperation.h
//  Async Network Example
//
//  Created by Jason Coco on 10/03/12.
//  Copyright 2010 9MMedia LLC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DataDownload : NSOperation
{
	// New Properties
	NSError*  _error;
  NSData* _downloadData;
	
	// In concurrent operations, we have to manage the operation's state
	BOOL      _executing;
	BOOL      _finished;
	
	// The actual NSURLConnection management
	NSURL*    _connectionURL;
	NSURLConnection*  _connection;
	NSMutableData*    _data;
}

@property(nonatomic,readonly) NSError* error;
@property(nonatomic,readonly) NSData* downloadData;

- (id)initWithURL:(NSURL *)url;

- (NSString*) getURL;

@end