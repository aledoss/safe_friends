package com.example.diaz.alejandro.nicolas.safefriends.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diaz.alejandro.nicolas.safefriends.R;
import com.example.diaz.alejandro.nicolas.safefriends.database.DBHelper;
import com.example.diaz.alejandro.nicolas.safefriends.database.ParadaUser;

import java.util.ArrayList;

/**
 * Created by Lenwe on 01/10/2016.
 */
public class AdapterListaParadas extends ArrayAdapter<ParadaUser> implements View.OnClickListener {
    Context context;
    ArrayList<ParadaUser> listaParadas = new ArrayList<>();

    public AdapterListaParadas(Context context, ArrayList<ParadaUser> listaParadas) {
        super(context, R.layout.row_parada, listaParadas);
        this.context = context;
        this.listaParadas = listaParadas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ParadaUser paradaUser = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_parada, parent, false);
            viewHolder.txtNombrParada = (TextView) convertView.findViewById(R.id.txtNombreParada);
            viewHolder.btnBorrarParada = (Button) convertView.findViewById(R.id.btnBorrarParada);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtNombrParada.setText(paradaUser.getNameParada());
        viewHolder.btnBorrarParada.setOnClickListener((View.OnClickListener) this);
        viewHolder.btnBorrarParada.setTag(position);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        switch (v.getId()){
            case R.id.btnBorrarParada:
                borrarParada(position);
                break;
        }
    }

    private void borrarParada(int id) {
        DBHelper db = new DBHelper(context);
        ParadaUser paradaUser = getItem(id);
        if (db.deleteParadaUser(paradaUser.getId())) {
            listaParadas.clear();
            listaParadas.addAll(db.getAllParadaUser());
            notifyDataSetChanged();
            Toast.makeText(context, "Parada borrada: " + paradaUser.getNameParada(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    private static class ViewHolder{
        TextView txtNombrParada;
        Button btnBorrarParada;
    }

}
