/**
 * Small utility for reading validated user input from the console.
 */
public final class UserInput {
    private UserInput() { /* utility */ }

    /**
     * Read an integer value from the given scanner that lies in the inclusive range [min, max].
     * The method will prompt repeatedly until a valid value is entered.
     *
     * @param prompt the text prompt to show before reading a line
     * @param min minimum acceptable value (inclusive)
     * @param max maximum acceptable value (inclusive)
     * @param scanner scanner to read lines from (e.g., GameManager.SCANNER)
     * @return the validated integer input
     */
    public static int readIntInRange(String prompt, int min, int max, java.util.Scanner scanner) {
        if (scanner == null) throw new IllegalArgumentException("scanner must not be null");
        int value = Integer.MIN_VALUE;
        while (value < min || value > max) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                value = Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }
            if (value < min || value > max) {
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            }
        }
        return value;
    }
}
