package dev.ruka.api_tarefas.model.user;

public enum Role {
    USER("user"),
    ADMIN("admin");

    private String roleString;

    Role(String roleString) {
        this.roleString = roleString;
    }

    public String getRoleString(){
        return this.roleString;
    }
}
