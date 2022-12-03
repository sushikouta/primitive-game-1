public class Timer {
    Thread timer = new Thread(new Runnable() {
        @Override public void run() {
            while (true) {
                every();
                
                try {
                    Thread.sleep(reflush);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    int reflush = 0;

    public Timer(int reflush) {
        reflush = 0;
    }

    public void start() {
        timer.start();
    }

    public void every() {

    }
}
