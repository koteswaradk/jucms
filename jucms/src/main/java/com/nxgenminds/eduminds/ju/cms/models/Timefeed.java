package com.nxgenminds.eduminds.ju.cms.models;

public class Timefeed
{
    private String error;

    private String status;

    private Posts[] posts;

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

    public Posts[] getPosts ()
    {
        return posts;
    }

    public void setPosts (Posts[] posts)
    {
        this.posts = posts;
    }
}
			
		