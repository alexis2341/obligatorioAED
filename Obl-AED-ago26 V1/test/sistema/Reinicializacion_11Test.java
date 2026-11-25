package sistema;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Reinicializacion_11Test {

    private Sistema sistema;

    @Before
    public void setUp() {

        sistema = new ImplementacionSistema();
        sistema.inicializarSistema();

        sistema.registrarDespliegue(
                "DEP-0001",
                "LLM-070-A1B2",
                "Modelo texto",
                true,
                Modalidad.TEXTO
        );

        sistema.registrarRegion(
                "us-east-1",
                "Virginia",
                "Estados Unidos",
                "us-east-1a"
        );

        sistema.solicitarDespliegueEnRegion(
                "DEP-0001",
                "us-east-1"
        );

        sistema.procesarSiguienteDespliegue(
                "us-east-1"
        );
    }

    @Test
    public void reinicializarBorraDespliegues() {

        sistema.inicializarSistema();

        Retorno r =
                sistema.buscarDesplieguePorId("DEP-0001");

        assertEquals(
                Retorno.Resultado.ERROR_2,
                r.getResultado()
        );
    }

    @Test
    public void reinicializarBorraRegiones() {

        sistema.inicializarSistema();

        Retorno r =
                sistema.obtenerRegion("us-east-1");

        assertEquals(
                Retorno.Resultado.ERROR_2,
                r.getResultado()
        );
    }

    @Test
    public void reinicializarDejaListadosVacios() {

        sistema.inicializarSistema();

        Retorno r =
                sistema.listarDesplieguesPorIdAscendente();

        assertEquals(
                Retorno.Resultado.OK,
                r.getResultado()
        );

        assertEquals(
                "",
                r.getValorString()
        );
    }
}