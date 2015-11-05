package project_team2;

import java.lang.reflect.Field;

/**
 * Created by Ethan on 2015-09-24.
 */
public interface Feature {

    public Field[] getNumericAttributes();

    public Field[] getNominalAttributes();

    public void setLabel(String label);

}
