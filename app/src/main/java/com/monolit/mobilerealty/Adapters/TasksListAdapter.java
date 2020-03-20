package com.monolit.mobilerealty.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.monolit.mobilerealty.RealtorObjects.Task;
import com.monolit.mobilerealty.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TasksListAdapter  extends  RecyclerView.Adapter<TasksListAdapter.TasksHolder>{

    public List<Task> taskArrayList;
    private OnNoteClickListener onNoteClickListener;
    private Context context;

    public interface OnNoteClickListener{
        void onNoteClick(int position);
    }

    public void setOnNoteClickListener(OnNoteClickListener onNoteClickListener) {
        this.onNoteClickListener = onNoteClickListener;
    }

    @NonNull
    @Override
    public TasksHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_task, parent, false);

       context = view.getContext();

       return new TasksHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksHolder holder, int position) {
        Task task = taskArrayList.get(position);

        holder.title.setText(task.getTitle());
        holder.author.setText(task.getAuthor());
        holder.date.setText(task.getDate());
        holder.deadline.setText(task.getDeadline());

        // Deadline date
        String deadlineDateString = task.getDeadline();

        if (!deadlineDateString.isEmpty()) {
            holder.img_deadline.setVisibility(View.VISIBLE);
            holder.deadline.setTextColor(context.getColor(R.color.task_date));
            holder.deadline.setBackground(null);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
            try {
                Date deadlineDate = dateFormat.parse(deadlineDateString);
                long timedeff = System.currentTimeMillis() - deadlineDate.getTime();
                if (timedeff > 0) {
                    holder.deadline.setTextColor(context.getColor(R.color.colorWhite));
                    holder.deadline.setBackground(context.getDrawable(R.drawable.deadline_shape_text));
                } else if (timedeff == 0) {

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            holder.img_deadline.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return taskArrayList.size();
    }

    class TasksHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView date;
        TextView author;
        TextView deadline;
        ImageView img_deadline;

        public TasksHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.task_txt_title);
            date = itemView.findViewById(R.id.task_txt_date);
            author = itemView.findViewById(R.id.task_txt_author);
            deadline = itemView.findViewById(R.id.task_txt_deadline);
            img_deadline = itemView.findViewById(R.id.task_img_deadline);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onNoteClickListener != null) {
                        onNoteClickListener.onNoteClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public void setTaskArrayList(List<Task> taskArrayList) {
        this.taskArrayList = taskArrayList;
    }
}
