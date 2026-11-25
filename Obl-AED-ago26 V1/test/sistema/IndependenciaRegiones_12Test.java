package sistema;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IndependenciaRegiones_12Test {

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

        sistema.registrarRegion(
                "sa-east-1",
                "Sao Paulo",
                "Brasil",
                "sa-east-1a"
        );
    }

    @Test
    public void mismoDesplieguePuedeSolicitarseEnDosRegiones() {

        Retorno r1 =
                sistema.solicitarDespliegueEnRegion(
                        "DEP-0001",
                        "us-east-1"
                );

        Retorno r2 =
                sistema.solicitarDespliegueEnRegion(
                        "DEP-0001",
                        "sa-east-1"
                );

        assertEquals(
                Retorno.Resultado.OK,
                r1.getResultado()
        );

        assertEquals(
                Retorno.Resultado.OK,
                r2.getResultado()
        );
    }

    @Test
    public void procesarUnaRegionNoProcesaLaOtra() {

        sistema.solicitarDespliegueEnRegion(
                "DEP-0001",
                "us-east-1"
        );

        sistema.solicitarDespliegueEnRegion(
                "DEP-0001",
                "sa-east-1"
        );

        sistema.procesarSiguienteDespliegue(
                "us-east-1"
        );

        Retorno activosVirginia =
                sistema.listarDesplieguesActivosDeRegion(
                        "us-east-1"
                );

        Retorno activosSaoPaulo =
                sistema.listarDesplieguesActivosDeRegion(
                        "sa-east-1"
                );

        assertEquals(
                "DEP-0001;LLM-070-A1B2;Modelo texto;true;Texto",
                activosVirginia.getValorString()
        );

        assertEquals(
                "",
                activosSaoPaulo.getValorString()
        );
    }

    @Test
    public void retirarDeUnaRegionNoAfectaLaOtra() {

        sistema.solicitarDespliegueEnRegion(
                "DEP-0001",
                "us-east-1"
        );

        sistema.solicitarDespliegueEnRegion(
                "DEP-0001",
                "sa-east-1"
        );

        sistema.procesarSiguienteDespliegue(
                "us-east-1"
        );

        sistema.procesarSiguienteDespliegue(
                "sa-east-1"
        );

        sistema.retirarUltimoDespliegueProcesado(
                "us-east-1"
        );

        Retorno activosVirginia =
                sistema.listarDesplieguesActivosDeRegion(
                        "us-east-1"
                );

        Retorno activosSaoPaulo =
                sistema.listarDesplieguesActivosDeRegion(
                        "sa-east-1"
                );

        assertEquals(
                "",
                activosVirginia.getValorString()
        );

        assertEquals(
                "DEP-0001;LLM-070-A1B2;Modelo texto;true;Texto",
                activosSaoPaulo.getValorString()
        );
    }
}