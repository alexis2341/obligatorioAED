package sistema;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SolicitudesPendientes_08Test {

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
    }

    @Test
    public void solicitarCorrectamente() {

        Retorno r =
                sistema.solicitarDespliegueEnRegion(
                        "DEP-0001",
                        "us-east-1"
                );

        assertEquals(
                Retorno.Resultado.OK,
                r.getResultado()
        );
    }

    @Test
    public void solicitarConDatoVacio() {

        Retorno r =
                sistema.solicitarDespliegueEnRegion(
                        "",
                        "us-east-1"
                );

        assertEquals(
                Retorno.Resultado.ERROR_1,
                r.getResultado()
        );
    }

    @Test
    public void solicitarDespliegueInexistente() {

        Retorno r =
                sistema.solicitarDespliegueEnRegion(
                        "DEP-9999",
                        "us-east-1"
                );

        assertEquals(
                Retorno.Resultado.ERROR_2,
                r.getResultado()
        );
    }

    @Test
    public void solicitarRegionInexistente() {

        Retorno r =
                sistema.solicitarDespliegueEnRegion(
                        "DEP-0001",
                        "no-existe"
                );

        assertEquals(
                Retorno.Resultado.ERROR_3,
                r.getResultado()
        );
    }

    @Test
    public void noPermiteSolicitudPendienteDuplicada() {

        sistema.solicitarDespliegueEnRegion(
                "DEP-0001",
                "us-east-1"
        );

        Retorno r =
                sistema.solicitarDespliegueEnRegion(
                        "DEP-0001",
                        "us-east-1"
                );

        assertEquals(
                Retorno.Resultado.ERROR_4,
                r.getResultado()
        );
    }
}