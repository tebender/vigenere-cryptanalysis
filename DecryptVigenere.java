import java.util.HashMap;
import java.util.Map;

/**
 * 
 */

/**
 * Code relevant to the decryption of text encrypted with a Vigenere Cipher
 * 
 * @author Tim Bender
 * @version 2019.01.12
 *
 */
public class DecryptVigenere {
    private Map<Character, Integer> map;
    private char[] letters;
    private double[] coincidenceVals;


    /**
     * A constructor
     */
    public DecryptVigenere() {
        letters = new char[26];
        map = new HashMap<Character, Integer>();
        int count = 0;
        for (char ch = 'a'; ch <= 'z'; ch++) {
            letters[ch - 'a'] = ch;
            map.put(ch, count);
            count++;
        }
        coincidenceVals = new double[26];
        populateCoincidenceVals();
    }


    /**
     * set up coincidence vals, values generated by external source
     */
    public void populateCoincidenceVals() {
        coincidenceVals[0] = 8.15;
        coincidenceVals[1] = 1.44;
        coincidenceVals[2] = 2.76;
        coincidenceVals[3] = 3.79;
        coincidenceVals[4] = 13.11;
        coincidenceVals[5] = 2.92;
        coincidenceVals[6] = 1.99;
        coincidenceVals[7] = 5.26;
        coincidenceVals[8] = 6.35;
        coincidenceVals[9] = .13;
        coincidenceVals[10] = .42;
        coincidenceVals[11] = 3.39;
        coincidenceVals[12] = 2.54;
        coincidenceVals[13] = 7.1;
        coincidenceVals[14] = 8;
        coincidenceVals[15] = 1.98;
        coincidenceVals[16] = .12;
        coincidenceVals[17] = 6.83;
        coincidenceVals[18] = 6.1;
        coincidenceVals[19] = 10.47;
        coincidenceVals[20] = 2.46;
        coincidenceVals[21] = .92;
        coincidenceVals[22] = 1.54;
        coincidenceVals[23] = .17;
        coincidenceVals[24] = 1.98;
        coincidenceVals[25] = .08;
    }


    /**
     * Try every possible key word length and look for coincidences
     * Used to find potential key lengths
     * 
     * @param str
     *            the cipher text
     */
    public void tryAllShifts(String str) {
        int bestShift = -1;
        int bestCoincidence = -1;
        // check all shifts
        for (int i = 1; i < str.length(); i++) {
            int tempCoincidence = 0;
            System.out.print("Shift " + i + ": ");
            // compare each char at this shift
            for (int j = 0; j < str.length(); j++) {
                if (str.charAt(j) == str.charAt((j + i) % str.length())) {
                    tempCoincidence++;
                }
            }
            System.out.println(tempCoincidence + " coincidences");
            if (tempCoincidence > bestCoincidence) {
                bestCoincidence = tempCoincidence;
                bestShift = i;
            }
        }
        System.out.println("Best Coincidence is shift " + bestShift + " at "
            + bestCoincidence + " coincidences.");
    }


    /**
     * Make substrings for frequency analysis
     * 
     * @param str
     *            cipher text
     * @param keyLength
     * @return an array of substrings
     */
    public String[] makeSubstrings(String str, int keyLength) {
        String[] arr = new String[keyLength];
        for (int i = 0; i < str.length(); i++) {
            // first instantiate strings in arrays
            if (i < keyLength) {
                arr[i] = Character.toString(str.charAt(i));
            }
            else {
                // add to them as needed
                arr[i % keyLength] = arr[i % keyLength] + Character.toString(str
                    .charAt(i));
            }
        }
        // print substrings
        for (int i = 0; i < keyLength; i++) {
            System.out.println(arr[i]);
        }
        return arr;
    }


    /**
     * calculates relative frequency of cipher text char in a substring
     * 
     * @param str
     *            a substring
     * @return
     */
    public double[] relativeFrequency(String str) {
        str = str.toLowerCase();
        double[] arr = new double[26];
        // count occurrences of cipher text letter
        for (int i = 0; i < str.length(); i++) {
            int c = map.get(str.charAt(i));
            arr[c]++;
        }
        // divide by total letters in substring
        for (int i = 0; i < 26; i++) {
            arr[i] = arr[i] / str.length();
        }
        return arr;
    }


    /**
     * Create a table of index of coincidences
     * 
     * @param strings
     *            array of substrings based on suspected key length
     * @return a table where columns are the substrings, rows are index of
     *         coincidence when shifted by corresponding letter
     */
    public double[][] indexOfCoincidenceTable(String[] strings) {
        double[][] arr = new double[strings.length][26];
        // Iterate through substrings
        for (int i = 0; i < strings.length; i++) {
            // get relative frequency vector
            double[] relFreq = relativeFrequency(strings[i]);
            double max = 0;
            int maxShift = -1;
            // iterate through shifts
            for (int j = 0; j < 26; j++) {
                double sum = 0;
                // compute dot product of relative frequency and coincidenceVals
                for (int k = 0; k < 26; k++) {
                    sum = sum + relFreq[(k + j) % 26] * coincidenceVals[k];
                }
                arr[i][j] = sum;
                // remember highest index of coincidence
                if (sum > max) {
                    max = sum;
                    maxShift = j;
                }
            }
            System.out.println("Max shift for substring " + i + " is shift "
                + maxShift + " with coincidence " + max);
        }
        return arr;
    }


    /**
     * Prints out an index of coincidence table
     * 
     * @param table
     */
    public void dumpTable(double[][] table) {
        System.out.println("Table Dump:");
        for (int i = 0; i < table.length; i++) {
            System.out.println(
                "__________________________________________________________");
            System.out.println("Begin Column " + i);
            for (int j = 0; j < 26; j++) {
                System.out.println("Shift " + j + ": " + table[i][j]);
            }
        }
    }


    /**
     * Given a cipher text and string, decrypts cipher text
     * 
     * @param str
     * @param key
     * @param spaced
     *            should we space at the end of each key use
     */
    public void decryptCipherText(String str, String key, boolean spaced) {
        System.out.print("Cipher Text: ");
        // print ckpher text
        for (int i = 0; i < str.length(); i++) {
            if (i % key.length() == 0 && spaced) {
                System.out.print(" ");
            }
            System.out.print(str.charAt(i));
        }
        System.out.println(" ");
        System.out.print("Plain Text:  ");
        str = str.toLowerCase();
        // print plain text
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            char k = key.charAt(i % key.length());
            int oppShift = map.get(c) - map.get(k);
            if (oppShift < 0) {
                oppShift = 26 + oppShift;
            }
            char p = letters[(oppShift) % 26];
            if (i % key.length() == 0 && spaced) {
                System.out.print(" ");
            }
            System.out.print(p);
        }
        System.out.println(" ");
    }


    /**
     * Given some cipher text, compute the frequency of each cipher text
     * character to see if it is a monoalphabetic cipher, or a candidate for
     * Vigenere decryption
     * 
     * @param str
     */
    public void printLetterFrequency(String str) {
        System.out.println("___________________________________");
        System.out.println("BEGIN LETTER FREQUENCY:");
        str = str.toLowerCase();
        double[] counts = new double[26];
        // sum occurences
        for (int i = 0; i < str.length(); i++) {
            int c = map.get(str.charAt(i));
            counts[c]++;
        }
        // create averages
        for (int i = 0; i < 26; i++) {
            counts[i] = counts[i] / str.length();
            counts[i] = counts[i] * 100;
            System.out.println("Occurences of " + letters[i] + " is: "
                + counts[i]);
        }

    }
}