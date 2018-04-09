package fr.ibragim.e_expense.Views;

/**
 * Created by ibragim.abubakarov on 09/04/2018.
 */

public interface MainActivityFragmentType {
    int MainFragment = 1;
    int StatFragment = 2;
    int AccountFragment = 3;


    int getMenuType();

    void init(int USERID);

}
