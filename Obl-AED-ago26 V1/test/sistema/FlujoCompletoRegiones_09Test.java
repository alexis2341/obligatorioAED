package sistema;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FlujoCompletoRegiones_09Test {

    private Sistema sistema;

    @Before
    public void setUp() {

        sistema = new ImplementacionSistema();
        sistema.inicializarSistema();

        sistema.registrarDespliegue(
                "DEP-0001",
                "VIS-040-Z9X8",
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
                "LLM-070-C3D4",
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
    public void procesarRespetaOrdenFIFO() {

        sistema.solicitarDespliegueEnRegion(
                "DEP-0001",
                "us-east-1"
        );

        sistema.solicitarDespliegueEnRegion(
                "DEP-0002",
                "us-east-1"
        );

        sistema.solicitarDespliegueEnRegion(
                "DEP-0003",
                "us-east-1"
        );

        Retorno primero =
                sistema.procesarSiguienteDespliegue(
                        "us-east-1"
                );

        assertEquals(
                Retorno.Resultado.OK,
                primero.getResultado()
        );

        assertEquals(
                "DEP-0001;VIS-040-Z9X8;Modelo vision;false;Visión",
                primero.getValorString()
        );

        Retorno segundo =
                sistema.procesarSiguienteDespliegue(
                        "us-east-1"
                );

        assertEquals(
                "DEP-0002;AUD-002-A1B2;Modelo audio;false;Audio",
                segundo.getValorString()
        );
    }

    @Test
    public void activosQuedanOrdenadosPorCodigo() {

        sistema.solicitarDespliegueEnRegion(
                "DEP-0001",
                "us-east-1"
        );

        sistema.solicitarDespliegueEnRegion(
                "DEP-0002",
                "us-east-1"
        );

        sistema.solicitarDespliegueEnRegion(
                "DEP-0003",
                "us-east-1"
        );

        sistema.procesarSiguienteDespliegue("us-east-1");
        sistema.procesarSiguienteDespliegue("us-east-1");
        sistema.procesarSiguienteDespliegue("us-east-1");

        Retorno r =
                sistema.listarDesplieguesActivosDeRegion(
                        "us-east-1"
                );

        assertEquals(
                Retorno.Resultado.OK,
                r.getResultado()
        );

        assertEquals(
                "DEP-0002;AUD-002-A1B2;Modelo audio;false;Audio"
                + "|DEP-0003;LLM-070-C3D4;Modelo texto;true;Texto"
                + "|DEP-0001;VIS-040-Z9X8;Modelo vision;false;Visión",
                r.getValorString()
        );
    }

    @Test
    public void retirarRespetaHistorialLIFO() {

        sistema.solicitarDespliegueEnRegion(
                "DEP-0001",
                "us-east-1"
        );

        sistema.solicitarDespliegueEnRegion(
                "DEP-0002",
                "us-east-1"
        );

        sistema.procesarSiguienteDespliegue("us-east-1");
        sistema.procesarSiguienteDespliegue("us-east-1");

        Retorno retirado =
                sistema.retirarUltimoDespliegueProcesado(
                        "us-east-1"
                );

        assertEquals(
                Retorno.Resultado.OK,
                retirado.getResultado()
        );

        assertEquals(
                "DEP-0002;AUD-002-A1B2;Modelo audio;false;Audio",
                retirado.getValorString()
        );
    }

    @Test
    public void retiradoDejaDeEstarActivo() {

        sistema.solicitarDespliegueEnRegion(
                "DEP-0001",
                "us-east-1"
        );

        sistema.procesarSiguienteDespliegue(
                "us-east-1"
        );

        sistema.retirarUltimoDespliegueProcesado(
                "us-east-1"
        );

        Retorno activos =
                sistema.listarDesplieguesActivosDeRegion(
                        "us-east-1"
                );

        assertEquals(
                Retorno.Resultado.OK,
                activos.getResultado()
        );

        assertEquals(
                "",
                activos.getValorString()
        );
    }

    @Test
    public void retiradoPuedeVolverASolicitarse() {

        sistema.solicitarDespliegueEnRegion(
                "DEP-0001",
                "us-east-1"
        );

        sistema.procesarSiguienteDespliegue(
                "us-east-1"
        );

        sistema.retirarUltimoDespliegueProcesado(
                "us-east-1"
        );

        Retorno nuevaSolicitud =
                sistema.solicitarDespliegueEnRegion(
                        "DEP-0001",
                        "us-east-1"
                );

        assertEquals(
                Retorno.Resultado.OK,
                nuevaSolicitud.getResultado()
        );
    }
}