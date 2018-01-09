package br.com.wellingtoncosta.comparative.ui.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wellington Costa on 08/01/2018.
 */
public final class ValidatorSet {

    private final List<BaseValidator> validators;

    public ValidatorSet() {
        this.validators = new ArrayList<>();
    }

    public void addValidator(BaseValidator validator) {
        validators.add(validator);
    }

    public boolean validate() {
        boolean fieldsAreValid = true;

        for (BaseValidator validator : validators) {
            if(!validator.validate()) {
                fieldsAreValid = false;
            }
        }

        return fieldsAreValid;
    }

}