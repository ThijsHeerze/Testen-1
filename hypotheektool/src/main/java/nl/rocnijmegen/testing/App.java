package nl.rocnijmegen.testing;

import java.text.NumberFormat;
import java.util.Scanner;

public class App {

    public static final double INTEREST_1_YEAR = 2.0;
    public static final double INTEREST_5_YEAR = 3.0;
    public static final double INTEREST_10_YEAR = 3.5;
    public static final double INTEREST_20_YEAR = 4.5;
    public static final double INTEREST_30_YEAR = 5.0;
    public static final double STUDENT_LOAN_REDUCTION = 0.75;
    public static final int MAX_LOAN_MULTIPLIER = 5;

    public static void main(String[] args) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        Scanner scanner = new Scanner(System.in);

        String postcode = getPostcode(scanner);
        if (isPostcodeRestricted(postcode)) {
            showErrorMessage("Hypotheekberekeningen voor deze postcode zijn niet toegestaan vanwege het aardbevingsgebied.");
            return;
        }

        double inkomen = getIncome(scanner, "Wat is uw maandinkomen? €");
        double partnerInkomen = getPartnerIncome(scanner);
        boolean heeftStudieschuld = hasStudentLoan(scanner);
        int jaren = getRentevastePeriode(scanner);

        if (jaren == -1) {
            showErrorMessage("Ongeldige rentevaste periode. Kies uit 1, 5, 10, 20 of 30 jaar.");
            return;
        }

        double totaalInkomen = calculateTotalIncome(inkomen, partnerInkomen);
        double maximaalLeenbedrag = calculateMaxLoan(totaalInkomen, heeftStudieschuld);
        double rente = getInterestRate(jaren);
        double maandlasten = calculateMonthlyMortgagePayment(maximaalLeenbedrag, rente, jaren);
        double totaalBetaling = calculateTotalPayment(maandlasten, jaren);
        double totaleRente = calculateTotalInterest(totaalBetaling, maximaalLeenbedrag);

        showResults(currencyFormat, maximaalLeenbedrag, maandlasten, totaleRente, jaren, totaalBetaling);
    }

    public static String getPostcode(Scanner scanner) {
        System.out.print("Wat is uw postcode? ");
        return scanner.nextLine();
    }

    public static boolean isPostcodeRestricted(String postcode) {
        return postcode.equals("9679") || postcode.equals("9681") || postcode.equals("9682");
    }

    public static double getIncome(Scanner scanner, String message) {
        System.out.print(message);
        return Double.parseDouble(scanner.nextLine());
    }

    public static double getPartnerIncome(Scanner scanner) {
        System.out.print("Heeft u een partner? (ja/nee) ");
        String response = scanner.nextLine();
        if (response.equalsIgnoreCase("ja")) {
            return getIncome(scanner, "Wat is het maandinkomen van uw partner? €");
        }
        return 0;
    }

    public static boolean hasStudentLoan(Scanner scanner) {
        System.out.print("Heeft u een studieschuld? (ja/nee) ");
        String response = scanner.nextLine();
        return response.equalsIgnoreCase("ja");
    }

    public static int getRentevastePeriode(Scanner scanner) {
        System.out.print("Kies een rentevaste periode: 1, 5, 10, 20 of 30 jaar ");
        try {
            int jaren = Integer.parseInt(scanner.nextLine());
            if (jaren == 1 || jaren == 5 || jaren == 10 || jaren == 20 || jaren == 30) {
                return jaren;
            }
        } catch (NumberFormatException e) {
            // Invalid input handling
        }
        return -1;
    }

    public static double calculateTotalIncome(double inkomen, double partnerInkomen) {
        return inkomen + partnerInkomen;
    }

    public static double calculateMaxLoan(double totaalInkomen, boolean heeftStudieschuld) {
        double maxLoan = totaalInkomen * 12 * MAX_LOAN_MULTIPLIER;
        if (heeftStudieschuld) {
            maxLoan *= STUDENT_LOAN_REDUCTION;
        }
        return maxLoan;
    }

    public static double getInterestRate(int jaren) {
        switch (jaren) {
            case 1: return INTEREST_1_YEAR;
            case 5: return INTEREST_5_YEAR;
            case 10: return INTEREST_10_YEAR;
            case 20: return INTEREST_20_YEAR;
            case 30: return INTEREST_30_YEAR;
            default: throw new IllegalArgumentException("Ongeldige rentevaste periode.");
        }
    }

    public static double calculateMonthlyMortgagePayment(double maxLoan, double interestRate, int jaren) {
        double maandRente = interestRate / 100 / 12;
        int aantalBetalingen = jaren * 12;
        return maxLoan * (maandRente * Math.pow(1 + maandRente, aantalBetalingen)) /
                (Math.pow(1 + maandRente, aantalBetalingen) - 1);
    }

    public static double calculateTotalPayment(double maandlasten, int jaren) {
        return maandlasten * jaren * 12;
    }

    public static double calculateTotalInterest(double totaalBetaling, double maxLoan) {
        return totaalBetaling - maxLoan;
    }

    public static void showResults(NumberFormat currencyFormat, double maxLoan, double maandlasten, double totaleRente, int jaren, double totaalBetaling) {
        System.out.println("Op basis van uw inkomen kunt u maximaal lenen: " + currencyFormat.format(maxLoan));
        System.out.println("Maandelijkse lasten: " + currencyFormat.format(maandlasten));
        System.out.println("Maandelijks te betalen rente: " + currencyFormat.format(totaleRente / (jaren * 12)));
        System.out.println("Maandelijks af te lossen bedrag: " + currencyFormat.format(maandlasten - (totaleRente / (jaren * 12))));
        System.out.println("Totale betaling over " + jaren + " jaar: " + currencyFormat.format(totaalBetaling));
    }

    public static void showErrorMessage(String message) {
        System.out.println("Error: " + message);
    }
}
