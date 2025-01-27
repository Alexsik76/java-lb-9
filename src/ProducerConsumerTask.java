public class ProducerConsumerTask {
    private static final int BUFFER_CAPACITY = 10;
    private static final int NUM_PRODUCERS = 5;
    private static final int NUM_TRANSLATORS = 2;
    private static final int NUM_MESSAGES = 100;

    public ProducerConsumerTask() throws InterruptedException {
        RingBuffer<String> buffer1 = new RingBuffer<>(BUFFER_CAPACITY);
        RingBuffer<String> buffer2 = new RingBuffer<>(BUFFER_CAPACITY);

        // Запускаємо потоки-генератори
        for (int i = 0; i < NUM_PRODUCERS; i++) {
            Thread producer = getProducer(i, buffer1);
            producer.start();
        }

        // Запускаємо потоки-перекладачі
        for (int i = 0; i < NUM_TRANSLATORS; i++) {
            Thread translator = getThread(i, buffer1, buffer2);
            translator.start();
        }

        // Виводимо повідомлення з другого буфера
        for (int i = 0; i < NUM_MESSAGES; i++) {
            String message = buffer2.take();
            System.out.println(message + " (виведено з буфера 2)");
        }
    }

    private static Thread getProducer(int i, RingBuffer<String> buffer1) {
        final int producerId = i;
        Thread producer = new Thread(() -> {
            try {
                for (int j = 0; j < NUM_MESSAGES; j++) {
                    String message = "Потік № " + producerId + " згенерував повідомлення " + i +"_" + j;
                    buffer1.put(message);
//                    System.out.println(message + " (додано до буфера 1)");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        producer.setDaemon(true);
        return producer;
    }

    private static Thread getThread(int i, RingBuffer<String> buffer1, RingBuffer<String> buffer2) {
        final int translatorId = i;
        Thread translator = new Thread(() -> {
            try {
                while (true) {
                    String message = buffer1.take();
                    String translatedMessage = "Потік № " + translatorId + " переклав повідомлення <" + message.split(" ")[5] + ">";
                    buffer2.put(translatedMessage);
//                    System.out.println(translatedMessage + " (додано до буфера 2)");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        translator.setDaemon(true);
        return translator;
    }
}
