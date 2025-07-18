package mc.zyntra.general.utils.string;

import mc.zyntra.general.utils.mojang.UUIDFetcher;

import java.util.Random;

public class NameRandomUtils {

    public static String getRandomName(int maxChars) {
        String name = generateName();
        if (name.length() > maxChars)
            return getRandomName(maxChars);

        if (UUIDFetcher.getUUID(name) != null)
            return getRandomName(maxChars);

        return name;
    }

    private static String generateName() {
        String name = "";
        int nameLength = (int) Math.round(Math.random() * 4.0D) + 5;
        String vowels = "aeiouy";
        String consonants = "bcdfghklmnprstvwz";
        int usedConsonants = 0;
        int usedVowels = 0;
        String lastLetter = "blah";
        for (int i = 0; i < nameLength; i++) {
            String nextLetter = lastLetter;
            if ((new Random().nextBoolean() || usedConsonants == 1) && usedVowels < 2) {
                while (nextLetter.equals(lastLetter)) {
                    int letterIndex = (int) (Math.random() * vowels.length() - 1.0D);
                    nextLetter = vowels.substring(letterIndex, letterIndex + 1);
                }
                usedConsonants = 0;
                usedVowels++;
            } else {
                while (nextLetter.equals(lastLetter)) {
                    int letterIndex = (int) (Math.random() * consonants.length() - 1.0D);
                    nextLetter = consonants.substring(letterIndex, letterIndex + 1);
                }
                usedConsonants++;
                usedVowels = 0;
            }
            lastLetter = nextLetter;
            name = name.concat(nextLetter);
        }
        int capitalMode = (int) Math.round(Math.random() * 2.0D);
        if (capitalMode == 1) {
            name = String.valueOf(name.substring(0, 1).toUpperCase()) + name.substring(1);
        } else if (capitalMode == 2) {
            for (int j = 0; j < nameLength; j++) {
                if ((int) Math.round(Math.random() * 3.0D) == 1)
                    name = String.valueOf(name.substring(0, j)) + name.substring(j, j + 1).toUpperCase() + ((j == nameLength) ? "" : name.substring(j + 1));
            }
        }
        int numberLength = (int) Math.round(Math.random() * 3.0D) + 1;
        int numberMode = (int) Math.round(Math.random() * 3.0D);
        boolean number = new Random().nextBoolean();
        if (number)
            if (numberLength == 1) {
                int nextNumber = (int) Math.round(Math.random() * 9.0D);
                name = name.concat(Integer.toString(nextNumber));
            } else if (numberMode == 0) {
                int nextNumber = (int) (Math.round(Math.random() * 8.0D) + 1L);
                for (int j = 0; j < numberLength; j++)
                    name = name.concat(Integer.toString(nextNumber));
            } else if (numberMode == 1) {
                int nextNumber = (int) (Math.round(Math.random() * 8.0D) + 1L);
                name = name.concat(Integer.toString(nextNumber));
                for (int j = 1; j < numberLength; j++)
                    name = name.concat("0");
            } else if (numberMode == 2) {
                int nextNumber = (int) (Math.round(Math.random() * 8.0D) + 1L);
                name = name.concat(Integer.toString(nextNumber));
                for (int j = 0; j < numberLength; j++) {
                    nextNumber = (int) Math.round(Math.random() * 9.0D);
                    name = name.concat(Integer.toString(nextNumber));
                }
            } else if (numberMode == 3) {
                int nextNumber = 99999;
                while (Integer.toString(nextNumber).length() != numberLength) {
                    nextNumber = (int) (Math.round(Math.random() * 12.0D) + 1L);
                    nextNumber = (int) Math.pow(2.0D, nextNumber);
                }
                name = name.concat(Integer.toString(nextNumber));
            }
        boolean leet = (!number && new Random().nextBoolean());
        if (leet) {
            String oldName = name;
            while (name.equals(oldName)) {
                int leetMode = (int) Math.round(Math.random() * 7.0D);
                if (leetMode == 0) {
                    name = name.replace("br/com/nordmc/core", "4");
                    name = name.replace("A", "4");
                }
                if (leetMode == 1) {
                    name = name.replace("e", "3");
                    name = name.replace("E", "3");
                }
                if (leetMode == 2) {
                    name = name.replace("g", "6");
                    name = name.replace("G", "6");
                }
                if (leetMode == 3) {
                    name = name.replace("h", "4");
                    name = name.replace("H", "4");
                }
                if (leetMode == 4) {
                    name = name.replace("i", "1");
                    name = name.replace("I", "1");
                }
                if (leetMode == 5) {
                    name = name.replace("o", "0");
                    name = name.replace("O", "0");
                }
                if (leetMode == 6) {
                    name = name.replace("s", "5");
                    name = name.replace("S", "5");
                }
                if (leetMode == 7) {
                    name = name.replace("l", "7");
                    name = name.replace("L", "7");
                }
            }
        }
        int special = (int) Math.round(Math.random() * 8.0D);
        if (special == 3) {
            name = "xX".concat(name).concat("Xx");
        } else if (special == 4) {
            name = name.concat("HG");
        } else if (special == 5) {
            name = name.concat("HD");
        }
        return name;
    }
}
