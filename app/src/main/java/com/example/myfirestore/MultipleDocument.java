package com.example.myfirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MultipleDocument extends AppCompatActivity {
    private EditText myFirstName, mySurName, myPriority;
    private Button saveButton, viewInfoButton;
    private TextView viewInfo;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users Info");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_document);

        myFirstName = findViewById(R.id.firstName);
        mySurName = findViewById(R.id.surName);
        myPriority = findViewById(R.id.myPriority);
        viewInfo = findViewById(R.id.viewInfo);
        saveButton = findViewById(R.id.saveButton);
        viewInfoButton = findViewById(R.id.viewInfoButton);

        //Saving Info
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = myFirstName.getText().toString().trim();
                String surName = mySurName.getText().toString().trim();

                if (myPriority.length() == 0) {
                    myPriority.setText("0");
                }
                int priority = Integer.parseInt(myPriority.getText().toString());

                MyMultipleInfo info = new MyMultipleInfo(firstName, surName, priority);
                collectionReference.add(info)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(MultipleDocument.this, "Info Saved !", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MultipleDocument.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Retrieving info
        viewInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // (Sorting and other things)
                collectionReference.whereGreaterThan("priority", 1) // results with priority under certain range
                        .orderBy("priority")
                        .orderBy("firstName")// to sort in descending order
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                String data = "";
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    MyMultipleInfo info = documentSnapshot.toObject(MyMultipleInfo.class);
                                    info.setDocumentId(documentSnapshot.getId());
                                    String Id = info.getDocumentId();
                                    String firstName = info.getFirstName();
                                    String surName = info.getSurName();
                                    int priority = info.getPriority();
                                    data += "Id: " + Id + "\nFirst Name: " + firstName + "\nSur Name: " + surName + "\nPriority: " + priority + "\n\n";
                                }
                                viewInfo.setText(data);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MultipleDocument.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Firebase Error", e.getMessage());
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
                viewInfo.setText(data);
            }
        });
    }

    public void nxtActivity(View view)
    {
        startActivity(new Intent(MultipleDocument.this, MergingTask.class));
    }
}