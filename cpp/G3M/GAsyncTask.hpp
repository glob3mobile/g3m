//
//  GAsyncTask.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/18/16.
//
//

#ifndef GAsyncTask_hpp
#define GAsyncTask_hpp

class G3MContext;


class GAsyncTask {
public:
  virtual ~GAsyncTask() {
  }

  virtual void runInBackground(const G3MContext* context) = 0;

  virtual void onPostExecute(const G3MContext* context) = 0;
};


#endif
