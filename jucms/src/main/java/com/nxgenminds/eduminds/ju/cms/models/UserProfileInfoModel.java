package com.nxgenminds.eduminds.ju.cms.models;

public class UserProfileInfoModel
{
    private Users[] users;

    private String error;

    private String status;

    public Users[] getUsers ()
    {
        return users;
    }

    public void setUsers (Users[] users)
    {
        this.users = users;
    }

    public String getError ()
    {
        return error;
    }

    public void setError (String error)
    {
        this.error = error;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }
}
	