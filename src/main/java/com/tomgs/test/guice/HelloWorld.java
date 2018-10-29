package com.tomgs.test.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author tangzhongyuan
 * @create 2018-10-02 16:12
 **/
@Singleton
public class HelloWorld {

    @Inject
    private HelloPrinter printer;

    public void hello() {
        printer.print();
    }

    public static void main(String[] args) {

        Injector injector = Guice.createInjector();
        HelloWorld instance = injector.getInstance(HelloWorld.class);
        instance.hello();
    }
}

@Singleton
class HelloPrinter {

    public void print() {
        System.out.printf("Hello World ...");
    }
}