package task2;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class PoolExample {

    public static void main(String[] args) throws InterruptedException {

        // создаем пул для выполнения наших задач
        //   максимальное количество созданных задач - 3
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                // не изменяйте эти параметры
                3, 3, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(3));

        // сколько задач выполнилось
        AtomicInteger count = new AtomicInteger(0);
        // сколько задач выполняется
        AtomicInteger inProgress = new AtomicInteger(0);

        // отправляем задачи на выполнение
        for (int i = 0; i < 30; i++) {
            final int number = i;
            Thread.sleep(10);

            System.out.println("creating #" + number);
            executor.submit(() -> {
                int working = inProgress.incrementAndGet();
                System.out.println("start #" + (number + 1) + ", in progress: " + working);
                try {
                    // тут какая-то полезная работа
                    while (working < (executor).getMaximumPoolSize())
                    {
                        executor.wait();
                        Thread.sleep(Math.round(1000 + Math.random() * 2000));
                    }
                } catch (InterruptedException e) {
                    // ignore
                    System.out.println("YOU DIED");
                }
                while(working > 0)
                {
                    working = inProgress.decrementAndGet();
                    System.out.println("end #" + (number + 1) + ", in progress: " + working + ", done tasks: " + count.incrementAndGet());

                }
                executor.notifyAll();

                return null;
            });

        }
        executor.shutdown();

    }
}



/*
Эта программа предназначена для того, чтобы выполнить 30 долгих и ресурсоемких задач.
Для ускорения процесса программист использовал ThreadPoolExecutor.
Так как данные для каждой задачи занимают много оперативной памяти, он ограничил количество задач в очереди до 3.
Но что-то пошло не так, и программа отказывается выполнять все задачи.
Исправьте код так, чтобы все 30 задач выполнились в 3 потока, и цикл создания задач не переполнял очередь задач.
*/

