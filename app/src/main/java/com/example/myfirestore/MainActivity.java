package com.example.myfirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String FIRST_NAME = "firstName";
    private static final String SUR_NAME = "surName";
    private EditText myFirstName, mySurName;
    private TextView dataView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef = db.document("Users Info/My Info"); //(One Way)
                            //docRef = db.collection("Users Info").document("My Info"); (Other Way)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myFirstName = findViewById(R.id.myFirstName);
        mySurName = findViewById(R.id.mySurName);
        dataView = findViewById(R.id.dataView);
    }

    // Saving data in FireStore
    public void saveButton(View view)
    {
        String firstName = myFirstName.getText().toString().trim();
        String surName = mySurName.getText().toString().trim();

        /*Replaced Hash Map with Model Class MyInfo
        Map<String, Object> info = new HashMap<>();
        info.put(FIRST_NAME, firstName);
        info.put(SUR_NAME, surName);
         */

        MyInfo info = new MyInfo(firstName,surName);

        docRef.set(info)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        myFirstName.setText("");
                        mySurName.setText("");
                        Toast.makeText(MainActivity.this, "Info Saved !!!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Retrieving data from FireStore (One Way) (By Clicking on a Button manually)
    public void viewData(View view)
    {
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    /* Replaced this with a Model Class MyInfo
                    //(One Way)
                    String firstName = documentSnapshot.getString(FIRST_NAME);
                    String surName = documentSnapshot.getString(SUR_NAME);
                    dataView.setText("First Name: " + firstName + "\n" + "Sur Name: " + surName);

                    (Other Way)
                    Map<String, Object> info = documentSnapshot.getData();
                    String firstName = info.get(FIRST_NAME).toString();
                    String surName = info.get(SUR_NAME).toString();
                    dataView.setText("First Name: " + firstName + "\n" + "Sur Name: " + surName);
                     */
                     MyInfo info = documentSnapshot.toObject(MyInfo.class);
                     String firstName = info.getFirstName();
                     String surName = info.getSurName();
                     dataView.setText("First Name: " + firstName + "\n" + "Sur Name: " + surName);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Document does not exists !!!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Retrieving data from FireStore (Other Way) (Automatically Updates the view (Text view or any other) without hitting any button )

    @Override
    protected void onStart() {
        super.onStart();
        docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null)
                {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (value.exists())
                {
                    /* Replaced with Model Class MyInfo
                    String firstName = value.getString(FIRST_NAME);
                    String surName = value.getString(SUR_NAME);
                    dataView.setText("First Name: " + firstName + "\n" + "Sur Name: " + surName);
                     */
                    MyInfo info = value.toObject(MyInfo.class);
                    String firstName = info.getFirstName();
                    String surName = info.getSurName();
                    dataView.setText("First Name: " + firstName + "\n" + "Sur Name: " + surName);
                }
                else
                {
                    dataView.setText("");
                }
            }
        });
    }

    public void updateFirstName(View view)
    {
        String firstName = myFirstName.getText().toString().trim();
        //Map<String, Object> info = new HashMap<>();
        //info.put(FIRST_NAME, firstName);
        // Only updates first name and sets surname as null
        //docRef.set(info);
        // Updates first name and leave surname as it is (If the document doesn't exist and
        // we update first name, then a new document is created and first name is set, with sur name as null)
        //docRef.set(info, SetOptions.merge());
        // Updates first name and leave surname as it is (If the document doesn't exist and
        // we update first name, then no new document is created)
        docRef.update(FIRST_NAME, firstName);
    }

    public void deleteFirstName(View view)
    {
        // (One Way)
        //Map<String, Object> info = new HashMap<>();
        //info.put(FIRST_NAME, FieldValue.delete());
        //docRef.update(info);
        // (Other Way)
        docRef.update(FIRST_NAME, FieldValue.delete())
              .addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void aVoid) {

                      Toast.makeText(MainActivity.this, "First Name Deleted !!!", Toast.LENGTH_SHORT).show();
                  }
              });
    }

    public void deleteMyInfo(View view)
    {
        docRef.delete()
              .addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void aVoid) {
                      Toast.makeText(MainActivity.this, "Info Deleted", Toast.LENGTH_SHORT).show();
                  }
              });
    }

    public void nextActivity(View view)
    {
        startActivity(new Intent(MainActivity.this, MultipleDocument.class));
    }
}