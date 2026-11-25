package tads;

public class Pila<T> {

    private NodoSimple<T> tope;

    public Pila() {
        tope = null;
    }

    public boolean esVacia() {
        return tope == null;
    }

    public void apilar(T dato) {

        NodoSimple<T> nuevo = new NodoSimple<>(dato);

        nuevo.setSiguiente(tope);
        tope = nuevo;
    }

    public T desapilar() {

        if (esVacia()) {
            return null;
        }

        T dato = tope.getDato();

        tope = tope.getSiguiente();

        return dato;
    }

    public T verTope() {

        if (esVacia()) {
            return null;
        }

        return tope.getDato();
    }
}