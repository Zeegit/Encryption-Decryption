package encryptdecrypt;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //
        //String direct  = "abcdefghijklmnopqrstuvwxyz";
        //String reverse = "zyxwvutsrqponmlkjihgfedcba";

        String operation = scanner.nextLine();
        String textInput = scanner.nextLine();
        int offset = Integer.parseInt(scanner.nextLine());

        String textOutput = "";
        char c;

        for(int i = 0; i < textInput.length(); i++) {
            c = textInput.charAt(i);
            c += "enc".equals(operation)?offset:-offset;
            textOutput += c;
        }

        System.out.println(textOutput);

    }
}
