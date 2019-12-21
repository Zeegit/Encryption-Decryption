package encryptdecrypt;

public class Main {
    public static void main(String[] args) {
        //
        String direct  = "abcdefghijklmnopqrstuvwxyz";
        String reverse = "zyxwvutsrqponmlkjihgfedcba";

        String textDirect = "we found a treasure!";
        String textReverse = "";
        int pos;
        for(int i = 0; i < textDirect.length(); i++) {
            pos = direct.indexOf(textDirect.charAt(i));
            if (pos > -1) {
                textReverse += reverse.charAt(pos);
            } else {
                textReverse += textDirect.charAt(i);
            }
        }

        System.out.println(textReverse);

    }
}
