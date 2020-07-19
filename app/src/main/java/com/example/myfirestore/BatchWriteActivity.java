package com.example.myfirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

public class BatchWriteActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = db.collection("Users Info");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_write);

        executeBatchedWrites();
    }

    private void executeBatchedWrites()
    {
        WriteBatch batch = db.batch();
        DocumentReference doc1 = collectionReference.document("New Info");
        batch.set(doc1, new MyMultipleInfo("Leo", "Messi",1 ));

        DocumentReference doc2 = collectionReference.document("qoLgAGtuyCUE3lBspIJt");
        batch.update(doc2, "firstName", "Mateo");

        DocumentReference doc3 = collectionReference.document("auz8QSwo1wxXk0zLE1Wq");
        batch.delete(doc3);

        DocumentReference doc4 = collectionReference.document();
        batch.set(doc4, new MyMultipleInfo("Added firstName", "Added surName", 1));

        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(BatchWriteActivity.this, "Added, Modified and Deleted Info\nSee Firebase Console ", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BatchWriteActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void transactionActivity(View view)
    {
        startActivity(new Intent(BatchWriteActivity.this,TransactionsActivity.class));
    }
}