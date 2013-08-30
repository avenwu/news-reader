package com.android.volley.volleyhelper;

import java.util.List;

/**
 * Communicate with SQLite database with the help of OrmLite library, including
 * basic CRUD operations to be implemented
 * 
 * @author chaobin
 * 
 * @param <T>
 */
public interface DataProvider<T> {
    /**
     * insert an item into cached database
     * 
     * @param item
     *            data to insert
     * @return true if add successfully
     */
    public boolean add(T item);

    /**
     * add new data list into table.
     * 
     * @param dataList
     * @param clearOld
     *            true if the table should be cleared before insert operations;
     * @return true if add successfully
     */
    public boolean addAll(List<T> dataList, boolean clearOld);

    /**
     * try to query the data from cached database
     * 
     * @return query result list
     */
    public List<T> getAll();

    /**
     * delete an item from cached database
     * 
     * @param item2Delete
     *            data to delete
     * @return true if delete successfully
     */
    public boolean delete(T item2Delete);

    /**
     * try to clear data table
     * 
     * @return true if clear all data successfully
     */
    public boolean clearAll();

    /**
     * update data in cached database after change
     * 
     * @param item2Update
     * @return true if update successfully
     */
    public boolean update(T item2Update);
}
