package com.electrician;

public class User {
    private String userName;
    private String sex;
    private int age;
    // private String ip;
    private boolean isAdmin;

    public User() {
    }

    public User(String userName, String sex, int age, boolean isAdmin) {
        this.userName = userName;
        this.sex = sex;
        this.age = age;
        this.isAdmin = isAdmin;
    }

    /**
     * 获取
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取
     * @return sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * 设置
     * @param sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 获取
     * @return age
     */
    public int getAge() {
        return age;
    }

    /**
     * 设置
     * @param age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * 获取
     * @return isAdmin
     */
    public boolean isIsAdmin() {
        return isAdmin;
    }

    /**
     * 设置
     * @param isAdmin
     */
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String toString() {
        return "User{userName = " + userName + ", sex = " + sex + ", age = " + age + ", isAdmin = " + isAdmin + "}";
    }
}
