package sistema;

import tads.Cola;
import tads.ListaDesplieguesCodigo;
import tads.Pila;

public class Region {

    private String codigo;
    private String nombre;
    private String pais;
    private String zona;
    private Cola<Despliegue> pendientes;
    private ListaDesplieguesCodigo activos;
    private Pila<Despliegue> historial;
    
    
    public Region(String codigo, String nombre, String pais, String zona) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.pais = pais;
        this.zona = zona;
        this.pendientes = new Cola<>();
        this.activos = new ListaDesplieguesCodigo();
        this.historial = new Pila<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPais() {
        return pais;
    }

    public String getZona() {
        return zona;
    }
    
    public void agregarPendiente(Despliegue despliegue) {
    pendientes.encolar(despliegue);
}

public boolean tienePendiente(Despliegue despliegue) {
    return pendientes.contiene(despliegue);
}

public boolean tieneActivo(Despliegue despliegue) {
    return activos.buscarPorCodigo(despliegue.getCodigo()) != null;
}

public Cola<Despliegue> getPendientes() {
    return pendientes;
}

public ListaDesplieguesCodigo getActivos() {
    return activos;
}
public Despliegue procesarSiguientePendiente() {

    Despliegue despliegue = pendientes.desencolar();

    if (despliegue == null) {
        return null;
    }

    activos.insertarOrdenado(despliegue);

    historial.apilar(despliegue);

    return despliegue;
}

public boolean tienePendientes() {
    return !pendientes.esVacia();
}

public Pila<Despliegue> getHistorial() {
    return historial;
}

public boolean tieneHistorial() {
    return !historial.esVacia();
}

public Despliegue retirarUltimoProcesado() {

    Despliegue despliegue = historial.desapilar();

    if (despliegue == null) {
        return null;
    }

    activos.eliminarPorCodigo(
            despliegue.getCodigo()
    );

    return despliegue;
}



    @Override
    public String toString() {
        return codigo + ";"
                + nombre + ";"
                + pais + ";"
                + zona;
    }
}