package net.ttddyy.dsproxy.example.service;

import java.sql.SQLException;
import java.sql.Statement;

public interface QueryListener {
    void beforeQuery(Statement st) throws SQLException;
    void afterQuery(Statement st) throws SQLException;
    void returningRegistered(boolean registered);
}
