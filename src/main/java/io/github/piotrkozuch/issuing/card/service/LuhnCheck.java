package io.github.piotrkozuch.issuing.card.service;

public class LuhnCheck {

    public static int calculateControlDigit(String payload) {
        var checksum = luhnChecksum(payload + "0");
        return checksum == 0 ? 0 : 10 - checksum;
    }

    public static boolean isValidCardNumber(String cardNumber) {
        return luhnChecksum(cardNumber) == 0;
    }

    private static int luhnChecksum(String code) {
        var length = code.length();
        var parity = length % 2;
        var sum = 0;
        for (var i = length - 1; i >= 0; i--) {
            var d = Character.getNumericValue(code.charAt(i));
            if (i % 2 == parity) {
                d *= 2;
            }
            if (d > 9) {
                d -= 9;
            }
            sum += d;
        }
        return sum % 10;
    }

}
