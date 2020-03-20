package com.monolit.mobilerealty.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.monolit.mobilerealty.RealtorObjects.Client;
import com.monolit.mobilerealty.R;


import java.util.ArrayList;
import java.util.List;

public class ClientsListAdapter extends RecyclerView.Adapter<ClientsListAdapter.ClientViewHolder>{

    public  List<Client> clients;
    private OnClickClientListener onClickClientListener;
    private Context context;
    private OnReachEndListener onReachEndListener;

    public ClientsListAdapter() {
        this.clients = new ArrayList<>();
    }

    public interface OnClickClientListener{
        void OnClickClient(int position);
    }

    public interface OnReachEndListener{
        void onReachEnd();
    }

    public void setOnReachEndListener(OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    public void setOnClickClientListener(OnClickClientListener onClickClientListener) {
        this.onClickClientListener = onClickClientListener;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_client, parent,false);

        context = view.getContext();

        return new ClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ClientViewHolder holder, int position) {
        if (position == clients.size() -10 && onReachEndListener != null){
            onReachEndListener.onReachEnd();
        }

        Client client = clients.get(position);

        String phone = client.getPhone();

        if (phone == null) {
            phone = context.getString(R.string.card_empty_value);
        }

        if (phone.isEmpty()) {
            phone = context.getString(R.string.card_empty_value);
        }

        holder.client_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.client_action);
                popup.inflate(R.menu.action_client);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_client_call:

                                break;
                        }
                        return false;
                    }
                });

                popup.show();
            }
        });

        holder.name.setText(client.getName());
        holder.phone.setText(phone);
    }

    @Override
    public int getItemCount() {
        if (clients != null) {
            return clients.size();
        } else {
            return 0;
        }
    }

    public class ClientViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView phone;
        TextView client_action;

        public ClientViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.client_card_name);
            phone = itemView.findViewById(R.id.client_card_phone);
            client_action = itemView.findViewById(R.id.client_card_action);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClickClientListener != null){
                        onClickClientListener.OnClickClient(getAdapterPosition());
                    }
                }
            });
        }
    }

}
