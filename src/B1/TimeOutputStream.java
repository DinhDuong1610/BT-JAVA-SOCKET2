package B1;

import java.io.OutputStream;

public class TimeOutputStream extends Thread {
    private final OutputStream outputStream;
    private final int delay;

    public TimeOutputStream(OutputStream outputStream, int delay) {
        this.outputStream = outputStream;
        this.delay = delay;
    }

    @Override
    public void run() {
        try {
            int i = 1;
            while (i <= 1000) {
                String message = Integer.toString(i) + "\n";
                outputStream.write(message.getBytes());
                outputStream.flush();
                Thread.sleep(delay);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
