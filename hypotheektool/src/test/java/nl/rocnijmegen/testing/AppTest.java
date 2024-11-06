package nl.rocnijmegen.testing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    void testCalculateTotalIncome() {
        // Test without partner income
        assertEquals(3000.0, App.calculateTotalIncome(3000.0, 0.0));

        // Test with partner income
        assertEquals(5000.0, App.calculateTotalIncome(3000.0, 2000.0));
    }

    @Test
    void testCalculateMaxLoan() {
        // Test max loan without student loan
        assertEquals(180000.0, App.calculateMaxLoan(250.0 * 12, false));

        // Test max loan with student loan reduction
        assertEquals(135000.0, App.calculateMaxLoan(250.0 * 12, true));
    }

    @Test
    void testGetInterestRate() {
        // Test each valid period
        assertEquals(2.0, App.getInterestRate(1));
        assertEquals(3.0, App.getInterestRate(5));
        assertEquals(3.5, App.getInterestRate(10));
        assertEquals(4.5, App.getInterestRate(20));
        assertEquals(5.0, App.getInterestRate(30));

        // Test invalid period
        Exception exception = assertThrows(IllegalArgumentException.class, () -> App.getInterestRate(15));
        assertEquals("Ongeldige rentevaste periode.", exception.getMessage());
    }

    @Test
    void testCalculateMonthlyMortgagePayment() {
        double maxLoan = 180000.0;
        double interestRate = 3.0;
        int jaren = 5;

        double monthlyPayment = App.calculateMonthlyMortgagePayment(maxLoan, interestRate, jaren);

        // Validate monthly payment with an approximate due to rounding
        assertEquals(3220.0, monthlyPayment, 40);
    }

    @Test
    void testCalculateTotalPayment() {
        double maandlasten = 3220.0;
        int jaren = 5;

        double totaalBetaling = App.calculateTotalPayment(maandlasten, jaren);

        // Validate total payment with an approximate due to rounding
        assertEquals(193200.0, totaalBetaling, 0.5);
    }

    @Test
    void testCalculateTotalInterest() {
        double totaalBetaling = 193200.0;
        double maxLoan = 180000.0;

        double totaleRente = App.calculateTotalInterest(totaalBetaling, maxLoan);

        // Validate total interest with an approximate due to rounding
        assertEquals(13200.0, totaleRente, 0.5);
    }

    @Test
    void testIsPostcodeRestricted() {
        // Restricted postcodes
        assertTrue(App.isPostcodeRestricted("9679"));
        assertTrue(App.isPostcodeRestricted("9681"));
        assertTrue(App.isPostcodeRestricted("9682"));

        // Non-restricted postcode
        assertFalse(App.isPostcodeRestricted("1000"));
    }
}
