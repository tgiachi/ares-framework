package com.github.tgiachi.ares.interfaces.database;

import javax.management.Query;
import java.util.List;

/**
 * In caso il database fosse di tipo ORM
 * implementare questa interfaccia all'oggetto
 */
public interface IOrmManager  {

    void persist(Object object);

    List<?> executeQuery(String hsql);

    Object createQuery(String hsql);
}
