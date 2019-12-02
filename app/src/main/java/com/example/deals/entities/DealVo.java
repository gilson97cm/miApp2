package com.example.deals.entities;

import java.io.Serializable;

public class DealVo  {
    String id;
    String ruc;
    String name;
    String direction;
    byte[] logo;

    public DealVo(String id, String ruc, String name, String direction, byte[] logo) {
        this.id = id;
        this.ruc = ruc;
        this.name = name;
        this.direction = direction;
        this.logo = logo;
    }

    public DealVo() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }
}
