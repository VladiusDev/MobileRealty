package com.monolit.mobilerealty.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.monolit.mobilerealty.Activities.ReservationActivity;
import com.monolit.mobilerealty.FormattingUtils.NumberFormat;
import com.monolit.mobilerealty.RealtorObjects.RealtyObject;
import com.monolit.mobilerealty.R;
import com.monolit.mobilerealty.RealtorObjects.Utils.RealtorObjectsUtils;

import java.util.ArrayList;
import java.util.List;

public class ObjectListAdapter extends RecyclerView.Adapter<ObjectListAdapter.ObjectViewHolder>{

    private List<RealtyObject> objectList;
    private Context context;
    private OnClickObjectListener onClickObjectListener;

    public ObjectListAdapter() {
        objectList = new ArrayList<>();
    }

    public interface OnClickObjectListener{
        void onClickObject(int position);
    }

    public void setOnClickObjectListener(OnClickObjectListener onClickObjectListener) {
        this.onClickObjectListener = onClickObjectListener;
    }

    @NonNull
    @Override
    public ObjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_object, parent,false);

        context = view.getContext();

        return new ObjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ObjectViewHolder holder, int position) {
        final RealtyObject realtyObject = objectList.get(position);

        String name = realtyObject.getName();
        String status = realtyObject.getStatus();
        Double square = realtyObject.getSquare();
        final Double cost = realtyObject.getAmount();

        String squareString = NumberFormat.convertToSumFormat(square) + " " + context.getString(R.string.square_meter);
        String costString =  NumberFormat.convertToSumFormat(cost);

        holder.name.setText(name);
        holder.square.setText(squareString);
        holder.cost.setText(costString);
        holder.status.setText(status);
        holder.status.setBackground(context.getDrawable(R.drawable.object_status_free));

        switch (realtyObject.getStatusId()){
            case 1:
                holder.status.setBackground(context.getDrawable(R.drawable.object_status_reservation));
                break;
            case 2:
                holder.status.setBackground(context.getDrawable(R.drawable.object_status_pledged));
                break;
            case 3:
                holder.status.setBackground(context.getDrawable(R.drawable.object_status_refusing));
                break;
        }

        holder.object_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popup = new PopupMenu(context, holder.object_action);
                popup.inflate(R.menu.action_object);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_object_plan:
                                RealtorObjectsUtils.openObjectPlan(realtyObject, context);
                                break;
                            case R.id.action_object_reservation:
                                Intent intent = new Intent(context, ReservationActivity.class);
                                intent.putExtra("objectId", realtyObject.getId1c());
                                intent.putExtra("object", realtyObject.getName());

                                context.startActivity(intent);

                                break;
                        }
                        return false;
                    }
                });

                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (objectList != null) {
            return objectList.size();
        } else {
            return 0;
        }
    }

    public class ObjectViewHolder extends RecyclerView.ViewHolder {
        CardView cardViewObject;
        TextView name;
        TextView square;
        TextView cost;
        TextView object_action;
        TextView status;

        ObjectViewHolder(View itemView) {
            super(itemView);

            cardViewObject    = itemView.findViewById(R.id.object_card_cardView);
            name              = itemView.findViewById(R.id.object_card_txt_name);
            square            = itemView.findViewById(R.id.object_card_txt_square);
            cost              = itemView.findViewById(R.id.object_card_txt_cost);
            status            = itemView.findViewById(R.id.object_card_txt_status);
            object_action     = itemView.findViewById(R.id.object_card_action);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClickObjectListener != null){
                        onClickObjectListener.onClickObject(getAdapterPosition());
                    }
                }
            });
        }
    }

    public void setObjectList(List<RealtyObject> objectList) {
        this.objectList = objectList;

        notifyDataSetChanged();
    }

    public List<RealtyObject> getObjectList() {
        return objectList;
    }
}
