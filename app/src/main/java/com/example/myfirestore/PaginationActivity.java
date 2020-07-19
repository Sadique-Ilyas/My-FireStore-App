package com.example.myfirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PaginationActivity extends AppCompatActivity {
    private EditText myFirstName2, mySurName2, myPriority2;
    private Button saveButton2, viewInfoButton2, viewInfoButton3;
    private TextView viewInfo2;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users Info");
    private DocumentSnapshot lastResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagination);

        myFirstName2 = findViewById(R.id.firstName2);
        mySurName2 = findViewById(R.id.surName2);
        myPriority2 = findViewById(R.id.myPriority2);
        viewInfo2 = findViewById(R.id.viewInfo2);
        saveButton2 = findViewById(R.id.saveButton2);
        viewInfoButton2 = findViewById(R.id.viewInfoButton2);
        viewInfoButton3 = findViewById(R.id.viewInfoButton3);

        //Saving Info
        saveButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = myFirstName2.getText().toString().trim();
                String surName = mySurName2.getText().toString().trim();

                if (myPriority2.length() == 0) {
                    myPriority2.setText("0");
                }
                int priority = Integer.parseInt(myPriority2.getText().toString());

                MyMultipleInfo info = new MyMultipleInfo(firstName, surName, priority);
                collectionReference.add(info)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(PaginationActivity.this, "Info Saved !", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PaginationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Retrieving info
        viewInfoButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query;
                if (lastResult == null)
                {
                    query = collectionReference.orderBy("priority")
                            .limit(3);
                }
                else
                {
                    query = collectionReference.orderBy("priority")
                            .startAfter(lastResult)
                            .limit(3);
                }
                query.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                String data = "";

                                for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots)
                                {
                                    MyMultipleInfo info = documentSnapshot.toObject(MyMultipleInfo.class);
                                    info.setDocumentId(documentSnapshot.getId());

                                    String Id = info.getDocumentId();
                                    String firstName = info.getFirstName();
                                    String surName = info.getSurName();
                                    int priority = info.getPriority();
                                    data += "Id: " + Id + "\nFirst Name: " + firstName + "\nSur Name: " + surName + "\nPriority: " + priority + "\n\n";
                                }
                                if (queryDocumentSnapshots.size() > 0) {
                                    data += "__________________\n\n";
                                    viewInfo2.append(data);

                                    lastResult = queryDocumentSnapshots.getDocuments()
                                            .get(queryDocumentSnapshots.size() - 1);
                                }
                            }
                        });
            }
        });

        viewInfoButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectionReference.document("OSzwQqS0huhqPaQGvLxo")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                collectionReference.orderBy("priority")
                                        .startAt(documentSnapshot)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                String data = "";

                                                for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots)
                                                {
                                                    MyMultipleInfo info = documentSnapshot.toObject(MyMultipleInfo.class);
                                                    info.setDocumentId(documentSnapshot.getId());

                                                    String Id = info.getDocumentId();
                                                    String firstName = info.getFirstName();
                                                    String surName = info.getSurName();
                                                    int priority = info.getPriority();
                                                    data += "Id: " + Id + "\nFirst Name: " + firstName + "\nSur Name: " + surName + "\nPriority: " + priority + "\n\n";
                                                }
                                                viewInfo2.setText(data);
                                            }
                                        });
                            }
                        });
            }
        });
    }

    public void documentChangesActivity(View view)
    {
        startActivity(new Intent(PaginationActivity.this,DocumentChangesActivity.class));
    }
}