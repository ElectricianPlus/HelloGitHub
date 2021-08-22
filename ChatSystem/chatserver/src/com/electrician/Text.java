package com.electrician;

import java.io.FileReader;
import java.io.IOException;

public class Text {
    public static void main(String[] args) throws IOException {
        FileReader fr = new FileReader("chatserver\\User\\Users.txt");
        int read = fr.read();
        System.out.println(read);
        fr.close();
    }
}
