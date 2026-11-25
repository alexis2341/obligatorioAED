package sistema;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ErroresOperacionesRegiones_10Test {

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
    public void procesarRegionVaciaONull() {

        Retorno r =
                sistema.procesarSiguienteDespliegue("   ");

        assertEquals(
                Retorno.Resultado.ERROR_1,
                r.getResultado()
        );
    }

    @Test
    public void procesarRegionInexistente() {

        Retorno r =
                sistema.procesarSiguienteDespliegue("no-existe");

        assertEquals(
                Retorno.Resultado.ERROR_2,
                r.getResultado()
        );
    }

    @Test
    public void procesarSinPendientes() {

        Retorno r =
                sistema.procesarSiguienteDespliegue("us-east-1");

        assertEquals(
                Retorno.Resultado.ERROR_3,
                r.getResultado()
        );
    }

    @Test
    public void listarActivosRegionVacia() {

        Retorno r =
                sistema.listarDesplieguesActivosDeRegion(" ");

        assertEquals(
                Retorno.Resultado.ERROR_1,
                r.getResultado()
        );
    }

    @Test
    public void listarActivosRegionInexistente() {

        Retorno r =
                sistema.listarDesplieguesActivosDeRegion("no-existe");

        assertEquals(
                Retorno.Resultado.ERROR_2,
                r.getResultado()
        );
    }

    @Test
    public void listarActivosSinActivosDevuelveVacio() {

        Retorno r =
                sistema.listarDesplieguesActivosDeRegion("us-east-1");

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
    public void retirarRegionVacia() {

        Retorno r =
                sistema.retirarUltimoDespliegueProcesado("   ");

        assertEquals(
                Retorno.Resultado.ERROR_1,
                r.getResultado()
        );
    }

    @Test
    public void retirarRegionInexistente() {

        Retorno r =
                sistema.retirarUltimoDespliegueProcesado("no-existe");

        assertEquals(
                Retorno.Resultado.ERROR_2,
                r.getResultado()
        );
    }

    @Test
    public void retirarSinHistorial() {

        Retorno r =
                sistema.retirarUltimoDespliegueProcesado("us-east-1");

        assertEquals(
                Retorno.Resultado.ERROR_3,
                r.getResultado()
        );
    }
}