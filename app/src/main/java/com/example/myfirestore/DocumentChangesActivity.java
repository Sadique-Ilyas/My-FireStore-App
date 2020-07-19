package com.example.myfirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class DocumentChangesActivity extends AppCompatActivity {
    private Button saveButton3;
    private EditText myFirstName3, mySurName3, myPriority3;
    private TextView viewInfo3;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users Info");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_changes);

        myFirstName3 = findViewById(R.id.firstName3);
        mySurName3 = findViewById(R.id.surName3);
        myPriority3 = findViewById(R.id.myPriority3);
        viewInfo3 = findViewById(R.id.viewInfo3);;
        saveButton3 = findViewById(R.id.saveButton3);

        saveButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = myFirstName3.getText().toString().trim();
                String surName = mySurName3.getText().toString().trim();

                if (myPriority3.length() == 0) {
                    myPriority3.setText("0");
                }
                int priority = Integer.parseInt(myPriority3.getText().toString());

                MyMultipleInfo info = new MyMultipleInfo(firstName, surName, priority);
                collectionReference.add(info)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(DocumentChangesActivity.this, "Info Saved !", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DocumentChangesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null)
                {
                    return;
                }
                for (DocumentChange documentChange: value.getDocumentChanges())
                {
                    DocumentSnapshot documentSnapshot = documentChange.getDocument();
                    String id = documentSnapshot.getId();
                    int oldIndex = documentChange.getOldIndex();
                    int newIndex = documentChange.getNewIndex();
                    switch (documentChange.getType())
                    {
                        case ADDED: viewInfo3.append("\nAdded: " +id+ "\nOld Index: " +oldIndex+
                                " New Index: " +newIndex);
                        break;
                        case MODIFIED: viewInfo3.append("\nModified: " +id+ "\nOld Index: " +oldIndex+
                                " New Index: " +newIndex);
                            break;
                        case REMOVED: viewInfo3.append("\nRemoved: " +id+ "\nOld Index: " +oldIndex+
                                " New Index: " +newIndex);
                            break;
                    }
                }
            }
        });
    }

    public void batchWrites(View view)
    {
        startActivity(new Intent(DocumentChangesActivity.this,BatchWriteActivity.class));
    }
}