package com.itechart.security.web.model.dto;

/**
 * @author yauheni.putsykovich
 */
public class AclEntryDto {
    private Long id;
    private String name;
    private String principalTypeName;
    private boolean canRead;
    private boolean canWrite;
    private boolean canCreate;
    private boolean canDelete;
    private boolean canAdmin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrincipalTypeName() {
        return principalTypeName;
    }

    public void setPrincipalTypeName(String principalTypeName) {
        this.principalTypeName = principalTypeName;
    }

    public boolean isCanRead() {
        return canRead;
    }

    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    public boolean isCanWrite() {
        return canWrite;
    }

    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }

    public boolean isCanCreate() {
        return canCreate;
    }

    public void setCanCreate(boolean canCreate) {
        this.canCreate = canCreate;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public boolean isCanAdmin() {
        return canAdmin;
    }

    public void setCanAdmin(boolean canAdmin) {
        this.canAdmin = canAdmin;
    }
}
