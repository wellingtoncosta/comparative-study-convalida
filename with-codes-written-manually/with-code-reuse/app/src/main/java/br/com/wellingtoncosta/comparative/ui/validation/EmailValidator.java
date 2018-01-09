package br.com.wellingtoncosta.comparative.ui.validation;

import android.support.design.widget.TextInputLayout;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

/**
 * @author Wellington Costa on 08/01/2018.
 */
public class EmailValidator extends BaseValidator {

    public EmailValidator(TextInputLayout textInputLayout, String errorMessage) {
        super(textInputLayout, errorMessage);
    }

    @Override
    protected boolean isValid(String value) {
        return compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", CASE_INSENSITIVE).matcher(value).matches();
    }

}