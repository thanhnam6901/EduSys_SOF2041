/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edusys.dao;

import java.util.List;

/**
 *
 * @author ThanhNam
 */
public abstract class EduSysDAO<E, K> {
    abstract public void insert(E entity);
    abstract public void update(E entity);
    abstract public void delete(K id);
    abstract public List<E> selectAll();
    abstract public E selectByID(K id);
    abstract protected List<E> selectBySQL(String sql, Object...args);
}
