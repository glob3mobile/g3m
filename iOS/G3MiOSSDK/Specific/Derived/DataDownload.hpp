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
	NSError*  error_;
  NSData* downloadData_;
	
	// In concurrent operations, we have to manage the operation's state
	BOOL      executing_;
	BOOL      finished_;
	
	// The actual NSURLConnection management
	NSURL*    connectionURL_;
	NSURLConnection*  connection_;
	NSMutableData*    data_;
  
  void * _downloadListeners;
}

@property(nonatomic,readonly) NSError* error;
@property(nonatomic,readonly) NSData* downloadData;

- (id)initWithURL:(NSURL *)url listener:(void*) dls;

@end