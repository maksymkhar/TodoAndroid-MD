package com.iesebre.dam2.max.todosandroid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.iesebre.dam2.max.todosandroid.adapters.TodoListAdapter;
import com.iesebre.dam2.max.todosandroid.models.TodoItem;
import com.iesebre.dam2.max.todosandroid.utils.Constants;
import com.iesebre.dam2.max.todosandroid.utils.Utils;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.lang.reflect.Type;

import static android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnClickListener {

    private Gson gson;
    public TodoArrayList tasks;
    private TodoListAdapter adapter;
    private SharedPreferences todoSharedPreference;

    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add listeners to fab buttons.
        findViewById(R.id.fabAdd).setOnClickListener(this);
        findViewById(R.id.fabRemove).setOnClickListener(this);

        // Left drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Load tasks from SharedPreferences
        //tasks = loadTasks();
        //if (tasks == null) { return; }
        todoSharedPreference = getSharedPreferences(Constants.SHARED_PREFERENCE_TODOS, 0);
        gson = new Gson();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTasks();
            }
        });

        loadTasks();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.fabAdd:
                displayTaskDialog(getString(R.string.add_task_dialog_title), -1);
                break;
            case R.id.fabRemove:
                removeTasks();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        saveTasks();
    }

    /**
     * Loads all tasks from SharedPreferences
     * @return Tasks ArrayList.
     */
    private void loadTasksFromPreferences()
    {
        String todoList = todoSharedPreference.getString(Constants.SHARED_PREFERENCE_TODO_LIST, null);

        if (todoList == null)
        {
            SharedPreferences.Editor editor = todoSharedPreference.edit();
            editor.putString(Constants.SHARED_PREFERENCE_TODO_LIST, Constants.INITIAL_TASKS_JSON);
            editor.apply();

            todoList = todoSharedPreference.getString(Constants.SHARED_PREFERENCE_TODO_LIST, null);
        }

        // Mapping the JSON
        Type arrayTodoList = new TypeToken<TodoArrayList>() {}.getType();
        TodoArrayList allTasks = gson.fromJson(todoList, arrayTodoList);

        swipeContainer.setRefreshing(false);

        if(allTasks == null) { return; }

        tasks = allTasks;
        setTasksView();
    }

    /**
     * Save all tasks in SharedPreferences.
     */
    private void saveTasks()
    {
        if (tasks == null) { return; }

        String tasksToSave = gson.toJson(tasks);

        SharedPreferences.Editor editor = todoSharedPreference.edit();
        editor.putString(Constants.SHARED_PREFERENCE_TODO_LIST, tasksToSave);
        editor.apply();
    }

    /**
     * Remove all done tasks.
     */
    private void removeTasks()
    {
        for (int i = tasks.size() -1; i >= 0; i--)
        {
            if (tasks.get(i).isDone()) { tasks.remove(i); }
        }

        adapter.notifyDataSetChanged();

        FloatingActionButton fabRemove = (FloatingActionButton) findViewById(R.id.fabRemove);
        fabRemove.hide();

        Utils.displayToast(this, getResources().getString(R.string.removed_message));
    }

    /**
     * Displays task dialog.
     * @param title Dialog title
     * @param taskId If id is < 0 display New task, if id is > 0 display Edit Task
     */
    public void displayTaskDialog(final String title, final int taskId)
    {
        // Initialize dialog.
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(title)
                .customView(R.layout.form_add_task, true)
                .positiveText(getResources().getString(R.string.save).toUpperCase())
                .negativeText(getResources().getString(R.string.cancel).toUpperCase())
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        // Task name EditText
                        EditText etName = (EditText) dialog.findViewById(R.id.etName);
                        String taskName = etName.getText().toString();

                        // Task priority
                        RadioGroup rgPriority = (RadioGroup) dialog.findViewById(R.id.rgPriority);

                        int priority = 1;
                        switch (rgPriority.getCheckedRadioButtonId()) {
                            case R.id.rbLowPriority:
                                priority = 1;
                                break;
                            case R.id.rbMediumPriority:
                                priority = 2;
                                break;
                            case R.id.rbHighPriority:
                                priority = 3;
                                break;
                        }

                        if (taskId < 0) { addTask(taskName, priority); }
                        else { editTask(taskName, priority, taskId); }
                    }
                })
                .show();

        // If task Id is invalid, temporarily disable Save button.
        final MDButton positiveButton = dialog.getActionButton(DialogAction.POSITIVE);
        if (taskId < 0) positiveButton.setEnabled(false);

        // Enable/Disable Save task Button
        EditText etName = (EditText) dialog.findViewById(R.id.etName);
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    positiveButton.setEnabled(false);
                } else positiveButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // If taskId is valid, inflate values from selected Task.
        if (taskId >= 0) inflateDialogValues(dialog, taskId);
    }

    /**
     * Inflate the values of Edit Task Dialog
     * @param dialog Displayed dialog instance.
     * @param taskId Id of task to edit.
     */
    private void inflateDialogValues(MaterialDialog dialog, int taskId)
    {
        EditText etName = (EditText) dialog.findViewById(R.id.etName);
        etName.setText(tasks.get(taskId).getName());

        RadioGroup rgPriority = (RadioGroup) dialog.findViewById(R.id.rgPriority);
        switch (tasks.get(taskId).getPriority())
        {
            case 1:
                rgPriority.check(R.id.rbLowPriority);
                break;
            case 2:
                rgPriority.check(R.id.rbMediumPriority);
                break;
            case 3:
                rgPriority.check(R.id.rbHighPriority);
                break;
            default:
                rgPriority.check(R.id.rbLowPriority);
                break;
        }
    }

    /**
     * Adds task to tasks ArrayList.
     * @param name The name of task.
     * @param priority The priority of task.
     */
    private void addTask(String name, int priority)
    {
        TodoItem todoItem = new TodoItem();
        todoItem.setName(name);
        todoItem.setDone(false);
        todoItem.setPriority(priority);

        tasks.add(todoItem);
        adapter.notifyDataSetChanged();

        Utils.displayToast(this, getResources().getString(R.string.saved_message));
    }

    /**
     * Edit task from tasks ArrayList.
     * @param name New task name.
     * @param priority New task priority.
     * @param taskId Id of task to edit.
     */
    private void editTask(String name, int priority, int taskId)
    {
        tasks.get(taskId).setName(name);
        tasks.get(taskId).setPriority(priority);
        adapter.notifyDataSetChanged();

        Utils.displayToast(this, getResources().getString(R.string.edited_message));
    }

    /**
     * Hides/Shows the FloatingActionButton depending of done tasks.
     */
    private void hideRemoveFabButton()
    {
        if (tasks == null) return;

        FloatingActionButton fabRemove = (FloatingActionButton) findViewById(R.id.fabRemove);

        for (int i=0; i<tasks.size(); i++)
        {
            if (tasks.get(i).isDone())
            {
                fabRemove.show();
                return;
            }

            if (i== tasks.size() - 1) fabRemove.hide();
        }
    }

    /**
     * Load tasks from network.
     */
    private void loadTasksFromNetwork ()
    {
        Ion.with(this)
                .load(Constants.URL_ALL_TASKS)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {

                        if (e != null)
                        {
                            // TODO
                            Log.e("REQUEST ERROR", e.toString());
                            loadTasksFromPreferences();
                            return;
                        }
                        else
                        {
                            Type arrayTodoList = new TypeToken<TodoArrayList>() {}.getType();
                            tasks = gson.fromJson(result.toString(), arrayTodoList);
                        }

                        swipeContainer.setRefreshing(false);

                        saveTasks();

                        if(tasks == null) { return; }

                        setTasksView();

                        Log.v("RESPONSE", tasks.toString());
                    }
                });

    }

    /**
     * Set tasks array to ListView
     */
    private void setTasksView()
    {
        // Initialize ListView
        ListView todoListView = (ListView) findViewById(R.id.todoListView);
        // Initialitze ListView adapter
        adapter = new TodoListAdapter(this, R.layout.list_item, tasks);
        todoListView.setAdapter(adapter);
        // Hide/Show the remove FloatingActionButton
        hideRemoveFabButton();

    }

    /**
     * Load tasks from network or sharedPreferences
     */
    private void loadTasks()
    {
        // Check Internet connection and load tasks
        if (Utils.isOnline(this))
        {
            if (!Utils.isUsingWifi(this))
            {
                Utils.displaySimpleDialog(this,
                        getString(R.string.not_using_wifi_title),
                        getString(R.string.not_using_wifi_message));
            }

            loadTasksFromNetwork();
        }
        else
        {
            Utils.displaySimpleDialog(this,
                    getString(R.string.no_internet_connection_title),
                    getString(R.string.no_internet_connection_message));

            loadTasksFromPreferences();
        }
    }
}
