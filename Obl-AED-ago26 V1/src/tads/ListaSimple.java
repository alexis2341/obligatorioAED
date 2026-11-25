package tads;

public class ListaSimple<T> {

    private NodoSimple<T> primero;

    public ListaSimple() {
        primero = null;
    }

    public NodoSimple<T> getPrimero() {
        return primero;
    }

    public void agregarAlInicio(T dato) {
        NodoSimple<T> nuevo = new NodoSimple<>(dato);

        nuevo.setSiguiente(primero);
        primero = nuevo;
    }

    public boolean esVacia() {
        return primero == null;
    }
}