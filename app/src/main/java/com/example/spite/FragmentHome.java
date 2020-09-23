package com.example.spite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentHome extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
   // private UserDBHandler dbh = new UserDBHandler();

    //Start Workout Button
    private Button startWorkout;

    //Progress Bars
    private ProgressBar userMainPB;
    private ProgressBar kyleMainPB;

    //Set Workout Dialog Variables
    private NumberPicker setWorkoutHr;
    private NumberPicker setWorkoutMin;
    private Button setWorkoutBtn;
    private Button cancelBtn;
    private String[] hourValues;
    private String[] minValues;

    //User variables
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String USER_UID = user.getUid();
    private static final String GOAL_KEY = "goal";
    private static final String KYLE_UID_KEY = "kyleUID";

    //Display fragment with layout res file fragment_home
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    //Initialising StartWorkout Button and Listener inside the Fragment
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        startWorkout = requireView().findViewById(R.id.startWorkout);
        userMainPB = requireView().findViewById(R.id.UserProgressMainScreen);
        kyleMainPB = requireView().findViewById(R.id.KyleProgressMainScreen);





        startWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSetWorkoutDialog();
            }
        });


        //Dummy values, access 7 day progress for com.example.spite.User and Kyle, make an int.
        //Need to access workout info in conjunction w goal to work out daily progress as a %
        DocumentReference mDocRef = db.collection("User").document(USER_UID);
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                double goal = documentSnapshot.getDouble(GOAL_KEY);
                String kyleID = documentSnapshot.getString(KYLE_UID_KEY);
                userMainPB.setProgress( (int) goal );

                DocumentReference kDocRef = db.collection("User").document(kyleID);
                kDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        double kGoal = documentSnapshot.getDouble(GOAL_KEY);
                        kyleMainPB.setProgress( (int) kGoal );
                    }
                });
            }
        });
    }

    /*Opens a number picker dialog to set workout time and passes the variable to Start Workout Activity*/
    public void showSetWorkoutDialog() {
        /*Enables the Build of a Custom Dialog Layout*/
        AlertDialog.Builder setWorkoutBuilder = new AlertDialog.Builder(requireActivity());
        View setWorkoutView = getLayoutInflater().inflate(R.layout.dialog_set_workout, null);

        setWorkoutHr = setWorkoutView.findViewById(R.id.setWorkoutHr);
        setWorkoutMin = setWorkoutView.findViewById(R.id.setWorkoutMin);
        setWorkoutBtn = setWorkoutView.findViewById(R.id.setWorkoutBtn);
        cancelBtn = setWorkoutView.findViewById(R.id.cancelBtn);

        /*Set Max and Min values displayed, and Customise Selector Wheel in Hour*/
        setWorkoutHr.setMinValue(0);
        setWorkoutHr.setMaxValue(12);
        /* Code to display double digits. Will reassess later.*/
        hourValues = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        setWorkoutHr.setDisplayedValues(hourValues);

        setWorkoutHr.setWrapSelectorWheel(true);

        /*Set Max and Min values displayed, and Customise Selector Wheel in Minutes*/
        setWorkoutMin.setMinValue(0);
        setWorkoutMin.setMaxValue(59);
        /* Code to display double digits*/
        minValues = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
                "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25",
                "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38",
                "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51",
                "52", "53", "54", "55", "56", "57", "58", "59"};
        setWorkoutMin.setDisplayedValues(minValues);

        setWorkoutMin.setWrapSelectorWheel(true);

        /*Display the dialog*/
        setWorkoutBuilder.setView(setWorkoutView);
        final AlertDialog setWorkoutDialog = setWorkoutBuilder.create();
        setWorkoutDialog.show();

        /*setWorkoutBtn, when pressed, will take the values in the number picker, send it to an activity, and exit*/

        setWorkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Retrieve and store user workout time*/
                int finalHr = Integer.parseInt(String.valueOf(setWorkoutHr.getValue()));
                int finalMin = Integer.parseInt(String.valueOf(setWorkoutMin.getValue()));

                /*Sends set hour and minutes user inputted into CurrentWorkout*/
                Intent sendWorkoutTime = new Intent(getActivity(), CurrentWorkout.class);
                sendWorkoutTime.putExtra("hour", finalHr);
                sendWorkoutTime.putExtra("minute", finalMin);
                getActivity().startActivity(sendWorkoutTime);
                startActivity(sendWorkoutTime);

                /*Test to see if values are correct in Logcat*/
                Log.d("picker value", String.valueOf(finalHr));
                Log.d("picker value", String.valueOf(finalMin));
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Exits 'Set Workout Time' dialog without closing the activity*/
                setWorkoutDialog.cancel();
            }
        });
    }
}
