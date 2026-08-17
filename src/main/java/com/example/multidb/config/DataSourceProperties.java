package com.example.multidb.config;

/**
 * Plain POJO bound to app.datasource.primary.* and app.datasource.secondary.*
 * Kept simple/explicit rather than reusing Spring's own DataSourceProperties
 * to avoid ambiguity between the two datasource blocks.
 */
public class DataSourceProperties {

    private String jdbcUrl;
    private String username;
    private String password;
    private String driverClassName;

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
}
