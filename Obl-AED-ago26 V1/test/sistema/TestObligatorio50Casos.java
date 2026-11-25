package sistema;

import org.junit.Test;
import sistema.ImplementacionSistema;
import sistema.Modalidad;
import sistema.Retorno;
import sistema.Sistema;

import static org.junit.Assert.*;

public class TestObligatorio50Casos {

    private static final String D1 = "DEP-0001;LLM-070-A1B2;Modelo texto;true;Texto";
    private static final String D2 = "DEP-0002;AUD-002-C3D4;Modelo audio;false;Audio";
    private static final String D3 = "DEP-0010;VIS-004-Z9z9;Modelo visión;false;Visión";
    private static final String D4 = "DEP-0005;MMM-010-0000;Modelo multimodal;true;Multimodal";
    private static final String D5 = "DEP-0003;OTH-999-a0B9;Modelo otros;false;Otros";

    private Sistema nuevoSistema() {
        Sistema s = new ImplementacionSistema();
        assertOK(s.inicializarSistema());
        return s;
    }

    private void cargarCincoDespliegues(Sistema s) {
        assertOK(s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Modelo texto", true, Modalidad.TEXTO));
        assertOK(s.registrarDespliegue("DEP-0002", "AUD-002-C3D4", "Modelo audio", false, Modalidad.AUDIO));
        assertOK(s.registrarDespliegue("DEP-0010", "VIS-004-Z9z9", "Modelo visión", false, Modalidad.VISION));
        assertOK(s.registrarDespliegue("DEP-0005", "MMM-010-0000", "Modelo multimodal", true, Modalidad.MULTIMODAL));
        assertOK(s.registrarDespliegue("DEP-0003", "OTH-999-a0B9", "Modelo otros", false, Modalidad.OTROS));
    }

    private void registrarRegionBase(Sistema s) {
        assertOK(s.registrarRegion("us-east-1", "Virginia", "Estados Unidos", "Este"));
    }

    private void assertOK(Retorno r) {
        assertNotNull(r);
        assertEquals(Retorno.Resultado.OK, r.getResultado());
    }

    private void assertError(int numero, Retorno r) {
        assertNotNull(r);
        assertEquals(Retorno.Resultado.valueOf("ERROR_" + numero), r.getResultado());
    }

    private void assertTexto(String esperado, Retorno r) {
        assertOK(r);
        assertEquals(esperado, r.getValorString());
    }

    private void assertNoTerminaEnBarra(Retorno r) {
        assertOK(r);
        assertNotNull(r.getValorString());
        assertFalse(r.getValorString().endsWith("|"));
    }

    // ============================================================
    // 01 - Inicializar Sistema (3 casos)
    // ============================================================

    @Test public void f01_inicializaYDejaSistemaVacio() {
        Sistema s = new ImplementacionSistema();
        assertOK(s.inicializarSistema());
        assertTexto("", s.listarDesplieguesPorIdAscendente());
        assertTexto("", s.listarDesplieguesPorCodigoAscendente());
    }

    @Test public void f01_reinicializarBorraTodoElEstado() {
        Sistema s = nuevoSistema();
        cargarCincoDespliegues(s);
        registrarRegionBase(s);
        assertOK(s.solicitarDespliegueEnRegion("DEP-0001", "us-east-1"));
        assertTexto(D1, s.procesarSiguienteDespliegue("us-east-1"));
        assertOK(s.solicitarDespliegueEnRegion("DEP-0002", "us-east-1"));

        assertOK(s.inicializarSistema());
        assertTexto("", s.listarDesplieguesPorIdAscendente());
        assertError(2, s.buscarDesplieguePorId("DEP-0001"));
        assertError(2, s.obtenerRegion("us-east-1"));
    }

    @Test public void f01_instanciasNoCompartenEstado() {
        Sistema a = nuevoSistema();
        Sistema b = nuevoSistema();
        assertOK(a.registrarDespliegue("DEP-A", "AAA-000-0000", "A", false, Modalidad.TEXTO));
        assertError(2, b.buscarDesplieguePorId("DEP-A"));
        assertTexto("", b.listarDesplieguesPorIdAscendente());
    }

    // ============================================================
    // 02 - Registrar despliegue (5 casos)
    // ============================================================

    @Test public void f02_registroValidoYFormatoExacto() {
        Sistema s = nuevoSistema();
        assertOK(s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Modelo texto", true, Modalidad.TEXTO));
        assertTexto(D1, s.buscarDesplieguePorId("DEP-0001"));
    }

    @Test public void f02_error1ParametrosNullVaciosOEspacios() {
        Sistema s = nuevoSistema();
        assertError(1, s.registrarDespliegue(null, "AAA-000-0000", "D", false, Modalidad.TEXTO));
        assertError(1, s.registrarDespliegue("", "AAA-000-0000", "D", false, Modalidad.TEXTO));
        assertError(1, s.registrarDespliegue("   ", "AAA-000-0000", "D", false, Modalidad.TEXTO));
        assertError(1, s.registrarDespliegue("id", null, "D", false, Modalidad.TEXTO));
        assertError(1, s.registrarDespliegue("id", "", "D", false, Modalidad.TEXTO));
        assertError(1, s.registrarDespliegue("id", "   ", "D", false, Modalidad.TEXTO));
        assertError(1, s.registrarDespliegue("id", "AAA-000-0000", null, false, Modalidad.TEXTO));
        assertError(1, s.registrarDespliegue("id", "AAA-000-0000", "", false, Modalidad.TEXTO));
        assertError(1, s.registrarDespliegue("id", "AAA-000-0000", "   ", false, Modalidad.TEXTO));
        assertError(1, s.registrarDespliegue("id", "AAA-000-0000", "D", false, null));
    }

    @Test public void f02_error2FormatoCodigoInvalidoYCasosBorde() {
        Sistema s = nuevoSistema();
        String[] invalidos = {
            "AA-000-0000", "AAAA-000-0000", "AaA-000-0000", "A1A-000-0000",
            "AAA-00-0000", "AAA-0000-0000", "AAA-0A0-0000",
            "AAA-000-000", "AAA-000-00000", "AAA-000-A_00", "AAA-000-A-00",
            "AAA_000-0000", "AAA-000_0000", " AAA-000-0000", "AAA-000-0000 "
        };
        int i = 0;
        for (String codigo : invalidos) {
            assertError(2, s.registrarDespliegue("id" + (i++), codigo, "D", false, Modalidad.TEXTO));
        }
        assertOK(s.registrarDespliegue("MIN", "AAA-000-0000", "Min", false, Modalidad.OTROS));
        assertOK(s.registrarDespliegue("MAX", "ZZZ-999-Zz9A", "Max", true, Modalidad.OTROS));
    }

    @Test public void f02_error3IdDuplicado() {
        Sistema s = nuevoSistema();
        assertOK(s.registrarDespliegue("DEP-X", "AAA-000-0000", "Uno", false, Modalidad.TEXTO));
        assertError(3, s.registrarDespliegue("DEP-X", "BBB-000-0000", "Dos", true, Modalidad.AUDIO));
    }

    @Test public void f02_error4CodigoDuplicado() {
        Sistema s = nuevoSistema();
        assertOK(s.registrarDespliegue("DEP-X", "AAA-000-0000", "Uno", false, Modalidad.TEXTO));
        assertError(4, s.registrarDespliegue("DEP-Y", "AAA-000-0000", "Dos", true, Modalidad.AUDIO));
    }

    // ============================================================
    // 03 - Buscar despliegue por id (3 casos)
    // ============================================================

    @Test public void f03_error1IdNullVacioOEspacios() {
        Sistema s = nuevoSistema();
        cargarCincoDespliegues(s);
        assertError(1, s.buscarDesplieguePorId(null));
        assertError(1, s.buscarDesplieguePorId(""));
        assertError(1, s.buscarDesplieguePorId("   "));
    }

    @Test public void f03_error2IdNoExiste() {
        Sistema s = nuevoSistema();
        cargarCincoDespliegues(s);
        assertError(2, s.buscarDesplieguePorId("NO-EXISTE"));
    }

    @Test public void f03_busquedaValidaDevuelveFormatoExacto() {
        Sistema s = nuevoSistema();
        cargarCincoDespliegues(s);
        assertTexto(D1, s.buscarDesplieguePorId("DEP-0001"));
        assertTexto(D2, s.buscarDesplieguePorId("DEP-0002"));
    }

    // ============================================================
    // 04 - Listar por id ascendente (2 casos)
    // ============================================================

    @Test public void f04_listaVacia() {
        Sistema s = nuevoSistema();
        assertTexto("", s.listarDesplieguesPorIdAscendente());
    }

    @Test public void f04_ordenLexicograficoAscendenteSinBarraFinal() {
        Sistema s = nuevoSistema();
        assertOK(s.registrarDespliegue("d2", "AAA-000-0002", "D2", false, Modalidad.TEXTO));
        assertOK(s.registrarDespliegue("d10", "AAA-000-0010", "D10", false, Modalidad.TEXTO));
        assertOK(s.registrarDespliegue("d1", "AAA-000-0001", "D1", false, Modalidad.TEXTO));
        Retorno r = s.listarDesplieguesPorIdAscendente();
        assertTexto("d1;AAA-000-0001;D1;false;Texto|d10;AAA-000-0010;D10;false;Texto|d2;AAA-000-0002;D2;false;Texto", r);
        assertNoTerminaEnBarra(r);
    }

    // ============================================================
    // 05 - Listar por id descendente (2 casos)
    // ============================================================

    @Test public void f05_listaVacia() {
        Sistema s = nuevoSistema();
        assertTexto("", s.listarDesplieguesPorIdDescendente());
    }

    @Test public void f05_ordenLexicograficoDescendenteSinBarraFinal() {
        Sistema s = nuevoSistema();
        assertOK(s.registrarDespliegue("d2", "AAA-000-0002", "D2", false, Modalidad.TEXTO));
        assertOK(s.registrarDespliegue("d10", "AAA-000-0010", "D10", false, Modalidad.TEXTO));
        assertOK(s.registrarDespliegue("d1", "AAA-000-0001", "D1", false, Modalidad.TEXTO));
        Retorno r = s.listarDesplieguesPorIdDescendente();
        assertTexto("d2;AAA-000-0002;D2;false;Texto|d10;AAA-000-0010;D10;false;Texto|d1;AAA-000-0001;D1;false;Texto", r);
        assertNoTerminaEnBarra(r);
    }

    // ============================================================
    // 06 - Buscar despliegue por codigo (4 casos)
    // ============================================================

    @Test public void f06_error1CodigoNullVacioOEspacios() {
        Sistema s = nuevoSistema();
        cargarCincoDespliegues(s);
        assertError(1, s.buscarDesplieguePorCodigo(null));
        assertError(1, s.buscarDesplieguePorCodigo(""));
        assertError(1, s.buscarDesplieguePorCodigo("   "));
    }

    @Test public void f06_error2FormatoIncorrecto() {
        Sistema s = nuevoSistema();
        cargarCincoDespliegues(s);
        String[] invalidos = {"llm-070-A1B2", "LL-070-A1B2", "LLM-70-A1B2", "LLM-070-A1_2", "LLM_070-A1B2"};
        for (String codigo : invalidos) assertError(2, s.buscarDesplieguePorCodigo(codigo));
    }

    @Test public void f06_error3FormatoValidoPeroNoExiste() {
        Sistema s = nuevoSistema();
        cargarCincoDespliegues(s);
        assertError(3, s.buscarDesplieguePorCodigo("AAA-111-B2C3"));
    }

    @Test public void f06_busquedaValidaDevuelveFormatoExacto() {
        Sistema s = nuevoSistema();
        cargarCincoDespliegues(s);
        assertTexto(D1, s.buscarDesplieguePorCodigo("LLM-070-A1B2"));
        assertTexto(D2, s.buscarDesplieguePorCodigo("AUD-002-C3D4"));
    }

    // ============================================================
    // 07 - Listar por codigo ascendente (3 casos)
    // ============================================================

    @Test public void f07_listaVacia() {
        Sistema s = nuevoSistema();
        assertTexto("", s.listarDesplieguesPorCodigoAscendente());
    }

    @Test public void f07_unSoloElemento() {
        Sistema s = nuevoSistema();
        assertOK(s.registrarDespliegue("x", "ABC-000-0000", "D", false, Modalidad.TEXTO));
        assertTexto("x;ABC-000-0000;D;false;Texto", s.listarDesplieguesPorCodigoAscendente());
    }

    @Test public void f07_ordenaPorCodigoNoPorIdNiAlta() {
        Sistema s = nuevoSistema();
        cargarCincoDespliegues(s);
        Retorno r = s.listarDesplieguesPorCodigoAscendente();
        assertTexto("DEP-0002;AUD-002-C3D4;Modelo audio;false;Audio|DEP-0001;LLM-070-A1B2;Modelo texto;true;Texto|DEP-0005;MMM-010-0000;Modelo multimodal;true;Multimodal|DEP-0003;OTH-999-a0B9;Modelo otros;false;Otros|DEP-0010;VIS-004-Z9z9;Modelo visión;false;Visión", r);
        assertNoTerminaEnBarra(r);
    }

    // ============================================================
    // 08 - Listar por modalidad (4 casos)
    // ============================================================

    @Test public void f08_error1ModalidadNull() {
        Sistema s = nuevoSistema();
        assertError(1, s.listarDesplieguesPorModalidad(null));
    }

    @Test public void f08_modalidadSinElementosRetornaVacio() {
        Sistema s = nuevoSistema();
        assertOK(s.registrarDespliegue("A", "AAA-000-0000", "D", false, Modalidad.TEXTO));
        assertTexto("", s.listarDesplieguesPorModalidad(Modalidad.AUDIO));
    }

    @Test public void f08_cadaModalidadDevuelveSoloSusElementos() {
        Sistema s = nuevoSistema();
        cargarCincoDespliegues(s);
        assertTexto(D1, s.listarDesplieguesPorModalidad(Modalidad.TEXTO));
        assertTexto(D2, s.listarDesplieguesPorModalidad(Modalidad.AUDIO));
        assertTexto(D3, s.listarDesplieguesPorModalidad(Modalidad.VISION));
        assertTexto(D4, s.listarDesplieguesPorModalidad(Modalidad.MULTIMODAL));
        assertTexto(D5, s.listarDesplieguesPorModalidad(Modalidad.OTROS));
    }

    @Test public void f08_ordenaPorCodigoDentroDeLaModalidad() {
        Sistema s = nuevoSistema();
        assertOK(s.registrarDespliegue("Z", "ZZZ-001-0000", "Z", false, Modalidad.TEXTO));
        assertOK(s.registrarDespliegue("A", "AAA-001-0000", "A", false, Modalidad.TEXTO));
        assertOK(s.registrarDespliegue("M", "MMM-001-0000", "M", false, Modalidad.TEXTO));
        Retorno r = s.listarDesplieguesPorModalidad(Modalidad.TEXTO);
        assertTexto("A;AAA-001-0000;A;false;Texto|M;MMM-001-0000;M;false;Texto|Z;ZZZ-001-0000;Z;false;Texto", r);
        assertNoTerminaEnBarra(r);
    }

    // ============================================================
    // 09 - Registrar region (4 casos)
    // ============================================================

    @Test public void f09_registroValidoYFormatoLibreDeCodigo() {
        Sistema s = nuevoSistema();
        assertOK(s.registrarRegion("region libre", "Nombre", "Pais", "Zona"));
        assertTexto("region libre;Nombre;Pais;Zona", s.obtenerRegion("region libre"));
    }

    @Test public void f09_error1ParametrosNullVaciosOEspacios() {
        Sistema s = nuevoSistema();
        assertError(1, s.registrarRegion(null, "N", "P", "Z"));
        assertError(1, s.registrarRegion("", "N", "P", "Z"));
        assertError(1, s.registrarRegion("   ", "N", "P", "Z"));
        assertError(1, s.registrarRegion("r", null, "P", "Z"));
        assertError(1, s.registrarRegion("r", "", "P", "Z"));
        assertError(1, s.registrarRegion("r", "N", null, "Z"));
        assertError(1, s.registrarRegion("r", "N", "", "Z"));
        assertError(1, s.registrarRegion("r", "N", "P", null));
        assertError(1, s.registrarRegion("r", "N", "P", "   "));
    }

    @Test public void f09_error2CodigoDuplicado() {
        Sistema s = nuevoSistema();
        assertOK(s.registrarRegion("r1", "N1", "P1", "Z1"));
        assertError(2, s.registrarRegion("r1", "N2", "P2", "Z2"));
    }

    @Test public void f09_precedenciaError1SobreDuplicado() {
        Sistema s = nuevoSistema();
        assertOK(s.registrarRegion("r1", "N1", "P1", "Z1"));
        assertError(1, s.registrarRegion("r1", "   ", "P2", "Z2"));
    }

    // ============================================================
    // 10 - Obtener region (3 casos)
    // ============================================================

    @Test public void f10_error1CodigoNullVacioOEspacios() {
        Sistema s = nuevoSistema();
        assertError(1, s.obtenerRegion(null));
        assertError(1, s.obtenerRegion(""));
        assertError(1, s.obtenerRegion("   "));
    }

    @Test public void f10_error2RegionNoExiste() {
        Sistema s = nuevoSistema();
        assertError(2, s.obtenerRegion("us-east-1"));
    }

    @Test public void f10_obtieneRegionCorrectaEntreVarias() {
        Sistema s = nuevoSistema();
        assertOK(s.registrarRegion("r1", "N1", "P1", "Z1"));
        assertOK(s.registrarRegion("r2", "N2", "P2", "Z2"));
        assertOK(s.registrarRegion("r3", "N3", "P3", "Z3"));
        assertTexto("r2;N2;P2;Z2", s.obtenerRegion("r2"));
    }

    // ============================================================
    // 11 - Solicitar despliegue en region (6 casos)
    // ============================================================

    @Test public void f11_solicitudValidaQuedaPendienteNoActiva() {
        Sistema s = nuevoSistema();
        cargarCincoDespliegues(s);
        registrarRegionBase(s);
        assertOK(s.solicitarDespliegueEnRegion("DEP-0001", "us-east-1"));
        assertTexto("", s.listarDesplieguesActivosDeRegion("us-east-1"));
        assertTexto(D1, s.procesarSiguienteDespliegue("us-east-1"));
    }

    @Test public void f11_error1ParametrosNullVaciosOEspacios() {
        Sistema s = nuevoSistema();
        cargarCincoDespliegues(s);
        registrarRegionBase(s);
        assertError(1, s.solicitarDespliegueEnRegion(null, "us-east-1"));
        assertError(1, s.solicitarDespliegueEnRegion("", "us-east-1"));
        assertError(1, s.solicitarDespliegueEnRegion("   ", "us-east-1"));
        assertError(1, s.solicitarDespliegueEnRegion("DEP-0001", null));
        assertError(1, s.solicitarDespliegueEnRegion("DEP-0001", ""));
        assertError(1, s.solicitarDespliegueEnRegion("DEP-0001", "   "));
    }

    @Test public void f11_error2DespliegueNoExiste() {
        Sistema s = nuevoSistema();
        cargarCincoDespliegues(s);
        registrarRegionBase(s);
        assertError(2, s.solicitarDespliegueEnRegion("NO", "us-east-1"));
    }

    @Test public void f11_error3RegionNoExiste() {
        Sistema s = nuevoSistema();
        cargarCincoDespliegues(s);
        registrarRegionBase(s);
        assertError(3, s.solicitarDespliegueEnRegion("DEP-0001", "no-region"));
    }

    @Test public void f11_error4YaPendienteOYaActivo() {
        Sistema s = nuevoSistema();
        cargarCincoDespliegues(s);
        registrarRegionBase(s);
        assertOK(s.solicitarDespliegueEnRegion("DEP-0001", "us-east-1"));
        assertError(4, s.solicitarDespliegueEnRegion("DEP-0001", "us-east-1"));
        assertTexto(D1, s.procesarSiguienteDespliegue("us-east-1"));
        assertError(4, s.solicitarDespliegueEnRegion("DEP-0001", "us-east-1"));
    }

    @Test public void f11_precedenciaYAislamientoEntreRegiones() {
        Sistema s = nuevoSistema();
        cargarCincoDespliegues(s);
        registrarRegionBase(s);
        assertOK(s.registrarRegion("eu-west-1", "Frankfurt", "Alemania", "Centro"));

        assertError(1, s.solicitarDespliegueEnRegion("", "no-region"));
        assertError(2, s.solicitarDespliegueEnRegion("NO", "no-region"));

        assertOK(s.solicitarDespliegueEnRegion("DEP-0001", "us-east-1"));
        assertOK(s.solicitarDespliegueEnRegion("DEP-0001", "eu-west-1"));
        assertTexto(D1, s.procesarSiguienteDespliegue("us-east-1"));
        assertTexto(D1, s.procesarSiguienteDespliegue("eu-west-1"));
    }

    // ============================================================
    // 12 - Procesar siguiente despliegue (4 casos)
    // ============================================================

    @Test public void f12_error1CodigoRegionNullVacioOEspacios() {
        Sistema s = nuevoSistema();
        registrarRegionBase(s);
        assertError(1, s.procesarSiguienteDespliegue(null));
        assertError(1, s.procesarSiguienteDespliegue(""));
        assertError(1, s.procesarSiguienteDespliegue("   "));
    }

    @Test public void f12_error2RegionNoExiste() {
        Sistema s = nuevoSistema();
        assertError(2, s.procesarSiguienteDespliegue("no-region"));
    }

    @Test public void f12_error3RegionSinPendientes() {
        Sistema s = nuevoSistema();
        registrarRegionBase(s);
        assertError(3, s.procesarSiguienteDespliegue("us-east-1"));
    }

    @Test public void f12_procesaFIFOYPasaAActivo() {
        Sistema s = nuevoSistema();
        cargarCincoDespliegues(s);
        registrarRegionBase(s);
        assertOK(s.solicitarDespliegueEnRegion("DEP-0005", "us-east-1"));
        assertOK(s.solicitarDespliegueEnRegion("DEP-0001", "us-east-1"));
        assertOK(s.solicitarDespliegueEnRegion("DEP-0002", "us-east-1"));

        assertTexto(D4, s.procesarSiguienteDespliegue("us-east-1"));
        assertTexto(D1, s.procesarSiguienteDespliegue("us-east-1"));
        assertTexto(D2, s.procesarSiguienteDespliegue("us-east-1"));
        assertError(3, s.procesarSiguienteDespliegue("us-east-1"));
        assertError(4, s.solicitarDespliegueEnRegion("DEP-0001", "us-east-1"));
    }

    // ============================================================
    // 13 - Listar activos de region (3 casos)
    // ============================================================

    @Test public void f13_error1CodigoRegionNullVacioOEspacios() {
        Sistema s = nuevoSistema();
        assertError(1, s.listarDesplieguesActivosDeRegion(null));
        assertError(1, s.listarDesplieguesActivosDeRegion(""));
        assertError(1, s.listarDesplieguesActivosDeRegion("   "));
    }

    @Test public void f13_error2RegionNoExiste() {
        Sistema s = nuevoSistema();
        assertError(2, s.listarDesplieguesActivosDeRegion("no-region"));
    }

    @Test public void f13_vacioPendientesNoCuentanYActivosOrdenadosPorCodigo() {
        Sistema s = nuevoSistema();
        cargarCincoDespliegues(s);
        registrarRegionBase(s);
        assertTexto("", s.listarDesplieguesActivosDeRegion("us-east-1"));

        assertOK(s.solicitarDespliegueEnRegion("DEP-0001", "us-east-1"));
        assertTexto("", s.listarDesplieguesActivosDeRegion("us-east-1"));

        assertOK(s.solicitarDespliegueEnRegion("DEP-0002", "us-east-1"));
        assertOK(s.solicitarDespliegueEnRegion("DEP-0010", "us-east-1"));
        assertTexto(D1, s.procesarSiguienteDespliegue("us-east-1"));
        assertTexto(D2, s.procesarSiguienteDespliegue("us-east-1"));
        assertTexto(D3, s.procesarSiguienteDespliegue("us-east-1"));

        Retorno r = s.listarDesplieguesActivosDeRegion("us-east-1");
        assertTexto(D2 + "|" + D1 + "|" + D3, r);
        assertNoTerminaEnBarra(r);
    }

    // ============================================================
    // 14 - Retirar ultimo despliegue procesado (4 casos)
    // ============================================================

    @Test public void f14_error1CodigoRegionNullVacioOEspacios() {
        Sistema s = nuevoSistema();
        assertError(1, s.retirarUltimoDespliegueProcesado(null));
        assertError(1, s.retirarUltimoDespliegueProcesado(""));
        assertError(1, s.retirarUltimoDespliegueProcesado("   "));
    }

    @Test public void f14_error2RegionNoExiste() {
        Sistema s = nuevoSistema();
        assertError(2, s.retirarUltimoDespliegueProcesado("no-region"));
    }

    @Test public void f14_error3SinProcesadosActivosInclusoConPendientes() {
        Sistema s = nuevoSistema();
        cargarCincoDespliegues(s);
        registrarRegionBase(s);
        assertError(3, s.retirarUltimoDespliegueProcesado("us-east-1"));
        assertOK(s.solicitarDespliegueEnRegion("DEP-0001", "us-east-1"));
        assertError(3, s.retirarUltimoDespliegueProcesado("us-east-1"));
    }

    @Test public void f14_retiroEsLIFOQuitaActivoNoCatalogoYPermiteReingreso() {
        Sistema s = nuevoSistema();
        cargarCincoDespliegues(s);
        registrarRegionBase(s);
        assertOK(s.solicitarDespliegueEnRegion("DEP-0005", "us-east-1"));
        assertOK(s.solicitarDespliegueEnRegion("DEP-0001", "us-east-1"));
        assertOK(s.solicitarDespliegueEnRegion("DEP-0002", "us-east-1"));
        assertTexto(D4, s.procesarSiguienteDespliegue("us-east-1"));
        assertTexto(D1, s.procesarSiguienteDespliegue("us-east-1"));
        assertTexto(D2, s.procesarSiguienteDespliegue("us-east-1"));

        assertTexto(D2, s.retirarUltimoDespliegueProcesado("us-east-1"));
        assertTexto(D1, s.retirarUltimoDespliegueProcesado("us-east-1"));
        assertTexto(D4, s.retirarUltimoDespliegueProcesado("us-east-1"));
        assertError(3, s.retirarUltimoDespliegueProcesado("us-east-1"));

        assertTexto(D2, s.buscarDesplieguePorId("DEP-0002"));
        assertOK(s.solicitarDespliegueEnRegion("DEP-0002", "us-east-1"));
        assertTexto(D2, s.procesarSiguienteDespliegue("us-east-1"));
    }

}
