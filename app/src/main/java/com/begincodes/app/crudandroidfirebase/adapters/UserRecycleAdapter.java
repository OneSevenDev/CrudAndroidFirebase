package com.begincodes.app.crudandroidfirebase.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.begincodes.app.crudandroidfirebase.R;
import com.begincodes.app.crudandroidfirebase.models.User;

import java.util.ArrayList;

/**
 * Created by manuelguarniz on 18/11/16.
 */
public class UserRecycleAdapter extends RecyclerView.Adapter<UserRecycleAdapter.ViewHolder> {

    //Variables para el adaptador, context (Activity en el que se usara) y la lista
    private Context context;
    //X cierto si lo igualo a null es lo mismo
    private ArrayList<User> listUser;

    //Contructor
    public UserRecycleAdapter(Context context) {
        this.context = context;
        this.listUser = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Infla la vista con el XML que eh diseñado, solo tiene TextView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listuserview_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Llena los datos al XML, segun los 3 TextView que llene que son nombre apellidos y edad
        //Todo de acuerdo a los datos que este ingresando mediante el constructor y su posicion
        User user = listUser.get(position);
        holder.nombre.setText(String.valueOf(user.getNombre()));
        holder.apellidos.setText(String.valueOf(user.getApellidos()));
        holder.edad.setText(String.valueOf(user.getEdad()));
// lo puedes castear o parsear a string - ya esta
   }

    @Override
    public int getItemCount() {
        //Si es que llego a necesitar la cantidad de datos, simplemente se llama esta funcion
        return listUser.size();
    }

    //Mediante este metodo lleno la lista de datos al adaptador
    public void addItemUser(ArrayList<User> list) {
        if ( listUser != null){
            listUser.clear();
        }
        listUser.addAll(list);
        //Mediante esta linea indico al adaptor que ya tiene datos y los muestre cuando haya cambios
        //Si esta linea la comento, la APP carga normal xq no llena los datos al RecycleView o Lista que es lo mismo
        notifyDataSetChanged();
    }

    //Clase que es de la vista de datos, esta es la parde defino como sera el detallado de la lista
    public class ViewHolder extends RecyclerView.ViewHolder{
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
