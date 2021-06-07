package org.mds.inss.generator.utility;

import org.mds.inss.domain.Gender;
import org.mds.inss.generator.format.InssFormatUtil;
import org.mds.inss.util.GeneralBirthNumberUtil;

public class BirthNumberUtil {

    private static final int MAX_BIRTH_NUMBER = 997;

    public static String getBirthNumber(final String birthNumber, final Gender gender) {
        String parsedBirthNumber = parseBirthNumber(birthNumber);

        int lastTwoNumbersOfBirthNumber;
        if (parsedBirthNumber.length() == 1) {
            lastTwoNumbersOfBirthNumber = determineLastNumberOfBirthNumber(parsedBirthNumber);
        } else {
            lastTwoNumbersOfBirthNumber = determineLastTwoNumbersOfBirthNumber(parsedBirthNumber);
        }

        int correctedNumber = correctLastNumberOfBirthNumberIfNecessary(
                lastTwoNumbersOfBirthNumber,
                gender
        );
        return correctBirthNumberIfNecessary(
                birthNumber,
                correctedNumber
        );
    }

    private static String correctBirthNumberIfNecessary(final String birthNumber, int correctedNumber) {
        int originalLastBirthNumber;
        if (birthNumber.length() == 1) {
            originalLastBirthNumber = Integer.parseInt(birthNumber);
            return correctBirthNumberForSingleValue(originalLastBirthNumber, correctedNumber);
        } else {
            originalLastBirthNumber = Integer.parseInt(birthNumber.substring(birthNumber.length() - 2));
            return correctBirthNumberForMultipleValue(birthNumber, originalLastBirthNumber, correctedNumber);
        }
    }

    private static String correctBirthNumberForSingleValue(final int originalLastBirtNumber, int correctedNumber) {
        if (originalLastBirtNumber != correctedNumber && correctedNumber < 9) {
            return InssFormatUtil.addAdditionalZerosBeforeBirthNumber(correctedNumber);
        } else if (correctedNumber >= 10) {
            return InssFormatUtil.addAdditionalCharacterAtStartOfString(String.valueOf(correctedNumber), '0');
        }
        return InssFormatUtil.addAdditionalZerosBeforeBirthNumber(originalLastBirtNumber);
    }

    private static String correctBirthNumberForMultipleValue(final String birthNumber, int originalLastBirthNumber, int correctedNumber) {
        if (originalLastBirthNumber != correctedNumber) {
            String firstTwoNumbers = birthNumber.substring(0, birthNumber.length() - 2);
            String correctedLastedNumbers = firstTwoNumbers + correctedNumber;
            if (correctedLastedNumbers.length() == 2) {
                return InssFormatUtil.addAdditionalCharacterAtStartOfString(correctedLastedNumbers, '0');
            }
            return correctedLastedNumbers;
        }
        if (birthNumber.length() == 2) {
            return InssFormatUtil.addAdditionalCharacterAtStartOfString(birthNumber, '0');
        }
        return birthNumber;
    }

    private static String parseBirthNumber(final String birthNumber) {
        int parsedBirthNumber = Integer.parseInt(birthNumber);
        if (parsedBirthNumber > MAX_BIRTH_NUMBER) {
            return String.valueOf(MAX_BIRTH_NUMBER);
        }
        return String.valueOf(parsedBirthNumber);
    }

    private static int correctLastNumberOfBirthNumberIfNecessary(final int lastTwoBirthNumbers, final Gender gender) {
        int lastTwoBirthNumbersModified = lastTwoBirthNumbers;
        if (Gender.MALE.equals(gender)) {
            if (GeneralBirthNumberUtil.isEven(lastTwoBirthNumbersModified)) {
                lastTwoBirthNumbersModified += 1;
            }
        } else {
            if (GeneralBirthNumberUtil.isOdd(lastTwoBirthNumbers)) {
                lastTwoBirthNumbersModified += 1;
            }
        }
        return lastTwoBirthNumbersModified;
    }

    private static int determineLastNumberOfBirthNumber(final String birthNumber) {
        return Integer.parseInt(birthNumber.substring(birthNumber.length() - 1));
    }

    private static int determineLastTwoNumbersOfBirthNumber(final String birthNumber) {
        return Integer.parseInt(birthNumber.substring(birthNumber.length() - 2));
    }
}
