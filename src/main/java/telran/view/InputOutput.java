package telran.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Predicate;

public interface InputOutput {
	String readString(String promt);
	void writeString(String str);
	default void writeLine(Object obj) {
		writeString(obj.toString()+ "\n");
	}
	default <T> T readObject(String prompt, String errorPromt, 
							Function<String, T> mapper) {    // Function interface
		T res = null;
		boolean running = false;
		do {
			String str = readString(prompt);
			running = false;
			try {
				res = mapper.apply(str); // принимает String, а возвращает Object T
			} catch (RuntimeException e) {
				writeLine(errorPromt + " " + e.getMessage());
				running = true;
			}
		}while(running);
		return res;
	}
	
	/*
	 * @param promt
	 * @param errorPromt
	 * @return Integer
	 */
	default Integer readInt(String prompt, String errorPrompt) {
		//Entered string must be a number otherwise, errorPromt with cycle
		return readObject(prompt, errorPrompt, Integer::parseInt);
	}
	
	default Long readLong(String prompt, String errorPrompt) {
		//Entered string must be a number otherwise, errorPromt with cycle
		return readObject(prompt, errorPrompt, Long::parseLong);
	}
	
	default Double readDouble(String prompt, String errorPrompt) {
		//Entered string must be a number otherwise, errorPromt with cycle
		return readObject(prompt, errorPrompt, Double::parseDouble);
	}
	
	default Double readNumberRange(String prompt, String errorPrompt,	
									double min, double max) {
		//Entered string must be a number in range (min <= number < max) 
		//otherwise, errorPromt with cycle
		return readObject(prompt, errorPrompt, str ->{
			double num = Double.parseDouble(str);
				if(num < min && num >= max) {
					throw new IllegalArgumentException();
				}
			return num;	
		});
	}
	
	default String readStringPredicate(String prompt, String errorPrompt,
			Predicate<String> predicate) {
		// DONE
		//Entered String must match a given predicate
		return readObject(prompt, errorPrompt, str -> {
			if(!predicate.test(str)) {
				throw new IllegalArgumentException();
			}
			return str;
		});
	}
	
	default String readStringOptions(String prompt, String errorPrompt, 
										HashSet<String> options){
		//Entered String must be one out of a given options
		return readObject(prompt, errorPrompt, str ->{
			if(!options.contains(str)) {
				throw new IllegalArgumentException();
			}
			return str;
		});
	}
	
	default LocalDate readIsoDate(String prompt, String errorPrompt) {
		//Entered String must be a LocalDate in format (yyyy-mm-dd)
		return readObject(prompt, errorPrompt, LocalDate::parse);
	}
	
	default LocalDate readIsoDateRange(String prompt, String errorPrompt, 
										LocalDate from, LocalDate to) {
		//Entered String must be a LocalDate in format (yyyy-mm-dd) in the (from, to)
		//after from and before to
		return readObject(prompt, errorPrompt, str ->{
			LocalDate date = LocalDate.parse(str);
			if(date.isBefore(from) && date.isAfter(to)) {
				throw new IllegalArgumentException();
			}
			return date;
		});
	}
	
}
