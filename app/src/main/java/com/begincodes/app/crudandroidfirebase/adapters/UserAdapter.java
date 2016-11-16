package com.begincodes.app.crudandroidfirebase.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.begincodes.app.crudandroidfirebase.R;
import com.begincodes.app.crudandroidfirebase.models.User;

import java.util.ArrayList;

/**
 * Created by manuelguarniz on 16/11/16.
 */
public class UserAdapter extends ArrayAdapter<User> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<User> listUser = new ArrayList<>();

    public UserAdapter(Context context, int resource, ArrayList<User> listUser) {
        super(context, resource,listUser);
        this.context = context;
        this.layoutResourceId = resource;
        this.listUser = listUser;
    }

    public View getView(int position, View view, ViewGroup group){
        View row = view;
        UsersHolder holder = null;
        if(row==null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId,group,false);

            holder = new UsersHolder();
            holder.nombre = (TextView) row.findViewById(R.id.nombreTextView);
            holder.apellidos = (TextView) row.findViewById(R.id.apellidosTextView);
            holder.edad = (TextView) row.findViewById(R.id.edadTextView);
            row.setTag(holder);
        } else {
            holder = (UsersHolder) row.getTag();
        }

        holder.nombre.setText(listUser.get(position).getNombre());
        holder.apellidos.setText(listUser.get(position).getApellidos());
        holder.edad.setText(listUser.get(position).getEdad());

        return row;
    }

    static class UsersHolder{
        TextView nombre;
        TextView apellidos;
        TextView edad;
    }
}
