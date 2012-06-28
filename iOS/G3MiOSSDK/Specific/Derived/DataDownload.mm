//
//  DataDownload.mm
//  Async Network Example
//
//  Created by José Miguel Santana

#import "DataDownload.hpp"

#include <vector>
#include "IDownloadListener.hpp"

@implementation DataDownload

@synthesize error = _error, downloadData = _downloadData;

#pragma mark -
#pragma mark Initialization & Memory Management

- (id)initWithURL:(NSURL *)url;
{
	NSParameterAssert( url );
	if( (self = [super init]) ) {
		_connectionURL = [url copy];
	}
	return self;
}

- (NSString*) getURL
{
  return [_connectionURL absoluteString];
}

- (void)dealloc
{
	if( _connection ) { [_connection cancel];  }
}

#pragma mark -
#pragma mark Start & Utility Methods

// This method is just for convenience. It cancels the URL connection if it
// still exists and finishes up the operation.
- (void)done
{
	if( _connection ) {
		[_connection cancel];
		_connection = nil;
	}
	
	if( _data ) {
    _downloadData = [[NSData alloc] initWithData: _data];  
		_data = nil;
	}
	
	// Alert anyone that we are finished
	[self willChangeValueForKey:@"isExecuting"];
	[self willChangeValueForKey:@"isFinished"];
	_executing = NO;
	_finished  = YES;
	[self didChangeValueForKey:@"isFinished"];
	[self didChangeValueForKey:@"isExecuting"];
}

- (void)start
{
	if (![NSThread isMainThread])
	{
		[self performSelectorOnMainThread:@selector(start)
							   withObject:nil waitUntilDone:NO];
		return;
	}
	
	
	// Ensure this operation is not being restarted and that it has not been cancelled
	if( _finished || [self isCancelled] ) { [self done]; return; }
	
	// From this point on, the operation is officially executing--remember, isExecuting
	// needs to be KVO compliant!
	[self willChangeValueForKey:@"isExecuting"];
	_executing = YES;
	[self didChangeValueForKey:@"isExecuting"];
	
	// Create the NSURLConnection--this could have been done in init, but we delayed
	// until no in case the operation was never enqueued or was cancelled before starting
	_connection = [[NSURLConnection alloc] initWithRequest:[NSURLRequest requestWithURL:_connectionURL]
												  delegate:self];
}

#pragma mark -
#pragma mark Overrides

- (BOOL)isConcurrent
{
	return YES;
}

- (BOOL)isExecuting
{
	return _executing;
}

- (BOOL)isFinished
{
	return _finished;
}

- (void)cancel
{
	[super cancel];
	[self done];
}

#pragma mark -
#pragma mark Delegate Methods for NSURLConnection

// For this example, we only handle the standard delegate call-backs

// The connection failed
- (void)connection:(NSURLConnection*)connection didFailWithError:(NSError*)error
{
	_data = nil;
	_error = error;
	[self done];
}

// The connection received more data
- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
{
	[_data appendData:data];
}

// Initial response
- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response
{
	NSHTTPURLResponse* httpResponse = (NSHTTPURLResponse*)response;
	NSInteger statusCode = [httpResponse statusCode];
	if( statusCode == 200 ) {
		NSUInteger contentSize = [httpResponse expectedContentLength] > 0 ? [httpResponse expectedContentLength] : 0;
		_data = [[NSMutableData alloc] initWithCapacity:contentSize];
	} else {
		NSString* statusError  = [NSString stringWithFormat:NSLocalizedString(@"HTTP Error: %ld", nil), statusCode];
		NSDictionary* userInfo = [NSDictionary dictionaryWithObject:statusError forKey:NSLocalizedDescriptionKey];
		_error = [[NSError alloc] initWithDomain:@"ExampleOperationDomain"
											code:statusCode
										userInfo:userInfo];
		[self done];
	}
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
  _error = nil;
	[self done];
}

@end