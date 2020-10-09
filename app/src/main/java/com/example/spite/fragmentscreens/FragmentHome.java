//Currently displays values from hardcoded week, 21-09-2020. Searches for doc titles based on current date.
//Need to get QueryTask working to get current week title

package com.example.spite.fragmentscreens;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.spite.CurrentWorkout;
import com.example.spite.R;
import com.example.spite.models.WorkoutLog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.series.DataPoint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class FragmentHome extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // private UserDBHandler dbh = new UserDBHandler();

    //TextViews
    private TextView You;
    private TextView kyleMainTV;
    private TextView userProgValueTV;
    private TextView kyleProgValueTV;

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
    private static final String dailyTimeLogged = "dailyTimeLogged";
    private static final String KYLE_NAME_KEY = "kyle";

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
        You = requireView().findViewById(R.id.userMainTV);
        kyleMainTV = requireView().findViewById(R.id.kyleMainTV);
        userProgValueTV = requireView().findViewById(R.id.userProgValueTV);
        kyleProgValueTV = requireView().findViewById(R.id.kyleProgValueTV);



        startWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSetWorkoutDialog();
            }
        });

        //Dummy values, access 7 day progress for com.example.spite.models.User and Kyle, make an int.
        //Need to access workout info in conjunction w goal to work out daily progress as a %
        DocumentReference mDocRef = db.collection("User").document(USER_UID);
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                double docGoal = 0;
                String docKyleID = "";
                String docKyleName= "";

                if(documentSnapshot.getDouble(GOAL_KEY) != null) {
                     docGoal = documentSnapshot.getDouble(GOAL_KEY);
                     docKyleID = documentSnapshot.getString(KYLE_UID_KEY);
                     docKyleName = documentSnapshot.getString(KYLE_NAME_KEY);
                }

                final double goal = docGoal;
                final String kyleID = docKyleID;
                final String kyleName = docKyleName;

                kyleMainTV.setText(kyleName);

                                        Calendar cal = Calendar.getInstance();
                                        Date date = cal.getTime();
                                        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                                        String strDate = dateFormat.format(date);
                                        DateFormat dayFormat = new SimpleDateFormat("EEE");
                                        String day = dayFormat.format(date);
                                        final String title = strDate+day;
                                        Log.d("MAD", "Title passed in is: " + title);

                                        DocumentReference docRef0 = db.collection("User").document(USER_UID)
                                                .collection("DailyWorkout").document(title);
                                        docRef0.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        Log.d("MAD", "docRef0 docSnap data TODAY");
                                                        double progress = document.getDouble(dailyTimeLogged);
                                                        Log.d("MAD", "Progress is " + progress );
                                                        Log.d("MAD", "Goal is " + goal );

                                                        double finalProg = 100 - (((goal-progress)/goal)*100);
                                                        Log.d("MAD", "Final output is " + finalProg );
                                                        userMainPB.setProgress( (int) finalProg );
                                                        String prog = "" + progress;
                                                        userProgValueTV.setText( prog );
                                                    } else {
                                                        Log.d("MAD", "User prog bar main screen no progress");

                                                    }
                                                } else {
                                                    //end of docRef0
                                                    Log.d("MAD", "get failed with ", task.getException());
                                                }
                                            }

                                        });

                                        DocumentReference kDocRef = db.collection("User").document(kyleID);
                                        kDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                final double kGoal = documentSnapshot.getDouble(GOAL_KEY);


                                                DocumentReference docRefk = db.collection("User").document(kyleID)
                                                        .collection("DailyWorkout").document(title);
                                                docRefk.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            if (document.exists()) {
                                                                Log.d("MAD", "docRefk docSnap data TODAY");
                                                                double progress = document.getDouble(dailyTimeLogged);
                                                                Log.d("MAD", "Progress is " + progress );
                                                                Log.d("MAD", "Goal is " + kGoal );

                                                                double finalProg = 100 - (((kGoal-progress)/kGoal)*100);
                                                                Log.d("MAD", "Final kyle output is " + finalProg );
                                                                kyleMainPB.setProgress( (int) finalProg );
                                                                String prog = "" + progress;
                                                                kyleProgValueTV.setText( prog );


                                                            } else {
                                                                Log.d("MAD", "kyle prog bar main screen no progress");

                                                            }
                                                        } else {
                                                            Log.d("MAD", "get failed with ", task.getException());
                                                        }
                                                    }
                                                });//end of docRefk
                                            }
                                        });//end of kDocRef
            }

        });//end of mDocRef
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