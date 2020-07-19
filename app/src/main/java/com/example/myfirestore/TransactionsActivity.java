package com.example.myfirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

public class TransactionsActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = db.collection("Users Info");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        executeTransaction();
    }

    private void executeTransaction()
    {
        db.runTransaction(new Transaction.Function<Long>()
        {
            @Override
            public Long apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference documentReference = collectionReference.document("New Info");
                DocumentSnapshot documentSnapshot = transaction.get(documentReference);
                long newPriority = documentSnapshot.getLong("priority") + 1;
                transaction.update(documentReference, "priority", newPriority);
                return newPriority;
            }
        }).addOnSuccessListener(new OnSuccessListener<Long>() {
            @Override
            public void onSuccess(Long result) {
                Toast.makeText(TransactionsActivity.this, "New Priority: " +result, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void arraysActivity(View view)
    {
        startActivity(new Intent(TransactionsActivity.this, ArraysActivity.class));
    }
}