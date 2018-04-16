package fr.ibragim.e_expense.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

    private OnFragmentInteractionListener mListener;


    //TRAJET FIELDS
    EditText distanceField;
    EditText dureeField;
    EditText dateAller;
    EditText dateRetour;
    EditText villeDepart;
    EditText villeArrivee;

    Trajet depense;


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


    public double getDistanceField() {
        double distance = Double.parseDouble(this.distanceField.getText().toString());
        return distance;
    }

    public double getDureeField() {
        double duree = Double.parseDouble(this.dureeField.getText().toString());
        return duree;
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

        return view;
    }

    public void initCurrentDepense(Trajet trajet){
        this.depense = trajet;
    }


    @Override
    public void onStart(){
        super.onStart();


        if (this.depense != null){
            distanceField.setText(String.valueOf(this.depense.getDistanceKM()));
            dureeField.setText(String.valueOf(this.depense.getDureeTrajet()));
            dateAller.setText(this.depense.getDateAller());
            dateRetour.setText(this.depense.getDateRetour());
            villeDepart.setText(this.depense.getVilleDepart());
            villeArrivee.setText(this.depense.getVilleArrivee());
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
