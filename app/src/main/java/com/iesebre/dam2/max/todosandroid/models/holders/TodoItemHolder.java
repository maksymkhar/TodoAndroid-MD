package com.iesebre.dam2.max.todosandroid.models.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hanks.library.AnimateCheckBox;
import com.iesebre.dam2.max.todosandroid.R;

/**
 * Created by max on 22/03/16.
 */
public class TodoItemHolder extends RecyclerView.ViewHolder {

    public LinearLayout llTodoItem;
    public AnimateCheckBox cbCompletedTask;
    public TextView tvTaskName;

    public TodoItemHolder(View itemView) {
        super(itemView);

        llTodoItem = (LinearLayout) itemView.findViewById(R.id.llTodoItem);
        cbCompletedTask = (AnimateCheckBox) itemView.findViewById(R.id.cbCompletedTask);
        tvTaskName = (TextView) itemView.findViewById(R.id.tvTaskName);
    }
}
