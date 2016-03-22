package com.iesebre.dam2.max.todosandroid.adapters;

import android.app.Activity;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hanks.library.AnimateCheckBox;
import com.iesebre.dam2.max.todosandroid.MainActivity;
import com.iesebre.dam2.max.todosandroid.R;
import com.iesebre.dam2.max.todosandroid.models.TodoItem;

import java.util.ArrayList;

/**
 * Created by max on 20/11/15.
 *
 */
public class TodoListAdapter extends BaseAdapter {

    private Activity activity;
    private int resource;
    private ArrayList<TodoItem> list;

    public TodoListAdapter(Activity activity, int resource, ArrayList<TodoItem> listData)
    {
        this.activity = activity;
        this.resource = resource;
        this.list = listData;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // TODO, this is not the best solution...
        convertView = null;

        final TodoItem item = list.get(position);

        if (convertView == null)
        {
            convertView = activity.getLayoutInflater().inflate(resource, null);

            if (item != null)
            {
                final TextView tv1 = (TextView) convertView.findViewById(R.id.tvTaskName);
                tv1.setText(item.getName());

                final AnimateCheckBox cbCompletedTask = (AnimateCheckBox) convertView.findViewById(R.id.cbCompletedTask);
                setPriorityColor(cbCompletedTask, item.getPriority());

                cbCompletedTask.setChecked(item.isDone());
                strikethroughName(tv1, item.isDone());

                cbCompletedTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!cbCompletedTask.isChecked())
                        {
                            cbCompletedTask.setChecked(true);
                            strikethroughName(tv1, true);
                            list.get(position).setDone(true);
                        }
                        else
                        {
                            cbCompletedTask.setChecked(false);
                            strikethroughName(tv1, false);
                            list.get(position).setDone(false);
                        }

                        checkDoneTasks();
                    }
                });
            }
        }

        // Item click event listener
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Initialize MainActivity
                MainActivity mainActivity = (MainActivity) activity;
                // Call edit task.
                mainActivity.displayTaskDialog(activity.getString(R.string.edit_task_dialog_title), position);
            }
        });

        return convertView;
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

        for (int i=0; i<list.size(); i++)
        {
            if (list.get(i).isDone())
            {
                fabRemove.show();
                return;
            }
            if (i == list.size() - 1) { fabRemove.hide(); }
        }
    }
}
