
package tads;

public class ListaDoble<T extends Comparable<T>> {

    private NodoDoble<T> primero;
    private NodoDoble<T> ultimo;
    private int cantidad;

    public ListaDoble() {
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

    public NodoDoble<T> getPrimero() {
        return primero;
    }

    public NodoDoble<T> getUltimo() {
        return ultimo;
    }

    public void insertarOrdenado(T dato) {
        NodoDoble<T> nuevo = new NodoDoble<>(dato);

        // Caso 1: lista vacía
        if (esVacia()) {
            primero = nuevo;
            ultimo = nuevo;
        }
        // Caso 2: insertar antes del primero
        else if (dato.compareTo(primero.getDato()) < 0) {
            nuevo.setSiguiente(primero);
            primero.setAnterior(nuevo);
            primero = nuevo;
        }
        // Caso 3: insertar después del último
        else if (dato.compareTo(ultimo.getDato()) >= 0) {
            nuevo.setAnterior(ultimo);
            ultimo.setSiguiente(nuevo);
            ultimo = nuevo;
        }
        // Caso 4: insertar en el medio
        else {
            NodoDoble<T> actual = primero;

            while (actual.getDato().compareTo(dato) <= 0) {
                actual = actual.getSiguiente();
            }

            NodoDoble<T> anterior = actual.getAnterior();

            nuevo.setAnterior(anterior);
            nuevo.setSiguiente(actual);

            anterior.setSiguiente(nuevo);
            actual.setAnterior(nuevo);
        }

        cantidad++;
    }

    public T buscar(T dato) {
        NodoDoble<T> actual = primero;

        while (actual != null) {
            if (actual.getDato().compareTo(dato) == 0) {
                return actual.getDato();
            }

            actual = actual.getSiguiente();
        }

        return null;
    }
}
