package RESTservice;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class Record
{
    public String name;
    public String phone;

    @JsonCreator
    public Record(@JsonProperty("name") String name, @JsonProperty("phone") String phone)
    {
        this.name = name;
        this.phone = phone;
    }

    @Override
    public String toString()
    {
        return String.format("name: %s, phone: %s", name, phone);
    }

    void setKey(String name)
    {
        this.name = name;
    }
    void setValue(String phone)
    {
        this.phone = phone;
    }
    String getKey()
    {
        return name;
    }
    String getValue()
    {
        return phone;
    }
}