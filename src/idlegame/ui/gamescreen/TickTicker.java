package idlegame.ui.gamescreen;

import idlegame.data.GameData;
import idlegame.data.Resource;
import idlegame.data.ResourceType;
import javafx.application.Platform;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TickTicker {
    private final ScheduledThreadPoolExecutor stpe;
    private Ticker handler;

    public TickTicker(GameData data){

        stpe = new ScheduledThreadPoolExecutor(1);
        handler = new Ticker(data);


    }

    public void start(){
        stpe.scheduleAtFixedRate(handler, 0, 16666, TimeUnit.MICROSECONDS);
    }
    public void stop(){
        stpe.shutdownNow();
    }

    private static class Ticker implements Runnable {
        private GameData gameData;
        private static final long SECOND_IN_NANO = 1000000000;
        private static final int TICK_BETWEEN_UPDATES = 6; // should give 10 updates per second
        private int tick = 1;
        private long lastTime = 0;
        private long tillSecond = 0;
        private long timePerSecond = 0;

        private int acted = 0;

        public Ticker(GameData data){
            gameData = data;
        }

        /**
         * When an object implementing interface {@code Runnable} is used
         * to create a thread, starting the thread causes the object's
         * {@code run} method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method {@code run} is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            Platform.runLater(() -> {long now = System.nanoTime();
                long tickTime;
                if (lastTime != 0){
                    tickTime = now - lastTime;
                    tillSecond += tickTime;
                    if (tillSecond >= SECOND_IN_NANO){
                        System.out.println(tick + " ticks in a second of " + tillSecond + " nanoSecond");
                        System.out.println("Acted " + acted + " times. Tick Average time = " + (timePerSecond / 10) / 1000000.0 + " milliseconds.");
                        tick = 1;
                        acted = 0;
                        tillSecond = 0;
                        timePerSecond = 0;
                    }
                }
                lastTime = now;

                if (tick % TICK_BETWEEN_UPDATES == 0){
                    acted++;

                    long start = System.nanoTime();
                    for (ResourceType r: ResourceType.getAll().values()) {
                        r.getTanks().forEach(Resource::moveTick);
                    }

                    gameData.getMyShip().getLocation().getPriorityList().run();

                    long end = System.nanoTime();

                    long dur = end - start;

                    timePerSecond += dur;

                    System.out.println("Took " + dur/1000000f + " ms to run.");


                }
                tick++;});

        }
    }
}