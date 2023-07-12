package View;

import java.io.IOException;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // new ChatView();
            }
        });
        unitTest();
    }
    
    static void unitTest() {
        Vehicle vehicle1 = new Car();
        Vehicle vehicle2 = new Motor();
        vehicle1.makeSound();
        vehicle2.makeSound();
        new Vehicle().makeSound();
    }

    static class Vehicle {
        Vehicle() {}
        void makeSound() {
            System.out.println("It is moving");
        }
    }

    static class Car extends Vehicle {
        Car() {}
        void makeSound() {
            System.out.println("Im a car");
        }
    }

    static class Motor extends Vehicle {
        Motor() {}
        void makeSound() {
            System.out.println("Im a racing boys");
        }
    }
}