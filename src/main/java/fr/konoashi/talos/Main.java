package fr.konoashi.talos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        //Testing Deployment
        createFile();
        addPid();
        SpringApplication.run(Main.class);
    }

    public static void createFile() {
        try {
            File myObj = new File("pid.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void addPid() {
        try {
            FileWriter myWriter = new FileWriter("pid.txt");
            myWriter.write(String.valueOf(ProcessHandle.current().pid()));
            myWriter.close();
            System.out.println("Successfully wrote pid.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
