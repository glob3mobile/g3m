//
//  Logger_iOS.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 31/05/12.
//

#include "Logger_iOS.hpp"

#include <cstdio>
#include <cstdlib>


// Formats with vasprintf and only then converts to NSString.
//
// NSLogv would decode every "%s" argument with the system C-string encoding
// (Mac Roman), so UTF-8 text such as "Ámsterdam" came out as "√Åmsterdam".
// vasprintf copies the argument bytes untouched, and the single UTF-8
// conversion afterwards keeps them intact.
static void logFormatted(NSString* prefix,
                         const std::string& format,
                         va_list args) {
  char* buffer = NULL;
  const int length = vasprintf(&buffer, format.c_str(), args);
  if (length < 0 || buffer == NULL) {
    NSLog(@"%@%s", prefix, format.c_str());
    return;
  }

  NSString* message = [[NSString alloc] initWithBytes: buffer
                                               length: (NSUInteger) length
                                             encoding: NSUTF8StringEncoding];
  if (message == nil) {
    // Not valid UTF-8: fall back to a lossy conversion rather than dropping the log line.
    message = [[NSString alloc] initWithBytes: buffer
                                       length: (NSUInteger) length
                                     encoding: NSISOLatin1StringEncoding];
  }
  NSLog(@"%@%@", prefix, message);

  free(buffer);
}

void Logger_iOS::logInfo(const std::string x, ...) const {
  if (_level <= InfoLevel) {
    va_list args;
    va_start(args, x);
    logFormatted(@"", x, args);
    va_end(args);
  }
}

void Logger_iOS::logWarning(const std::string x, ...) const {
  if (_level <= WarningLevel) {
    va_list args;
    va_start(args, x);
    logFormatted(@"Warning: ", x, args);
    va_end(args);
  }
}

void Logger_iOS::logError(const std::string x, ...) const {
  if (_level <= ErrorLevel) {
    va_list args;
    va_start(args, x);
    logFormatted(@"ERROR: ", x, args);
    va_end(args);
  }
}
