package com.ikkong.common.vo;

import java.io.Serializable;

public class DictSelected implements Serializable
{
    private static final long serialVersionUID = -3794285870065465330L;
    private String value;
    private String name;
    private String Selected;

    public String getValue()
    {
        return this.value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSelected()
    {
        return this.Selected;
    }

    public void setSelected(String selected)
    {
        this.Selected = selected;
    }
}
