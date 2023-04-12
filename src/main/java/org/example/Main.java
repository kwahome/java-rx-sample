package org.example;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {

    private static final String fileName = "./src/main/resources/file.txt";

    public static void main(String[] args) throws FileNotFoundException {
        /*
        Flowable<Integer> flow = Flowable.just(1, 2, 3, 4);

        flow.filter(el -> el % 2 == 0)
            .map(el -> el * 10)
            .subscribe(el -> System.out.println(el));
        */

        Flowable<String> flow = getSource();

        flow.parallel(4)
            .runOn(Schedulers.computation())
            .map(String::toUpperCase)
            .sequential()
            .subscribe(System.out::println);
    }

    private static Flowable<String> getSource() throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(Main.fileName));

        return Flowable.generate(emitter -> {
            String line = reader.readLine();

            if (line != null) {
                emitter.onNext(line);
            } else {
                emitter.onComplete();
            }
        });
    }

}