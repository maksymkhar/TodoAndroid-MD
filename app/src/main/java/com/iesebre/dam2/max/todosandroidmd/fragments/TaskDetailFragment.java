package com.iesebre.dam2.max.todosandroidmd.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iesebre.dam2.max.todosandroidmd.R;
import com.iesebre.dam2.max.todosandroidmd.models.TodoItem;
import com.iesebre.dam2.max.todosandroidmd.utils.Constants;

/**
 * Created by max on 23/03/16.
 */
public class TaskDetailFragment extends Fragment {

    private TodoItem todoItem;

    private View mainView;
    private TextView tvTaskName, tvTaskDescription, tvTaskPriority, tvTaskDone;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mainView = inflater.inflate(R.layout.item_detail_container, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            todoItem = (TodoItem) bundle.getSerializable(Constants.KEY_TASK);
        }

        initView();
        loadTaskToView();

        return mainView;
    }


    private void initView()
    {

        tvTaskName = (TextView) mainView.findViewById(R.id.tvTaskName);
        tvTaskDescription = (TextView) mainView.findViewById(R.id.tvTaskDescription);
        tvTaskPriority = (TextView) mainView.findViewById(R.id.tvTaskPriority);
        tvTaskDone = (TextView) mainView.findViewById(R.id.tvTaskDone);
    }

    private void loadTaskToView()
    {
        if (todoItem == null) { return; }

        tvTaskName.setText(todoItem.getName());

        if (todoItem.getDescription() == null || todoItem.getDescription().trim().length() == 0) {
            tvTaskDescription.setText("-");
        }
        else {
            tvTaskDescription.setText(todoItem.getDescription());
        }

        if (todoItem.isDone()) { tvTaskDone.setText(getString(R.string.affirmative)); }
        else { tvTaskDone.setText(getString(R.string.negative)); }

        switch (todoItem.getPriority())
        {
            case 1:
                tvTaskPriority.setText(getString(R.string.low));
                break;
            case 2:
                tvTaskPriority.setText(getString(R.string.medium));
                break;
            case 3:
                tvTaskPriority.setText(getString(R.string.hight));
                break;
        }
    }

}
