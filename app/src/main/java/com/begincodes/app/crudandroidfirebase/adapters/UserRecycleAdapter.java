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

    private Context context;
    private ArrayList<User> listUser;

    public UserRecycleAdapter(Context context) {
        this.context = context;
        this.listUser = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listuserview_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = listUser.get(position);
        holder.nombre.setText(user.getNombre());
        holder.apellidos.setText(user.getApellidos());
        holder.edad.setText(user.getEdad());
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public void addItemUser(ArrayList<User> list) {
        listUser.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nombre;
        private TextView apellidos;
        private TextView edad;

        public ViewHolder(View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.nombreTextView);
            apellidos = (TextView) itemView.findViewById(R.id.apellidosTextView);
            edad = (TextView) itemView.findViewById(R.id.edadTextView);

        }
    }
}
