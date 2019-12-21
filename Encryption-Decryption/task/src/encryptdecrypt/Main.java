package encryptdecrypt;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //
        String direct  = "abcdefghijklmnopqrstuvwxyz";
        //String reverse = "zyxwvutsrqponmlkjihgfedcba";

        String textDirect = scanner.nextLine();
        int offset = Integer.parseInt(scanner.nextLine());

        String textEncryption = "";
        int pos;
        for(int i = 0; i < textDirect.length(); i++) {
            pos = direct.indexOf(textDirect.charAt(i));
            if (pos > -1) {
                int x = (pos+offset) % direct.length();
                textEncryption += direct.charAt(x);
            } else {
                textEncryption += textDirect.charAt(i);
            }
        }

        System.out.println(textEncryption);

    }
}
