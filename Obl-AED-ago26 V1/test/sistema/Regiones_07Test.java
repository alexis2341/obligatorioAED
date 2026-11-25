package sistema;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Regiones_07Test {

    private Sistema sistema;

    @Before
    public void setUp() {
        sistema = new ImplementacionSistema();
        sistema.inicializarSistema();
    }

    @Test
    public void registrarRegionCorrectamente() {

        Retorno r = sistema.registrarRegion(
                "us-east-1",
                "Virginia",
                "Estados Unidos",
                "us-east-1a"
        );

        assertEquals(
                Retorno.Resultado.OK,
                r.getResultado()
        );
    }

    @Test
    public void registrarRegionConDatoVacio() {

        Retorno r = sistema.registrarRegion(
                "",
                "Virginia",
                "Estados Unidos",
                "us-east-1a"
        );

        assertEquals(
                Retorno.Resultado.ERROR_1,
                r.getResultado()
        );
    }

    @Test
    public void registrarRegionDuplicada() {

        sistema.registrarRegion(
                "us-east-1",
                "Virginia",
                "Estados Unidos",
                "us-east-1a"
        );

        Retorno r = sistema.registrarRegion(
                "us-east-1",
                "Otra",
                "Otro pais",
                "otra-zona"
        );

        assertEquals(
                Retorno.Resultado.ERROR_2,
                r.getResultado()
        );
    }

    @Test
    public void obtenerRegionCorrectamente() {

        sistema.registrarRegion(
                "sa-east-1",
                "Sao Paulo",
                "Brasil",
                "sa-east-1a"
        );

        Retorno r =
                sistema.obtenerRegion("sa-east-1");

        assertEquals(
                Retorno.Resultado.OK,
                r.getResultado()
        );

        assertEquals(
                "sa-east-1;Sao Paulo;Brasil;sa-east-1a",
                r.getValorString()
        );
    }

    @Test
    public void obtenerRegionInexistente() {

        Retorno r =
                sistema.obtenerRegion("no-existe");

        assertEquals(
                Retorno.Resultado.ERROR_2,
                r.getResultado()
        );
    }
}