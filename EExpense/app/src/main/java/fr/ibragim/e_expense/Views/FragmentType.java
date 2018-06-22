package fr.ibragim.e_expense.Views;

import android.app.Dialog;

/**
 * Created by ibragim.abubakarov on 08/04/2018.
 */

public interface FragmentType {
    int FraisFragment = 1;
    int TrajetFragment = 2;

    Dialog onCreateDialog(int id);

    int getFragmentType();

}
