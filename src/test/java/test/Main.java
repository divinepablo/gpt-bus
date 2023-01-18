package test;

public class Main {

    public static void main(String[] args) {
        System.out.println("Starting " + OneMillionEventsTest.ITERATIONS + " iterations of Event Register Test:");
        OneMillionEventsTest.test();
        System.out.println();
        System.out.println("Starting " + SubUnsubTest.ITERATIONS + " iterations of SubUnsub Test:");
        SubUnsubTest.test();
        System.out.println();
//        System.out.println("Starting " + EventTest.ITERATIONS + " iterations of Event Test:");
//        EventTest.test();
    }

}