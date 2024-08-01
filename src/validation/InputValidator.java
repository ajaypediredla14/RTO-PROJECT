package validation;


public class InputValidator {

    public static boolean isValidMobile(String mobile) {
        return mobile.matches("\\d{10}");
    }

    public static boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static boolean isValidPassword(String password) {
        return password.length() >= 6;  // Example validation
    }

    public static boolean isValidAadhar(String aadhar) {
        return aadhar.matches("\\d{12}"); // Assuming Aadhar number is 12 digits
    }



    // Add other validations as needed
}
