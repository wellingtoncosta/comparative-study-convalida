package br.com.wellingtoncosta.comparative.ui.validation;

import android.support.design.widget.TextInputLayout;

/**
 * @author Wellington Costa on 08/01/2018.
 */
public class NotEmptyValidator extends BaseValidator {

    public NotEmptyValidator(TextInputLayout textInputLayout, String errorMessage) {
        super(textInputLayout, errorMessage);
    }

    @Override
    protected boolean isValid(String value) {
        return value != null && !value.isEmpty();
    }

}