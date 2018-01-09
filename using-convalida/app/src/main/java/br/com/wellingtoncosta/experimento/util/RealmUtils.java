package br.com.wellingtoncosta.experimento.util;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * @author  Wellington Costa on 20/09/2017.
 */
public class RealmUtils {

    public static<T extends RealmObject> int getNextValue(Class<T> clazz) {
        Realm realm = Realm.getDefaultInstance();
        Number currentIdNumber = realm.where(clazz).max(("id"));

        if (currentIdNumber == null) {
            return 1;
        } else {
            return currentIdNumber.intValue() + 1;
        }
    }
}
