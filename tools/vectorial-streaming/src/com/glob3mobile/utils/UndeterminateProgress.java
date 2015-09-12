

package com.glob3mobile.utils;


public abstract class UndeterminateProgress {


   //private static final MessageFormat FORMATTER = new MessageFormat("{0,number,#0.00}%");


   private long          _done;
   private final long    _started;
   private long          _lastInformTime;
   private final long    _timeToInform;
   private boolean       _finished;
   private int           _informedStep  = 0;
   private final boolean _informThroughput;

   private long          _lastStepsDone = 0;
   private long          _lastElapsed   = 0;


   public UndeterminateProgress() {
      this(false);
   }


   public UndeterminateProgress(final boolean informThroughput) {
      this(10, informThroughput);
   }


   public UndeterminateProgress(final long secondsToInform) {
      this(secondsToInform, false);
   }


   public UndeterminateProgress(final long secondsToInform,
                                final boolean informThroughput) {
      _done = 0;
      _finished = false;

      final long now = System.currentTimeMillis();
      _started = now;
      _timeToInform = secondsToInform * 1000;
      _lastInformTime = now - _timeToInform - 1;

      _informThroughput = informThroughput;

      processSteps();
   }


   public void stepDone() {
      stepsDone(1);
   }


   private static final String[] STEPS_MESSAGES = { //
                                                "[+     ]", //
            "[ +    ]", //
            "[  +   ]", //
            "[   +  ]", //
            "[    + ]", //
            "[     +]", //
            "[    + ]", //
            "[   +  ]", //
            "[  +   ]", //
            "[ +    ]" //
                                                };


   protected synchronized String progressString(final long stepsDone,
                                                final long elapsed) {
      final String thrMsg = getThroughputMessage(stepsDone, elapsed);

      if (_finished) {
         return " [ done ] " + _done + " steps" + " [Finished in " + StrUtils.getTimeMessage(elapsed) + "]" + thrMsg;
      }

      final String stepChar = STEPS_MESSAGES[_informedStep++ % STEPS_MESSAGES.length];

      final String bar = " " + stepChar + " ";

      if (_done == 0) {
         return bar + "[Started]";
      }
      return bar + _done + " steps" + " [Elapsed: " + StrUtils.getTimeMessage(elapsed) + "]" + thrMsg;
   }


   private String getThroughputMessage(final long stepsDone,
                                       final long elapsed) {
      if (!_informThroughput || (stepsDone == 0)) {
         return "";
      }

      final long deltaSteps = stepsDone - _lastStepsDone;
      final long deltaElapsed = elapsed - _lastElapsed;

      _lastStepsDone = stepsDone;
      _lastElapsed = elapsed;

      final String inst = StrUtils.getSpaceMessage(((double) deltaSteps / deltaElapsed) * 1000) + "/sec";
      final String avr = StrUtils.getSpaceMessage(((double) stepsDone / elapsed) * 1000) + "/sec";

      return " " + inst + " (avr=" + avr + ")";
   }


   public synchronized void stepsDone(final long steps) {
      _done += steps;
      processSteps();
   }


   public synchronized void finish() {
      _finished = true;
      processSteps();
   }


   private void processSteps() {

      final long now = System.currentTimeMillis();
      final long elapsedSinceLastInform = now - _lastInformTime;
      if ((_finished) || (elapsedSinceLastInform > _timeToInform)) {
         _lastInformTime = now;

         final long elapsed = now - _started;
         informProgress(_done, elapsed);
      }
   }


   public abstract void informProgress(final long stepsDone,
                                       final long elapsed);


   public static void main(final String[] args) {
      System.out.println("UndeterminateProgress 0.1");
      System.out.println("-------------------------\n");


      final int steps = 5000;

      final UndeterminateProgress progress = new UndeterminateProgress(5, true) {
         @Override
         public void informProgress(final long stepsDone,
                                    final long elapsed) {
            System.out.println("Task" + progressString(stepsDone, elapsed));
         }
      };

      for (int i = 0; i < steps; i++) {
         try {
            Thread.sleep(5);
         }
         catch (final InterruptedException e) {
         }
         progress.stepDone();
      }
      progress.finish();

   }


}
