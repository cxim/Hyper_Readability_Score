package readability;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Read {
    String real;
    int characters;
    int sentences;
    int words;
    double score;
    double scoreFk;
    double scoreSmog;
    double scoreCl;
    int syllables;
    int polysyllables = 0;

    boolean silente(String word) {
        word = word.substring(0, word.length()-1);

        Pattern yup = Pattern.compile("[aeiouy]");
        Matcher m = yup.matcher(word);

        return m.find();
    }

    int countit(String word) {
        int count = 0;
        Pattern splitter = Pattern.compile("[^aeiouy]*[aeiouy]+");
        Matcher m = splitter.matcher(word);

        while (m.find()) {
            count++;
        }
        return count;
    }

    void findSyllables() {
        int count = 0;
        String word = real.toLowerCase();
        word = word.replaceAll("([0-9]|,|!|\\.|\\?)", "");
        String[] arr = word.split(" ");
        for (String s : arr) {
            word = s;
            if (word.length() > 0 && word.charAt(word.length() - 1) == 'e') {
                if (silente(word)) {
                    int countunit;
                    String newword = word.substring(0, word.length() - 1);
                    countunit = countit(newword);
                    if (countunit > 2) {
                        polysyllables++;
                    }
                    count = count + countunit;
                } else {
                    count++;
                }
            } else {
                int c = countit(word);
                if (c > 2) {
                    polysyllables++;
                }
                count = count + c;
            }
        }
        syllables = count;
    }

    public Read(String real) {
        this.real = real;
    }

    void takeWords() {

        String[] tmp = real.split("\\s+");
        words = tmp.length;
    }

    void getScore() {
        score = 4.71 * (double) characters / (double) words + 0.5 * (double) words / (double) sentences - 21.43;
        System.out.print("Automated Readability Index: " + score);
        System.out.println(" (about " + understood(score) + " year olds)");
    }

    void push() {
        String tmp;
        real = real.trim();
        tmp = real.replaceAll("\\s+", "");
        characters = tmp.length();
        String[] arr = real.split("(\\.+|!+|\\?+)");
        sentences = arr.length;
        takeWords();
        getScore();
        findSyllables();
        output();
    }

    String understood(double inc) {
        int res = (int) inc + 1;
        if (res > 14) {
            res = 14;
        }
        switch (res) {
            case 1:
                return "5";
            case 2:
                return "6";
            case 3:
                return "7";
            case 4:
                return "9";
            case 5:
                return "10";
            case 6:
                return "11";
            case 7:
                return "12";
            case 8:
                return "13";
            case 9:
                return "14";
            case 10:
                return "15";
            case 11:
                return "17";
            case 12:
                return "18";
            case 13:
                return "19";
            case 14:
                return "24";
            default:
                return "error";
        }
    }

    void fk() {
        scoreFk = (0.39 * ((double) words / (double) sentences)) + (11.8 * ((double) syllables / (double) words)) - 15.59;
        System.out.print("Flesch–Kincaid readability tests: " + scoreFk);
        System.out.println(" (about " + understood(scoreFk) + " year olds)");
    }

    void smog() {
        scoreSmog = 1.043 * Math.sqrt((double) polysyllables * 30.0 / (double) sentences) + 3.1291;
        System.out.print("Simple Measure of Gobbledygook: " + scoreSmog);
        System.out.println(" (about " + understood(scoreSmog) + " year olds)");
    }

    void cl() {
        scoreCl = ((double) characters / (double) words * 100) * 0.0588 - ((double) sentences / (double) words * 100) * 0.296 - 15.8;
        System.out.print("Coleman–Liau index: " + scoreCl);
        System.out.println(" (about " + understood(scoreCl) + " year olds)");
    }

    void all() {
        getScore();
        fk();
        smog();
        cl();
        double res = (double) (Integer.parseInt(understood(score)) + Integer.parseInt(understood(scoreCl)) + Integer.parseInt(understood(scoreFk)) + Integer.parseInt(understood(scoreSmog))) / 4;
        System.out.print("\nThis text should be understood in average by " + res + "");
    }
    void findIndex() {
        Scanner scanner = new Scanner(System.in);
        String inc = scanner.nextLine();
        System.out.println();
        switch (inc) {
            case "ARI":
                getScore();
                break;
            case "FK":
                fk();
                break;
            case "SMOG":
                smog();
                break;
            case "CL":
                cl();
                break;
            case "all":
                all();
                break;
            default:
                System.out.println("invalid command");
                break;
        }
    }

    void output() {
        System.out.println("The text is:");
        System.out.println(real + '\n');
        System.out.println("Words: " + words);
        System.out.println("Sentences: " + sentences);
        System.out.println("Characters: " + characters);
        System.out.println("Syllables: " + syllables);
        System.out.println("Polysyllables: " + polysyllables);
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        findIndex();
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("error! plz input Argument");
            System.exit(0);
        }
        FileInputStream file = new FileInputStream(args[0]);
        StringBuilder str = new StringBuilder();
        int b = file.read();
        while (b != -1) {
            str.append((char) b);
            b = file.read();
        }
        file.close();
        Read read = new Read(str.toString());
        read.push();
    }
}
