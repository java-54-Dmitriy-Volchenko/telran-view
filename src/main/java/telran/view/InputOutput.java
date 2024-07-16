package telran.view;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Predicate;

public interface InputOutput {
	String readString(String prompt);

	void writeString(String str);

	default void writeLine(Object obj) {
		writeString(obj.toString() + "\n");
	}

	default <T> T readObject(String prompt, String errorPrompt, Function<String, T> mapper) {
		T res = null;
		boolean running = false;
		do {
			String str = readString(prompt);
			running = false;
			try {
				res = mapper.apply(str);
			} catch (RuntimeException e) {
				writeLine(errorPrompt + " " + e.getMessage());
				running = true;
			}

		} while (running);
		return res;
	}

	/**
	 * 
	 * @param prompt
	 * @param errorPrompt
	 * @return Integer number
	 */
	default Integer readInt(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, str -> {
			try {
				return Integer.parseInt(str);
			} catch (NumberFormatException e) {
				throw new RuntimeException("Your input is not an integer");
			}
		});
	}

	default Long readLong(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, str -> {
			try {
				return Long.parseLong(str);
			} catch (NumberFormatException e) {
				throw new RuntimeException("Your input is not a long");
			}
		});
	}

	default Double readDouble(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, str -> {
			try {
				return Double.parseDouble(str);
			} catch (NumberFormatException e) {
				throw new RuntimeException("Your input is not a double");
			}
		});
	}

	default Double readNumberRange(String prompt, String errorPrompt, double min, double max) {
		return readObject(prompt, errorPrompt, str -> {
			double number;
			try {
				number = Double.parseDouble(str);
			} catch (NumberFormatException e) {
				throw new RuntimeException("Your input is not a number");
			}
			if (number < min || number >= max) {
				throw new RuntimeException("Your number not in range");
			}
			return number;
		});
	}
	
	default String readStringPredicate(String prompt, String errorPrompt, Predicate<String> predicate) {
		return readObject(prompt, errorPrompt, str -> {
			if (!predicate.test(str)) {
				throw new RuntimeException("No matching the predicate");
			}
			return str;
		});
	}

	default String readStringOptions(String prompt, String errorPrompt, HashSet<String> options) {
		return readObject(prompt, errorPrompt, str -> {
			if (!options.contains(str)) {
				throw new RuntimeException("String doesn't matching options");
			}
			return str;
		});
	}

	default LocalDate readIsoDate(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, str -> {
			try {
				return LocalDate.parse(str);
			} catch (Exception e) {
				throw new RuntimeException("Date format is not mathing ISO requirements");
			}
		});
	}

	default LocalDate readIsoDateRange(String prompt, String errorPrompt, LocalDate from, LocalDate to) {
		return readObject(prompt, errorPrompt, str -> {
			LocalDate date;
			try {
				date = LocalDate.parse(str);
			} catch (Exception e) {
				throw new RuntimeException("Date format is not mathing ISO requirements");
			}
			if (date.isBefore(from) || date.isAfter(to)) {
				throw new RuntimeException("Date is not in range");
			}
			return date;
		});
	}
}

