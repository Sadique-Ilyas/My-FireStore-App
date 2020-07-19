package com.example.myfirestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MergingTask extends AppCompatActivity {
    private EditText myFirstName1, mySurName1, myPriority1;
    private Button saveButton1, viewInfoButton1;
    private TextView viewInfo1;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users Info");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merging_task);

        myFirstName1 = findViewById(R.id.firstName1);
        mySurName1 = findViewById(R.id.surName1);
        myPriority1 = findViewById(R.id.myPriority1);
        viewInfo1 = findViewById(R.id.viewInfo1);
        saveButton1 = findViewById(R.id.saveButton1);
        viewInfoButton1 = findViewById(R.id.viewInfoButton1);

        //Saving Info
        saveButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = myFirstName1.getText().toString().trim();
                String surName = mySurName1.getText().toString().trim();

                if (myPriority1.length() == 0) {
                    myPriority1.setText("0");
                }
                int priority = Integer.parseInt(myPriority1.getText().toString());

                MyMultipleInfo info = new MyMultipleInfo(firstName, surName, priority);
                collectionReference.add(info)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(MergingTask.this, "Info Saved !", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MergingTask.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Retrieving info
        viewInfoButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // (Sorting and other things)
                Task task1 = collectionReference.whereLessThan("priority", 2) // results with priority under certain range
                        .orderBy("priority")
                        .get();

                Task task2 = collectionReference.whereGreaterThan("priority", 2)
                        .orderBy("priority")
                        .get();

                Task<List<QuerySnapshot>> allTask = Tasks.whenAllSuccess(task1, task2);
                allTask.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
                    @Override
                    public void onSuccess(List<QuerySnapshot> querySnapshots) {
                        String data = "";
                        for (QuerySnapshot queryDocumentSnapshots : querySnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                MyMultipleInfo info = documentSnapshot.toObject(MyMultipleInfo.class);
                                info.setDocumentId(documentSnapshot.getId());
                                String Id = info.getDocumentId();
                                String firstName = info.getFirstName();
                                String surName = info.getSurName();
                                int priority = info.getPriority();
                                data += "Id: " + Id + "\nFirst Name: " + firstName + "\nSur Name: " + surName + "\nPriority: " + priority + "\n\n";
                            }
                        }
                        viewInfo1.setText(data);
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        collectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                String data = "";
                for (QueryDocumentSnapshot documentSnapshot : value) {
                    MyMultipleInfo info = documentSnapshot.toObject(MyMultipleInfo.class);
                    info.setDocumentId(documentSnapshot.getId());
                    String Id = info.getDocumentId();
                    String firstName = info.getFirstName();
                    String surName = info.getSurName();
                    int priority = info.getPriority();
                    data += "Id: " + Id + "\nFirst Name: " + firstName + "\nSur Name: " + surName + "\nPriority: " + priority + "\n\n";
                }
                viewInfo1.setText(data);
            }
        });
    }

    public void paginationActivity(View view)
    {
        startActivity(new Intent(MergingTask.this, PaginationActivity.class));
    }
}