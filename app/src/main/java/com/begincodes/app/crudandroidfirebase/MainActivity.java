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

        //Inicializando variables
        name = (TextView) findViewById(R.id.name);
        nombreEditText = (EditText) findViewById(R.id.nombreEditText);
        apellidosEditText = (EditText) findViewById(R.id.apellidosEditText);
        edadEditText = (EditText) findViewById(R.id.edadEditText);

        //Creacion del RecycleView y su adaptador
        recyclerView = (RecyclerView) findViewById(R.id.listPerson);
        // instancia del Adapter para la lista
        recycleAdapter = new UserRecycleAdapter(this);
        //agregando el adapter al recycleView (parecido a ListView solo que en MaterialDesing)
        recyclerView.setAdapter(recycleAdapter);
        recyclerView.setHasFixedSize(true);
        //Condiguro la estricutra de que sea a una fila                    (---)
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        //agrego la fila a la lista
        recyclerView.setLayoutManager(layoutManager);

        guardarButton = (Button) findViewById(R.id.guardarButton);
        guardarButton.setOnClickListener(this);
    }

    private void getData() {
        //llamo a una lista de datos Json usando noSQL
        mRoot = database.getReference("users");
        //Indico exactamente el dato que quiero mostar, hasta aqui funciona normal
        mRoot.child("-KWij7oYbhYKVKtX5KfZ").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    ArrayList<User> listUser = new ArrayList<>();
                    //Recojo mi entidad de datos en User, funciona normal
                    User user = dataSnapshot.getValue(User.class);
                    //Compruebo si mi lista es vacia
                    if (user != null) {
                        //Esta llena, si llena los daots
                        listUser.add(user);
                    } else {
                        //En el caso de que este vacio, le puse datos falsos
                        User u = new User();
                        u.setNombre("Error");
                        u.setApellidos("Error");
                        u.setEdad(00);
                        //agrego datos a la lista
                        listUser.add(u);
                    }

                    //Agrego datos a li adapter
                    recycleAdapter.addItemUser(listUser);
                    //Por si las dudas imprimimo en el "Monitor" para ver si aun conserva los datos, si conserva datos
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
        getData();
        getUsersIDs();
    }

    private void getUsersIDs() {
        mRoot = database.getReference("users");
        mRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {

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
