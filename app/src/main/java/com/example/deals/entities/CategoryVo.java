package com.example.deals.entities;

public class CategoryVo {
    String id;
    String name;
    String description;
    String idDeal;

    public CategoryVo(String id, String name, String description, String idDeal) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.idDeal = idDeal;
    }

    public CategoryVo(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdDeal() {
        return idDeal;
    }

    public void setIdDeal(String idDeal) {
        this.idDeal = idDeal;
    }
}
