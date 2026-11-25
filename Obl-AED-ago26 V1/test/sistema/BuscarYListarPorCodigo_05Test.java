package sistema;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BuscarYListarPorCodigo_05Test {

    private Sistema sistema;

    @Before
    public void setUp() {
        sistema = new ImplementacionSistema();
        sistema.inicializarSistema();

        sistema.registrarDespliegue(
                "DEP-0001",
                "VIS-004-C3D4",
                "Modelo vision",
                false,
                Modalidad.VISION
        );

        sistema.registrarDespliegue(
                "DEP-0002",
                "AUD-002-A1B2",
                "Modelo audio",
                false,
                Modalidad.AUDIO
        );

        sistema.registrarDespliegue(
                "DEP-0003",
                "LLM-070-Z9X8",
                "Modelo texto",
                true,
                Modalidad.TEXTO
        );
    }

    @Test
    public void buscarPorCodigoCorrecto() {

        Retorno r =
                sistema.buscarDesplieguePorCodigo("AUD-002-A1B2");

        assertEquals(
                Retorno.Resultado.OK,
                r.getResultado()
        );

        assertEquals(
                "DEP-0002;AUD-002-A1B2;Modelo audio;false;Audio",
                r.getValorString()
        );
    }

    @Test
    public void buscarPorCodigoVacio() {

        Retorno r =
                sistema.buscarDesplieguePorCodigo("   ");

        assertEquals(
                Retorno.Resultado.ERROR_1,
                r.getResultado()
        );
    }

    @Test
    public void buscarPorCodigoFormatoIncorrecto() {

        Retorno r =
                sistema.buscarDesplieguePorCodigo("aud-002-A1B2");

        assertEquals(
                Retorno.Resultado.ERROR_2,
                r.getResultado()
        );
    }

    @Test
    public void buscarPorCodigoInexistente() {

        Retorno r =
                sistema.buscarDesplieguePorCodigo("LLM-999-AAAA");

        assertEquals(
                Retorno.Resultado.ERROR_3,
                r.getResultado()
        );
    }

    @Test
    public void listarPorCodigoAscendente() {

        Retorno r =
                sistema.listarDesplieguesPorCodigoAscendente();

        assertEquals(
                Retorno.Resultado.OK,
                r.getResultado()
        );

        assertEquals(
                "DEP-0002;AUD-002-A1B2;Modelo audio;false;Audio"
                + "|DEP-0003;LLM-070-Z9X8;Modelo texto;true;Texto"
                + "|DEP-0001;VIS-004-C3D4;Modelo vision;false;Visión",
                r.getValorString()
        );
    }
}   