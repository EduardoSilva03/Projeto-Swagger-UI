package com.example.demo;

public class Login {
    private Long id;
    private String usuario;
    private String senha;

    public Login() {
        this.id = 1L;
        this.usuario = "admin";
        this.senha = "admin";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}