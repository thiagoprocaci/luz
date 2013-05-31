package br.com.tbp.model;

public class CoreEntity {
    private static final int PRIME = 31;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = PRIME * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!getClass().isAssignableFrom(obj.getClass()) && !obj.getClass().isAssignableFrom(getClass())) {
            return false;
        }
        CoreEntity other = (CoreEntity) obj;
        if (getId() == null) {
            if (other.getId() != null) {
                return false;
            }
            // Quando os dois objetos tem id == null, considera que sao diferentes
            return false;
        } else if (!getId().equals(other.getId())) {
            return false;
        }
        return true;
    }
}
