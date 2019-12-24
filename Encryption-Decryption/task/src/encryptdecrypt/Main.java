package encryptdecrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

abstract class Algorithm {
    protected String mode;
    protected String text;
    protected int key;
    protected String result;


    Algorithm(String mode, int key, String text) {
        this.mode = mode;
        this.key = key;
        this.text = text;
    }

    public String getName() {
        return "";
    }
    public String getResult() {
        return result;
    }

    abstract void encode() ;

    abstract void decode();

    void executeAlgorithm() {
            if ("enc".equals(mode)) {
                encode();
            } else {
                decode();
            }
    }
}

class ShiftAlgorithm extends Algorithm {
    private String direct = "abcdefghijklmnopqrstuvwxyz";

    ShiftAlgorithm(String mode, int key, String text) {
        super(mode, key, text);
    }

    private int find(char[] charArray, char c) {
        for (int i = 0; i < charArray.length; i++)
            if (charArray[i] == c) {
                return i;
            }
        return -1;
    }

    @Override
    public void encode() {
        int index;
        char[] d = direct.toCharArray();
        StringBuilder b = new StringBuilder();

        for(char c: text.toCharArray()) {
            index = find(d, Character.toLowerCase(c));
            if (index != -1) {
                index += key;
                while (index >= d.length) index -= d.length;
                b.append(Character.isLowerCase(c) ? d[index] : Character.toUpperCase(d[index]));
            } else {
                b.append(c);
            }
            result = b.toString();
        }
    }

    @Override
    public void decode() {
        int index;
        char[] d = direct.toCharArray();
        StringBuilder b = new StringBuilder();

        for(char c: text.toCharArray()) {
            index = find(d, Character.toLowerCase(c));
            if (index != -1) {
                index -= key;
                while (index < 0) index += d.length;
                b.append(Character.isLowerCase(c) ? d[index] : Character.toUpperCase(d[index]));
            } else {
                b.append(c);
            }
            result = b.toString();
        }
    }
}

class UnicodeAlgorithm extends Algorithm {
    UnicodeAlgorithm(String mode, int key, String text) {
        super(mode, key, text);
    }

    @Override
    protected void encode() {
        StringBuilder b = new StringBuilder();
        for(char c: text.toCharArray()) {
            b.append((char)(c+key));
        }
        result = b.toString();
    }

    @Override
    protected void decode() {
        StringBuilder b = new StringBuilder();
        for(char c: text.toCharArray()) {
            b.append((char)(c-key));
        }
        result = b.toString();
    }
}

abstract class AlgorithmFactory {

        abstract Algorithm createAlgorithm(String alg, String mode, int key, String text);

        Algorithm processAlgorithm(String alg, String mode, int key, String text) {
            Algorithm algorithm = createAlgorithm(alg, mode, key, text);

            if (algorithm == null) {
                System.out.println("Sorry, we are not able to create this kind of algorithm\n");
                return null;
            }
            algorithm.executeAlgorithm();
            return algorithm;
        }
}

class AlgorithmStore extends AlgorithmFactory {
        @Override
        Algorithm createAlgorithm(String alg, String mode, int key, String text) {
            if ("shift".equals(alg)) {
                return new ShiftAlgorithm(mode, key, text);
            } else if ("unicode".equals(alg)) {
                return new UnicodeAlgorithm(mode, key, text);
            } else return null;
        }
}


public class Main {

    public static void main(String[] args) throws IOException {
        String mode = "enc";
        int key = 0;
        String data = "";
        String inFileName = "";
        String outFileName = "";
        String alg = "shift";
        for(int i = 0; i < args.length; i++) {
            if ("-mode".equals(args[i]) && i+1 < args.length) {
                mode = args[i+1];
            }
            if ("-key".equals(args[i]) && i+1 < args.length) {
                key = Integer.parseInt(args[i+1]);
            }
            if ("-data".equals(args[i]) && i+1 < args.length) {
                data = args[i+1];
            }
            if ("-in".equals(args[i]) && i+1 < args.length) {
                inFileName = args[i+1];
            }
            if ("-out".equals(args[i]) && i+1 < args.length) {
                outFileName = args[i+1];
            }
            if ("-alg".equals(args[i]) && i+1 < args.length) {
                alg = args[i+1];
            }
        }

        if (data.isEmpty() && !inFileName.isEmpty()) {
            data = readFileAsString(inFileName);
        }

        AlgorithmStore algorithmStore = new AlgorithmStore();
        Algorithm algorithm = algorithmStore.processAlgorithm(alg, mode, key, data);
        String textOutput = algorithm.getResult();

        if (outFileName.isEmpty()) {
            System.out.println(textOutput);
        } else {
            exportFile(outFileName, textOutput);
        }
    }

    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    public static void exportFile(String fName, String data) {
        File file = new File(fName);

        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.println(data);
        } catch (IOException e) {
            System.out.println("An exception occurs "+e.getMessage());
        }
    }
}
