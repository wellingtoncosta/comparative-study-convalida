package br.com.wellingtoncosta.comparative.ui.validation;

import android.support.design.widget.TextInputLayout;

/**
 * @author Wellington Costa on 08/01/2018.
 */
public class ConfirmPasswordValidator extends BaseValidator {

    private final TextInputLayout passwordInputLayout;

    public ConfirmPasswordValidator(TextInputLayout passwordInputLayout, TextInputLayout confirmPasswordInput, String errorMessage) {
        super(confirmPasswordInput, errorMessage);
        this.passwordInputLayout = passwordInputLayout;
    }

    @Override
    protected boolean isValid(String value) {
        boolean passwordInputIsNotNull = passwordInputLayout != null && passwordInputLayout.getEditText() != null;
        return  passwordInputIsNotNull && passwordInputLayout.getEditText().getText().toString().equals(value);

    }

}