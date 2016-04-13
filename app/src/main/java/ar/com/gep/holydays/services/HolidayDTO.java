package ar.com.gep.holydays.services;

import java.io.Serializable;

public class HolidayDTO implements Serializable {

    private static final long serialVersionUID = -8693082840151713551L;
    private Integer dia;
    private Integer mes;
    private String motivo;
    private String tipo;

    public Integer getDia() {
        return this.dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public Integer getMes() {
        return this.mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public String getMotivo() {
        return this.motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}