package bizmessage.in.touchswitch.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    public static boolean isValidString(String s) {
        return !(s == null || s.isEmpty());
    }

    public static boolean isRequiredField(String s) {
        return isValidString(s.trim()) && getTrimedString(s.trim()).length() > 0;
    }

    public static boolean isValidMobile(String s) {
        return isValidString(s.trim()) && getTrimedString(s.trim()).length() == 10;
    }

    public static boolean isValidEmail(String email) {
        return isRequiredField(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(getValidateString(email)).matches();
    }

    public static String getTrimedString(final String s) {
        return s.trim();
    }

    public static String getValidateString(final String s) {
        return isRequiredField(s) ? s.replace("\\n", "\n") : "";
    }

    public static boolean isValidPassword(String password) {
        return password.length() >= 2;
    }

    private boolean isValidPasswordAdvance(String password) {
        String regexPassword = "^[a-z0-9_$@$!%*?&]{1,24}$";
        CharSequence inputStr = password;
        Pattern pattern = Pattern.compile(regexPassword, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }

    public static boolean isPasswordsMatch(String p1, String p2) {
        return p1.equals(p2);
    }

}
