package fr.ibragim.e_expense.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import fr.ibragim.e_expense.Metier.Depense;
import fr.ibragim.e_expense.Metier.Trajet;
import fr.ibragim.e_expense.R;
import fr.ibragim.e_expense.Views.FragmentType;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrajetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrajetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrajetFragment extends Fragment implements FragmentType{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int year, month, day;

    private OnFragmentInteractionListener mListener;


    //TRAJET FIELDS
    private EditText distanceField;
    private EditText dureeField;
    private EditText dateAller;
    private EditText dateRetour;
    private EditText villeDepart;
    private EditText villeArrivee;

    private JSONObject currentTrajet;


    public void setDistanceField(double distanceField) {
        this.distanceField.setText(String.valueOf(distanceField));
    }

    public void setDureeField(double dureeField) {
        this.dureeField.setText(String.valueOf(dureeField));
    }

    public void setDateAller(String dateAller) {
        this.dateAller.setText(dateAller);
    }

    public void setDateRetour(String dateRetour) {
        this.dateRetour.setText(dateRetour);
    }

    public void setVilleDepart(String villeDepart) {
        this.villeDepart.setText(villeDepart);
    }

    public void setVilleArrivee(String villeArrivee) {
        this.villeArrivee.setText(villeArrivee);
    }


    public String getDistanceField() {
        return this.distanceField.getText().toString();
    }

    public String getDureeField() {
        return this.dureeField.getText().toString();
    }

    public String getDateAller() {
        return dateAller.getText().toString();
    }

    public String getDateRetour() {
        return dateRetour.getText().toString();
    }

    public String getVilleDepart() {
        return villeDepart.getText().toString();
    }

    public String getVilleArrivee() {
        return villeArrivee.getText().toString();
    }



    public TrajetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrajetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrajetFragment newInstance(String param1, String param2) {
        TrajetFragment fragment = new TrajetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    View rootView;
    EditText from_date, to_date;
    DatePickerDialog.OnDateSetListener date;
    static final int DATE_DIALOG_ID = 999;
    Calendar myCalendar;
    int val;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trajet, container, false);

        distanceField = view.findViewById(R.id.distanceTrajet);
        dureeField = view.findViewById(R.id.dureeTrajet);
        dateAller = view.findViewById(R.id.dateAllerTrajet);
        dateRetour = view.findViewById(R.id.dateRetourTrajet);
        villeDepart = view.findViewById(R.id.villeDepartTrajet);
        villeArrivee = view.findViewById(R.id.villeArriveeTrajet);

        context = getActivity();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                // TODO Auto-generated method stub
                year = selectedYear;
                month = selectedMonth;
                day = selectedDay;

                // Show selected date
                dateAller.setText(new StringBuilder().append(day)
                        .append("/").append(month + 1).append("/").append(year));
            }

        };
        dateAller.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialog(DATE_DIALOG_ID);
                val = 1;
            }
        });

        dateRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        return view;
    }



    protected Dialog showDialog(int dateDialogId) {
        final Calendar now = Calendar.getInstance();
        switch (dateDialogId) {

            case DATE_DIALOG_ID:
                DatePickerDialog _date = new DatePickerDialog(context, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)){

                };
        }

        return null;
    }



    public void initCurrentDepense(JSONObject trajet){
        this.currentTrajet = trajet;
    }


    @Override
    public void onStart(){
        super.onStart();


        if (this.currentTrajet != null){
            try {
                distanceField.setText(String.valueOf(currentTrajet.getDouble("distanceKilometres")));
                dureeField.setText(String.valueOf(currentTrajet.getDouble("dureeTrajet")));
                dateAller.setText(currentTrajet.getString("dateAller"));
                dateRetour.setText(currentTrajet.getString("dateRetour"));
                villeDepart.setText(currentTrajet.getString("villeDepart"));
                villeArrivee.setText(currentTrajet.getString("villeArrivee"));

                if (this.currentTrajet.getString("etatValidation").equals("Validé") || currentTrajet.getString("etatValidation").equals("Refusé")){
                    distanceField.setEnabled(false);
                    dureeField.setEnabled(false);
                    dateAller.setEnabled(false);
                    dateRetour.setEnabled(false);
                    villeDepart.setEnabled(false);
                    villeArrivee.setEnabled(false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public int getFragmentType() {
        return FragmentType.TrajetFragment;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
