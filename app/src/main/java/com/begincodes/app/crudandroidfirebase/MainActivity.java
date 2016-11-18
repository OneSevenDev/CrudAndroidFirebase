package com.begincodes.app.crudandroidfirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.begincodes.app.crudandroidfirebase.adapters.UserRecycleAdapter;
import com.begincodes.app.crudandroidfirebase.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private boolean unocero = false;

    private TextView name;
    private EditText nombreEditText;
    private EditText apellidosEditText;
    private EditText edadEditText;
    private Button guardarButton;

    private String nombre, apellidos, edad;

    private static FirebaseDatabase database;
    private DatabaseReference mRoot, mRef;

    private RecyclerView recyclerView;
    private UserRecycleAdapter recycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Quitar focus EditText
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Referencia a la base de datos en firebase
        database = FirebaseDatabase.getInstance();

        name = (TextView) findViewById(R.id.name);
        nombreEditText = (EditText) findViewById(R.id.nombreEditText);
        apellidosEditText = (EditText) findViewById(R.id.apellidosEditText);
        edadEditText = (EditText) findViewById(R.id.edadEditText);

        //Creacion del RecycleView y su adaptador
        recyclerView = (RecyclerView) findViewById(R.id.listPerson);
        recycleAdapter = new UserRecycleAdapter(this);
        recyclerView.setAdapter(recycleAdapter);
        recyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        getData();

        guardarButton = (Button) findViewById(R.id.guardarButton);
        guardarButton.setOnClickListener(this);
    }

    private void getData() {
        //Solo estoy haciendo el llamado a un solo dato, para ver si funciona o no
        mRoot = database.getReference("users");
        mRoot.child("-KWij7oYbhYKVKtX5KfZ").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    ArrayList<User> listUser = new ArrayList<>();
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        listUser.add(user);
                    } else {
                        User u = new User();
                        u.setNombre("Error");
                        u.setApellidos("Error");
                        u.setEdad(00);
                        listUser.add(u);
                    }
                    recycleAdapter.addItemUser(listUser);
                    Log.i(TAG, "Put data list -> " + recycleAdapter.getItemCount());

                } catch (Exception ex) {
                    Log.e(TAG, "Error: " + ex.getMessage());
                    throw ex;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Cancelado: ", databaseError.toException());
                Toast.makeText(getApplicationContext(), R.string.cancelError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.guardarButton:
                nombre = nombreEditText.getText().toString();
                apellidos = apellidosEditText.getText().toString();
                edad = edadEditText.getText().toString();

                guardarDatos(nombre, apellidos, edad);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mRoot = database.getReference("users");
    }

    private void guardarDatos(String nombre, String apellidos, String edad) {
        int nedad = Integer.parseInt(edad.toString());
        if (nedad >= 0) {
            unocero = true;
            if (unocero) {
                User user = new User();
                user.setNombre(nombre);
                user.setApellidos(apellidos);
                user.setEdad(nedad);
                String key = mRoot.push().getKey();
                mRoot.child(key).setValue(user);
                nombreEditText.setText("");
                apellidosEditText.setText("");
                edadEditText.setText("");
                unocero = false;
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.datoNoNumeric, Toast.LENGTH_SHORT).show();
            edadEditText.setText("");
        }
    }
}
