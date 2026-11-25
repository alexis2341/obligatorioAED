package sistema;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ListarPorModalidad_06Test {

    private Sistema sistema;

    @Before
    public void setUp() {

        sistema = new ImplementacionSistema();
        sistema.inicializarSistema();

        sistema.registrarDespliegue(
                "DEP-0001",
                "LLM-070-Z9X8",
                "Texto grande",
                true,
                Modalidad.TEXTO
        );

        sistema.registrarDespliegue(
                "DEP-0002",
                "LLM-010-A1B2",
                "Texto chico",
                false,
                Modalidad.TEXTO
        );

        sistema.registrarDespliegue(
                "DEP-0003",
                "AUD-020-C3D4",
                "Audio",
                false,
                Modalidad.AUDIO
        );
    }

    @Test
    public void listarTextoOrdenadoPorCodigo() {

        Retorno r =
                sistema.listarDesplieguesPorModalidad(
                        Modalidad.TEXTO
                );

        assertEquals(
                Retorno.Resultado.OK,
                r.getResultado()
        );

        assertEquals(
                "DEP-0002;LLM-010-A1B2;Texto chico;false;Texto"
                + "|DEP-0001;LLM-070-Z9X8;Texto grande;true;Texto",
                r.getValorString()
        );
    }

    @Test
    public void modalidadSinDespliegues() {

        Retorno r =
                sistema.listarDesplieguesPorModalidad(
                        Modalidad.VISION
                );

        assertEquals(
                Retorno.Resultado.OK,
                r.getResultado()
        );

        assertEquals(
                "",
                r.getValorString()
        );
    }

    @Test
    public void modalidadNull() {

        Retorno r =
                sistema.listarDesplieguesPorModalidad(null);

        assertEquals(
                Retorno.Resultado.ERROR_1,
                r.getResultado()
        );
    }
}