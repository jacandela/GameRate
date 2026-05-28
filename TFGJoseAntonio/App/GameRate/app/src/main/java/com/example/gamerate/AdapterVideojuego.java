package com.example.gamerate;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.List;

public class AdapterVideojuego extends RecyclerView.Adapter<AdapterVideojuego.VideojuegoViewHolder> {

    private List<Videojuego> listaVideojuegos;
    private Context context;
    private OnVideojuegoClickListener listener;

    public interface OnVideojuegoClickListener {
        void onItemClick(Videojuego videojuego);
        void onItemLongClick(Videojuego videojuego);
    }

    public AdapterVideojuego(List<Videojuego> listaVideojuegos, Context context, OnVideojuegoClickListener listener) {
        this.listaVideojuegos = listaVideojuegos;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VideojuegoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_videojuego, parent, false);
        return new VideojuegoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull VideojuegoViewHolder holder, int position) {
        Videojuego videojuego = listaVideojuegos.get(position);

        holder.tvTitulo.setText(videojuego.getTitulo());
        holder.tvDesarrollador.setText(videojuego.getDesarrollador());

        if (videojuego.getPlataformas() != null && !videojuego.getPlataformas().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < videojuego.getPlataformas().size(); i++) {
                sb.append(videojuego.getPlataformas().get(i));
                if (i < videojuego.getPlataformas().size() - 1) {
                    sb.append(", ");
                }
            }
            holder.tvPlataformas.setText(sb.toString());
            holder.tvPlataformas.setTextColor(Color.parseColor("#2196F3"));
        } else {
            holder.tvPlataformas.setText("Sin plataformas");
            holder.tvPlataformas.setTextColor(Color.parseColor("#9E9E9E"));
        }

        android.util.Log.d("ImagenDebug", "URL recibida: " + videojuego.getImagen());

        //Se Usa esta URLImagenTemporal para demostrar que el glider funciona
        String urlImagenTemporal = "https://picsum.photos/id/1/300/200";
        Glide.with(context)
                .load(urlImagenTemporal)
                .placeholder(android.R.color.darker_gray)
                .error(android.R.drawable.ic_menu_gallery)
                .transform(new RoundedCorners(24))
                .into(holder.ivPortada);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(videojuego);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onItemLongClick(videojuego);
                v.showContextMenu();
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return listaVideojuegos.size();
    }

    public static class VideojuegoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvDesarrollador, tvPlataformas; // Cambiamos tvNota por tvPlataformas
        ImageView ivPortada;

        public VideojuegoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvItemTitulo);
            tvDesarrollador = itemView.findViewById(R.id.tvItemGenero);
            tvPlataformas = itemView.findViewById(R.id.tvItemPlataformas); // Asegúrate de actualizar el ID
            ivPortada = itemView.findViewById(R.id.ivGameCover);
        }
    }
}
