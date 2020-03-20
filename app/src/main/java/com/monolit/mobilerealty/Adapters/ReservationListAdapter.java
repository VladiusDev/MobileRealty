package com.monolit.mobilerealty.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.monolit.mobilerealty.RealtorObjects.Reservation;
import com.monolit.mobilerealty.R;

import java.util.List;

public class ReservationListAdapter extends RecyclerView.Adapter<ReservationListAdapter.ReservationViewHolder>{

    public List<Reservation> reservations;
    private Context context;

    @NonNull
    @Override
    public ReservationListAdapter.ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_reservation_card, parent,false);

        context = view.getContext();

        return new ReservationListAdapter.ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);

        holder.client.setText(reservation.getClient());
        holder.data.setText(reservation.getData());
        holder.object.setText(reservation.getObject());
        holder.reservation.setText(reservation.getReservation());

        if (reservation.getStatus() == 1) {
            holder.status.setText(context.getString(R.string.reservation_agreed));
            holder.status.setBackground(context.getDrawable(R.drawable.reservation_status_reserved));
        }else{
            holder.status.setText(context.getString(R.string.reservation_on_reservation));
            holder.status.setBackground(context.getDrawable(R.drawable.reservation_status_on_reservation));
        }
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public class ReservationViewHolder extends RecyclerView.ViewHolder{
        TextView object;
        TextView client;
        TextView data;
        TextView status;
        TextView reservation;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);

            object = itemView.findViewById(R.id.reservation_card_object);
            client = itemView.findViewById(R.id.reservation_card_client);
            data = itemView.findViewById(R.id.reservation_card_data);
            status = itemView.findViewById(R.id.reservation_card_btn_status);
            reservation = itemView.findViewById(R.id.reservation_card_reservation);
        }
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
