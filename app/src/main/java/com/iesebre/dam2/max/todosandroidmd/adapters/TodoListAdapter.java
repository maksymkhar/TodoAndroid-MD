package com.iesebre.dam2.max.todosandroidmd.adapters;

import android.app.Activity;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hanks.library.AnimateCheckBox;
import com.iesebre.dam2.max.todosandroidmd.MainActivity;
import com.iesebre.dam2.max.todosandroidmd.R;
import com.iesebre.dam2.max.todosandroidmd.models.TodoItem;
import com.iesebre.dam2.max.todosandroidmd.models.holders.TodoItemHolder;
import com.iesebre.dam2.max.todosandroidmd.utils.Utils;

import java.util.ArrayList;

/**
 * Created by max on 22/03/16.
 */
public class TodoListAdapter extends RecyclerView.Adapter<TodoItemHolder> {

    private Activity activity;
    private ArrayList<TodoItem> todoItems;

    public TodoListAdapter(Activity activity, ArrayList<TodoItem> todoItems) {
        this.activity = activity;
        this.todoItems = todoItems;
    }

    @Override
    public TodoItemHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new TodoItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final TodoItemHolder holder, final int position) {

        TodoItem item = todoItems.get(position);

        holder.tvTaskName.setText(item.getName());
        setPriorityColor(holder.cbCompletedTask, item.getPriority());

        holder.cbCompletedTask.setChecked(item.isDone());
        strikethroughName(holder.tvTaskName, item.isDone());

        holder.cbCompletedTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!holder.cbCompletedTask.isChecked()) {
                    holder.cbCompletedTask.setChecked(true);
                    strikethroughName(holder.tvTaskName, true);
                    todoItems.get(position).setDone(true);
                } else {
                    holder.cbCompletedTask.setChecked(false);
                    strikethroughName(holder.tvTaskName, false);
                    todoItems.get(position).setDone(false);
                }

                checkDoneTasks();
            }
        });

        // Initialize MainActivity
        final MainActivity mainActivity = (MainActivity) activity;

        // Item click event listener
        holder.llTodoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: Open task
                mainActivity.openTask(position);
            }
        });


        holder.llTodoItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                // Call edit task.
                mainActivity.displayTaskDialog(activity.getString(R.string.edit_task_dialog_title), position);
                // Short vibration
                Utils.vibrate(activity, 1000);

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return todoItems.size();
    }

    /**
     * Set the priority color to CheckBox.
     * @param checkBox Is the CheckBox to change color.
     * @param priority [1, 2 or 3] Colors [Green, orange, red]
     */
    private void setPriorityColor(AnimateCheckBox checkBox, int priority)
    {
        switch (priority)
        {
            case 1:
                checkBox.setCircleColor(ContextCompat.getColor(activity, R.color.green));
                checkBox.setUnCheckColor(ContextCompat.getColor(activity, R.color.green));
                break;
            case 2:
                checkBox.setCircleColor(ContextCompat.getColor(activity, R.color.orange));
                checkBox.setUnCheckColor(ContextCompat.getColor(activity, R.color.orange));
                break;
            case 3:
                checkBox.setCircleColor(ContextCompat.getColor(activity, R.color.red));
                checkBox.setUnCheckColor(ContextCompat.getColor(activity, R.color.red));
                break;
            default:
                checkBox.setCircleColor(ContextCompat.getColor(activity, R.color.green));
                checkBox.setUnCheckColor(ContextCompat.getColor(activity, R.color.green));
                break;
        }
    }

    /**
     * Strike the TextView if the task is done.
     * @param name TextVIew to Strike
     * @param isDone True if is done, False if is not.
     */
    private void strikethroughName (TextView name, boolean isDone)
    {
        if (isDone) {  name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG); }
        else { name.setPaintFlags(name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG)); }
    }

    /**
     * Called every time a isDone checkbox is pulsed, hides/shows the remove FloatingActionButton.
     */
    private void checkDoneTasks()
    {
        FloatingActionButton fabRemove = (FloatingActionButton) activity.findViewById(R.id.fabRemove);

        for (int i=0; i<todoItems.size(); i++)
        {
            if (todoItems.get(i).isDone())
            {
                fabRemove.show();
                return;
            }
            if (i == todoItems.size() - 1) { fabRemove.hide(); }
        }
    }
}
