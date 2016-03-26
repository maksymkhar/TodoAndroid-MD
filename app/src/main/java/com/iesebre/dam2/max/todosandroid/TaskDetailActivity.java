package com.iesebre.dam2.max.todosandroid;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.iesebre.dam2.max.todosandroid.models.TodoItem;
import com.iesebre.dam2.max.todosandroid.utils.Constants;

/**
 * Created by max on 23/03/16.
 */
public class TaskDetailActivity extends AppCompatActivity {

    private TodoItem todoItem;

    private TextView tvTaskName, tvTaskDescription, tvTaskPriority, tvTaskDone;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        todoItem = (TodoItem) getIntent().getSerializableExtra(Constants.KEY_TASK);

        initView();
        loadTaskToView();
    }

    private void initView()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) { getSupportActionBar().setDisplayHomeAsUpEnabled(true); }

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        tvTaskName = (TextView) findViewById(R.id.tvTaskName);
        tvTaskDescription = (TextView) findViewById(R.id.tvTaskDescription);
        tvTaskPriority = (TextView) findViewById(R.id.tvTaskPriority);
        tvTaskDone = (TextView) findViewById(R.id.tvTaskDone);
    }

    private void loadTaskToView()
    {
        if (todoItem == null) { return; }

        tvTaskName.setText(todoItem.getName());
        tvTaskDescription.setText(todoItem.getDescription());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(todoItem.getName());
        }

        if (todoItem.isDone()) { tvTaskDone.setText(getString(R.string.affirmative)); }
        else { tvTaskDone.setText(getString(R.string.negative)); }

        switch (todoItem.getPriority())
        {
            case 1:
                tvTaskPriority.setText(getString(R.string.low));
                collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
                break;
            case 2:
                tvTaskPriority.setText(getString(R.string.medium));
                collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.orange));
                break;
            case 3:
                tvTaskPriority.setText(getString(R.string.hight));
                collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
