public class Main {

    private static final int TARGET_FPS = 30;
    private static final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

    public static void main(String[] args) {

        long lastLoopTime = System.nanoTime();
        long lastFrameTime;

        while (true) {
            long now = System.nanoTime();
            long elapsedTime = now - lastLoopTime;
            lastLoopTime = now;

            double delta = elapsedTime / ((double) OPTIMAL_TIME);

            // do updates here

            // render here
                System.out.println("FPS: " + delta);
            // sleep calculations
            lastFrameTime = System.nanoTime() - now;
            long sleepTime = (OPTIMAL_TIME - lastFrameTime) / 1000000;

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}