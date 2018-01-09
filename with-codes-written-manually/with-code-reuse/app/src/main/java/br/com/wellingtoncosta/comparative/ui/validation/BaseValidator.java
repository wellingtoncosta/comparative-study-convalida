package br.com.wellingtoncosta.comparative.ui.validation;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * @author Wellington Costa on 08/01/2018.
 */
public abstract class BaseValidator {

    private final TextInputLayout textInputLayout;
    private final EditText editText;
    private final String errorMessage;
    private boolean hasError;

    BaseValidator(TextInputLayout textInputLayout, String errorMessage) {
        this.textInputLayout = textInputLayout;
        this.editText = textInputLayout.getEditText();
        this.errorMessage = errorMessage;
        this.hasError = false;

        addTextChangeListener();
    }

    protected abstract boolean isValid(String value);

    boolean validate() {
        if (isValid(editText.getText().toString())) {
            clearError();
        } else {
            setError();
        }

        return !hasError;
    }

    private void addTextChangeListener() {
        if(textInputLayout.getEditText() != null) {
            textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    validate();
                }

                @Override
                public void afterTextChanged(Editable s) { }
            });
        }
    }

    private void setError() {
        if (textInputLayout != null) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(errorMessage);
        }

        hasError = true;
    }

    private void clearError() {
        if (textInputLayout != null) {
            textInputLayout.setErrorEnabled(false);
            textInputLayout.setError(null);
        }

        hasError = false;
    }

}