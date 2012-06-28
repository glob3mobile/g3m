//
//  DataDownload.mm
//  Async Network Example
//
//  Created by José Miguel Santana

#import "DataDownload.hpp"

#include <vector>
#include "IDownloadListener.hpp"

@implementation DataDownload

@synthesize error = error_, downloadData = downloadData_;

#pragma mark -
#pragma mark Initialization & Memory Management

- (id)initWithURL:(NSURL *)url;
{
	NSParameterAssert( url );
	if( (self = [super init]) ) {
		connectionURL_ = [url copy];
	}
	return self;
}

- (void)dealloc
{
	if( connection_ ) { [connection_ cancel];  }
}

#pragma mark -
#pragma mark Start & Utility Methods

// This method is just for convenience. It cancels the URL connection if it
// still exists and finishes up the operation.
- (void)done
{
	if( connection_ ) {
		[connection_ cancel];
		connection_ = nil;
	}
	
	if( data_ ) {
    downloadData_ = [[NSData alloc] initWithData:data_];  
		data_ = nil;
	}
	
	// Alert anyone that we are finished
	[self willChangeValueForKey:@"isExecuting"];
	[self willChangeValueForKey:@"isFinished"];
	executing_ = NO;
	finished_  = YES;
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
	if( finished_ || [self isCancelled] ) { [self done]; return; }
	
	// From this point on, the operation is officially executing--remember, isExecuting
	// needs to be KVO compliant!
	[self willChangeValueForKey:@"isExecuting"];
	executing_ = YES;
	[self didChangeValueForKey:@"isExecuting"];
	
	// Create the NSURLConnection--this could have been done in init, but we delayed
	// until no in case the operation was never enqueued or was cancelled before starting
	connection_ = [[NSURLConnection alloc] initWithRequest:[NSURLRequest requestWithURL:connectionURL_]
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
	return executing_;
}

- (BOOL)isFinished
{
	return finished_;
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
	data_ = nil;
	error_ = error;
	[self done];
}

// The connection received more data
- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
{
	[data_ appendData:data];
}

// Initial response
- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response
{
	NSHTTPURLResponse* httpResponse = (NSHTTPURLResponse*)response;
	NSInteger statusCode = [httpResponse statusCode];
	if( statusCode == 200 ) {
		NSUInteger contentSize = [httpResponse expectedContentLength] > 0 ? [httpResponse expectedContentLength] : 0;
		data_ = [[NSMutableData alloc] initWithCapacity:contentSize];
	} else {
		NSString* statusError  = [NSString stringWithFormat:NSLocalizedString(@"HTTP Error: %ld", nil), statusCode];
		NSDictionary* userInfo = [NSDictionary dictionaryWithObject:statusError forKey:NSLocalizedDescriptionKey];
		error_ = [[NSError alloc] initWithDomain:@"ExampleOperationDomain"
											code:statusCode
										userInfo:userInfo];
		[self done];
	}
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
  error_ = nil;
	[self done];
}

@end