package br.com.wellingtoncosta.comparative.ui;

import android.support.design.widget.TextInputLayout;

import com.mobsandgeeks.saripaar.adapter.ViewDataAdapter;
import com.mobsandgeeks.saripaar.exception.ConversionException;

/**
 * @author Wellington Costa on 02/10/2017.
 */
public class TextInputLayoutAdapter implements ViewDataAdapter<TextInputLayout, String> {

    @Override
    public String getData(TextInputLayout view) throws ConversionException {
        return view.getEditText().getText().toString();
    }
}
