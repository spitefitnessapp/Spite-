package com.example.spite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
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

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EndWorkout extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button toMainBtn;
    private Button toProgressBtn;
    private TextView goalTitleTV;
    private TextView loggedTitleTV;
    private TextView workoutGoalTV;
    private TextView loggedTimeTV;
    private ProgressBar userEndPB;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String USER_UID = user.getUid();
    private final String PROGRESS_KEY = "timeLogged";
    private final String GOAL_KEY = "goal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_workout);

        toMainBtn = findViewById(R.id.endToMainBtn);
        toProgressBtn = findViewById(R.id.endToProgressBtn);
        goalTitleTV = findViewById(R.id.endGoalTitle);
        loggedTitleTV = findViewById(R.id.endLoggedTitle);
        workoutGoalTV = findViewById(R.id.endWorkoutGoal);
        loggedTimeTV = findViewById(R.id.endLoggedTime);
        userEndPB = findViewById(R.id.UserProgressEndWorkout);


        /*Sends user back to the FragmentHome*/
        toMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EndWorkout.this, MainActivity.class);
                EndWorkout.this.startActivity(intent);
            }
        });

        /*TODO: Send user to FragmentProgress. Temporarily sends user to FragmentHome*/
        toProgressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EndWorkout.this, MainActivity.class);
                intent.putExtra("TabView", "EndWorkoutToProgress");
                startActivity(intent);
            }
        });

        /*Shows the user their Set Workout Goal and their Logged Time for their workout for comparison*/
        Intent getWorkoutGoal = getIntent();
        int hour = getWorkoutGoal.getIntExtra("hour",0);
        int minute = getWorkoutGoal.getIntExtra("minute", 0);
        String goal = String.format("%02d", hour) + ":" + String.format("%02d", minute) ;
        workoutGoalTV.setText(goal);

        String loggedTime = getWorkoutGoal.getStringExtra("loggedTimeString");
        loggedTimeTV.setText(loggedTime);

        //setting the progress bar
        DocumentReference mDocRef = db.collection("User").document(USER_UID);
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                final double goal = documentSnapshot.getDouble(GOAL_KEY);
                userEndPB.setProgress( (int) goal );

                db.collection("User").document(USER_UID).collection("WeeklyWorkout")
                        .orderBy("date", Query.Direction.DESCENDING)
                        .limit(1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("HERE", document.getId() + " => " + document.getData());
                                        final String thisWeek = document.getId();
                                        Calendar cal = Calendar.getInstance();

                                        Date date = cal.getTime();
                                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                        String strDate = dateFormat.format(date);
                                        DateFormat dayFormat = new SimpleDateFormat("EEE");
                                        String day = dayFormat.format(date);
                                        final String title = strDate+day;
                                        Log.d("MAD", "Title passed in is: " + title);

                                        DocumentReference docRef0 = db.collection("User").document(USER_UID).collection("WeeklyWorkout").document(thisWeek)
                                                .collection("DailyWorkout").document(title);
                                        docRef0.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        Log.d("MAD", "docRef0 docSnap data TODAY");
                                                        double progress = document.getDouble(PROGRESS_KEY);
                                                        Log.d("MAD", "Progress is " + progress );
                                                        Log.d("MAD", "Goal is " + goal );

                                                        double finalProg = 100 - (((goal-progress)/goal)*100);
                                                        Log.d("MAD", "Final output is " + finalProg );
                                                        userEndPB.setProgress( (int) finalProg );

                                                    } else {
                                                        Log.d("MAD", "User prog bar main screen no progress");

                                                    }
                                                } else {
                                                    //end of docRef0
                                                    Log.d("MAD", "get failed with ", task.getException());
                                                }
                                            }

                                        });

                                    }
                                } else {
                                    Log.d("HERE", "Error getting documents: ", task.getException());
                                }
                            }
                        }); //end of Query for weekly workout Title
            }

        });//end of mDocRef
    }
}