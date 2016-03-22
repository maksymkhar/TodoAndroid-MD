package com.iesebre.dam2.max.todosandroid.utils;

/**
 * Created by max on 4/12/15.
 */
public class Constants {

    /*
        Todos Shared preference
     */
    public static final String SHARED_PREFERENCE_TODOS      = "SP_TODOS";
    public static final String SHARED_PREFERENCE_TODO_LIST  = "todo_list";

    /*
        Default Json Strings.
     */
    public static final String INITIAL_TASKS_JSON = "[" +
            "{\"name\":\"Comprar llet\", \"done\": true, \"priority\": 2}," +
            "{\"name\":\"Comprar pa\", \"done\": false, \"priority\": 1},"  +
            "{\"name\":\"Comprar ous\", \"done\": false, \"priority\": 3}"  +
            "]";

    /*
        TODO: API
     */

    public static final String URL_BASE         = "http://acacha.github.io";
    public static final String URL_ALL_TASKS    = URL_BASE + "/json-server-todos/db_todos.json";

}
