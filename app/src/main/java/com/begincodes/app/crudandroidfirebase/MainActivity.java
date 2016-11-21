package com.begincodes.app.crudandroidfirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.begincodes.app.crudandroidfirebase.models.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private boolean unocero = false;

    private EditText nombreEditText;
    private EditText apellidosEditText;
    private EditText edadEditText;
    private Button guardarButton;

    private String nombre, apellidos, edad;

    private static FirebaseDatabase database;
    private DatabaseReference mRoot, mRef;

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<User, ViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Referencia a la base de datos en firebase
        database = FirebaseDatabase.getInstance();
        mRoot = database.getReference();

        //Inicializando variables
        nombreEditText = (EditText) findViewById(R.id.nombreEditText);
        apellidosEditText = (EditText) findViewById(R.id.apellidosEditText);
        edadEditText = (EditText) findViewById(R.id.edadEditText);

        //Creacion del RecycleView y su adaptador
        recyclerView = (RecyclerView) findViewById(R.id.listPerson);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        guardarButton = (Button) findViewById(R.id.guardarButton);
        guardarButton.setOnClickListener(this);
    }

    private void getData() {
        //llamo a una lista de datos Json usando noSQL
        //mRef = mRoot.child("users");
        mRef.keepSynced(true);
        mAdapter = new FirebaseRecyclerAdapter<User, ViewHolder>(User.class, R.layout.listuserview_item_row, ViewHolder.class, mRef) {

            @Override
            protected void populateViewHolder(ViewHolder v, final User model, int position) {

                final String key = getRef(position).getKey();

                v.nombre.setText(String.valueOf(model.getNombre()));
                v.apellidos.setText(String.valueOf(model.getApellidos()));
                v.edad.setText(String.valueOf(model.getEdad()));

                v.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra(DetailActivity.EXTRA_POST_KEY, key);
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(mAdapter);
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
        mRef = mRoot.child("users");
        getData();
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
                String key = mRef.push().getKey();
                mRef.child(key).setValue(user);
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
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nombre;
        private TextView apellidos;
        private TextView edad;

        public ViewHolder(View itemView) {
            super(itemView);
            //Indico a cada variable con que Control estara trabajando (Inicializando Controles o Labels que es lo mismo)
            nombre = (TextView) itemView.findViewById(R.id.nombreTextView);
            apellidos = (TextView) itemView.findViewById(R.id.apellidosTextView);
            edad = (TextView) itemView.findViewById(R.id.edadTextView);
        }
    }
}