package com.begincodes.app.crudandroidfirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.begincodes.app.crudandroidfirebase.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POST_KEY = "post_key";
    private static final String TAG = "DetailActivity";

    private String postKey;

    private TextView nombreDetail;
    private TextView apellidosDetail;
    private TextView edadDetail;

    private static FirebaseDatabase database;
    private DatabaseReference mRoot, mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Referencia a la base de datos en firebase
        database = FirebaseDatabase.getInstance();
        mRoot = database.getReference();

        nombreDetail = (TextView) findViewById(R.id.nombreDetail);
        apellidosDetail = (TextView) findViewById(R.id.apellidosDetail);
        edadDetail = (TextView) findViewById(R.id.edadDetail);

        postKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (postKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }
    }

    private void viewData(String postKey) {
        mRef.child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                nombreDetail.setText(String.valueOf(user.getNombre()));
                apellidosDetail.setText(String.valueOf(user.getApellidos()));
                edadDetail.setText(String.valueOf(user.getEdad()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Cancelado: ", databaseError.toException());
                Toast.makeText(getApplicationContext(), R.string.cancelError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRef = mRoot.child("users");
        viewData(postKey);
    }
}
