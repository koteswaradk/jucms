package com.nxgenminds.eduminds.ju.cms.classSchedule;

public class ClassScheduleAdminModel
{
    private Sections[] sections;

    private String error;

    private String status;

    private Streams[] streams;

    private Semester[] semester;

    public Sections[] getSections ()
    {
        return sections;
    }

    public void setSections (Sections[] sections)
    {
        this.sections = sections;
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

    public Streams[] getStreams ()
    {
        return streams;
    }

    public void setStreams (Streams[] streams)
    {
        this.streams = streams;
    }

    public Semester[] getSemester ()
    {
        return semester;
    }

    public void setSemester (Semester[] semester)
    {
        this.semester = semester;
    }
}
			
		