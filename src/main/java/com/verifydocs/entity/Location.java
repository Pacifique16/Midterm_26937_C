package com.verifydocs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String level; // PROVINCE, DISTRICT, SECTOR, CELL, VILLAGE

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Location parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Location> children;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Institution> institutions;

    public Location() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public Location getParent() { return parent; }
    public void setParent(Location parent) { this.parent = parent; }

    public List<Location> getChildren() { return children; }
    public void setChildren(List<Location> children) { this.children = children; }

    public List<Institution> getInstitutions() { return institutions; }
    public void setInstitutions(List<Institution> institutions) { this.institutions = institutions; }
}
