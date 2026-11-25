package sistema;

public class Despliegue implements Comparable<Despliegue> {

    private String id;
    private String codigo;
    private String descripcion;
    private boolean cuantizado;
    private Modalidad modalidad;

    public Despliegue(String id, String codigo, String descripcion,
                      boolean cuantizado, Modalidad modalidad) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.cuantizado = cuantizado;
        this.modalidad = modalidad;
    }

    public String getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isCuantizado() {
        return cuantizado;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }

    @Override
    public int compareTo(Despliegue otro) {
        return this.id.compareTo(otro.id);
    }

    @Override
    public String toString() {
        return id + ";"
                + codigo + ";"
                + descripcion + ";"
                + cuantizado + ";"
                + modalidad.getTexto();
    }
}