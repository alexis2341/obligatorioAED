package tads;

import sistema.Despliegue;

public class ListaDesplieguesCodigo {

    private NodoDoble<Despliegue> primero;
    private NodoDoble<Despliegue> ultimo;
    private int cantidad;

    public ListaDesplieguesCodigo() {
        primero = null;
        ultimo = null;
        cantidad = 0;
    }

    public boolean esVacia() {
        return primero == null;
    }

    public int getCantidad() {
        return cantidad;
    }

    public NodoDoble<Despliegue> getPrimero() {
        return primero;
    }

    public NodoDoble<Despliegue> getUltimo() {
        return ultimo;
    }

    public void insertarOrdenado(Despliegue dato) {

        NodoDoble<Despliegue> nuevo = new NodoDoble<>(dato);

        if (esVacia()) {
            primero = nuevo;
            ultimo = nuevo;

        } else if (dato.getCodigo()
                .compareTo(primero.getDato().getCodigo()) < 0) {

            nuevo.setSiguiente(primero);
            primero.setAnterior(nuevo);
            primero = nuevo;

        } else if (dato.getCodigo()
                .compareTo(ultimo.getDato().getCodigo()) >= 0) {

            nuevo.setAnterior(ultimo);
            ultimo.setSiguiente(nuevo);
            ultimo = nuevo;

        } else {

            NodoDoble<Despliegue> actual = primero;

            while (actual.getDato().getCodigo()
                    .compareTo(dato.getCodigo()) <= 0) {

                actual = actual.getSiguiente();
            }

            NodoDoble<Despliegue> anterior = actual.getAnterior();

            nuevo.setAnterior(anterior);
            nuevo.setSiguiente(actual);

            anterior.setSiguiente(nuevo);
            actual.setAnterior(nuevo);
        }

        cantidad++;
    }

    public Despliegue buscarPorCodigo(String codigo) {

        NodoDoble<Despliegue> actual = primero;

        while (actual != null) {

            if (actual.getDato().getCodigo().equals(codigo)) {
                return actual.getDato();
            }

            actual = actual.getSiguiente();
        }

        return null;
    }
    public Despliegue eliminarPorCodigo(String codigo) {

    NodoDoble<Despliegue> actual = primero;

    while (actual != null) {

        if (actual.getDato().getCodigo().equals(codigo)) {

            // Caso 1: es el único elemento
            if (actual == primero && actual == ultimo) {

                primero = null;
                ultimo = null;

            }
            // Caso 2: es el primero
            else if (actual == primero) {

                primero = actual.getSiguiente();
                primero.setAnterior(null);

            }
            // Caso 3: es el último
            else if (actual == ultimo) {

                ultimo = actual.getAnterior();
                ultimo.setSiguiente(null);

            }
            // Caso 4: está en el medio
            else {

                NodoDoble<Despliegue> anterior =
                        actual.getAnterior();

                NodoDoble<Despliegue> siguiente =
                        actual.getSiguiente();

                anterior.setSiguiente(siguiente);
                siguiente.setAnterior(anterior);
            }

            cantidad--;

            return actual.getDato();
        }

        actual = actual.getSiguiente();
    }

    return null;
}
}