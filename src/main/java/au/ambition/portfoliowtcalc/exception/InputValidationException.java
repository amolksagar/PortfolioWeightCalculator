package au.ambition.portfoliowtcalc.exception;

public class InputValidationException extends Exception {


/**
 * @author Amol Kshirsagar
 *
 */
	private static final long serialVersionUID = 1L;

	public InputValidationException() {
	}
	
	public InputValidationException(String message) {
		System.out.println("Invalid Input Validation Exception "+message);
	}
	
	public InputValidationException(String message,Throwable e) {
		System.out.println("Invalid Input Validation Exception "+message);
		e.printStackTrace();
	}
	
}
