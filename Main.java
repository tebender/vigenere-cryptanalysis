/**
 * Runs project
 * 
 * @author Tim Bender
 * @version 2019.05.21
 *
 */
public class Main {
    public static void main(String[] args) {
        DecryptVigenere dv = new DecryptVigenere();
        dv.tryAllShifts(args[4]);
        dv.decryptCipherText(args[4], args[1], false);
        String[] arr = dv.makeSubstrings(args[4], args[1].length());
        double[][] table = dv.indexOfCoincidenceTable(arr);
        dv.dumpTable(table);
        dv.printLetterFrequency(args[2]);
        dv.printLetterFrequency(args[3]);
    }
}
