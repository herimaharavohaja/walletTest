package com.wallet.Haja.repository;

import com.wallet.Haja.entity.Account;

import java.util.List;

public interface CrudOperations  <T,I>{

    T findById(I id);

    List<T> findAll();

    List<T> saveAll(List<T> toSave);

    T save(T toSave);

    T delete(T toDelete);

}
