package com.begincodes.app.crudandroidfirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.begincodes.app.crudandroidfirebase.models.User;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    private boolean unocero = false;

    private TextView nombreTextView;
    private EditText nombreEditText;
    private EditText apellidosEditText;
    private EditText edadEditText;
    private Button guardarButton;

    private ListView listView;
    private View header;

    private String nombre, apellidos, edad;

    private static  FirebaseDatabase database;
    private DatabaseReference myRef;

    private ArrayList<User> listUser = null;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();

        nombreTextView = (TextView) findViewById(R.id.nombreTextView);
        nombreEditText = (EditText) findViewById(R.id.nombreEditText);
        apellidosEditText = (EditText) findViewById(R.id.apellidosEditText);
        edadEditText = (EditText) findViewById(R.id.edadEditText);

        myRef.child("-KWikEhFJfwBq45gDzS2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    User user = dataSnapshot.getValue(User.class);
                    listUser.add(user);
                } catch (Exception ex){
                    Log.e(TAG,"Error: "+ex.getMessage());
                    throw  ex;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Cancelado: ", databaseError.toException());
                Toast.makeText(getApplicationContext(), R.string.cancelError,Toast.LENGTH_SHORT).show();
            }
        });

        listView = (ListView) findViewById(R.id.listPerson);
        header = (View) getLayoutInflater().inflate(R.layout.listuser_header_row,null);

        if (listUser != null){
            adapter = new ArrayAdapter(this,R.layout.listuserview_item_row,listUser);
        } else {
            User user = new User();
            user.setNombre("ErrorNombre");
            user.setApellidos("ErrorApellidos");
            user.setEdad(0);
            listUser =  new ArrayList<>();
            listUser.add(user);
            adapter = new ArrayAdapter(this,R.layout.listuserview_item_row,listUser);
        }

        listView.addHeaderView(header);
        listView.setAdapter(adapter);

        guardarButton = (Button) findViewById(R.id.guardarButton);
        guardarButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.guardarButton:
                nombre = nombreEditText.getText().toString();
                apellidos = apellidosEditText.getText().toString();
                edad = edadEditText.getText().toString();

                guardarDatos(nombre,apellidos,edad);
                break;
            default:break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        myRef = database.getReference("users");
    }

    private void guardarDatos(String nombre, String apellidos, String edad) {
        int nedad = Integer.parseInt(edad.toString());
        if ( nedad >= 0){
            unocero = true;
            if (unocero){
                User user = new User();
                user.setNombre(nombre);
                user.setApellidos(apellidos);
                user.setEdad(nedad);
                String key = myRef.push().getKey();
                myRef.child(key).setValue(user);
                nombreEditText.setText("");
                apellidosEditText.setText("");
                edadEditText.setText("");
                unocero = false;
            }
        } else {
            Toast.makeText(getApplicationContext(),R.string.datoNoNumeric,Toast.LENGTH_SHORT).show();
            edadEditText.setText("");
        }
    }
}
