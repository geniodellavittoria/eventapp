package ch.mobpro.eventapp.dto;

import java.util.List;

public class JwtToken {

    public String sub;

    public List<String> roles;

    public String surname;

    public String name;

    public String email;

    public long iat;

    public long exp;
}
