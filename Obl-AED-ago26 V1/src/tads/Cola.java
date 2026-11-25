package tads;

public class Cola<T> {

    private NodoSimple<T> primero;
    private NodoSimple<T> ultimo;

    public Cola() {
        primero = null;
        ultimo = null;
    }

    public boolean esVacia() {
        return primero == null;
    }

    public void encolar(T dato) {

        NodoSimple<T> nuevo = new NodoSimple<>(dato);

        if (esVacia()) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            ultimo.setSiguiente(nuevo);
            ultimo = nuevo;
        }
    }

    public T desencolar() {

        if (esVacia()) {
            return null;
        }

        T dato = primero.getDato();
        primero = primero.getSiguiente();

        if (primero == null) {
            ultimo = null;
        }

        return dato;
    }

    public T verPrimero() {

        if (esVacia()) {
            return null;
        }

        return primero.getDato();
    }

    public boolean contiene(T dato) {

        NodoSimple<T> actual = primero;

        while (actual != null) {

            if (actual.getDato().equals(dato)) {
                return true;
            }

            actual = actual.getSiguiente();
        }

        return false;
    }
}