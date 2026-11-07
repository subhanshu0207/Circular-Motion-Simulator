package utils;

public class CustomTime {
    private long lastTime;
    private double deltaTime;

    private double timeScale = 1.0;
    private boolean paused = false;

    public CustomTime() {
        lastTime = System.nanoTime();
    }

    public void update() {
        long currentTime = System.nanoTime();

        deltaTime = (currentTime - lastTime) / 1_000_000_000.0;

        lastTime = currentTime;

        // Prevent enormous dt if the program freezes/minimizes.
        deltaTime = Math.min(deltaTime, 0.1);

        if (paused) {
            deltaTime = 0;
        } else {
            deltaTime *= timeScale;
        }
    }

    public double getDeltaTime() {
        return deltaTime;
    }

    public double getFPS() {
        if (deltaTime == 0)
            return 0;

        return 1.0 / deltaTime;
    }

    public void setTimeScale(double scale) {
        if (scale < 0)
            throw new IllegalArgumentException("Time scale cannot be negative.");

        timeScale = scale;
    }

    public double getTimeScale() {
        return timeScale;
    }

    public void pause() {
        paused = true;
    }

    public void reset() {
        lastTime = System.nanoTime();
        deltaTime = 0;
    }

    public void resume() {
        paused = false;
        lastTime = System.nanoTime();
        deltaTime = 0;
    }

    public void togglePause() {
        if (paused)
            resume();
        else
            pause();
    }

    public boolean isPaused() {
        return paused;
    }
}