package net.ttddyy.dsproxy.example.listener;

import net.ttddyy.dsproxy.example.service.QueryListener;

import java.sql.SQLException;
import java.sql.Statement;

public class CustomQueryListener implements QueryListener {
    @Override
    public void beforeQuery(Statement st) throws SQLException {

    }

    @Override
    public void afterQuery(Statement st) throws SQLException {

    }

    @Override
    public void returningRegistered(boolean registered) {

    }
}
