package store.validator;

import store.util.Parser;
import java.util.List;

import static store.constants.ErrorConstants.INVALID_ETC;
import static store.constants.ErrorConstants.INVALID_INPUT_FORMAT;
import static store.constants.UtilContants.*;

public class InputValidator {
    private final static int SIZE = 2;

    public static void validateInput(String input) {
        checkNull(input);
        checkEmpty(input);
        List<String> splitInput = List.of(input.split(SEPARATOR));
        for (String single : splitInput) {
            validateSingleInput(single);
            splitSingleInput(single);
        }
    }

    private static void checkNull(String input) {
        if (input == null) {
            throw new IllegalArgumentException(INVALID_INPUT_FORMAT.getMessage());
        }
    }

    private static void checkEmpty(String input) {
        if (input.isEmpty()) {
            throw new IllegalArgumentException(INVALID_INPUT_FORMAT.getMessage());
        }
    }

    private static void checkSize(List<String> input) {
        if (input.size() != SIZE) {
            throw new IllegalArgumentException(INVALID_INPUT_FORMAT.getMessage());
        }
    }

    private static void validateSingleInput(String input) {
        validateFormat(input);
    }

    private static void validateFormat(String input) {
        if (!input.startsWith(OPEN_BRACKET) || !input.endsWith(CLOSE_BRACKET)) {
            throw new IllegalArgumentException(INVALID_INPUT_FORMAT.getMessage());
        }
    }

    private static void splitSingleInput(String input) {
        input = Parser.replaceBracket(input);
        List<String> splitInput = Parser.splitInput(input, SEPARATOR_DASH);
        checkSize(splitInput);
        validateCountIsNumber(splitInput.getLast());
        validateCountIsNonNegative(splitInput.getLast());
    }

    private static void validateCountIsNumber(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INVALID_INPUT_FORMAT.getMessage());
        }
    }

    private static void validateCountIsNonNegative(String input) {
        int number = Integer.parseInt(input);
        if (number <= 0) {
            throw new IllegalArgumentException(INVALID_INPUT_FORMAT.getMessage());
        }
    }

    public static void validateSimpleAnswer(String input) {
        if (!input.equals(YES) && !input.equals(NO)) {
            throw new IllegalArgumentException(INVALID_ETC.getMessage());
        }
    }
}
