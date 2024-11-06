package nl.rocnijmegen.testing;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.NumberFormat;

import static org.junit.jupiter.api.Assertions.*;

class AppIntegrationTest {

    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    @Test
    public void testFullHypotheekBerekeningMetCorrecteInvoer() {
        // Simuleer gebruikersinvoer
        String input = "1234\n2000\nja\n2000\nja\n1";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        // Vang de standaarduitvoer op
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Voer de applicatie uit
        App.main(new String[0]);

        // Zet de standaarduitvoer terug naar de oorspronkelijke staat
        System.setOut(originalOut);

        // Vang de uitvoer op en controleer deze
        String actualOutput = outputStream.toString();
        String expectedOutput = "Totale betaling over 1 jaar: £181,955.95";
        assertTrue(actualOutput.contains(expectedOutput));
    }

    @Test
    public void testFullHypotheekBerekeningMetOnjuisteInvoer() {
        // Simuleer gebruikersinvoer
        String input = "1234ab\n2000.0\nnee\nnee\n3";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        // Vang de standaarduitvoer op
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Voer de applicatie uit
        App.main(new String[0]);

        // Zet de standaarduitvoer terug naar de oorspronkelijke staat
        System.setOut(originalOut);

        // Vang de uitvoer op en controleer deze
        String actualOutput = outputStream.toString();
        String expectedOutput = "Ongeldige rentevaste periode. Kies uit 1, 5, 10, 20 of 30 jaar.";
        assertTrue(actualOutput.contains(expectedOutput));
    }

    @Test
    public void testFullHypotheekBerekeningMetOnjuisteInvoer2() {
        // Simuleer gebruikersinvoer
        String input = "9679";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        // Vang de standaarduitvoer op
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Voer de applicatie uit
        App.main(new String[0]);

        // Zet de standaarduitvoer terug naar de oorspronkelijke staat
        System.setOut(originalOut);

        // Vang de uitvoer op en controleer deze
        String actualOutput = outputStream.toString();
        String expectedOutput = "Hypotheekberekeningen voor deze postcode zijn niet toegestaan vanwege het aardbevingsgebied.";
        assertTrue(actualOutput.contains(expectedOutput));
    }

}
