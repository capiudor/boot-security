package top.capiudor.security.entity;

import java.io.Serializable;
import java.util.Objects;

public class RoleResourceDTO implements Serializable {

    private String resourceName;

    private String resourceUrl;

    private String roleName;

    @Override
    public String toString() {
        return "RoleResourceDTO{" +
                "resourceName='" + resourceName + '\'' +
                ", resourceUrl='" + resourceUrl + '\'' +
                ", roleName='" + roleName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleResourceDTO that = (RoleResourceDTO) o;
        return Objects.equals(resourceName, that.resourceName) &&
                Objects.equals(resourceUrl, that.resourceUrl) &&
                Objects.equals(roleName, that.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceName, resourceUrl, roleName);
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
