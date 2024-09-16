public class Main {

    private static final int TARGET_FPS = 30;
    private static final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

    public static void main(String[] args) {

        long lastLoopTime = System.nanoTime();
        long lastFrameTime;

        // Variables for FPS calculation
        int frames = 0;
        long fpsTimeCounter = 0;
        int realFPS = 0;

        //create start state and other variables here

        Particle particle = new Particle();
        particle.position = new Vector3(0,0,0);
        particle.velocity = new Vector3(1,2,3);
        particle.acceleration = new Vector3(0,0,0);
        particle.setMass(100);
        particle.clearAccumulator();

        while (true) {
            long now = System.nanoTime();
            long elapsedTime = now - lastLoopTime;
            lastLoopTime = now;

            // Accumulate time for FPS calculation
            fpsTimeCounter += elapsedTime;
            frames++;

            double delta = elapsedTime / ((double) OPTIMAL_TIME);

            // do updates here
            particle.integrate(delta);

            if ((frames & 1) == 0) {
                //render here
                System.out.println(particle.position.x + " " + particle.position.y + " " + particle.position.z);
            }


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

            //fps counter
            if (fpsTimeCounter >= 1000000000) {
                realFPS = frames;
                //System.out.println("FPS: " + realFPS);
                fpsTimeCounter = 0;
                frames = 0;
            }

        }

    }
}