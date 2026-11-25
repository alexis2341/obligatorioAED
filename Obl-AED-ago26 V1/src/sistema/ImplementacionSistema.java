package sistema;
import tads.ListaDoble;
import tads.NodoDoble;
import tads.ListaDesplieguesCodigo;
import tads.ListaSimple;
import tads.NodoSimple;

//Alexis Vargas - 352494
public class ImplementacionSistema implements Sistema {
    
private ListaDoble<Despliegue> desplieguesPorId;
private ListaDesplieguesCodigo desplieguesPorCodigo;
private ListaDesplieguesCodigo[] desplieguesPorModalidad;
private ListaSimple<Region> regiones;
    @Override
    public Retorno inicializarSistema() {
        desplieguesPorId = new ListaDoble<>();
         desplieguesPorCodigo = new ListaDesplieguesCodigo();
         
             desplieguesPorModalidad = new ListaDesplieguesCodigo[5];

    for (int i = 0; i < desplieguesPorModalidad.length; i++) {
        desplieguesPorModalidad[i] = new ListaDesplieguesCodigo();
    }

         regiones = new ListaSimple<>();
         
        return Retorno.ok();
    }

    @Override
    public Retorno registrarDespliegue(String id, String codigo, String descripcion,
                                      boolean cuantizado, Modalidad modalidad) {

    // ERROR 1:
    // id, codigo o descripcion vacíos/null,
    // o modalidad null.
    if (textoVacio(id)
            || textoVacio(codigo)
            || textoVacio(descripcion)
            || modalidad == null) {
        return Retorno.error1();
    }

    // ERROR 2:
    // El código debe respetar AAA-BBB-CCCC.
    if (!codigo.matches("[A-Z]{3}-[0-9]{3}-[A-Za-z0-9]{4}")) {
        return Retorno.error2();
    }

    // ERROR 3:
    // Ya existe un despliegue con ese ID.
    if (buscarPorId(id) != null) {
        return Retorno.error3();
    }

    // ERROR 4:
    // Ya existe un despliegue con ese código.
    if (buscarPorCodigo(codigo) != null) {
        return Retorno.error4();
    }

    Despliegue nuevo = new Despliegue(
            id,
            codigo,
            descripcion,
            cuantizado,
            modalidad
    );

    desplieguesPorId.insertarOrdenado(nuevo);
    desplieguesPorCodigo.insertarOrdenado(nuevo);
desplieguesPorModalidad[modalidad.getIndice()]
        .insertarOrdenado(nuevo);
    return Retorno.ok();
}


@Override
public Retorno buscarDesplieguePorId(String id) {

    // ERROR 1:
    // id vacío, con espacios o null
    if (textoVacio(id)) {
        return Retorno.error1();
    }

    Despliegue encontrado = buscarPorId(id);

    // ERROR 2:
    // no existe un despliegue con ese id
    if (encontrado == null) {
        return Retorno.error2();
    }

    return Retorno.ok(encontrado.toString());
}
@Override
public Retorno listarDesplieguesPorIdAscendente() {

    String resultado = "";
    NodoDoble<Despliegue> actual = desplieguesPorId.getPrimero();

    while (actual != null) {

        if (!resultado.isEmpty()) {
            resultado += "|";
        }

        resultado += actual.getDato().toString();

        actual = actual.getSiguiente();
    }

    return Retorno.ok(resultado);
}   

@Override
public Retorno listarDesplieguesPorIdDescendente() {

    String resultado = "";
    NodoDoble<Despliegue> actual = desplieguesPorId.getUltimo();

    while (actual != null) {

        if (!resultado.isEmpty()) {
            resultado += "|";
        }

        resultado += actual.getDato().toString();

        actual = actual.getAnterior();
    }

    return Retorno.ok(resultado);
}

@Override
public Retorno buscarDesplieguePorCodigo(String codigo) {

    // ERROR 1
    if (textoVacio(codigo)) {
        return Retorno.error1();
    }

    // ERROR 2
    if (!codigo.matches("[A-Z]{3}-[0-9]{3}-[A-Za-z0-9]{4}")) {
        return Retorno.error2();
    }

    Despliegue encontrado =
            desplieguesPorCodigo.buscarPorCodigo(codigo);

    // ERROR 3
    if (encontrado == null) {
        return Retorno.error3();
    }

    return Retorno.ok(encontrado.toString());
}
@Override
public Retorno listarDesplieguesPorCodigoAscendente() {

    String resultado = "";

    NodoDoble<Despliegue> actual =
            desplieguesPorCodigo.getPrimero();

    while (actual != null) {

        if (!resultado.isEmpty()) {
            resultado += "|";
        }

        resultado += actual.getDato().toString();

        actual = actual.getSiguiente();
    }

    return Retorno.ok(resultado);
}
@Override
public Retorno listarDesplieguesPorModalidad(Modalidad unaModalidad) {

    // ERROR 1
    if (unaModalidad == null) {
        return Retorno.error1();
    }

    ListaDesplieguesCodigo lista =
            desplieguesPorModalidad[unaModalidad.getIndice()];

    String resultado = "";

    NodoDoble<Despliegue> actual = lista.getPrimero();

    while (actual != null) {

        if (!resultado.isEmpty()) {
            resultado += "|";
        }

        resultado += actual.getDato().toString();

        actual = actual.getSiguiente();
    }

    return Retorno.ok(resultado);
}

@Override
public Retorno registrarRegion(String codigo, String nombre,
                               String pais, String zona) {

    // ERROR 1
    if (textoVacio(codigo)
            || textoVacio(nombre)
            || textoVacio(pais)
            || textoVacio(zona)) {
        return Retorno.error1();
    }

    // ERROR 2
    if (buscarRegionPorCodigo(codigo) != null) {
        return Retorno.error2();
    }

    Region nueva = new Region(
            codigo,
            nombre,
            pais,
            zona
    );

    regiones.agregarAlInicio(nueva);

    return Retorno.ok();
}
@Override
public Retorno obtenerRegion(String codigo) {

    // ERROR 1
    if (textoVacio(codigo)) {
        return Retorno.error1();
    }

    Region encontrada = buscarRegionPorCodigo(codigo);

    // ERROR 2
    if (encontrada == null) {
        return Retorno.error2();
    }

    return Retorno.ok(encontrada.toString());
}
@Override
public Retorno solicitarDespliegueEnRegion(String idDespliegue,
                                           String codigoRegion) {

    // ERROR 1
    if (textoVacio(idDespliegue)
            || textoVacio(codigoRegion)) {
        return Retorno.error1();
    }

    // ERROR 2
    Despliegue despliegue = buscarPorId(idDespliegue);

    if (despliegue == null) {
        return Retorno.error2();
    }

    // ERROR 3
    Region region = buscarRegionPorCodigo(codigoRegion);

    if (region == null) {
        return Retorno.error3();
    }

    // ERROR 4
    if (region.tieneActivo(despliegue)
            || region.tienePendiente(despliegue)) {
        return Retorno.error4();
    }

    region.agregarPendiente(despliegue);

    return Retorno.ok();
}
@Override
public Retorno procesarSiguienteDespliegue(String codigoRegion) {

    // ERROR 1
    if (textoVacio(codigoRegion)) {
        return Retorno.error1();
    }

    // ERROR 2
    Region region = buscarRegionPorCodigo(codigoRegion);

    if (region == null) {
        return Retorno.error2();
    }

    // ERROR 3
    if (!region.tienePendientes()) {
        return Retorno.error3();
    }

    Despliegue procesado =
            region.procesarSiguientePendiente();

    return Retorno.ok(procesado.toString());
}
@Override
public Retorno listarDesplieguesActivosDeRegion(String codigoRegion) {

    // ERROR 1
    if (textoVacio(codigoRegion)) {
        return Retorno.error1();
    }

    // ERROR 2
    Region region = buscarRegionPorCodigo(codigoRegion);

    if (region == null) {
        return Retorno.error2();
    }

    String resultado = "";

    NodoDoble<Despliegue> actual =
            region.getActivos().getPrimero();

    while (actual != null) {

        if (!resultado.isEmpty()) {
            resultado += "|";
        }

        resultado += actual.getDato().toString();

        actual = actual.getSiguiente();
    }

    return Retorno.ok(resultado);
}

@Override
public Retorno retirarUltimoDespliegueProcesado(String codigoRegion) {

    // ERROR 1
    if (textoVacio(codigoRegion)) {
        return Retorno.error1();
    }

    // ERROR 2
    Region region = buscarRegionPorCodigo(codigoRegion);

    if (region == null) {
        return Retorno.error2();
    }

    // ERROR 3
    if (!region.tieneHistorial()) {
        return Retorno.error3();
    }

    Despliegue retirado =
            region.retirarUltimoProcesado();

    return Retorno.ok(retirado.toString());
}

    private boolean textoVacio(String texto) {
    return texto == null || texto.trim().isEmpty();
}

private Despliegue buscarPorId(String id) {
    NodoDoble<Despliegue> actual = desplieguesPorId.getPrimero();

    while (actual != null) {
        if (actual.getDato().getId().equals(id)) {
            return actual.getDato();
        }

        actual = actual.getSiguiente();
    }

    return null;
}

private Despliegue buscarPorCodigo(String codigo) {
    NodoDoble<Despliegue> actual = desplieguesPorId.getPrimero();

    while (actual != null) {
        if (actual.getDato().getCodigo().equals(codigo)) {
            return actual.getDato();
        }

        actual = actual.getSiguiente();
    }

    return null;
}
private Region buscarRegionPorCodigo(String codigo) {

    NodoSimple<Region> actual = regiones.getPrimero();

    while (actual != null) {

        if (actual.getDato().getCodigo().equals(codigo)) {
            return actual.getDato();
        }

        actual = actual.getSiguiente();
    }

    return null;
}

}
