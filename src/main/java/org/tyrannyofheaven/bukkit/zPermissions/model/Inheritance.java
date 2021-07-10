package org.tyrannyofheaven.bukkit.zPermissions.model;

public class Inheritance implements Comparable<Inheritance> {

    private Long id;

    private PermissionEntity child;

    private PermissionEntity parent;

    private int ordering;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PermissionEntity getChild() {
        return child;
    }

    public void setChild(PermissionEntity child) {
        this.child = child;
    }

   public PermissionEntity getParent() {
        return parent;
    }

    public void setParent(PermissionEntity parent) {
        this.parent = parent;
    }

    public int getOrdering() {
        return ordering;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Inheritance)) return false;
        Inheritance o = (Inheritance) obj;
        return getParent().equals(o.getParent()) &&
                getChild().equals(o.getChild());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + getParent().hashCode();
        result = 37 * result + getChild().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("Inheritance[%s > %s]", getParent().getName(), getChild().getName());
    }

    @Override
    public int compareTo(Inheritance o) {
        // Assumes child is the same... i.e. this sorts the parents
        return getOrdering() - o.getOrdering();
    }

}
